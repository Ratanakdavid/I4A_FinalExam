pipeline {
    agent any
    
    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Build & Test') {
            steps {
                sh './mvnw clean package -Dspring.profiles.active=test'
            }
        }
        stage('Deploy & Backup') {
            steps {
                sh 'ansible-playbook -i inventory.ini deploy.yml'
            }
        }
    }
    
    post {
        failure {
            mail to: 'srengty@gmail.com',
                 subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                 body: "Build or Deployment failed. Check the console output here: ${env.BUILD_URL}"
        }
    }
}