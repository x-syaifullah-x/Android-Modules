#! /bin/bash

# Authentication
kill -9 $(lsof -t -i tcp:9099) &>/dev/null
# Functions
kill -9 $(lsof -t -i tcp:5001) &>/dev/null
# Firestore
kill -9 $(lsof -t -i tcp:8080) &>/dev/null
# Database
kill -9 $(lsof -t -i tcp:9000) &>/dev/null
# Hosting
kill -9 $(lsof -t -i tcp:5000) &>/dev/null
# Pub/Sub
kill -9 $(lsof -t -i tcp:8085) &>/dev/null
# Storage
kill -9 $(lsof -t -i tcp:9199) &>/dev/null
# Eventarc
kill -9 $(lsof -t -i tcp:9299) &>/dev/null

# export GOOGLE_APPLICATION_CREDENTIALS="${PWD}/adminsdk.json"

IMPORT_DIR=/tmp/firebase/emulators/exports
[ ! -d $IMPORT_DIR ] && mkdir -p $IMPORT_DIR

exec firebase emulators:start --import=$IMPORT_DIR --export-on-exit