../gradlew clean build -p ktor

JAR_FILE=ktor/build/libs/ktor-0.0.1.jar

docker build --build-arg JAR_FILE=$JAR_FILE -t ktor:latest .

# docker rm ktor
# docker rmi ktor:latest
# docker run -d --name ktor --network=host -m 512m --cpus=2 ktor:latest
# wrk -t 6 -c 500 -d 30s http://localhost:19005/ktor
