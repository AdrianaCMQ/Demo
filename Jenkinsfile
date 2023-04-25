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
         withEnv(['PATH+DOCKER=/opt/homebrew/bin', 'AWS_ACCESS_KEY_ID=ASIASKRH5RYAMY66ECVX', 'AWS_SECRET_ACCESS_KEY=w8g5Dd165DpepGtW1tsFksRGP8Idw2GA+qT1K1ji', 'AWS_DEFAULT_REGION=eu-central-1']) {
          sh 'export AWS_SESSION_TOKEN="IQoJb3JpZ2luX2VjEAMaDGV1LWNlbnRyYWwtMSJHMEUCIGeGKq8TiU5EtXoA1BlZWQ2ZpdSeIENllq0p+6q3OWzPAiEAiAfM3rjFAlWtFOfiBGu5/Ef9OhdyMzD/7DQxyAMj3cAqnwMI/P//////////ARAEGgwxNjAwNzEyNTc2MDAiDAS1TpuBpWhAbSSbqirzAmclNlxaiy53fAfecFfpEPl7cG2L55iXCY3WP/Nv3ILspOn9L8BXQfvYgAVabvcVDW27DOyaI1hKr5EdjPK1Mvc2J7DSm/0xOPVM5v73HWjmvwIpkmdB5Tjeth9sjGxOiii3S3UtTHb9hiJ8QWhmB9AQhVBU0BJNsyaZqS7x+LBrpZlCfpX6WTwbK94QEOrYlhFI+XNpXFypo5cs34Lh1zH0f526ubm+L1XoAROxLhfjzG8pMFpDTMowTiwmTHQ8VUsm+pvl0ECkdhTp7VdrxeZOYUBZwacrmgdoprgQgv96NbVWgFzY5/5vx1nIc3cfdE/QBJIK/5kgh+xMijtWb/BN92Qg4meoqc66b+w29aqKo7iD2aMigvlgKXimdje/XIz12uFgRcmQE3N9FHp8HjtTgYIO+nqtA12i1tXcnC299YY2JX8nbh6E+d3Qo6LzS014yB56WxqUl+jusVTpHjUtUyv2/lfshW4lvwkKOUkcrnsBMJ3/nKIGOqYBM7Zb/+JwI/EYGlLvNQddUpQkvJp3E2Xe51Tz4VVa1oMGmao7H920X0XVZMGylAErBx9cShzj+cr37MJoPQivplr2AH7rgrjgLKlnTETgPCaCmYV6XTAPBJhK5Apm8tjkaw3EmEg1MAQwyxJHZjDifFjHvBCVDpAJ2dxEMZjUZTjqAyGhVqa5h8x8fMVvJAe12yC/heIthR/L44PatJcb76A20EpSZQ=="'
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
