#!/bin/sh -x
./gradlew createProdWar \
    -PGIT_COMMIT=$(git rev-parse HEAD) \
    -PGIT_BRANCH=${BRANCH_NAME} \
    -PBUILD_TAG=${BUILD_TAG} \
    -PBUILD_ID=${BUILD_ID} \
    -PBUILD_NUMBER=${BUILD_NUMBER} 1>&2 && cat target/de.war
