package frc.robot.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import edu.wpi.first.wpilibj.Preferences;
import frc.robot.Logger;

public class Config {
    private static Logger logger = new Logger(Config.class.getSimpleName());
    // GAMEPAD CONFIGS
    public static final int AXIS_leftXJoyStick = 0; //left joystick x-axis
    public static final int AXIS_leftYJoyStick = 1; //left joystick y-axis
    public static final int AXIS_leftTrigger = 2; //left trigger 
    public static final int AXIS_rightTrigger = 3; //right trigger
    public static final int AXIS_rightXJoyStick = 4; //right joystick x-axis
    public static final int AXIS_rightYJoyStick = 5; //right joystick y-axis

    public static final int BUTTON_A = 1; //A button 
    public static final int BUTTON_B = 2; //B button 
    public static final int BUTTON_X = 3; //X button 
    public static final int BUTTON_Y = 4; //Y button 
    public static final int BUTTON_LB = 5; //LB button 
    public static final int BUTTON_RB = 6; //RB button 
    public static final int BUTTON_BACK = 7; //Back button 
    public static final int BUTTON_START = 8; //Start button 
    public static final int BUTTON_leftJoyStick = 9; //Press the left joystick 
    public static final int BUTTON_rightJoyStick = 10; //Press the right joystick 

    public static final int POV_Up = 0;
    public static final int POV_Right = 90;
    public static final int POV_Down = 180;
    public static final int POV_Left = 270;


    public static final double MIN_AXIS_VALUE_FOR_PRESS = 0.1;

    // GAMEPAD CONFIG
    public static final int GAMEPAD_driverJoystickId = 0;
    public static final int GAMEPAD_playerJoystickId = 1;
    public static final int GAMEPAD_playerEleJoystickId = 2;
	
    public static final int GAMEPAD_driveTurnAxisId = AXIS_leftXJoyStick; 
    public static final int GAMEPAD_driveReverseAxisId = AXIS_leftTrigger; 
    public static final int GAMEPAD_driveForwardAxisId = AXIS_rightTrigger; 
    public static final int GAMEPAD_driveTurnInPlaceModeButton = BUTTON_A; 
    public static final int GAMEPAD_driveHighSpeedButton = BUTTON_Y;
    public static final int GAMEPAD_driveLowSpeedButton = BUTTON_B;
    public static final int GAMEPAD_driveAutoButton = BUTTON_LB;
    public static final int GAMEPAD_driveFastTurnModeButton = BUTTON_RB; 
    public static final int GAMEPAD_driveToddlerModeButton = BUTTON_LB; 
    public static final int GAMEPAD_driveBrakeModeButton = BUTTON_X; 

    public static final int GAMEPAD_driveToggleIdleModeButton = BUTTON_BACK;

    public static final int GAMEPAD_wristUpButton = BUTTON_RB;
    public static final int GAMEPAD_wristDownAxis = AXIS_rightTrigger;
    public static final int GAMEPAD_cargoIntakeRollerInButton = BUTTON_LB;
    public static final int GAMEPAD_cargoIntakeRollerOutAxis = AXIS_leftTrigger;

    public static final int GAMEPAD_hatchExtendPOV = POV_Left;
    public static final int GAMEPAD_hatchRetractPOV = POV_Right;
    public static final int GAMEPAD_hatchIntakeInPOV = POV_Down;
    public static final int GAMEPAD_hatchIntakeOutPOV = POV_Up;
    
    public static final int GAMEPAD_elevatorUpButton = BUTTON_Y;
    public static final int GAMEPAD_elevatorDownButton = BUTTON_A;
    public static final int GAMEPAD_elevatorCargoSkipUpButton = BUTTON_B; 
    public static final int GAMEPAD_elevatorHatchSkipUpButton = BUTTON_X; 

    public static final int GAMEPAD_zeroEncoders = BUTTON_START;

    public static final int GAMEPAD_elevatorAxisId = AXIS_rightYJoyStick;
    public static final int GAMEPAD_wristAxisId = AXIS_leftYJoyStick;

    public static final int GAMEPAD_togglePhotoelectric = BUTTON_BACK; 

    public static final int GAMEPAD_ledOn = BUTTON_leftJoyStick; 
    public static final int GAMEPAD_ledOff = BUTTON_rightJoyStick; 

    public static final int GAMEPAD_elevatorCargoLoadButton = AXIS_leftTrigger; 
    public static final int GAMEPAD_elevatorRocketCargoLowButton = BUTTON_RB; 
    public static final int GAMEPAD_elevatorHatchLowButton = AXIS_rightTrigger; 
    public static final int GAMEPAD_elevatorCargoShipScoreButton = BUTTON_LB; 
    public static final int GAMEPAD_elevatorRocketCargoMidButton = BUTTON_Y; 
    public static final int GAMEPAD_elevatorHatchMidButton = BUTTON_B; 
    public static final int GAMEPAD_elevatorRocketCargoHighButton = BUTTON_X; 
    public static final int GAMEPAD_elevatorHatchHighButton = BUTTON_A; 

