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

# Sending Message
![image](https://github.com/user-attachments/assets/b0c00741-4164-4865-b40f-2e1b132a9d72) - Click the button from the player to trigger a message 

# Example of Message Sent

![image](https://github.com/user-attachments/assets/ac7a8438-50d4-4a5d-8fbd-7c825a237579)

# Object Detection
![detected](https://github.com/user-attachments/assets/72778681-ed9b-413a-b025-73c9b56e2f22)  
-The web cam is able to detect a moving object.

# Example of Detected Object Message Sent
![detected2](https://github.com/user-attachments/assets/a208b6f7-dcce-4d0b-85ab-0280af0cabec) - A message will be triggered and send to MQTT server
