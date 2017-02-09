package org.usfirst.frc2509.Robot.commands;

import com.ctre.CANTalon;

import org.usfirst.frc2509.Robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

//This took Alex and Mason 3hrs to do, and it would have taken Nate 5 minutes. This should tell you out


public class ClimbUp extends Command {
	private CANTalon motor = RobotMap.tenzingNorgay;
    public ClimbUp() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	motor.set(0.5);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	motor.set(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
