package org.usfirst.frc.team2509.robot;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * ORGANIZE VARIABLES IN THIS ORDER(ALPHABETICALLY)
 * 1. JAVA
 * 2. FIRST/WPI
 * 3. OPENCV
 * @author Nate
 *
 */

public class Vision{
	
	/**
	 * BEGIN VARIABLES
	 */
	
	private boolean 
		initialized = false;
	protected final double
		CAMERA_ANGLE = 15,
		CAMERA_OFFSET_FRONT = 0,
		CAMERA_OFFSET_CENTER = 0,
		VERTICAL_FOV = 30.25,
		HORIZANTAL_FOV = 53.75;
	protected final int
		CAMERA_HEIGHT = 0,
		BOILER_HEIGHT = 88,
		BOILER_WIDTH = 15,
		GEAR_PEG_HEIGHT =16;
	public final UsbCamera 
		FRONT_CAM = CameraServer.getInstance().startAutomaticCapture(0);;
	private final CvSink
		CVSINK = CameraServer.getInstance().getVideo();
	private final CvSource 
		OUTPUT_STREAM = CameraServer.getInstance().putVideo("Blur", 640, 480);
	private final Mat
		OUTPUT = new Mat(),
		SOURCE = new Mat();
	protected final Scalar 
	//Thresholds values
		LOWER_BOUNDS = new Scalar(0,0,0), 
		UPPER_BOUNDS = new Scalar(0,0,0);
	protected final Size 
		RESIZE = new Size(320,240);
	
	
	/**
	 * BEGIN METHODS
	 */
	
	public void init(){
		FRONT_CAM.setResolution(640,480);
		initialized = true;
	}
	public void cvt2Gray(){             
        if(initialized){
        	CVSINK.grabFrame(SOURCE);
            Imgproc.cvtColor(SOURCE, OUTPUT, Imgproc.COLOR_BGR2GRAY);
            OUTPUT_STREAM.putFrame(OUTPUT);
        }
        SmartDashboard.putBoolean("Camera Status:", initialized);
	}
}