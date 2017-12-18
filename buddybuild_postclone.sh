#!/usr/bin/env bash

echo "Deploying to bintray/jcenter..."

sh scripts/cp-env-to-properties.sh
sh scripts/deploy.sh

#if [[ "$BUDDYBUILD_BRANCH" =~ "feature/setup-buddybuild" ]]; then
#
#else
#    echo "This wasn't a release branch!"
#fi