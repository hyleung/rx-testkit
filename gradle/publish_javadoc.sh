#!/bin/bash
cd $TRAVIS_BUILD_DIR

if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
    cd build

    echo "Updating Javadoc..."
    git config --local user.email "travis@travis-ci.org"
    git config --local user.name "travis ci"

    rsync -r --delete ../docs/javadoc/ ./javadoc

    cd ../docs/javadoc

    printf "Date: $(date)\nBuild: $TRAVIS_BUILD_NUMBER"  > lastupdated
    git add .
    git commit -m "Javadoc for travis build: $TRAVIS_BUILD_NUMBER auto-pushed to master"

    echo "Pushing changes to remote"
    git push

    cd ..
else
    echo "Skipping Javadoc publish for $TRAVIS_BRANCH, $TRAVIS_BUILD_NUMBER"
fi
