#!/usr/bin/env bash
#
# Publish to bintray
#

./gradlew build --stacktrace

./gradlew lib:uploadArchives --stacktrace
./gradlew lib:bintrayUpload --stacktrace

./gradlew reporter-pivotal:uploadArchives --stacktrace
./gradlew reporter-pivotal:bintrayUpload --stacktrace