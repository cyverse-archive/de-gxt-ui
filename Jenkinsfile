#!groovy
def repo = "de"
def dockerUser = "discoenv"

node {
    stage "Prepare"
    checkout scm
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'jenkins-sencha-credentials', usernameVariable: 'SENCHA_USERNAME', passwordVariable: 'SENCHA_PASSWORD']]) {
      writeFile file: 'sencha_gradle.properties', text: "sencha_support_user=${env.SENCHA_USERNAME}\nsencha_support_password=${env.SENCHA_PASSWORD}"
    }

    stage "Create Build Image"
    dockerRepoBuild = "build-${repo}-${env.BRANCH_NAME}"
    sh "docker build --rm -f Dockerfile-build -t ${dockerRepoBuild} ."
    try {
        stage "Test"
        sh "docker run --rm ${dockerRepoBuild} ./gradlew test"

        stage "Build WAR"
        sh "mkdir -p target/"
        sh """docker run --rm -e GIT_BRANCH -e BUILD_TAG -e BUILD_ID -e BUILD_NUMBER ${dockerRepoBuild} > target/de-copy.war"""
    } finally {
        sh "rm sencha_gradle.properties"
        sh "docker rmi ${dockerRepoBuild}"
    }

    stage "Package Public Image"
    sh 'git rev-parse HEAD > GIT_COMMIT'
    git_commit = readFile('GIT_COMMIT').trim()
    echo git_commit

    dockerRepo = "${dockerUser}/${repo}:${env.BRANCH_NAME}"
    sh "docker build --rm --build-arg git_commit=${git_commit} -t ${dockerRepo} ."


    stage "Docker Push"
    sh "docker push ${dockerRepo}"
}
