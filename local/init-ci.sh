#!/bin/bash

set -eu
set -x

repo_path="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd .. && pwd )"

cd $repo_path/local
vagrant destroy -f ci
vagrant up ci

