cd quarkus-resteasy-reactive

./gradlew clean build

cd ..

JAR_FILE=quarkus-resteasy-reactive/build/quarkus-app

docker build -f Dockerfile-quarkus --build-arg JAR_FILE=$JAR_FILE -t quarkus:latest .

# docker rm quarkus
# docker rmi quarkus:latest
# docker run -d --name quarkus --network=host -m 512m --cpus=2 quarkus:latest
# wrk -t 6 -c 500 -d 30s http://localhost:19003/quarkus
