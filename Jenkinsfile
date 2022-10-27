pipeline {
    agent any
    stages {
        stage('Build') {
            tools {
                jdk "JDK17"
            }
            steps {
                echo 'Building...'
                sh 'docker ps'
                sh './gradlew clean assemble -Penv=production'
            }
        }
        stage('Test') {
            tools {
                jdk "JDK17"
            }
            steps {
                echo 'Testing..'
                sh './gradlew test -Penv=production'
            }
        }
        stage('Building Image') {
            tools {
                jdk "JDK17"
            }
            steps {
                echo 'start build image and publish it...'
                withCredentials([usernamePassword(credentialsId: 'AkogareDockerRegistry', passwordVariable: 'password', usernameVariable: 'username')]) {
                    sh "docker login https://registry.akogare.de -u $username -p $password"
                    sh 'docker build -f Dockerfile -t registry.akogare.de/akogare-invoice-ws:1.0.4-RELEASE .'

                }
            }
        }

        stage('Pushing Image') {
            steps {
                echo 'start pushing image'
                withCredentials([usernamePassword(credentialsId: 'AkogareDockerRegistry', passwordVariable: 'password', usernameVariable: 'username')]) {
                    sh "docker login https://registry.akogare.de -u $username -p $password"
                    sh 'docker push registry.akogare.de/akogare-invoice-ws:1.0.4-RELEASE'

                }

            }
        }
    }
}
