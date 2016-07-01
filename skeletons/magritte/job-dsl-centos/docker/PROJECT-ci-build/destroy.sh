#!/bin/bash
set -eu

cd "$(dirname "$BASH_SOURCE")"
source ../../.magritte/util/docker.sh

name="$(basename "$(pwd)")"

if container_exists "$name"; then
  ./stop.sh
  echo "Removing container $name..."
  docker rm "$name" >/dev/null
fi

if image_exists "$name"; then
  echo "Removing image $name..."
  docker rmi "$name" >/dev/null
fi
