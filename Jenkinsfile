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
                sh './gradlew clean assemble'
            }
        }
        stage('Test') {
            tools {
                jdk "JDK17"
            }
            steps {
                echo 'Testing..'
                sh './gradlew :test --tests "main.MatchResourceTest" "-Dquarkus.profile=test"'
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
                    sh "ls -la"
                    sh 'docker build -f src/main/docker/Dockerfile -t registry.akogare.de/seven-one-backend:1.0.0-BETA .'

                }
            }
        }

        stage('Pushing Image') {
            steps {
                echo 'start pushing image'
                withCredentials([usernamePassword(credentialsId: 'AkogareDockerRegistry', passwordVariable: 'password', usernameVariable: 'username')]) {
                    sh "docker login https://registry.akogare.de -u $username -p $password"
                    sh 'docker push registry.akogare.de/seven-one-backend:1.0.0-BETA'
                }
            }
        }
    }
}
