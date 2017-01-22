package org.usfirst.frc.team2509.robot;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision{
	public UsbCamera frontCam;
	private Mat source, output;
	private CvSink cvSink;
	private CvSource outputStream;
	private boolean initialized = false;
	public void init(){
		frontCam = CameraServer.getInstance().startAutomaticCapture(0);
		frontCam.setResolution(640,480);
		source = new Mat();
		output = new Mat();
		cvSink = CameraServer.getInstance().getVideo(); 
		outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
		initialized = true;
	}
	public void cvt2Gray(){             
        if(initialized){
        	cvSink.grabFrame(source);
            Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
            outputStream.putFrame(output);
        }
        SmartDashboard.putBoolean("Camera Status:", initialized);
	}
}