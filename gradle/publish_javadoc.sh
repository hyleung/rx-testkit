#!/bin/bash

if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
    echo "Cloning gh-pages..."
    cd build
    git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/hyleung/rx-testkit gh-pages 
    
    cd gh-pages
    git config --local user.email "travis@travis-ci.org"
    git config --local user.name "travis ci"

    rsync -r --delete ../docs/javadoc/ ./javadoc

    printf "Date: $(date)\nBuild: $TRAVIS_BUILD_NUMBER"  > lastupdated
    git add -u .
    git add .

    git commit -m "Javadoc for travis build: $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"

    echo "Pushing changes to remote"
    git push

    cd ..
    rm -rf gh-pages
else
    echo "Skipping Javadoc publish for $TRAVIS_BRANCH, $TRAVIS_BUILD_NUMBER"
fi
