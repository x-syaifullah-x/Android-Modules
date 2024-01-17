#! /bin/bash

firebase target:apply hosting x-recaptcha-x x-recaptcha-x
firebase deploy --only hosting:x-recaptcha-x