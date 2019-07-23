pipeline {
  agent {
    docker {
      image 'xmartlabs/jenkins-android'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'gradlew tasks'
      }
    }
  }
}