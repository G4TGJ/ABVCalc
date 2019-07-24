pipeline {
  agent {
    docker {
      image 'xmartlabs/jenkins-android:lts'
      args '''-v /root/.gradle:/root/.gradle
-v /root/.android:/root/.android
'''
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'ls -al /opt/android-sdk-linux/licenses'
        sh 'ls -al /home/Android/Sdk'
        sh '''yes | /opt/android-sdk-linux/tools//bin/sdkmanager --update
'''
        sh 'yes | /opt/android-sdk-linux/tools//bin/sdkmanager --licenses'
        sh './gradlew build'
      }
    }
    stage('Deliver') {
      steps {
        archiveArtifacts 'app/build/outputs/apk/**/*.apk'
        archiveArtifacts 'app/build/reports/lint-results.html'
      }
    }
  }
}