package org.usfirst.frc.team2509.robot;


import org.usfirst.frc.team2509.robot.commands.AutonomousCommand;
import org.usfirst.frc.team2509.robot.commands.ClimbUp;
import org.usfirst.frc.team2509.robot.commands.FilterGearTarget;
import org.usfirst.frc.team2509.robot.commands.OpDrive;
import org.usfirst.frc.team2509.robot.commands.ShooterRun;
import org.usfirst.frc.team2509.robot.commands.SweeperForward;
import org.usfirst.frc.team2509.robot.commands.SweeperReverse;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public Joystick OPSTICK;
    public Joystick COOPSTICK;
    public JoystickButton CLIMB;
    public JoystickButton FORWARD_SWEEP;
    public JoystickButton REVERSE_SWEEP;
    public JoystickButton SHOOT;
    public String defaultAuto = "Default";
	public String customAuto = "My Auto";
	public String autoSelected;
	public SendableChooser<String> chooser = new SendableChooser<>();

	public OI() {
        OPSTICK = new Joystick(0);
        COOPSTICK = new Joystick(1);
        // SmartDashboard Buttons
        SmartDashboard.putData("Climb Up", new ClimbUp());
        SmartDashboard.putData("OpDrive", new OpDrive());
        SmartDashboard.putData("Shooter Start", new ShooterRun());
        SmartDashboard.putData("ReverseSweep", new SweeperReverse());
        SmartDashboard.putData("Forward Sweep", new SweeperForward());
        SmartDashboard.putData("Filter", new FilterGearTarget());
        // Joystick Buttons
        CLIMB = new JoystickButton(OPSTICK, 2);
        CLIMB.whileHeld(new ClimbUp());
        SHOOT = new JoystickButton(OPSTICK, 1);
        SHOOT.whileHeld(new ShooterRun());
        FORWARD_SWEEP = new JoystickButton(COOPSTICK,3);
        FORWARD_SWEEP.whileHeld(new SweeperForward());
        REVERSE_SWEEP = new JoystickButton(COOPSTICK, 5);
        REVERSE_SWEEP.whileHeld(new SweeperReverse());
        //Autonomous Chooser
        chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
	}
    public Joystick getOpStick() {
        return OPSTICK;
    }
    public Joystick getCoopStick(){
    	return COOPSTICK;
    }
    public double getScaledX(){
		return (OPSTICK.getX()*((OPSTICK.getRawAxis(3)+3)*0.25));
	}
	public double getScaledY(){
		return (OPSTICK.getY()*((OPSTICK.getRawAxis(3)+3)*0.25));
	}
	public double getScaledZ(){
		return (OPSTICK.getZ()*((OPSTICK.getRawAxis(3)+3)*0.25));
	}
	public Command getAutonomous(String autoChoice){
		return new AutonomousCommand();
	}
}

