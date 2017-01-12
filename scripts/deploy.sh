#!/usr/bin/env bash
#
# Publish to bintray
#

./gradlew build
./gradlew lib:uploadArchives
./gradlew lib:bintrayUpload