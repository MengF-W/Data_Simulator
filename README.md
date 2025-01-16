# Overall Idea

![image](https://github.com/user-attachments/assets/786b1c3a-2cfe-456f-b0c2-4e147e8a59b0)
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

