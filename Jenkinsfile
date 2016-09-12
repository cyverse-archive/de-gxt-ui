#!groovy
def repo = "de"
def dockerUser = "discoenv"

node {
    stage "Checkout"
    checkout scm

    stage "Create credentials file"
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'jenkins-sencha-credentials', usernameVariable: 'SENCHA_USERNAME', passwordVariable: 'SENCHA_PASSWORD']]) {
      writeFile file: 'sencha_gradle.properties', text: "sencha_support_user=${env.SENCHA_USERNAME}\nsencha_support_password=${env.SENCHA_PASSWORD}"
    }

    dockerRepoBuild = "build-${repo}-${env.BRANCH_NAME}"
    try {
        stage "Create Build Image & Test"
        sh "docker build --rm -f Dockerfile-build -t ${dockerRepoBuild} ."
        stage "Build WAR"
        sh "mkdir -p target/"
        sh """docker run --rm ${dockerRepoBuild} > target/de-copy.war"""
    } finally {
        stage "Clean"
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
