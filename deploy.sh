#!/bin/bash

. .env

mvn clean deploy -e -Prelease -s $MVN_SETTINGS &