    // CAN IDs
    public static final int CAN_leftDrivePrimary = 4;
    public static final int CAN_leftDriveFollowerA = 5;
    public static final int CAN_leftDriveFollowerB = 6;

    public static final int CAN_rightDrivePrimary = 1;
    public static final int CAN_rightDriveFollowerA = 2;
    public static final int CAN_rightDriveFollowerB = 3;

    public static final int CAN_elevatorLeft = 12; 
    public static final int CAN_elevatorRight = 11; 
    public static final int CAN_cargoIntake = 7;
    public static final int CAN_hatchIntake = 14;
    public static final int CAN_wrist = 15;
    

    // SOLENOID IDs
    public static final int SOLENOID_driveLowSpeed = 6;
    public static final int SOLENOID_driveHighSpeed = 7;
    public static final int SOLENOID_hatchRetract = 1;
    public static final int SOLENOID_hatchExtend = 0;

    // CAMERA CONFIG 
    public static final int CAMERA_frontUsbPort = 0;
    public static final int CAMERA_bottomUsbPort = 1;
    public static double CAMERA_frontResX = 320; 
    public static double CAMERA_frontResY = 240;
    public static double CAMERA_frontFPS = 20;
    public static double CAMERA_bottomResX = 320; 
    public static double CAMERA_bottomResY = 240; 
    public static double CAMERA_bottomFPS = 10;

    // DRIVE CONFIG
    public static boolean DRIVE_disableNeoInHighSpeed = false;
    public static boolean DRIVE_startInHighSpeed = false; 
    public static boolean DRIVE_startInBrakeMode = false;
    public static double DRIVE_driveMultiplier = .65; 
    public static double DRIVE_turnMultiplier = .65; 
    public static double DRIVE_fastTurnSpeedMultiplier = 1.0;
    public static double DRIVE_fastTurnTurnConstant = 2;  
    public static double DRIVE_turnInPlaceMultiplier = -.4; 
    public static double DRIVE_turnInPlaceSpeedMultiplier = .08; 
    public static double DRIVE_toddlerModeSpeedMultiplier = .7; 
    public static double DRIVE_toddlerModeTurnMultiplier = .7; 
    public static double DRIVE_isFastSpeed = .6; 

    // CARGO INTAKE CONFIG
    public static final int CARGO_INTAKE_photoelectricDIO = 2; 
    public static double CARGO_INTAKE_cargoLoadTimeThreshold = 1.0; 
    public static double CARGO_INTAKE_rollerInSpeed = 0.60; 
    public static double CARGO_INTAKE_rollerOutSpeed = -0.80; 
    public static double CARGO_INTAKE_rollerOutFastSpeed = -1.0; 
    public static double CARGO_INTAKE_holdSpeed = 0.15; 

    // WRIST CONFIG
    public final static int WRIST_backstopDIO = 7;

    public static double WRIST_lowerSpeed = -0.1; 
    public static double WRIST_raiseSpeed = 0.3;
    public static double WRIST_holdSpeedUp = 0.08;
    public static double WRIST_holdSpeedCargo = 0.05;
    public static double WRIST_holdSpeedFlat = 0.05;
    public static double WRIST_holdSpeedLoad = 0.1;
    public static double WRIST_speedMultiplier = -.25;

    public static double WRIST_positionUp = 0;
    public static double WRIST_positionCargo = -1000;
    public static double WRIST_positionFlat = -2500;
    public static double WRIST_positionLoad = -2900;
    public static double WRIST_acceptableError = 100;

    public static boolean WRIST_skipDown = false; 

    // WRIST MOTION MAGIC CONFIGS
    public final static int WRIST_kSlotIdx = 0;
    public final static int WRIST_kPIDLoopIdx = 0;
    public final static int WRIST_kTimeoutMs = 0;
    public static boolean WRIST_usePID = true;
    public static double WRIST_kP = 0.7;
    public static double WRIST_kI = 0;
    public static double WRIST_kD = 0;
    public static double WRIST_kF = 0;
    public static double WRIST_kIzone = 0;
    public static double WRIST_kPeakOutput = 0.2;
    public static double WRIST_kCruiseVelocity = 700;
    public static double WRIST_kAccelerationNumSec = 0.75;

    // HATCH INTAKE CONFIG
    public static final int HATCH_INTAKE_leftSwitchDIO = 0; 
    public static final int HATCH_INTAKE_rightSwitchDIO = 1;
    public static double HATCH_INTAKE_rollerSpeedIn = 0.4; 
    public static double HATCH_INTAKE_rollerSpeedOut = -0.6;
    public static double HATCH_INTAKE_rollerSpeedHold = 0.1;

    public static boolean hatchExtendDisabled = false;
    public static boolean forceLeftSwithOn = false;

