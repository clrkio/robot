package frc.robot.config;

public class Config {
    // GAMEPAD CONFIG
    public static int GAMEPAD_driveJoystickId = 0;

    public static int GAMEPAD_driveTurnAxisId = 0; //left joystick x-axis 
    public static int GAMEPAD_axis1 = 1; //left joystick y-axis
    public static int GAMEPAD_driveReverseAxisId = 2; //left trigger
    public static int GAMEPAD_driveForwardAxisId = 3; //right trigger 
    public static int GAMEPAD_axis4 = 4; //right joystick x-axis 
    public static int GAMEPAD_cargoIntakeArmAxisId = 5; //right joystick y-axis 

    public static int GAMEPAD_driveQuickTurnButton = 1; //A
    public static int GAMEPAD_driveAutoButton = 2; //B
    public static int GAMEPAD_3 = 3; //X
    public static int GAMEPAD_4 = 4; //Y 
    public static int GAMEPAD_cargoIntakeRollerIn = 5; //LB 
    public static int GAMEPAD_cargoIntakeRollerOut = 6; //RB 
    public static int GAMEPAD_7 = 7; //BACK 
    public static int GAMEPAD_8 = 8; //START 
    public static int GAMEPAD_ledOn = 9; //Press the left joystick 
    public static int GAMEPAD_ledOff = 10; //Press the right joystick 

    
    // CAN IDs
    public static int CAN_leftDrivePrimary = 1;
    public static int CAN_leftDriveFollowerA = 2;
    public static int CAN_leftDriveFollowerB = 3;

    public static int CAN_rightDrivePrimary = 4;
    public static int CAN_rightDriveFollowerA = 5;
    public static int CAN_rightDriveFollowerB = 6;


    // DRIVE CONFIG
    public static boolean DRIVE_rightIsInverted = false;
    public static double DRIVE_maxSpeed = .25;
    public static double DRIVE_minSpeed = -.25;
    public static double DRIVE_driveMultiplier = .25; 
    public static double DRIVE_turnMultiplier = .75; 
    public static double DRIVE_quickTurnMultiplier = -.25; 
    public static double DRIVE_autoDriveMultiplier = .1; 
    public static double DRIVE_autoTurnMultiplier = 1;
    public static double DRIVE_autoMaxSpeed = .15; 
    public static double DRIVE_autoMinSpeed = 0; 

    // CARGO INTAKE CONFIG 
    public static int CARGO_INTAKE_rollerPort = 0; 
    public static int CARGO_INTAKE_armPort = 1; 
    public static double CARGO_INTAKE_rollerSpeedIn = 0.25; 
    public static double CARGO_INTAKE_rollerSpeedOut = -0.25; 
    public static double CARGO_INTAKE_maxArmMinSpeed = 0.1; 
    public static double CARGO_INTAKE_maxArmMaxSpeed = 0.25;
    public static double CARGO_INTAKE_armMultiplier = .25; 

}