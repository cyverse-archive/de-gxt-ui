#!groovy
milestone 0
def repo = "de"
def dockerUser = "discoenv"

timestamps {
  node('docker && gwtbuild') {
      slackJobDescription = "job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
      try {
          stage "Prepare"
          checkout scm
          git_commit = sh(returnStdout: true, script: "git rev-parse HEAD").trim()
          echo git_commit

          descriptive_version = sh(returnStdout: true, script: 'git describe --long --tags --dirty --always').trim()
          echo descriptive_version

          withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'jenkins-sencha-credentials', usernameVariable: 'SENCHA_USERNAME', passwordVariable: 'SENCHA_PASSWORD']]) {
            writeFile file: 'sencha_gradle.properties', text: "sencha_support_user=${env.SENCHA_USERNAME}\nsencha_support_password=${env.SENCHA_PASSWORD}"
          }

          stage "Create Build Image"
          milestone 20
          dockerRepoBuild = "build-${repo}-${env.BUILD_TAG}"
          sh "docker build --pull --rm -f Dockerfile-build -t ${dockerRepoBuild} ."
          milestone 21

          dockerCacher = "precache-${repo}-${env.BUILD_TAG}"
          dockerTestRunner = "test-${repo}-${env.BUILD_TAG}"
          dockerNpmTestRunner = "test-npm-${repo}-${env.BUILD_TAG}"
          dockerWarBuilder = "war-${repo}-${env.BUILD_TAG}"
          dockerPusher = "push-${env.BUILD_TAG}"

          dockerSrcRootDir = "/usr/src/app"
          dockerCacheVolumes = """-v /tmp:/tmp -v "\$(pwd)/.gradle/caches:/root/.gradle/caches\" -v "\$(pwd)/node_modules:${dockerSrcRootDir}/react-components/node_modules\""""

          milestone 30
          sh "docker run ${dockerCacheVolumes} --name ${dockerCacher} --rm ${dockerRepoBuild} ./gradlew clean classes testClasses npmInstall"
          milestone 31

          try {
              stage "Test"
              milestone 40
              sh returnStatus: true, script: "rm -rf jenkins_tests"
              sh "docker run ${dockerCacheVolumes} --name ${dockerTestRunner} ${dockerRepoBuild} ./gradlew test"
              sh "docker run ${dockerCacheVolumes} --name ${dockerNpmTestRunner} ${dockerRepoBuild} ./gradlew npmTest"
              sh "docker cp ${dockerTestRunner}:${dockerSrcRootDir}/de-lib/build/test-results jenkins_tests"
              junit "jenkins_tests/*.xml"
              milestone 41

              stage "Build WAR"
              milestone 50
              sh "mkdir -p target/"
              sh """docker run ${dockerCacheVolumes} --name ${dockerWarBuilder} --rm -e BRANCH_NAME -e BUILD_TAG -e BUILD_ID -e BUILD_NUMBER ${dockerRepoBuild} > target/de-copy.war"""
              milestone 51

              fingerprint 'target/de-copy.war'

              stage "Package Public Image and Push"

              milestone 100
              dockerRepo = "${dockerUser}/${repo}:${env.BRANCH_NAME}"
              lock("docker-push-${dockerRepo}") {
                milestone 101
                sh "docker build --rm --build-arg git_commit=${git_commit} --build-arg descriptive_version=${descriptive_version} -t ${dockerRepo} ."
                milestone 102

                image_sha = sh(returnStdout: true, script: "docker inspect -f '{{ .Config.Image }}' ${dockerRepo}").trim()
                echo image_sha

                writeFile(file: "${dockerRepo}.docker-image-sha", text: "${image_sha}")
                fingerprint "${dockerRepo}.docker-image-sha"

                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'jenkins-docker-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME']]) {
                    sh """docker run -e DOCKER_USERNAME -e DOCKER_PASSWORD \\
                                     -v /var/run/docker.sock:/var/run/docker.sock \\
                                     --rm --name ${dockerPusher} \\
                                     docker:\$(docker version --format '{{ .Server.Version }}') \\
                                     sh -e -c \\
                          'docker login -u \"\$DOCKER_USERNAME\" -p \"\$DOCKER_PASSWORD\" && \\
                           docker push ${dockerRepo} && \\
                           docker logout'"""
                }
            }
          } finally {
              // using returnStatus so if these are gone it doesn't error
              sh returnStatus: true, script: "rm -rf jenkins_tests"
              sh returnStatus: true, script: "rm sencha_gradle.properties"

              sh returnStatus: true, script: "docker kill ${dockerCacher}"
              sh returnStatus: true, script: "docker rm ${dockerCacher}"

              sh returnStatus: true, script: "docker kill ${dockerTestRunner}"
              sh returnStatus: true, script: "docker rm ${dockerTestRunner}"

              sh returnStatus: true, script: "docker kill ${dockerNpmTestRunner}"
              sh returnStatus: true, script: "docker rm ${dockerNpmTestRunner}"

              sh returnStatus: true, script: "docker kill ${dockerWarBuilder}"
              sh returnStatus: true, script: "docker rm ${dockerWarBuilder}"

              sh returnStatus: true, script: "docker kill ${dockerPusher}"
              sh returnStatus: true, script: "docker rm ${dockerPusher}"

              sh returnStatus: true, script: "docker rmi ${dockerRepoBuild}"

              step([$class: 'hudson.plugins.jira.JiraIssueUpdater',
                      issueSelector: [$class: 'hudson.plugins.jira.selector.DefaultIssueSelector'],
                      scm: scm,
                      labels: [ "ui-${descriptive_version}" ]])
          }

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
}
