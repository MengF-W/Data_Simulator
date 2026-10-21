# Overall Idea

![image](https://github.com/user-attachments/assets/9bde98a4-4076-417e-a573-c4e66da02955)
The idea is to monitor and analsyt MQTT enabled devices by collect messages and feed into data pipeline


# Data_Simulator Description
The data simulator application act as a message publisher simulator. Currently the web camera simulate as a MQTT enabled devices and publish a message to the MQTT broker.

# Libraries used
* Spring Boot
* Eclipse Paho Java MQTT
* OpenCV Java
* JavaFX

# Packaging Command
`mvn clean package` - To clean and create JAR

# Execute Command
`java -jar Data_Simulator-1.0-SNAPSHOT.jar` - To run the application

# Docker Image Command
`docker build -t data_simulator .`    -To build the docker image

# Docker Container Command
`docker-compose up -d`      -To start the docker container from the docker image with the docker compose file configuration  

# Sending Message
![image](https://github.com/user-attachments/assets/b0c00741-4164-4865-b40f-2e1b132a9d72) - Click the button from the player to trigger a message 

# Example of Message Sent

![image](https://github.com/user-attachments/assets/ac7a8438-50d4-4a5d-8fbd-7c825a237579)

# Object Detection
![detected](https://github.com/user-attachments/assets/72778681-ed9b-413a-b025-73c9b56e2f22)  
-The web cam is able to detect a moving object.

# Example of Detected Object Message Sent
![detected2](https://github.com/user-attachments/assets/a208b6f7-dcce-4d0b-85ab-0280af0cabec) - A message will be triggered and send to MQTT server

# Publish
It has been published as a service and it publish messages to online MQTT broker server.

# MQTT Broker Server Option
* Public Server : Public MQTT Broker Server such as [Mosquitto](https://test.mosquitto.org/) is available and it is free to test. Clients can read or publish to public topics. Therefore, it is not safe or private for sensitive data
* Private Host Server : The MQTT Broker can be deployed into cloud service provider such as [Render](https://render.com/deploy-docker/eclipse-mosquitto). After testing find that there is message packet size limitation (for about 5000 bytes length) in Render host service and the client receiver's connection will disconnect when receive message. The max_packet_size setting does not affect it. The explanation from Render about the connection can drop due to: <br />

-Instance lifecycle events<br />
	Render periodically replaces instances during deploys or maintenance. When an instance shuts down, all its WebSocket connections close — even mid-transfer.<br />
<br />
-Network interruptions<br />
	Outbound IP routing changes can reset TCP connections to external resources. From your service's perspective, this looks like any transient network issue.<br />

* Cloud's Managed MQTT Platform : Cloud's Managed MQTT Platform provider such as [EMQX](https://www.emqx.com/en/cloud) that has Free Tier usage service with limited quota