    // ELEVATOR CONFIG 
    public static final int ELEVATOR_bottomSwitchDIO = 5; 
    public static final int ELEVATOR_topSwitchDIO = 4; 

    public static double ELEVATOR_speedMultiplier = -.5;
    public static double ELEVATOR_encoderPerInch = 430;

    
    public static double ELEVATOR_heightCargoLoadGround = 1;
    public static double ELEVATOR_heightCargoScore = 22.5;
    public static double ELEVATOR_heightHatchLow = 15.5;
    public static double ELEVATOR_heightHatchMid = 47;
    public static double ELEVATOR_heightHatchHigh = 70;
    public static double ELEVATOR_heightCargoRocketLow = 7.5;
    public static double ELEVATOR_heightCargoRocketMid = 36.5;
    public static double ELEVATOR_heightCargoRocketHigh = ELEVATOR_heightHatchHigh;
    
    public static boolean ELEVATOR_IGNORE_HATCH = false;


    public static double ELEVATOR_acceptableError = 200;
    public static double ELEVATOR_upSpeed = 0.4;
    public static double ELEVATOR_downSpeed = -0.2;
    public static double ELEVATOR_holdSpeed = 0.08;

    public static final int ELEVATOR_cargoLoad = 1; 
    public static final int ELEVATOR_rocketCargoLow = 2; 
    public static final int ELEVATOR_hatchLow = 3; 
    public static final int ELEVATOR_cargoShipScore = 4; 
    public static final int ELEVATOR_rocketCargoMid = 5; 
    public static final int ELEVATOR_hatchMid = 6; 
    public static final int ELEVATOR_rocketCargoHigh = 7; 
    public static final int ELEVATOR_hatchHigh = 8; 
    public static final int ELEVATOR_manual = 9;
    public static final boolean ELEVATOR_useNewCargoHigh = true;

    // ELEVATOR MOTION MAGIC CONFIGS
    public final static int ELEVATOR_kSlotIdx = 0;
    public final static int ELEVATOR_kPIDLoopIdx = 0;
    public final static int ELEVATOR_kTimeoutMs = 0;
    public static boolean ELEVATOR_usePID = true;
    public static double ELEVATOR_kP = .6;
    public static double ELEVATOR_kI = 0;
    public static double ELEVATOR_kD = 0;
    public static double ELEVATOR_kF = 0;
    public static double ELEVATOR_kIzone = 0;
    public static double ELEVATOR_kPeakOutput = 0.2;
    public static double ELEVATOR_kCruiseVelocity = 2500;
    public static double ELEVATOR_kAccelerationNumSec = 0.75;


    // LIMELIGHT CONFIG
    public static final int LIMELIGHT_LED_ON = 3; 
    public static final int LIMELIGHT_LED_OFF = 1;

    public static void updateConfig(Preferences prefs) {
        logger.log("Updating config");

        Field[] fields = Config.class.getDeclaredFields();
        for (Field f : fields) {
            PrefTypes prefType = getFieldPrefType(f);
            if (isFieldUpdatable(f.getModifiers()) && prefType != null) {
                logger.log(String.format("Will update: %s [%s]", f.getName(), prefType));
                try {
                    switch(prefType) {
                        case STRING: {
                            String currV = f.get(null).toString();
                            String newV = prefs.getString(f.getName(), currV);
                            if (!currV.equals(newV)) {
                                f.set(null, newV);
                                logger.log("Updated to: " + newV);
                            }
                        } break;
                        case DOUBLE: {
                            double currV = f.getDouble(null);
                            double newV = prefs.getDouble(f.getName(), currV);
                            
                            if (currV != newV) {
                                f.setDouble(null, newV);
                                logger.log("Updated to: " + newV);
                            }
                        } break;
                        case BOOLEAN: {
                            boolean currV = f.getBoolean(null);
                            boolean newV = prefs.getBoolean(f.getName(), currV);
                            
                            if (currV != newV) {
                                f.setBoolean(null, newV);
                                logger.log("Updated to: " + newV);
                            }
                        } break;
                        default:
                            continue;
                    }
                } catch (Exception e) {
                    logger.log("Exception when trying to update configs: " + e);
                    return;
                }
            }
        }
    }

    enum PrefTypes {
        STRING,
        DOUBLE,
        BOOLEAN
    }

    private static PrefTypes getFieldPrefType(Field f) {
        String fieldType = f.getType().getSimpleName();
        if (fieldType.equalsIgnoreCase("boolean")) {
            return PrefTypes.BOOLEAN;
        }
        if (fieldType.equalsIgnoreCase("double")) {
            return PrefTypes.DOUBLE;
        }
        if (fieldType.equalsIgnoreCase("string")) {
            return PrefTypes.STRING;
        }
        return null;
    }

    private static boolean isFieldUpdatable(int modifer) {
        return !Modifier.isFinal(modifer) && Modifier.isPublic(modifer);
    }
}