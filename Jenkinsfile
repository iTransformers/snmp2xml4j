/*
 * Jenkinsfile
 *
 * This work is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This work is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 *
 * Copyright (c) 2010-2016 iTransformers Labs. All rights reserved.
 */


node() {
    def server;
    def buildInfo;
    def rtMaven;
    def version;
    def devVersion;

    def snmp2xml4j;

    stage('Preparation') {
        git url: 'https://github.com/iTransformers/snmp2xml4j'
        sh "ls -l";
        sh "df -h"
    }

    stage('Artifactory configuration') {
        server = Artifactory.server '819081409@1432230914724';

        rtMaven = Artifactory.newMavenBuild()
        rtMaven.tool =  'M3'
        //withMaven(maven: 'M3', mavenSettingsConfig: '55234634-bcc4-4034-9a07-a1df766290f5');

        // Tool name from Jenkins configuration
        rtMaven.deployer releaseRepo: 'libs-release-local', snapshotRepo: 'libs-snapshot-local', server: server
        rtMaven.resolver releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot', server: server
        rtMaven.deployer.deployArtifacts = false // Disable artifacts deployment during Maven run

        buildInfo = Artifactory.newBuildInfo()
        buildInfo.env.capture = true
        buildInfo.retention maxBuilds: 10
        buildInfo.retention maxDays: 7
    }



    stage('Unit test') {
//            sh "mvn clean test"
        rtMaven.run pom: 'pom.xml', goals: 'clean test'

    }

    // Enable and fix a broken functional test

    stage('Package') {
        rtMaven.run pom: 'pom.xml', goals: 'package -DskipTests=true'

//            sh "mvn package -DskipTests=true"
    }

    stage('Functional test') {
        rtMaven.run pom: 'pom.xml', goals: 'test -P functional-test'

//            sh "mvn test -P functional-test"
    }

    stage('Install') {

        rtMaven.run pom: 'pom.xml', goals: 'install', buildInfo: buildInfo

    }


    stage('Build image') {
        /* This builds the actual image; synonymous to
         * docker build on the command line */
        snmp2xml4j = docker.build("itransformers/snmp2xml4j")
    }


    stage('Test image') {

        sh "docker run -i   itransformers/snmp2xml4j:latest -O walk -v 2c -a 193.19.175.150 -p 161 -pr udp -c netTransformer-aaa -t 1000 -r 1 -m 100 -o 'sysDescr, sysName'"
    }

    stage('Release') {
      //  rtMaven.deployer.deployArtifacts = true
        rtMaven.deployer.deployArtifacts buildInfo

        //rtMaven.run pom: 'pom.xml', goals: "-DreleaseVersion=${version} -DdevelopmentVersion=${pom.version} -DpushChanges=false -DlocalCheckout=true -DpreparationGoals=initialize release:prepare release:perform -B", buildInfo: buildInfo

    }

    stage('Publish build info') {
        server.publishBuildInfo buildInfo
    }

    stage('Push image') {
        echo "pushing image";
        docker.withRegistry('https://registry.hub.docker.com', '15887ad0-0df2-4b81-b7f7-96b1dc0dbaf4') {
            snmp2xml4j.push("${env.BUILD_NUMBER}")
            snmp2xml4j.push("latest")
        }


    }

    stage ('Promotion') {
        def promotionConfig = [
                //Mandatory parameters
                'buildName'          : buildInfo.name,
                'buildNumber'        : buildInfo.number,
                'targetRepo'         : 'libs-release-local',

                //Optional parameters
                'comment'            : 'this is the promotion comment',
                'sourceRepo'         : 'libs-snapshot-local',
                'status'             : 'Released',
                'includeDependencies': true,
                'failFast'           : true,
                'copy'               : true
        ]
    }

    // Promote build
    server.promote promotionConfig


}