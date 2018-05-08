// RobotBuilder Version: 1.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.
package org.usfirst.frc3173.IgKnighters2014.subsystems;
import org.usfirst.frc3173.IgKnighters2014.RobotMap;
import org.usfirst.frc3173.IgKnighters2014.commands.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc3173.IgKnighters2014.utilities.CANJaguar;
/**
 *
 */
public class Drives extends Subsystem {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    CANJaguar drivesRearLeft = RobotMap.drivesDrivesRearLeft;
    CANJaguar drivesRearRight = RobotMap.drivesDrivesRearRight;
    CANJaguar drivesFrontLeft = RobotMap.drivesDrivesFrontLeft;
    CANJaguar drivesFrontRight = RobotMap.drivesDrivesFrontRight;
    Relay shifter = RobotMap.drivesShifter;
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    boolean shiftStateLow=true;
    
    public static byte SYNCGROUP = 1;
    
    public Drives() {
//        drivesMotors.setExpiration(1000);
    }
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    public void setSpeed(final double left, final double right) {
        RobotMap.drivesDrivesFrontLeft.setX(-left, SYNCGROUP);
        RobotMap.drivesDrivesRearLeft.setX(-left, SYNCGROUP);
        RobotMap.drivesDrivesFrontRight.setX(right, SYNCGROUP);
        RobotMap.drivesDrivesRearRight.setX(right, SYNCGROUP);
    }
    
    public void outputShift(){
        if(shiftStateLow){
            SmartDashboard.putString("Shift State","Low");
        }
        else{
            SmartDashboard.putString("Shift State", "High");
        }
    }
    public void shift(boolean low){
        shiftStateLow=low;
        if(shiftStateLow){
           shifter.set(Relay.Value.kOff); //shifts to low gear
           outputShift();
        }
        else{
           shifter.set(Relay.Value.kForward); //shifts to high gear
           outputShift();
        }
    }
    
    public boolean getShift(){
        return shiftStateLow;
    }
    
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
        setDefaultCommand(new DriveWithJoysticks());
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
	
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
