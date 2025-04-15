package com.analyzer.datasimulator.camera.model;


import java.time.LocalDateTime;

public class Camera {
    public Camera(String name, String deviceType, String messageContent, LocalDateTime timeStamp) {
        this.name = name;
        this.deviceType = deviceType;
        this.messageContent = messageContent;
        this.timeStamp = timeStamp;
    }


    private String name;
    private String deviceType;

    private String messageContent;

    private LocalDateTime timeStamp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }





}
