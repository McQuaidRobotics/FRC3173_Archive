This tutorial is an introduction to Mercurial, RobotBuilder, and the command-based
architecture.

You'll need [NetBeans with the FRC Plugins](http://wpilib.screenstepslive.com/s/3120/m/7885/l/79405-installing-the-java-development-tools) and [Mercurial](http://mercurial.selenic.com/downloads/).

This tutorial does *not* provide specific instructions on using Mercurial; see [our Mercurial documentation](Mercurial.md) for that.

In this tutorial, we will be adding holonomic drive to the drive subsystem. In
addition, we will make the trigger on the left joystick activate holonomic driving.

1. Clone the IgKnighters repository.
2. Look around the current code. 
   
   CommandBased code is divided into commands and subsystems, which are kept in
   org.usfirst.frc3173.IgKnighters2013.commands and
   org.usfirst.frc3173.IgKnighters2013.subsystems (hereafter referred to as
   'commands' and 'subsystems') respectively.
   
   TODO write more
   
2. Open the RobotBuilder by double-clicking the JAR file in the RobotBuilder
   folder. If prompted, click 'Open Existing Project' and select the
   IgKnighters 2013.yml file. 
3. Add a gyro.
4. Add a new command for holonomic driving.
   
   When I named the command for driving with joysticks I didn't think about the
   possibility that there might be more than one way to drive with joysticks.
   Pick a better name for your command.
   (Don't try to rename DriveWithJoysticks--it gets very complecated.)
5. Add a trigger, and configure it to use the new command.
4. Save and export to Java.
   
   This would also be a good place to commit.
6. In NetBeans, add code to the Drive subsystem for holonomic drives.
   (The 2011 code may be helpful here. Note that the gyro will be available
   in RobotMap. You may also want to consult the existing mecanumDrive() method)
7. Add code to your new command to run Drives. (See the DriveWithJoysticks command
   for hints.)

