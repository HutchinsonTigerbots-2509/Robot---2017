package org.usfirst.frc2509.Robot.subsystems;

import com.ctre.CANTalon;

import org.usfirst.frc2509.Robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Sweeper extends Subsystem {
	public final CANTalon MOTOR = RobotMap.SWEEP_MOTOR;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

