pipeline {
  agent any
  
  stages {

    stage('Test') {
      steps {
        sh '${DOCKER_COMPOSE_PATH}/docker-compose up test'
      }
    }

    stage('Build and genImage') {
      steps {
        sh '${DOCKER_COMPOSE_PATH}/docker-compose up build'
        sh '${DOCKER_COMPOSE_PATH}/docker-compose build image'

      }
    }

    stage('start service locally') {
      steps {

           withEnv(['PATH+DOCKER=/opt/homebrew/bin']) {
           script {
                     def runningContainers = sh(script: 'docker ps -q --filter ancestor=quiz-final', returnStdout: true).trim()
                     if (runningContainers) {
                       sh 'docker stop $(docker ps -a -q --filter ancestor=quiz-final)
'
                     } else {
                       echo 'No running containers found'
                  }
           }
           sh '${DOCKER_COMPOSE_PATH}/docker-compose up -d backend'
        }
      }
    }
  }
}
