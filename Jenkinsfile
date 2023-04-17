pipeline {
  agent any
  
  stages {

    stage('Stop Containers') {
      steps {
        withEnv(['PATH+DOCKER=/opt/homebrew/bin']) {
        sh 'docker stop $(docker ps -q)'
        }
      }
    }

    stage('Test') {
      steps {
        sh '${DOCKER_COMPOSE_PATH}/docker-compose up test'
      }
    }

    stage('Build') {
      steps {
        sh '${DOCKER_COMPOSE_PATH}/docker-compose up build'
      }
    }

    stage('GenImage') {
      steps {
        sh '${DOCKER_COMPOSE_PATH}/docker-compose build image'
      }
    }

    stage('backend') {
      steps {
        sh '${DOCKER_COMPOSE_PATH}/docker-compose up -d backend'
      }
    }
  }
}
