#!/bin/bash

git pull

./gradlew clean
./gradlew build
./gradlew jlink
build/image/bin/weatherApp
