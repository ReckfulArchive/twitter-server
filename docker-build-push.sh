#!/bin/bash

echo "Building..."
./gradlew clean build

if [ $? -eq 0 ]; then
  echo "Building the image..."
  docker build -t registry.internal.arpa:80/reckful-archive/twitter-server:latest .

  echo "Publishing the image to the registry..."
  docker image push registry.internal.arpa:80/reckful-archive/twitter-server:latest
  echo "The image has been pushed."
else
  echo "Build failed, aborting."
fi
