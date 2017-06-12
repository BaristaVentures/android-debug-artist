#!/usr/bin/env bash
#
# Publish to bintray
#

./gradlew build --stacktrace

./gradlew lib:build --stacktrace
./gradlew lib:install --stacktrace
./gradlew lib:bintrayUpload --stacktrace

./gradlew reporter-pivotal:build --stacktrace
./gradlew reporter-pivotal:install --stacktrace
./gradlew reporter-pivotal:bintrayUpload --stacktrace