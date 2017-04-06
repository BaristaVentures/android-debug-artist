#!/usr/bin/env bash
#
# Publish to bintray
#

./gradlew build

./gradlew lib:build || echo "Failed"
./gradlew lib:uploadArchives || echo "Failed"
./gradlew lib:bintrayUpload || echo "Failed"

./gradlew reporter-pivotal:build || echo "Failed"
./gradlew reporter-pivotal:uploadArchives || echo "Failed"
./gradlew reporter-pivotal:bintrayUpload || echo "Failed"