package org.usfirst.frc.team3132.subsystems;

import org.usfirst.frc.team3132.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.VictorSP;

public class Drivebase {

	DriverStation ds = DriverStation.getInstance();
	
	// voltage ramping and traction control
	protected boolean tractionControl = false;
	protected double allowedChangePerCycle = 0.1;
	protected double leftPrevVal = 0.0;
	protected double rightPrevVal = 0.0;
	
	// voltage protection
	protected boolean genericTopMotorDisabled = false;
	protected boolean voltLimitDisableMotor = false;
	protected double voltageThreshhold = 11.0;
	
	// drive motors
	protected VictorSP leftFrontMotor;
	protected VictorSP leftBackMotor;
	protected VictorSP leftTopMotor;
	protected VictorSP rightFrontMotor;
	protected VictorSP rightBackMotor;
	protected VictorSP rightTopMotor;
	
	// gearbox shifter
	protected DoubleSolenoid shifter;
	
	// wheel drive calculations
	protected double oldTurn = 0;
	
	public Drivebase(){
		// config drivetrain
		leftFrontMotor = new VictorSP(7);
		leftBackMotor = new VictorSP(9);
		leftTopMotor = new VictorSP(5);
		rightFrontMotor = new VictorSP(4);
		rightBackMotor = new VictorSP(8);
		rightTopMotor = new VictorSP(6);
		
		rightFrontMotor.setInverted(true);
		leftFrontMotor.setInverted(true);
		rightTopMotor.setInverted(true);
		rightBackMotor.setInverted(true);
		
		// config gearbox shifters
		shifter = new DoubleSolenoid(RobotMap.SHIFTER_SOLENOID_A,RobotMap.SHIFTER_SOLENOID_B);
		
	}
	
	public void driveTank(double left, double right){
		setLeft(left);
		setRight(right);
	}
	
	public void driveWheel(double move, double turn, boolean quickTurn){
		double left = 0;
        double right = 0;
        double leftOverpower = 0;
        double rightOverpower = 0;
        double overPowerGain = 0.1;
        boolean useOverpower = false;

        // calculate negative inertia to help robot anticipate driver actions
        /*double negIntertia = turn - oldTurn;
        oldTurn = turn;
        */

        if(quickTurn){
            left = move + turn;
            right = move - turn;
        } else {
            left = move + Math.abs(move)*turn;
            right = move - Math.abs(move)*turn;
        }

        if(!useOverpower){
            setLeft(left);
            setRight(right);
            return;
        }

        if(left > 1){
            leftOverpower = left - 1;
            left = 1;
        }else if(left < -1){
            leftOverpower = left + 1;
            left = -1;
        }

        if(right > 1){
            rightOverpower = right - 1;
            right = 1;
        } else if(right < -1){
            rightOverpower = right + 1;
            right = -1;
        }

        left = left - rightOverpower*overPowerGain;
        right = right - leftOverpower*overPowerGain;

        setLeft(left);
        setRight(right);
	}
	
	public void setHighGear(){
		shifter.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void setLowGear(){
		shifter.set(DoubleSolenoid.Value.kForward);
	}
	
	protected void setLeft(double val){
		if(tractionControl){
			if(Math.abs(val - leftPrevVal) > allowedChangePerCycle){
				val += allowedChangePerCycle*Math.signum(val);
			}
			leftPrevVal = val;
		}
		
		leftFrontMotor.set(val);
		leftBackMotor.set(val);
		
		if(genericTopMotorDisabled)
			val = 0;
		if(voltLimitDisableMotor){
			if(ds.isBrownedOut() || ds.getBatteryVoltage() < voltageThreshhold){
				val = 0;
			}
		}
		leftTopMotor.set(val);
	}
	
	protected void setRight(double val){
		if(tractionControl){
			if(Math.abs(val - rightPrevVal) > allowedChangePerCycle){
				val += allowedChangePerCycle*Math.signum(val);
			}	
			rightPrevVal = val;
		}
		
		rightFrontMotor.set(val);
		rightBackMotor.set(val);
		
		if(genericTopMotorDisabled)
			val = 0;
		if(voltLimitDisableMotor){
			if(ds.isBrownedOut() || ds.getBatteryVoltage() < voltageThreshhold){
				val = 0;
			}
		}
		rightTopMotor.set(val);
	}
}
