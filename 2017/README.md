2016-2017 code for the McQuaid Jesuit IgKNIGHTERS FIRST Robotics team 3173

In order to find code for vision software, navigate to the folder titled "Nvidia..."
In order to find the actual robot code, navigate to "roboRio/IgKNIGHTers/src/org/usfirst/frc3173/IgKNGIHTers"
To get a working copy of the robot code open in eclipse, go to file>Open projects from File System, then navigate to "roboRio/IgKNIGHTers/src/org/usfirst/frc3173/" and select the IgKNIGHTers directory from this specified path
It may be neccessary to create a wpilib sample project (of any type) to change some factory setting within eclipse in order to eradicate errors in the setup above. If you have errors (not warnings, but errors) with imports in the code above do the following steps:
--> Go to: file>New>Project>WPILib Robot Java Development^Example Robot Java Project>'Next'>Actuators^Tank Drive>'Finish'
    --> Doing this step above should fix all errors in the robot code project (Especially those that stem from imports not being recognized, WPILIB imports that is)
        --> If you still have errors, you should make sure you have followed through firstscreensteps live and installed both WPILib libraries, and a recent JDK. Also make sure that you have the JDK checked and that you have selected 'apply' and not the JRE selected.