#!/usr/bin/env bash
#
# Publish to bintray
#

./gradlew build --stacktrace -PsignPackage

./gradlew lib:build --stacktrace -PsignPackage
./gradlew lib:install --stacktrace -PsignPackage
./gradlew lib:bintrayUpload --stacktrace -PsignPackage