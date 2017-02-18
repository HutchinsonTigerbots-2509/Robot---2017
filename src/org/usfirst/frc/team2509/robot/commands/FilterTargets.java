package org.usfirst.frc.team2509.robot.commands;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.usfirst.frc.team2509.robot.Robot;
import org.usfirst.frc.team2509.robot.RobotMap;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class FilterTargets extends Command {
		private final ArrayList<MatOfPoint> 
		contours = new ArrayList<MatOfPoint>();
		public int 
		CENTER[],
		WIDTH[];
	public final boolean 
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
	public  UsbCamera 
	//IP FOR STREAM http://roborio-2509-frc.local:1181/?action=stream
		GEAR_CAM = RobotMap.GEAR_CAM;
	private final CvSink
		CVSINK = CameraServer.getInstance().getVideo();
	private final CvSource 
		OUTPUT_STREAM = CameraServer.getInstance().putVideo("ALT-Cam", 640, 480);
	public Mat 
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

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.vision.filterImage();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	SmartDashboard.putInt("CENTER", Robot.vision.TARGET.width);
	

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
