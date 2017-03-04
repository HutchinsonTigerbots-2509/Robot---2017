package org.usfirst.frc.team2509.robot.commands;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.usfirst.frc.team2509.robot.Robot;
import org.usfirst.frc.team2509.robot.RobotMap;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TestAuto extends Command {
	public Command filterTargets = new FilterGearTarget();
	private ArrayList<MatOfPoint>
		contours = new ArrayList<MatOfPoint>();
	private final CvSink
		CVSINK = CameraServer.getInstance().getVideo("GEAR");
	private final CvSource 
		OUTPUT_STREAM = CameraServer.getInstance().putVideo("ALT-Cam", 640, 480);
	private final Mat
		BINARY = new Mat(),
		CLUSTERS = new Mat(),
		HEIRARCHY = new Mat(),
		HSV = new Mat(),
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
		private final RobotDrive DT = RobotMap.DRIVETRAIN;
		private final ADXRS450_Gyro GYRO = RobotMap.DT_GYRO;
		private final DigitalInput SWITCH = RobotMap.GEAR_SWITCH;
		private Rect TARGET;
	    public TestAuto() {
	    	requires(Robot.driveTrain);
	    }

	    // Called just before this Command runs the first time 
	    protected void initialize() {
	    	
	    }
	    protected void execute() {
	    	if(Robot.isTeleop) end();
	    }
	    

	    // Make this return true when this Command no longer needs to run execute()
	    protected boolean isFinished() {
	        if(Timer.getMatchTime()>0&&Timer.getMatchTime()<14.5){
	        	return false;
	        }else{
	        	return true;
	        }
	    }

	    // Called once after isFinished returns true
	    protected void end() {
	    	DT.drive(0, 0);
	    }

	    // Called when another command which requires one or more of the same
	    // subsystems is scheduled to run
	    protected void interrupted() {
	    	end();
	    }
	}