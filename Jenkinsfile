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

properties([
        buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '5', numToKeepStr: '2')),
        parameters([

                string(name: 'cleanup', defaultValue: 'YES'),
                string(name: 'release', defaultValue: 'NO'),
                string(name: 'docker', defaultValue: 'NO'),
                string(name: 'relVersion', defaultValue: "1.0.6"),
                string(name: 'devVersion', defaultValue: '1.0.7-SNAPSHOT'),
        ])
])



node() {
    def server;
    def buildInfo;
    def rtMaven;
    def version;
    def relVersion;
    def snmp2xml4j;
    def pom;
    String relFlag=params.release;
    String dockerFlag=params.docker;


    BRANCH_NAME = "1.0.5-work"
    stage('Preparation') {
        checkout([$class           : 'GitSCM', branches: [[name: "*/${BRANCH_NAME}"]],
                  userRemoteConfigs: [[url: "https://github.com/iTransformers/snmp2xml4j.git"]]])

        server = Artifactory.server '819081409@1432230914724';
        rtMaven = Artifactory.newMavenBuild()
        rtMaven.tool = 'M3'
        //withMaven(maven: 'M3', mavenSettingsConfig: '55234634-bcc4-4034-9a07-a1df766290f5');

        // Deceide where to release snapshots and where releases
        rtMaven.deployer releaseRepo: 'ext-release-local', snapshotRepo: 'ext-snapshot-local', server: server
        rtMaven.resolver releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot', server: server
        // Disable artifacts deployment during Maven run
        rtMaven.deployer.deployArtifacts = false

        //Figure out how much time to retain the build info in artefactory
        buildInfo = Artifactory.newBuildInfo()
        buildInfo.env.capture = true
        buildInfo.retention maxBuilds: 20
        buildInfo.retention maxDays: 21

        pom = readMavenPom file: 'pom.xml'

        version = pom.version;
        releaseVersion = version.replace("-SNAPSHOT", ".${currentBuild.number}")

    }

    stage('Unit test') {
//      sh "mvn clean test"
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




    stage('Release') {
        if (relFlag == 'YES') {
            rtMaven.deployer.deployArtifacts = true
            //  rtMaven.deployer.deployArtifacts buildInfo
            rtMaven.run pom: 'pom.xml', goals: "-DreleaseVersion=${version} -DdevelopmentVersion=${relVersion} -DpushChanges=false -DlocalCheckout=true -DpreparationGoals=initialize release:prepare release:perform -B", buildInfo: buildInfo
            sh "git push ${pom.artifactId}-${version}"
        } else {
            rtMaven.deployer.deployArtifacts = true
            rtMaven.deployer.deployArtifacts buildInfo
        }

    }

    stage('Publish build info') {
        server.publishBuildInfo buildInfo
    }
    stage('Docker') {

        if (dockerFlag == 'YES') {
            /* This builds the actual image; synonymous to
             * docker build on the command line */
            env.BUNDLE_JAR_NAME = "snmp2xml4j-bundle-" + version + ".jar"

            echo "building an image"
            snmp2xml4j = docker.build("itransformers/snmp2xml4j")
            echo "testing the image";
            sh "docker run -i   itransformers/snmp2xml4j:latest -O walk -v 2c -a 193.19.175.150 -p 161 -pr udp -c netTransformer-aaa -t 1000 -r 1 -m 100 -o 'sysDescr, sysName'"
            echo "pushing image";
            docker.withRegistry('https://registry.hub.docker.com', '15887ad0-0df2-4b81-b7f7-96b1dc0dbaf4') {
                snmp2xml4j.push("${env.BUILD_NUMBER}")
                snmp2xml4j.push("latest")
            }
        }

    }


    stage('Promotion') {

        if (relFlag == 'YES') {
            //If we have artefactory pro ;)
            def promotionConfig = [
                    //Mandatory parameters
                    'buildName'          : buildInfo.name,
                    'buildNumber'        : buildInfo.number,
                    'targetRepo'         : 'ext-release-local',

                    //Optional parameters
                    'comment'            : 'this is the promotion comment',
                    'sourceRepo'         : 'ext-snapshot-local',
                    'status'             : 'Released',
                    'includeDependencies': true,
                    'failFast'           : false,
                    'copy'               : true
            ]
            try {
                // Promote build
                server.promote promotionConfig
            } catch (all) {
                echo "Promotion failed..."
            }
        }

    }


}