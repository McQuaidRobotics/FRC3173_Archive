# FRC 2018

Team 3173's 2018 FRC robot code for Smokey. It's written in Java and based off of WPILib's Java control system.

## Projects (Eclipse IDE)
- There are two projects in this repository: IgKnighters2018, and COOKIECODE2018.
- COOKIECODE2018 is our testing project to deploy to our testing platform, Cookiebot.
 - IgKnighters2018 is our robot code that will be deployed to our competition robot.
 
## How do I set up my Eclipse project?
- Follow instructions on https://wpilib.screenstepslive.com/s/currentCS/m/getting_started/l/599679-installing-eclipse-c-java

## Code Highlights
- <insert cool stuff, we have no cool stuff right now>

## Package Functions
- org.usfirst.frc3173.IgKnighters2018.* [not in packages, (default)]

	Contains upper-level control-management classes such as the base class, Robot.java, and the parts reference class, RobotMap.java
	
- org.usfirst.frc3173.IgKnighters2018.commands

	Contains commands and command groups that can be added to the command scheduler for execution. For example, TeleOpManualDrive.java is the default command of the Drives that is executed constantly in TeleOp for Tele-Operation. It also contains simpler, organizational, one-time commands like AcquisitionStop.java
	
- org.usfirst.frc3173.IgKnighters2018.subsystems

	Contains the subsystems that are instantiated in the base class. These contain methods that actually interface with the robot parts for operation. Normally, these subsystems' methods will be called by commands.
	
- org.usfirst.frc3173.IgKnighters2018.utilities

	Contains relatively miscellaneous classes such as the Constants.java class, used for any constants that are used in the other parts of the code for easy debugging and user mutation.
