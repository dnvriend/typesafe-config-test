#!/bin/bash
export PERSON_FIRST_NAME="first name via environment var"
export PERSON_AGE="43"
export LUCKY_NUMBER_0="2"
export LUCKY_NUMBER_1="4"
export LUCKY_NUMBER_2="6"
sbt -Dperson.accessCodes.0="9" -Dperson.accessCodes.0="8" -Dperson.accessCodes.0="7" run