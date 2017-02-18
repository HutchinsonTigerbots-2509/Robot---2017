/**
 * @author Tigerbots
 */
package org.usfirst.frc.team2509.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    public static CANTalon CLIMB_MOTOR;
    public static Talon CLIMB_ALT;
    public static CANTalon DT_LEFTFRONT;
    public static CANTalon DT_RIGHTFRONT;
    public static CANTalon DT_LEFTREAR;
    public static CANTalon DT_RIGHTREAR;
    public static AnalogGyro DT_GYRO;
    public static RobotDrive DRIVETRAIN;
    public static UsbCamera GEAR_CAM;
    public static UsbCamera SHOOT_CAM;
    public static CANTalon SHOOT_MOTOR;
    public static Encoder SHOOT_ENCODER;
    public static Talon SHOOT_KICKER;
    
    public static Talon SWEEP_MOTOR;

    public static void init() {
    	CLIMB_MOTOR = new CANTalon(5);
        LiveWindow.addActuator("Climb", "Motor", CLIMB_MOTOR);
        CLIMB_ALT = new Talon(2);

        DT_LEFTFRONT = new CANTalon(0);
        LiveWindow.addActuator("DriveTrain", "DT_LEFTFRONT", DT_LEFTFRONT);
        
        DT_RIGHTFRONT = new CANTalon(1);
        LiveWindow.addActuator("DriveTrain", "DT_RIGHTFRONT", DT_RIGHTFRONT);
        
        DT_LEFTREAR = new CANTalon(2);
        LiveWindow.addActuator("DriveTrain", "DT_LEFTREAR", DT_LEFTREAR);
        
        DT_RIGHTREAR = new CANTalon(3);
        LiveWindow.addActuator("DriveTrain", "DT_RIGHTREAER", DT_RIGHTREAR);
        
        DT_GYRO = new AnalogGyro(0);
        DT_GYRO.initGyro();
        DT_GYRO.calibrate();
        LiveWindow.addSensor("Gyro", 0, DT_GYRO);
                
        DRIVETRAIN = new RobotDrive(DT_LEFTFRONT, DT_LEFTREAR,DT_RIGHTFRONT, DT_RIGHTREAR);
        DRIVETRAIN.setSafetyEnabled(false);
        DRIVETRAIN.setExpiration(1.0);
        DRIVETRAIN.setSensitivity(1.0);
        DRIVETRAIN.setMaxOutput(1.0);
        DRIVETRAIN.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        DRIVETRAIN.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
            
        GEAR_CAM = CameraServer.getInstance().startAutomaticCapture("GEAR", 0);
        GEAR_CAM.setBrightness(30);
        
        SHOOT_CAM = CameraServer.getInstance().startAutomaticCapture("SHOOTER", 1);
        SHOOT_CAM.setBrightness(30);
        
        SHOOT_KICKER = new Talon(0);
        SHOOT_KICKER.setInverted(true);
        LiveWindow.addActuator("Shooter", "Kicker", SHOOT_KICKER);
        
        SHOOT_MOTOR = new CANTalon(8);
        SHOOT_MOTOR.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        SHOOT_MOTOR.reverseSensor(false);
        SHOOT_MOTOR.configNominalOutputVoltage(+0.0f, -0.0f);
        SHOOT_MOTOR.configPeakOutputVoltage(+12.0f, -12.0f);
        SHOOT_MOTOR.setProfile(0); 
        //SHOOT_MOTOR.setF();
        //SHOOT_MOTOR.setP();
        //SHOOT_MOTOR.setI();
        //SHOOT_MOTOR.setD();
        LiveWindow.addActuator("Shooter", "Motor", (CANTalon) SHOOT_MOTOR);
                
        SWEEP_MOTOR = new Talon(1);
        LiveWindow.addActuator("Sweeper", "Motor", (Talon) SWEEP_MOTOR);
        
    }
}
