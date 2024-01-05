../gradlew clean build -p webflux

java -Xms128m -Xmx128m -XX:ActiveProcessorCount=2 -jar webflux/build/libs/webflux-0.0.1.jar
