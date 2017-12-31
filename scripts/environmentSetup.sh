#
#           Circle CI & gradle.properties live in harmony
#
# Android convention is to store your API keys in a local, non-versioned
# gradle.properties file. Circle CI doesn't allow users to upload pre-populated
# gradle.properties files to store this secret information, but instead allows
# users to store such information as environment variables.
#
# This script creates a local gradle.properties file on current the Circle CI
# instance. It then reads environment variable TEST_API_KEY_ENV_VAR which a user
# has defined in their Circle CI project settings environment variables, and
# writes this value to the Circle CI instance's gradle.properties file.
#
# You must execute this script via your circle.yml as a pre-process dependency,
# so your gradle build process has access to all variables.
#
#   dependencies:
#       pre:
#        - source environmentSetup.sh && copyEnvVarsToGradleProperties

#!/bin/bash

function copyEnvVarsToGradleProperties {
    PATH=$CIRCLE_WORKING_DIRECTORY"/.gradle/gradle.properties"

    if [ ! -f "$PATH" ]; then
        echo "Gradle Properties does not exist"
    fi
        echo "Creating Gradle Properties file..."
        cat > ${PATH}

        echo "Writing STORE_KEY to gradle.properties"
                echo "STORE_KEY=$STORE_KEY" >> ${PATH}

        echo "Writing API_KEY to gradle.properties..."
                echo "API_KEY=$API_KEY" >> ${PATH}

}

copyEnvVarsToGradleProperties