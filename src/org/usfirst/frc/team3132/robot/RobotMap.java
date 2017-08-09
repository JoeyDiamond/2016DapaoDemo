package org.usfirst.frc.team3132.robot;

public class RobotMap {
	// drivetrain
	public static final int DRIVE_LEFT_FRONT_MOTOR_ID = 4;
	public static final int DRIVE_LEFT_BACK_MOTOR_ID = 8;
	public static final int DRIVE_LEFT_TOP_MOTOR_ID = 5;
	public static final int DRIVE_RIGHT_FRONT_MOTOR_ID = 7;
	public static final int DRIVE_RIGHT_BACK_MOTOR_ID = 9;
	public static final int DRIVE_RIGHT_TOP_MOTOR_ID = 6;
	public static final int SHIFTER_SOLENOID_A = 0;
	public static final int SHIFTER_SOLENOID_B = 1;
	
	// shooter
	public static final int SHOOTER_A_ID = 6;
	public static final int SHOOTER_B_ID = 7;
	//Change this to change shooter speed!
	public static final double SHOOTER_SPEED_VBUS_SLOW = 0.35;
	public static final double SHOOTER_SPEED_VBUS_MED = 0.42;
	public static final double SHOOTER_SPEED_VBUS_FULL = 1;
	public static final int FEEDER_SOLENOID_EXTEND_ID = 5;
	public static final int FEEDER_SOLENOID_RETRACT_ID = 4;
	public static final int RAMP_MOTOR = 5;		// CAN bus ID
	
	// climber
	public static final int CLIMBER_SOLENOID_A = 2;
	public static final int CLIMBER_SOLENOID_B = 3;
	
	// controllers
	public static final int DRIVER_GAMEPAD_ID = 0;
	public static final int OPERATOR_GAMEPAD_ID = 1;

}
