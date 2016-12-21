#!/usr/bin/env bash
#
# Publish to bintray
#

gw build
gw lib:uploadArchives
gw lib:bintrayUpload
