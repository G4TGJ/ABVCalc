pipeline {
  agent {
    docker {
      image 'xmartlabs/jenkins-android:lts'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh '''yes | /var/lib/jenkins/tools/android-sdk/tools/bin/sdkmanager --update
yes | /var/lib/jenkins/tools/android-sdk/tools/bin/sdkmanager --licenses'''
        sh './gradlew build'
      }
    }
  }
}