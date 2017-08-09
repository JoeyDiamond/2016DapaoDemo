package org.usfirst.frc.lib.util;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;

public class SmartCompressor {
	Compressor compressor;
	DriverStation ds = DriverStation.getInstance();
	protected boolean manuallyDisabled = false;
	
	public SmartCompressor(){
		compressor = new Compressor();
		compressor.setClosedLoopControl(true);
	}
	
	public void update(){
		if(ds.isBrownedOut() || manuallyDisabled){
			compressor.setClosedLoopControl(false);
			compressor.stop();
		} else {
			compressor.setClosedLoopControl(true);
		}
	}
	
	public void disable(){
		manuallyDisabled = true;
	}
	
	public void enable(){
		manuallyDisabled = false;
	}
	
}
