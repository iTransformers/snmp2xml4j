node{
       // docker.image('maven:alpine').inside {
            stage('Preparation') {    // Maven installation declared in the Jenkins "Global Tool Configuration"
                git url: 'https://github.com/iTransformers/snmp2xml4j'
            }
            withMaven(
                        maven: 'M3',
                        // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
                        // Maven settings and global settings can also be defined in Jenkins Global Tools Configuration
                        mavenSettingsConfig: 'MyGlobalSettings',
                        mavenLocalRepo: '.repository')
       // }
            stage('Unit test'){
                sh "mvn clean test"
            }
            stage('Integration test'){
                sh "mvn test -P functional-test"
            }
            stage('Package'){
                sh "mvn package -DskipTests=true"
            }


        // withMaven will discover the generated Maven artifacts, JUnit Surefire & FailSafe reports and FindBugs reports

}