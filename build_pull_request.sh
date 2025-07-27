#!/bin/bash

set -e

git config --global user.name "github-actions"
git config --global user.email "bot@users.noreply.github.com"

git clone --quiet "https://x-access-token:${GITHUB_TOKEN}@github.com/${GITHUB_REPOSITORY}.git"
cd "$(basename "$GITHUB_REPOSITORY")"

git checkout -b test || git checkout test
git push origin test
