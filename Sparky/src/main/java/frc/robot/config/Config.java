package frc.robot.config;


public class Config {
    public static final int GAMEPAD_leftXJoyStick = 0; //left joystick x-axis
    public static final int GAMEPAD_leftYJoyStick = 1; //left joystick y-axis
    public static final int GAMEPAD_leftTrigger = 2; //left trigger 
    public static final int GAMEPAD_rightTrigger = 3; //right trigger
    public static final int GAMEPAD_rightXJoyStick = 4; //right joystick x-axis
    public static final int GAMEPAD_rightYJoyStick = 5; //right joystick y-axis

    public static final int GAMEPAD_A = 1; //A button 
    public static final int GAMEPAD_B = 2; //B button 
    public static final int GAMEPAD_X = 3; //X button 
    public static final int GAMEPAD_Y = 4; //Y button 
    public static final int GAMEPAD_LB = 5; //LB button 
    public static final int GAMEPAD_RB = 6; //RB button 
    public static final int GAMEPAD_BACK = 7; //Back button 
    public static final int GAMEPAD_START = 8; //Start button 
    public static final int GAMEPAD_leftJoyStick = 9; //Press the left joystick 
    public static final int GAMEPAD_rightJoyStick = 10; //Press the right joystick 
    // GAMEPAD CONFIG
    public static final int GAMEPAD_driverJoystickId = 0;
    public static final int GAMEPAD_playerJoystickId = 1; 

    public static int GAMEPAD_driveTurnAxisId = GAMEPAD_leftXJoyStick; 
    public static int GAMEPAD_driveReverseAxisId = GAMEPAD_leftTrigger; 
    public static int GAMEPAD_driveForwardAxisId = GAMEPAD_rightTrigger; 
    public static int GAMEPAD_driveQuickTurnButton = GAMEPAD_A; 
    public static int GAMEPAD_driveAutoButton = GAMEPAD_B; 

    public static int GAMEPAD_cargoIntakeArmAxisId = GAMEPAD_rightYJoyStick; 
    public static int GAMEPAD_cargoIntakeRollerIn = GAMEPAD_leftTrigger; 
    public static int GAMEPAD_cargoIntakeRollerOut = GAMEPAD_rightTrigger; 
    public static int GAMEPAD_ledOn = GAMEPAD_leftJoyStick; 
    public static int GAMEPAD_ledOff = GAMEPAD_rightJoyStick;  

    
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
    public static int CARGO_INTAKE_photoelectricPort = 0; 
    public static double CARGO_INTAKE_rollerSpeedIn = 0.25; 
    public static double CARGO_INTAKE_rollerSpeedOut = -0.25; 

    // ARM CONFIG
    public static int ARM_armPort = 1; 
    public static double ARM_maxArmMinSpeed = 0.1; 
    public static double ARM_maxArmMaxSpeed = 0.25;
    public static double ARM_armMultiplier = .25; 
    public static double ARM_armUpPosition = 10; 
    public static double ARM_armDownPosition = 0; 
    public static double ARM_armStop = 0; 

    // HATCH INTAKE CONFIG
    public static int HATCH_INTAKE_rollerPort = 2; 
    public static int HATCH_INTAKE_leftSolenoidPort = 0; 
    public static int HATCH_INTAKE_rightSolenoidPort = 1; 
    public static int HATCH_INTAKE_leftSwitchPort = 1; 
    public static int HATCH_INTAKE_rightSwitchPort = 2; 
    public static int HATCH_INTAKE_in = 90; 
    public static int HATCH_INTAKE_out = 270; 

    // LIMELIGHT CONFIG
    public static int LIMELIGHT_LED_ON = 3; 
    public static int LIMELIGHT_LED_OFF = 1; 

}