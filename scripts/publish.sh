#!/usr/bin/env bash
#
# Publish to bintray
#

gw clean build lib:install lib:bintrayUpload --info
