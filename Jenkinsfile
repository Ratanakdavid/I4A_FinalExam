pipeline {
    agent any
    
    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Build & Test') {
            steps {
                // Build and run tests
                sh './mvnw clean package -Dspring.profiles.active=test'
            }
        }
        stage('Deploy & Backup') {
            steps {
                // Runs the local playbook
                sh 'ansible-playbook -i inventory.ini deploy.yml'
            }
        }
    }
    
    // post {
    //     failure {
    //         // Note: Ensure your Jenkins "System" email settings are configured
    //         mail to: 'srengty@gmail.com',
    //              subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
    //              body: "Build or Deployment failed. Check the console output here: ${env.BUILD_URL}"
    //     }
    // }
}