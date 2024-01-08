../gradlew clean build -p vertx

java -Xms128m -Xmx128m -XX:ActiveProcessorCount=2 -jar vertx/build/libs/vertx-0.0.1.jar
