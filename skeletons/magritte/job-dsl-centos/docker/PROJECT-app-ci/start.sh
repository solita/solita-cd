#!/bin/bash
set -eu

cd "$(dirname "$BASH_SOURCE")"
source ../../.magritte/util/docker.sh

name="$(basename "$(pwd)")"

if ! container_running "$name"; then
  ./create.sh
  echo "Starting container $name..."
  docker start "$name" >/dev/null
  ../../imagination/update-inventory
fi
