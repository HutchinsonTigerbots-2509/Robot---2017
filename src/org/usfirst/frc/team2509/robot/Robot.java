package org.usfirst.frc.team2509.robot;

import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;

import com.ctre.CANTalon;

public class Robot extends IterativeRobot {
	Vision Vision;
	UsbCamera cam;
	Joystick stick;
	CANTalon m1, m2, m3, m4,m5;
	RobotDrive drive;
	
	public void robotInit() {
		cam = Vision.FRONT_CAM;
		stick= new Joystick(0);
		m1= new CANTalon(0);
		m2= new CANTalon(1);
		m2.setInverted(true);
		m3= new CANTalon(2);
		m4= new CANTalon(3);
		m4.setInverted(true);
		m5 = new CANTalon(4);
		drive= new RobotDrive(m1,m3,m2,m4);
	}

	public void autonomousInit() {
		drive.drive(0.5, 0);
		Timer.delay(2.0);
		drive.drive(0, 0);
	}

	public void autonomousPeriodic() {
		
	}

	public void teleopPeriodic(){
		
		drive.setSafetyEnabled(true);
		while(isEnabled()&& isOperatorControl()){
			procImg();
			drive.mecanumDrive_Cartesian(getScaledX(), getScaledY(), getScaledZ(), 0);
			if(stick.getRawButton(1)){
				m5.set(0.25);
			}else{
				m5.set(0);
			}
			if(stick.getRawButton(2)){
				m5.set(0);
			}
		}
	}

	public void testPeriodic() {
	}
	
	public double getScaledX(){
		return (stick.getX()*((stick.getRawAxis(3)+1.5)*0.4));
	}
	public double getScaledY(){
		return (stick.getY()*((stick.getRawAxis(3)+1.5)*0.4));
	}
	public double getScaledZ(){
		return (-stick.getZ()*((stick.getRawAxis(3)+1)*0.5));
	}
	public void procImg(){
			camera.setResolution(640, 480);
            CvSink cvSink = CameraServer.getInstance().getVideo();
            CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);            
            while(!Thread.interrupted()) {
                cvSink.grabFrame(source);
                Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                outputStream.putFrame(output);
            }
	}
}