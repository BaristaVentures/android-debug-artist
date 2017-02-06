#!/usr/bin/env bash
#
# Publish to bintray
#

./gradlew build

./gradlew lib:uploadArchives
./gradlew pivotaltracker-reporter:uploadArchives

./gradlew lib:bintrayUpload || echo "something happened uploading lib to bintray"
./gradlew pivotaltracker-reporter:bintrayUpload || echo "something happened uploading reporter to bintray"