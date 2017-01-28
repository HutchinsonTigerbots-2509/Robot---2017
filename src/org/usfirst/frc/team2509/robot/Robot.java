package org.usfirst.frc.team2509.robot;

import com.ctre.CANTalon;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

public class Robot extends IterativeRobot {
	Vision Vision;
	UsbCamera cam;
	Joystick stick;
	CANTalon m1, m2, m3, m4,m5;
	RobotDrive drive;
	
	public void robotInit() {
		Vision = new Vision();
		//Vision2 = new Vision2();
		
		cam = Vision.FRONT_CAM;
		cam.setBrightness(30);
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
		while(isEnabled()){
			//Vision.cvt2Gray();}
			Vision.Procces();
		}
	}

	public void autonomousPeriodic() {
		
	}

	public void teleopPeriodic(){
		//Vision.Procces();
		drive.setSafetyEnabled(true);
		while(isEnabled()&& isOperatorControl()){
			drive.mecanumDrive_Cartesian(getScaledX(), getScaledY(), getScaledZ(), 0);
			if(stick.getRawButton(1)){
				m5.set(0.25);
			}else{
				m5.set(0);
			}
			if(stick.getRawButton(2)){
				m5.set(0);
			}
			if(stick.getRawButton(12)){
				Vision.Procces();
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
	
}