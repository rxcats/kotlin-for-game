cd quarkus-resteasy-reactive

./gradlew clean build

java -Xms128m -Xmx128m -XX:ActiveProcessorCount=2 -jar build/quarkus-app/quarkus-run.jar

cd ..
