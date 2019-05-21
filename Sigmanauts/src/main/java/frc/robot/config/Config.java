package frc.robot.config;

public class Config {
    // GAMEPAD CONFIG
    public static int GAMEPAD_driveJoystickId = 0;
    public static int GAMEPAD_driveForwardAxisId = 3;
    public static int GAMEPAD_driveReverseAxisId = 2;
    public static int GAMEPAD_driveTurnAxisId = 0;

    public static int GAMEPAD_driveQuickTurnButton = 1;
    public static int GAMEPAD_driveAutoButton = 2;

    
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

    public static double DRIVE_AUTO_maxSpeed = DRIVE_maxSpeed;
}