../gradlew clean build -p undertow

JAR_FILE=undertow/build/libs/undertow-0.0.1.jar

docker build --build-arg JAR_FILE=$JAR_FILE -t undertow:latest .

# docker rm undertow
# docker rmi undertow:latest
# docker run -d --name undertow --network=host -m 512m --cpus=2 undertow:latest
# wrk -t 6 -c 500 -d 30s http://localhost:19001/undertow
