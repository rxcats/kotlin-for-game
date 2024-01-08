../gradlew clean build -p ktor

java -Xms128m -Xmx128m -XX:ActiveProcessorCount=2 -jar ktor/build/libs/ktor-0.0.1.jar
