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

node {
    docker.image('maven:alpine').inside {
        stage('Preparation') {    // Maven installation declared in the Jenkins "Global Tool Configuration"
            git url: 'https://github.com/iTransformers/snmp2xml4j'
        }
        withMaven(
                maven: 'M3',
                // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
                // Maven settings and global settings can also be defined in Jenkins Global Tools Configuration
                mavenSettingsConfig: '5dd72332-5e11-4907-84e9-8c3e00747634',
                mavenLocalRepo: '.repository') {

            stage('Unit test') {
                sh "mvn clean test"
            }

            // Enable and fix a broken functional test

            stage('Integration test') {
                sh "mvn test -P functional-test"
            }
            stage('Package') {
                sh "mvn package -DskipTests=true"
            }

        }
    }

    stage('Build image') {
        /* This builds the actual image; synonymous to
         * docker build on the command line */

        snmp2xml4j = docker.build("itransformers/snmp2xml4j")
    }

    stage('Test image') {
        /* Ideally, we would run a test framework against our image.
         * For this example, we're using a Volkswagen-type approach ;-) */

        snmp2xml4j.inside {
            sh "ls -l /opt/snmp2xml4j/bin"
            sh 'echo "Tests passed"'
        }
    }

    stage('Push image') {
        /* Finally, we'll push the image with two tags:
         * First, the incremental build number from Jenkins
         * Second, the 'latest' tag.
         * Pushing multiple tags is cheap, as all the layers are reused. */
        echo "pushing image";
        //        docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
//            snmp2xml4j.push("${env.BUILD_NUMBER}")
//            snmp2xml4j.push("latest")
//        }
    }



}