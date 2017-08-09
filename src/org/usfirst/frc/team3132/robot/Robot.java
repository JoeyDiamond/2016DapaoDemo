
package org.usfirst.frc.team3132.robot;

import org.usfirst.frc.lib.util.Latch;
import org.usfirst.frc.lib.util.LogitechGamepadF310;
import org.usfirst.frc.team3132.subsystems.Drivebase;
import org.usfirst.frc.team3132.subsystems.Shooter;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;

/*
Currently implemented functions:
+ Shooter wheels
+ Feeder solenoid
+ Drive
Currently unimplemented functions:
- Shooter ramp (I believe it's on PWM00 if you want to implement; check wiring though)
- Climb solenoid
- Shifter solenoid (drive only uses two motors, third is left idling)
*/

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	CANTalon tongue = new CANTalon(RobotMap.RAMP_MOTOR);
	
	DriverStation driverStation = DriverStation.getInstance();
	
	RobotDrive dapao;
	LogitechGamepadF310 driverGamepad;
	LogitechGamepadF310 operatorGamepad;
	Shooter shooter = new Shooter();
	Drivebase drivebase = new Drivebase();
	public DoubleSolenoid climber;
	
	Latch opEnabledLatch = new Latch();
	public boolean operatorEnabled = true;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	// config drivebase
    	drivebase.setLowGear();
    	
    	// config climber
    	climber = new DoubleSolenoid(RobotMap.CLIMBER_SOLENOID_A,RobotMap.CLIMBER_SOLENOID_B);
    	climber.set(DoubleSolenoid.Value.kForward);
    	
    	// config joysticks
    	driverGamepad = new LogitechGamepadF310(RobotMap.DRIVER_GAMEPAD_ID);
    	operatorGamepad = new LogitechGamepadF310(RobotMap.OPERATOR_GAMEPAD_ID);
    	
    	// config tongue
    	tongue.enableForwardSoftLimit(false);
    	tongue.enableReverseSoftLimit(false);
    }
    
    public void autonomousInit() {}
    public void autonomousPeriodic() {}

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
    	//Two-stick Arcade using a Gamepad
        drivebase.driveWheel(driverGamepad.getLeftYScaled(), driverGamepad.getRightXScaled(),true);
        System.out.println("left: " + driverGamepad.getLeftYScaled() + " right: " + driverGamepad.getRightXScaled());
    	
    	//drivebase.driveTank(driverGamepad.getLeftYScaled(), driverGamepad.getRightYScaled());
    	//System.out.println("left: " + driverGamepad.getLeftYScaled() + " right: " + driverGamepad.getRightYScaled());
    	
    	// operate drivetrain shifters
        if (driverGamepad.getGreenButton()){
        	drivebase.setLowGear();
        } else if (driverGamepad.getYellowButton()){
        	drivebase.setHighGear();
        }
        
        //Run shooter on left trigger
        if(fullShoot()){
        	shooter.setWheelSpeed(RobotMap.SHOOTER_SPEED_VBUS_FULL);
        } else if(medShoot()){
        	shooter.setWheelSpeed(RobotMap.SHOOTER_SPEED_VBUS_MED);
        } else if(lowShoot()){
        	shooter.setWheelSpeed(RobotMap.SHOOTER_SPEED_VBUS_SLOW);
        } else {
        	shooter.setWheelSpeed(0);
        }
        
        // leave pusher under frisbee unless trigger
        // trigger primes the shooter, fires when released
        if (driverGamepad.getTriggerRightBtn() || (operatorGamepad.getTriggerRightBtn() && operatorEnabled)) {
        	if(!shooter.wheelSpinning())
        		shooter.setWheelSpeed(RobotMap.SHOOTER_SPEED_VBUS_MED);
        	shooter.fireSolenoidReset();
        } else {
        	shooter.fireSolenoidFire();
        }
        
        // enable and disable operator control for demos
        if(opEnabledLatch.update(driverGamepad.getStartButton())){
        	operatorEnabled = !operatorEnabled;
        	System.out.println("operator now: " + operatorEnabled);
        }
        
        switch(driverGamepad.getPOVVal()){
        case 0: 
        	tongue.set(0.2);
        	break;
        case 180:
        	tongue.set(-0.2);
        	break;
        default:
        	tongue.set(0);
        }
        
        if(operatorGamepad.getYellowButton() && operatorEnabled){
        	climber.set(DoubleSolenoid.Value.kReverse);
        } else if(operatorGamepad.getGreenButton() && operatorEnabled){
        	climber.set(DoubleSolenoid.Value.kForward);
        }
        
    }
    
    /** This function is called periodically during test mode */
    public void testPeriodic() {}
    
    public boolean fullShoot(){
    	boolean enabled = driverGamepad.getTriggerLeftBtn() && driverGamepad.getLeftBumper();
    	return enabled && driverGamepad.getRedButton();
    }
    
    public boolean medShoot(){
    	return driverGamepad.getTriggerLeftBtn() || (operatorGamepad.getTriggerLeftBtn() && operatorEnabled);
    }
    
    public boolean lowShoot(){
    	return driverGamepad.getLeftBumper() || (operatorGamepad.getLeftBumper() && operatorEnabled);
    }
    
    
    public void disabledInit(){
    	System.out.println("disabled overloaded, yay :)");
    }
}
