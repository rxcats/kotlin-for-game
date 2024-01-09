../gradlew clean build -p webflux

JAR_FILE=webflux/build/libs/webflux-0.0.1.jar

docker build --build-arg JAR_FILE=$JAR_FILE -t webflux:latest .

# docker rm webflux
# docker rmi webflux:latest
# docker run -d --name webflux --network=host -m 512m --cpus=2 webflux:latest
# wrk -t 6 -c 500 -d 30s http://localhost:19002/webflux
