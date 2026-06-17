pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')
    }

    environment {
        SPRING_PROFILES_ACTIVE = 'test'
        BUILD_LOG = 'jenkins-build-output.txt'
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    writeFile file: env.BUILD_LOG, text: "# Jenkins Build Output\n\nJob: ${env.JOB_NAME}\nBuild: #${env.BUILD_NUMBER}\nStarted: ${new Date()}\n\n"
                }
            }
        }

        stage('Build with Maven') {
            steps {
                sh """
                    mvn clean package -DskipTests 2>&1 | tee -a "$BUILD_LOG"
                """
            }
        }

        stage('Test with SQLite Test Database') {
            steps {
                sh """
                    mvn clean test -Dspring.profiles.active=test 2>&1 | tee -a "$BUILD_LOG"
                """
            }
        }

        stage('Deploy with Ansible') {
            steps {
                // Ensure we use the hyphenated docker-compose command
                sh """
                    echo "=== Running Ansible Playbook ===" | tee -a "$BUILD_LOG"
                    ansible-playbook -i inventory.ini playbook.yml 2>&1 | tee -a "$BUILD_LOG"
                """
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'jenkins-build-output.txt,target/*.jar,target/surefire-reports/*.xml', allowEmptyArchive: true
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }
        failure {
            echo 'Build failed. Skipping email notification due to system SMTP configuration.'
        }
    }
}