package org.usfirst.frc.team3132.subsystems;

import org.usfirst.frc.team3132.robot.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Shooter {
	
	protected CANTalon shooterA = new CANTalon(RobotMap.SHOOTER_A_ID);
	protected CANTalon shooterB = new CANTalon(RobotMap.SHOOTER_B_ID);
	protected DoubleSolenoid feeder;
	protected double previousShotSpeed = 0;
	
	public Shooter(){
		shooterA.setInverted(true);
		shooterA.changeControlMode(TalonControlMode.PercentVbus);
	/*	shooterA.changeControlMode(TalonControlMode.Speed);
		shooterA.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		shooterA.configEncoderCodesPerRev(360);
		shooterA.setPID(RobotMap.SHOOTER_KP, RobotMap.SHOOTER_KI, RobotMap.SHOOTER_KD);
		*/
		shooterB.changeControlMode(TalonControlMode.Follower);
		shooterB.set(shooterA.getDeviceID());
		
		feeder = new DoubleSolenoid(RobotMap.FEEDER_SOLENOID_EXTEND_ID,RobotMap.FEEDER_SOLENOID_RETRACT_ID);
    	
	}
	
	public void shoot(){
		if(Math.abs(shooterA.get()) > 0.2){
			fireSolenoidFire();
		}
	}
	
	public void setWheelSpeed(double speed){
	/*	if(speed == 0){
			if(shooterA.getControlMode() == TalonControlMode.Speed)
				shooterA.changeControlMode(TalonControlMode.PercentVbus);
			shooterA.set(0);
		} else {
			previousShotSpeed = speed;
			if(shooterA.getControlMode() == TalonControlMode.PercentVbus)
				shooterA.changeControlMode(TalonControlMode.Speed);
			shooterA.set(speed);
		} */
		shooterA.set(speed);
	}
	
	public void fireSolenoidFire(){
		feeder.set(DoubleSolenoid.Value.kForward);
	}
	
	public void fireSolenoidReset(){
		feeder.set(DoubleSolenoid.Value.kReverse);
	}
	
	public boolean wheelSpinning(){
		return Math.abs(shooterA.get()) > 0.2;
	}
	
}
