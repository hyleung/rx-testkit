#!/bin/bash

echo "Cloning gh-pages..."
cd build
git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/hyleung/rx-testkit gh-pages 
cp -r doc/javadoc gh-pages
cd gh-pages
git config --local user.email "travis@travis-ci.org"
git config --local user.name "travis ci"

echo "Date: $(date)\nBuild: $TRAVIS_BUILD_NUMBER"  > lastupdated
git add -f .
git commit -m "Javadoc for travis build: $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"

echo "Pushing changes to remote"
git push

cd ..
rm -rf gh-pages
