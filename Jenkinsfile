pipeline {
  agent {
    docker {
      image 'xmartlabs/jenkins-android:lts'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'yes | sdkmanager --licenses'
        sh './gradlew build'
      }
    }
  }
}