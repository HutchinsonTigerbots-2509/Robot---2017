package org.usfirst.frc.team2509.robot;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class Vision{
	public final UsbCamera 
		frontCam = CameraServer.getInstance().startAutomaticCapture(0);
	private final Mat
		source = new Mat(),
		output = new Mat();
	public void procImg(){
		new Thread(()->{
			frontCam.setResolution(640, 480);
            CvSink cvSink = CameraServer.getInstance().getVideo();
            CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);            
            while(!Thread.interrupted()) {
                cvSink.grabFrame(source);
                Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                outputStream.putFrame(output);
            }
		}).start();
	}
}