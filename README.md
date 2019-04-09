1. Build and package the project using following command (verify your system has docker)
	mvn package docker:build -DskipTests
2. To execute the application in Docker use below command
	docker run -p <port>:9090 -t demo/shopping
3. Access the application http://<ipaddress>:<port>/swagger-ui.html