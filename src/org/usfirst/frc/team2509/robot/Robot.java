


package org.usfirst.frc.team2509.robot;

import org.usfirst.frc.team2509.robot.commands.AutonomousCommand;
import org.usfirst.frc.team2509.robot.commands.FilterGearTarget;
import org.usfirst.frc.team2509.robot.commands.GyroTurn;
import org.usfirst.frc.team2509.robot.commands.OpDrive;
import org.usfirst.frc.team2509.robot.commands.SweeperForward;
import org.usfirst.frc.team2509.robot.subsystems.Climb;
import org.usfirst.frc.team2509.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2509.robot.subsystems.Shooter;
import org.usfirst.frc.team2509.robot.subsystems.Sweeper;
import org.usfirst.frc.team2509.robot.subsystems.Vision;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    public Command 
    	autonomousCommand,
    	climbUp,
    	dropGear,
    	filterGearTarget,
    	gyroTurn,
    	opDrive,
    	shoot,
    	sweepForward;
    public Joystick 
    	OpStick,
    	CoopStick;
    public static OI oi;
    public static Climb climb;
    public static DriveTrain driveTrain;
    public static Shooter shooter;
    public static Sweeper sweeper;
    public static Vision vision;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	RobotMap.init();
        climb = new Climb();
        driveTrain = new DriveTrain();
        shooter = new Shooter();
        sweeper = new Sweeper();
        vision = new Vision();
        vision.initDefaultCommand();
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();
        // instantiate the command used for the autonomous period
        autonomousCommand = new AutonomousCommand();
        filterGearTarget = new FilterGearTarget();
        gyroTurn = new GyroTurn();
        opDrive = new OpDrive();
        sweepForward = new SweeperForward();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){
    }

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
    	if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
		
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
  //      if (autonomousCommand != null) autonomousCommand.isCanceled();
        if(isEnabled()&&isOperatorControl()) opDrive.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	Scheduler.getInstance().run();
      //  SmartDashboard.putDouble("Encoder", RobotMap.SHOOT_MOTOR.getEncVelocity());
        SmartDashboard.putInt("POV", OpStick.getPOV());
        SmartDashboard.putDouble("GYRO ANGLE" , RobotMap.DT_GYRO.getAngle());
        
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
        opDrive.start();
    }
}
