#!groovy
def repo = "de"
def dockerUser = "discoenv"

node('docker') {
    slackJobDescription = "job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
    try {
        stage "Prepare"
        checkout scm
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'jenkins-sencha-credentials', usernameVariable: 'SENCHA_USERNAME', passwordVariable: 'SENCHA_PASSWORD']]) {
          writeFile file: 'sencha_gradle.properties', text: "sencha_support_user=${env.SENCHA_USERNAME}\nsencha_support_password=${env.SENCHA_PASSWORD}"
        }

        stage "Create Build Image"
        dockerRepoBuild = "build-${repo}-${env.BUILD_TAG}"
        sh "docker build --rm -f Dockerfile-build -t ${dockerRepoBuild} ."

        dockerTestRunner = "test-${repo}-${env.BUILD_TAG}"
        dockerWarBuilder = "war-${repo}-${env.BUILD_TAG}"
        try {
            stage "Test"
            sh "docker run --name ${dockerTestRunner} --rm ${dockerRepoBuild} ./gradlew test"

            stage "Build WAR"
            sh "mkdir -p target/"
            sh """docker run -v /tmp:/tmp --name ${dockerWarBuilder} --rm -e BRANCH_NAME -e BUILD_TAG -e BUILD_ID -e BUILD_NUMBER ${dockerRepoBuild} > target/de-copy.war"""
        } finally {
            // using returnStatus so if these are gone it doesn't error
            sh returnStatus: true, script: "rm sencha_gradle.properties"
            sh returnStatus: true, script: "docker kill ${dockerTestRunner}"
            sh returnStatus: true, script: "docker rm ${dockerTestRunner}"
            sh returnStatus: true, script: "docker kill ${dockerWarBuilder}"
            sh returnStatus: true, script: "docker rm ${dockerWarBuilder}"
            sh returnStatus: true, script: "docker rmi ${dockerRepoBuild}"
        }

        stage "Package Public Image and Push"
        sh 'git rev-parse HEAD > GIT_COMMIT'
        git_commit = readFile('GIT_COMMIT').trim()
        echo git_commit

        dockerRepo = "${dockerUser}/${repo}:${env.BRANCH_NAME}"
        sh "docker build --rm --build-arg git_commit=${git_commit} -t ${dockerRepo} ."

        sh "docker push ${dockerRepo}"
    } catch (InterruptedException e) {
        currentBuild.result = "ABORTED"
        slackSend color: 'warning', message: "ABORTED: ${slackJobDescription}"
        throw e
    } catch (e) {
        currentBuild.result = "FAILED"
        sh "echo ${e}"
        slackSend color: 'danger', message: "FAILED: ${slackJobDescription}"
        throw e
    }
}
