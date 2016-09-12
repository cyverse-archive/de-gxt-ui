#!groovy
def repo = "de"
def dockerUser = "discoenv"

node {
    stage "Build & Test"
    checkout scm

    dockerRepoBuild = "build-${repo}-${env.BRANCH_NAME}"
    try {
        sh "docker build --rm -f Dockerfile-build -t ${dockerRepoBuild} ."
        sh "mkdir -p target/"
        sh """docker run --rm -v \$HOME/.gradle:/root/.gradle ${dockerRepoBuild} > target/de-copy.war"""
    } finally {
        stage "Clean"
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
