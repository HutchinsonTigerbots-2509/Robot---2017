package org.usfirst.frc2509.Robot.commands;

import com.ctre.CANTalon;

import org.usfirst.frc2509.Robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * @author Nate
 */
public class ShooterRun extends Command {
	private final CANTalon MOTOR = Robot.shooter.SHOOT;
    public ShooterRun() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	MOTOR.set(0.75);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	MOTOR.set(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
