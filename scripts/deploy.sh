#!/usr/bin/env bash
#
# Publish to bintray
#

./gradlew build --stacktrace -PsignPackage

./gradlew lib:build --stacktrace -PsignPackage
./gradlew lib:install --stacktrace -PsignPackage
./gradlew lib:bintrayUpload --stacktrace -PsignPackage

./gradlew reporter-pivotal:build --stacktrace -PsignPackage
./gradlew reporter-pivotal:install --stacktrace -PsignPackage
./gradlew reporter-pivotal:bintrayUpload --stacktrace -PsignPackage