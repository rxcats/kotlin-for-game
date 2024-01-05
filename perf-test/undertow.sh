../gradlew clean build -p undertow

java -Xms128m -Xmx128m -XX:ActiveProcessorCount=2 -jar undertow/build/libs/undertow-0.0.1.jar
