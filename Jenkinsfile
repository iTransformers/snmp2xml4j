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
    def snmp2xml4j;

    stage('Preparation') {    // Maven installation declared in the Jenkins "Global Tool Configuration"
        git url: 'https://github.com/iTransformers/snmp2xml4j'
    }

    stage('Unit test') {
        sh "mvn clean test"
    }

    // Enable and fix a broken functional test

    stage('Package') {
        sh "mvn package -DskipTests=true"
    }

    stage('Functional test') {
        sh "mvn test -P functional-test"
    }


    stage('Build image') {
        /* This builds the actual image; synonymous to
         * docker build on the command line */
        snmp2xml4j = docker.build("itransformers/snmp2xml4j")
    }


    stage('Test image') {

        sh "docker run -ti   itransformers/snmp2xml4j:1.0 -O walk -v 2c -a 193.19.175.129 -p 161 -pr udp -c netTransformer-r -t 1000 -r 1 -m 100 -o 'sysDescr, sysName'"
    }

    stage('Push image') {
        echo "pushing image";
                docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
            snmp2xml4j.push("${env.BUILD_NUMBER}")
            snmp2xml4j.push("latest")
        }
    }



}