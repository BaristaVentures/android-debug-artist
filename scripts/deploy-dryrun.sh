#!/usr/bin/env bash
#
# Publish to bintray
#

./gradlew lib:build --stacktrace -PsignPackage -PdryRun
./gradlew lib:install --stacktrace -PsignPackage -PdryRun
./gradlew lib:bintrayUpload --stacktrace -PsignPackage -PdryRun