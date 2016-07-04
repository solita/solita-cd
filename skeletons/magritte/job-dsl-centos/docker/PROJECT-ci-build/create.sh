#!/bin/bash
set -eu

cd "$(dirname "$BASH_SOURCE")"
source ../../.magritte/util/docker.sh

name="$(basename "$(pwd)")"

if ! image_exists "$name"; then
  ../PROJECT-common/create.sh
  echo "Building image $name..."
  docker build -t $name .
fi

if ! container_exists "$name"; then
  echo "Creating container $name..."
  docker create -h $name --name $name \
    --security-opt seccomp=unconfined \
    --stop-signal=SIGRTMIN+3 \
    --tmpfs /run \
    --cap-add NET_ADMIN \
    -v /sys/fs/cgroup:/sys/fs/cgroup:ro \
    -v "$(cd ../.. && pwd):/pipeline" \
    -P \
    -t \
    $name \
    >/dev/null
fi
