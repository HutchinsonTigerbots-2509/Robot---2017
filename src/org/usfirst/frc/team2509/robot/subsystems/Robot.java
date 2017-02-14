package org.usfirst.frc.team2509.robot.subsystems;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private final ArrayList<MatOfPoint> 
	contours = new ArrayList<MatOfPoint>();
private final boolean 
	isINIT = true;
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
	FRAME_RATE = 0,
	GEAR_PEG_HEIGHT =16;
public final UsbCamera 
//IP FOR STREAM http://roborio-2509-frc.local:1181/?action=stream
	FRONT_CAM = CameraServer.getInstance().startAutomaticCapture();
private final CvSink
	CVSINK = CameraServer.getInstance().getVideo();
private final CvSource 
	OUTPUT_STREAM = CameraServer.getInstance().putVideo("ALT-Cam", 640, 480);
public final Mat 
	ALT = new Mat(),
	BINARY = new Mat(),
	CLUSTERS = new Mat(),
	CONTOURS = new Mat(),
	HEIRARCHY = new Mat(),
	HSV = new Mat(),
	OUTPUT = new Mat(),
	SOURCE = new Mat(),
	THRESH = new Mat();
protected final Scalar 
//COLOR VALUES
	BLACK = new Scalar(0,0,0),
	BLUE = new Scalar(255, 0, 0),
	GREEN = new Scalar(0, 255, 0),
	RED = new Scalar(0, 0, 255),
	YELLOW = new Scalar(0, 255, 255),
	//Thresholds values
	LOWER_BOUNDS = new Scalar(180,190,40),
	UPPER_BOUNDS = new Scalar(200,210,60);
protected final Size 
	RESIZE = new Size(320,240);

	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		vision();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
	public void vision(){
		while(isINIT){
	    	contours.clear();
			CVSINK.grabFrame(SOURCE);
			Imgproc.cvtColor(SOURCE, HSV, Imgproc.COLOR_BGR2RGB);
			Imgproc.threshold(HSV, BINARY, 185, 190, Imgproc.THRESH_BINARY);	
			Imgproc.cvtColor(BINARY, dst, code);
			Imgproc.cvtColor(BINARY, THRESH, Imgproc.COLOR_BGR2GRAY);
			//Core.inRange(THRESH	, LOWER_BOUNDS, UPPER_BOUNDS, CLUSTERS);	
	        Imgproc.findContours(THRESH, contours, HEIRARCHY, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
	        for(MatOfPoint mop :contours){
				Rect rec = Imgproc.boundingRect(mop);
				Imgproc.rectangle(SOURCE, rec.br(), rec.tl(), RED);
			
			
			}
			if(contours.size()==1){
				Rect rec = Imgproc.boundingRect(contours.get(0));
				Point center = new Point(rec.br().x-rec.width / 2.0 - 15,rec.br().y - rec.height / 2.0);
				Point centerw = new Point(rec.br().x-rec.width / 2.0 - 15,rec.br().y - rec.height / 2.0 - 20);
				SmartDashboard.putString("Bottom Right:", ""+(Point)rec.br());
				SmartDashboard.putString("Top Left", ""+(Point)rec.tl());
				SmartDashboard.putDouble("Center", rec.x);
				
				Imgproc.putText(SOURCE, ""+(Point)rec.br(), center, Core.FONT_HERSHEY_PLAIN, 1, BLACK);
				Imgproc.putText(SOURCE, ""+(Point)rec.tl(), centerw, Core.FONT_HERSHEY_PLAIN, 1, BLACK);
			}
			OUTPUT_STREAM.putFrame(BINARY);
	}}
}

