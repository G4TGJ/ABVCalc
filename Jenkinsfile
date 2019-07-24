pipeline {
  agent {
    docker {
      image 'xmartlabs/jenkins-android:lts'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh '''yes | /opt/android-sdk-linux/tools//bin/sdkmanager --update
yes | /opt/android-sdk-linux/tools//bin/sdkmanager --licenses'''
        sh './gradlew build'
      }
    }
    stage('Deliver') {
      steps {
        archiveArtifacts '**'
      }
    }
  }
}