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
                // Using absolute path for mvn to avoid "mvn not found"
                sh """
                    /usr/bin/mvn clean package -DskipTests 2>&1 | tee -a "$BUILD_LOG"
                """
            }
        }

        stage('Test with SQLite Test Database') {
            steps {
                sh """
                    /usr/bin/mvn clean test -Dspring.profiles.active=test 2>&1 | tee -a "$BUILD_LOG"
                """
            }
        }

        stage('Deploy with Ansible') {
            steps {
                sh """
                    echo "=== Running Ansible Playbook ===" | tee -a "$BUILD_LOG"
                    ansible-playbook -i inventory.ini deploy.yml 2>&1 | tee -a "$BUILD_LOG"
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
            script {
                emailext (
                    subject: "FAILED: Job ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
                    body: """<p>The Jenkins build failed. Please check the logs below.</p>
                             <p>Build URL: ${env.BUILD_URL}</p>
                             <p>Check the attached build log for errors.</p>""",
                    to: 'srengty@gmail.com',
                    attachmentsPattern: 'jenkins-build-output.txt'
                )
            }
        }
    }
}