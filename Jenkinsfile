pipeline {
  agent any
  
  stages {

    stage('Test') {
      steps {
        sh 'ls -al'
        sh 'docker compose up test'
      }
    }

    stage('Build') {
      steps {
        sh 'docker compose up build'
      }
    }

    stage('GenImage') {
      steps {
        sh 'docker build . -t quiz final'
      }
    }

    stage('DeployToDev') {
      steps {
        sh '''
         set +x
         ./ci/deploy.sh dev
         '''
      }
    }

    stage('DeployToQA') {
      options {
          timeout(time: 60, unit: 'SECONDS')
      }

      input {
          message 'Do you want to deploy to QA?'
          ok 'Yes, go ahead.'
      }

      steps {
          sh '''
          set +x
          ./ci/deploy.sh qa
          '''
      }
    }

    stage('Confirm staging') {
      options {
          timeout(time: 60, unit: 'SECONDS')
      }

      input {
          message 'Do you want to deploy to staging?'
          ok 'Yes, go ahead.'
      }

      steps {
          sh '''
          set +x
          ./ci/deploy.sh staging
          '''
      }
    }

    stage('Confirm prod') {
      options {
          timeout(time: 60, unit: 'SECONDS')
      }

      input {
          message 'Do you want to deploy to prod?'
          ok 'Yes, go ahead.'
      }

      steps {
          sh '''
              set +x
              ./ci/deploy.sh prod
              '''
      }
    }
  }
}
