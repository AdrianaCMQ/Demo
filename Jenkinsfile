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
         withEnv(['PATH+DOCKER=/opt/homebrew/bin', 'AWS_ACCESS_KEY_ID=ASIASKRH5RYAMY66ECVX', 'AWS_SECRET_ACCESS_KEY=w8g5Dd165DpepGtW1tsFksRGP8Idw2GA+qT1K1ji', 'AWS_DEFAULT_REGION=eu-central-1']) {
          sh 'export AWS_SESSION_TOKEN="IQoJb3JpZ2luX2VjEAEaDGV1LWNlbnRyYWwtMSJHMEUCIQDkePtLwjbYbit51poxH8cNdEyCS0Io1yD6f1KKmCflSQIgRsyuMEuB2on9SK/oFBNAdoGZ36XxnT2EUOTAX7cVxKkqnwMI+v//////////ARAEGgwxNjAwNzEyNTc2MDAiDOwFSHcGlWo71Jo3LSrzAqmbF7mcHEgiG0x1VRUjD+t28d7Pr9kkrUtf03+Z4Ig0gxnjeinEe8iElh/ue4MOSi3eoz96hr0LNsdFHOORayz8GR/+vJn2Hw6HhvV6EH3ZzSkWU3shCdbsZvb+bYYROl+C0gIr+590CYFD1woaQTKUt6PXxA4xPB2VRMllUhwqNMtLgIJ9P7qUu8/HUIwmlaEQMTYuWpfdkfFErWhx84Sq9eGKoyypGi/uwo3oUfmqCvj3tmkAfQMt9n20B5WNSwgUuMMQ5IktpxfleUyt57MfwoiRJTKuztfYVofZwZwm3j/q0LJ9IH2xF6ZR1slc5MgSnPW31SqAE5PlTLEA4bm6HW6OSsjxLpWvfsp8DyYXgiatPYVTdxMj1AbFxjcBK3nDJPwHT0P3LmhWWatFA97KC3VQFbeGv1KytwbP3zw+qtjuXzZgd7nqHeXybLbVndeAA1Uh/npwYGPTTZgrLvM7nfXMuuGfYB/O/0Ptlybv76DdMP3TnKIGOqYBJO3vzMTH9zoU/Tib5MNaFmsemh6EWB2cJaq1AIXRGkwS31vBkoKzUVyCPjoaocfxNuzOnkqSDVP6q8p6/IGaMPec+EWKcF/hm+gvAsNFf5c8LNI/Kh3iGStNb70PxBiy7YiypqDycDl5d+hgw6eIYAkNwZXlkRy6FV08QDzMzM1z2l/OXhOi+SZ2ma8hNtv4pjzBB1ojn1cYCwVMC2o+CKiZEm3hBA=="'
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
