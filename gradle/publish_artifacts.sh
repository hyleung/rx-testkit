#!/bin/bash
if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then 
    echo "Publishing artifacts to Sonatype OSS..." 
    ./gradlew upload -Psigning.keyId=$SIGNING_KEY -Psigning.password=$SIGNING_PASSWORD -Psigning.secretKeyRingFile=gradle/secring.gpg
else
    echo "Skipping artifact publish for $TRAVIS_BRANCH, $TRAVIS_BUILD_NUMBER"
fi
