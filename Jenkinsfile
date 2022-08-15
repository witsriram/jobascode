pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                 echo ${params.SITE_NAME}
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
