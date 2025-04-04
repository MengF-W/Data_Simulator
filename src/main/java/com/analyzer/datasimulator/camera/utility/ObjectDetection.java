package com.analyzer.datasimulator.camera.utility;

import com.analyzer.datasimulator.messenger.processor.DeviceMessageProcessor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ObjectDetection {

    @Autowired
    DeviceMessageProcessor deviceMessageProcessor;

    public Mat processFrame(Mat videoFrame,BackgroundSubtractorMOG2 mog2){

        Mat foreGroundMask=new Mat();
        mog2.apply(videoFrame, foreGroundMask,0.005);

        Mat erode = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(
                8, 8));
        Mat dilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                new Size(8, 8));

        Mat openElem = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                new Size(3, 3), new Point(1, 1));
        Mat closeElem = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                new Size(7, 7), new Point(3, 3));

        Imgproc.threshold(foreGroundMask, foreGroundMask, 80, 255, Imgproc.THRESH_BINARY);

        Imgproc.morphologyEx(foreGroundMask, foreGroundMask, Imgproc.MORPH_OPEN, erode);
        Imgproc.morphologyEx(foreGroundMask, foreGroundMask, Imgproc.MORPH_OPEN, dilate);
        Imgproc.morphologyEx(foreGroundMask, foreGroundMask, Imgproc.MORPH_OPEN, openElem);
        Imgproc.morphologyEx(foreGroundMask, foreGroundMask, Imgproc.MORPH_CLOSE, closeElem);

        return foreGroundMask;
    }

    public void detectObject(Mat foregroundMask,Mat videoFrame) throws MqttException {

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchey = new Mat();
        Imgproc.findContours(foregroundMask, contours, hierarchey, Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_SIMPLE);

        double maxArea = 300;
        int maxAreaIdx;
        Rect rect;
        ArrayList<Rect> rect_array = new ArrayList<>();

        if(!contours.isEmpty())
        {
            deviceMessageProcessor.publishObjectDetectedMessage();

            for(int index = 0; index < contours.size(); index++)
            {
                Mat contour = contours.get(index);
                double contourArea = Imgproc.contourArea(contour);
                if(contourArea > maxArea)
                {
                    maxAreaIdx = index;
                    rect = Imgproc.boundingRect(contours.get(maxAreaIdx));
                    rect_array.add(rect);
                }
            }

            if(!rect_array.isEmpty())
            {
                for (Rect obj : rect_array) {
                    Imgproc.rectangle(videoFrame, obj.br(), obj.tl(),
                            new Scalar(0, 255, 0), 2);
                }
            }

        }

    }
}
