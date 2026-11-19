#!/usr/bin/env bash

SPRING_PROFILES_ACTIVE="$1" ./gradlew :app:bootRun
