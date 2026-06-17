pipeline {
    agent any
    
    triggers {
        // Poll SCM every 5 minutes
        pollSCM('H/5 * * * *')
    }
    
    stages {
        stage('Build & Test') {
            steps {
                // Assuming you have Maven wrapper in your project
                sh './mvnw clean package test'
            }
        }
        stage('Deploy') {
            steps {
                // This triggers the Ansible playbook you already created
                sh 'ansible-playbook -i inventory.ini deploy.yml'
            }
        }
    }
    
    post {
        failure {
            // Sends email if build or test fails
            mail to: 'srengty@gmail.com',
                 subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                 body: "Something went wrong! Check the console output here: ${env.BUILD_URL}"
        }
    }
}
