#!/bin/bash

echo "input module name (directory name):"
read -r MODULE_NAME

if [[ "$MODULE_NAME" == "" ]]; then
  echo "require module name!"
  exit 1
fi

PACKAGE_NAME="${MODULE_NAME}"
PACKAGE_NAME=$(echo "${PACKAGE_NAME}" | sed 's/-//g')
PACKAGE_NAME=$(echo "${PACKAGE_NAME}" | sed 's/_//g')

MAIN_PROPERTIES_FILE="${MODULE_NAME}/src/main/resources/application.properties"
TEST_PROPERTIES_FILE="${MODULE_NAME}/src/test/resources/application.properties"

PARENT_GROUP_ID="io.github.rxcats"

BUILD_SCRIPT_FILE="${MODULE_NAME}/build.gradle.kts"

BUILD_SCRIPT_BODY=$(cat <<EOL
dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
EOL
)

# prepare src directories
mkdir -p "${MODULE_NAME}/src/main/kotlin/io/github/rxcats"
mkdir -p "${MODULE_NAME}/src/main/resources"
mkdir -p "${MODULE_NAME}/src/test/kotlin/io/github/rxcats"
mkdir -p "${MODULE_NAME}/src/test/resources"

# prepare application.properties
echo "" > "${MAIN_PROPERTIES_FILE}"
echo "" > "${TEST_PROPERTIES_FILE}"

# prepare pom.xml
echo "${BUILD_SCRIPT_BODY}" > "${BUILD_SCRIPT_FILE}"
