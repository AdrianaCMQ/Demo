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
          sh 'export AWS_SESSION_TOKEN="IQoJb3JpZ2luX2VjEAYaDGV1LWNlbnRyYWwtMSJIMEYCIQDaVsRdRvXyG/ZCVKAtwYLfzI8Xxbd3t29kXkCvNT8RugIhAMsatilqzXHuBAlJWjAO0BL4S/N8rPb2TM6NedW/TCZpKp8DCP///////////wEQBBoMMTYwMDcxMjU3NjAwIgxteYOZyd/UIw6v1yYq8wJhuiqBTC73OdppRecSBDizxE0TMFL3okPM0HhOxWR2HUpDUvD1RpxQX2LRUPOWhEkuN4Nj/qB9enfDXflHYE4kn3mwSRKYEOYejM2MLy83trgaz7TDqElpDbcpX86ZMdvEaal7Kf5rdwhmGIRTjwoe/DCZ9ngK7p0UmulLk5OzypVUhJjgVYXWmnCoy/Y/Pf+0yrcOCIqSX6FTPzXJZzfj/PXy4kRG8fn8TjOcUrUaEfua5An2unASdbBNAqHyrTHlccFqyvlGCAkqdGVUAzCS5E/i7G6EvgUgLjG25ExdfDE2GSgWxAAocW+Bpd06Pt8nGddqGpWzDw6gZvbZD+EI3lv1b53tt7dzKSlCxn6i9CYyBTnNhs3SgXZbe31J/plnFcNtXB1jU+WdLyx57L4isN1gUUTNpPErNihhTVZ4n44Y+uGmmig1yZVPOarPjuxiEy8mW2AZRkQGQEDez7afQxD3yQWqgv5BpPetWjHJ7zdaVzDEyp2iBjqlAVn2NH23Q0DEchMo3KYFIa1IgMDq4dQ1LnapHHZXmo2+D9LOUXsctEZdf3mbK1xSN+HdFogQsn7oolpwjz/Gvdjo3AdimonHd/XSVZItherFDiVA7rOMpBJK20qyVYirhwnQtNd/0UQsKjAZgdQ+Ai/NcZWRL4ivcsTpxjCVmUfdeTff9AqnMWCyWE2xFsmniY+OSUV7fYab7deT1NtnoLRrfUqqEQ=="'
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
