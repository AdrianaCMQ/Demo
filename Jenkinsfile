pipeline {
  agent any
  
  stages {

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
  }
}
