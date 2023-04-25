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

    stage('Push image to ECR') {
        steps {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'AWS credentials']]) {
          sh 'env'
        }
         withEnv(['PATH+DOCKER=/opt/homebrew/bin']) {
          sh 'export AWS_ACCESS_KEY_ID=${env.AWS_ACCESS_KEY_ID}'
          sh 'export AWS_SECRET_ACCESS_KEY=${env.AWS_SECRET_ACCESS_KEY}'
          sh 'export AWS_SESSION_TOKEN=${AWS_SESSION_TOKEN}'
          sh 'aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin 160071257600.dkr.ecr.eu-central-1.amazonaws.com'
          sh 'docker tag demo 160071257600.dkr.ecr.eu-central-1.amazonaws.com/demo'
          sh 'docker push 160071257600.dkr.ecr.eu-central-1.amazonaws.com/demo'
         }
       }
    }

    stage('start service locally') {
      steps {

           withEnv(['PATH+DOCKER=/opt/homebrew/bin']) {
              script {
                        def runningContainers = sh(script: 'docker ps -q --filter ancestor=quiz-final', returnStdout: true).trim()
                        if (runningContainers) {
                          sh 'docker stop $(docker ps -a -q --filter ancestor=quiz-final)'
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
