pipeline {
  agent {
    docker {
      image 'xmartlabs/jenkins-android:lts'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh './gradlew build'
      }
    }
  }
}