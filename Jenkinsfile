pipeline {
    agent any
    tools {
        maven '3.6.3'
        jdk 'Java11'
    }
    options {
        buildDiscarder logRotator(numToKeepStr: '10')
    }
    stages {
        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }
        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }
        stage('Release') {
              steps {
                    dir('.'){
                        echo 'Creating artifacts...';
                        sh "mkdir -p output"
                        sh "mv panapeepo-*/target/panapeepo*.jar output/"
                        archiveArtifacts artifacts: 'output/*'
                    }
              }
        }
        stage('CleanWorkspace') {
            steps {
                cleanWs()
            }
        }
    }
}
