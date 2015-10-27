#!/bin/bash

set -eu
set -x

script_path="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $script_path
vagrant destroy -f ci
vagrant up ci
