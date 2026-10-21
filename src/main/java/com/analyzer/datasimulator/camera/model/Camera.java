package com.analyzer.datasimulator.camera.model;


import java.time.LocalDateTime;

public class Camera {
    public Camera(String name, String deviceType, String messageContent, byte[] mediaContent, LocalDateTime timeStamp) {
        this.name = name;
        this.deviceType = deviceType;
        this.messageContent = messageContent;
        this.mediaContent = mediaContent;
        this.timeStamp = timeStamp;
    }


    private String name;
    private String deviceType;
    private String messageContent;
    private byte[] mediaContent;
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

    public byte[] getMediaContent() {return mediaContent;}

    public void setMediaContent(byte[] mediaContent) {this.mediaContent = mediaContent;}

    public LocalDateTime getTimeStamp() {return timeStamp;}

    public void setTimeStamp(LocalDateTime timeStamp) {this.timeStamp = timeStamp;}
}
