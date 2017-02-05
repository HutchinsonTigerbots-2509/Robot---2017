package org.usfirst.frc.team2509.robot;

import com.ctre.CANTalon;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

public class Robot extends IterativeRobot {
	Vision VISION;
	UsbCamera CAM;
	Joystick STICK;
	CANTalon MOTOR1, MOTOR2, MOTOR3, MOTOR4,MOTOR5;
	RobotDrive DRIVE;
	
	public void robotInit() {
		VISION = new Vision();
		//Vision2 = new Vision2();
		
		CAM = VISION.FRONT_CAM;
		CAM.setBrightness(30);
		STICK= new Joystick(0);
		MOTOR1= new CANTalon(0);
		MOTOR2= new CANTalon(1);
		MOTOR2.setInverted(true);
		MOTOR3= new CANTalon(2);
		MOTOR4= new CANTalon(3);
		MOTOR4.setInverted(true);
		MOTOR5 = new CANTalon(4);
		DRIVE= new RobotDrive(MOTOR1,MOTOR3,MOTOR2,MOTOR4);
		VISION.Procces();
	}

	public void autonomousInit() {
		while(isEnabled()){
			//VISION.cvt2Gray();
		}
	}

	public void autonomousPeriodic() {

		VISION.cvt2Gray();
	}

	public void teleopPeriodic(){
		//VISION.Procces();
		DRIVE.setSafetyEnabled(true);
		while(isEnabled()&& isOperatorControl()){
			DRIVE.mecanumDrive_Cartesian(getScaledX(), getScaledY(), getScaledZ(), 0);
			if(STICK.getRawButton(1)){
				MOTOR5.set(0.25);
			}else{
				MOTOR5.set(0);
			}
			if(STICK.getRawButton(2)){
				MOTOR5.set(0);
			}
			if(STICK.getRawButton(12)){
				VISION.Procces();
			}
			
		}
	}

	public void testPeriodic() {
	}
	
	public double getScaledX(){
		return (STICK.getX()*((STICK.getRawAxis(3)+1.5)*0.4));
	}
	public double getScaledY(){
		return (STICK.getY()*((STICK.getRawAxis(3)+1.5)*0.4));
	}
	public double getScaledZ(){
		return (-STICK.getZ()*((STICK.getRawAxis(3)+1)*0.5));
	}

}