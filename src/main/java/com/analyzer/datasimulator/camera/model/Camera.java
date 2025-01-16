package com.analyzer.datasimulator.camera.model;


public class Camera {
    public Camera(String name, String deviceType, String messageContent) {
        this.name = name;
        this.deviceType = deviceType;
        this.messageContent = messageContent;
    }


    private String name;
    private String deviceType;

    private String messageContent;

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
