/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.HashMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Logger;
import frc.robot.Robot;
import frc.robot.commands.Wrist.WristControlCommand;
import frc.robot.config.Config;
import frc.robot.impls.SmartDashboardSubsystem;

/**
 * Add your docs here.
 */
public class Wrist extends SmartDashboardSubsystem {
  private static final double RAD_IN_90 = 1.5708;

  private static Logger logger = new Logger(Wrist.class.getSimpleName());

  enum Direction {
    UP, DOWN, HOLD
  }

  enum States {
    UP,
    CARGO_SHIP,
    FLAT,
    LOAD,
    MANUAL, 
    ZEROING
  }

  class StateData {
    public double targetPosition;
    public States raiseState;
    public States lowerState;
    public double holdSpeed;

    public StateData(double t, States r, States l, double h) {
      targetPosition = t;
      raiseState = r;
      lowerState = l;
      holdSpeed = h;
    }
  }

  private HashMap<States, StateData> stateData;

  private WPI_TalonSRX wristMotor; // right joystick

  private double setSpeed;
  private double autoSpeed;
  private States targetState;
  private Direction direction;
  private DigitalInput upLimitSwitch;
  
  public Wrist() {
    setupStateData();
    wristMotor = new WPI_TalonSRX(Config.CAN_wrist);
    wristMotor.setNeutralMode(NeutralMode.Brake);
    wristMotor.setSensorPhase(true);
    wristMotor.setInverted(false);
    
    upLimitSwitch = new DigitalInput(Config.WRIST_backstopDIO);
    setupMotionMagic();

    targetState = States.MANUAL;
    direction = Direction.HOLD;
    setSpeed = 0;
    autoSpeed = 0;
  }

  private void setupStateData() {
    stateData = new HashMap<>();
    stateData.put(States.LOAD,
      new StateData(Config.WRIST_positionLoad, States.FLAT, States.LOAD, Config.WRIST_holdSpeedLoad));

    stateData.put(States.FLAT, 
      new StateData(Config.WRIST_positionFlat, States.UP, States.LOAD, Config.WRIST_holdSpeedFlat));

    stateData.put(States.CARGO_SHIP, 
      new StateData(Config.WRIST_positionCargo, States.UP, States.FLAT, Config.WRIST_holdSpeedCargo));

    stateData.put(States.UP, 
      new StateData(Config.WRIST_positionUp, States.UP, States.CARGO_SHIP, Config.WRIST_holdSpeedUp));
  
    stateData.put(States.MANUAL, 
      new StateData(Config.WRIST_positionUp, States.UP, States.FLAT, Config.WRIST_holdSpeedUp));

    stateData.put(States.ZEROING, 
      new StateData(Config.WRIST_positionUp, States.UP, States.FLAT, Config.WRIST_holdSpeedUp));
  }

  public double getWristPosition() {
    return wristMotor.getSelectedSensorPosition();
  }

  public void move(double speed) {
    if (Math.abs(speed) > 0.05) {
      targetState = States.MANUAL;
      setSpeed = speed;
      wristMotor.configForwardSoftLimitEnable(false);
      wristMotor.configReverseSoftLimitEnable(false);
    } else if (targetState == States.ZEROING) {
      return;
    } else if (targetState != States.MANUAL) {
      if (Config.WRIST_usePID) {
        wristMotor.configForwardSoftLimitEnable(true);
        wristMotor.configReverseSoftLimitEnable(true);
        wristMotor.set(ControlMode.MotionMagic,
                       getTargetPosition(), DemandType.ArbitraryFeedForward, getFeedForward());
        return;
      }
      // old school auto code...
      setSpeed = getAutoSpeed();
    }

    double position = getWristPosition(); 
    if (isUpLimitHit() && setSpeed > 0) {
      setSpeed = 0;
      logger.log("Too high");
    }

    // if (position > Config.WRIST_positionLoad && setSpeed < 0) {
    //   setSpeed = Config.WRIST_holdSpeedLoad;
    //   logger.log("Too low");
    // }

    wristMotor.set(setSpeed);
    double currMotorOutput = wristMotor.getMotorOutputPercent();
    double currVelocity = wristMotor.getSelectedSensorVelocity();
    double maxVelocity = 0;
    if (currMotorOutput != 0) {
      maxVelocity = currVelocity / currMotorOutput;
      logger.log(String.format("Motor values (output, velocity, max): (%f, %f, %f)", currMotorOutput, currVelocity, maxVelocity));
    }
  }

  // Magic motion code
  private void setupMotionMagic() {
    wristMotor.configFactoryDefault();
    wristMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
                                            Config.WRIST_kPIDLoopIdx,
                                            Config.WRIST_kTimeoutMs);
    wristMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Config.WRIST_kTimeoutMs);
    wristMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Config.WRIST_kTimeoutMs);

    wristMotor.configNominalOutputForward(0, Config.WRIST_kTimeoutMs);
		wristMotor.configNominalOutputReverse(0, Config.WRIST_kTimeoutMs);
		wristMotor.configPeakOutputForward(1, Config.WRIST_kTimeoutMs);
    wristMotor.configPeakOutputReverse(-1, Config.WRIST_kTimeoutMs);

    wristMotor.configForwardSoftLimitThreshold((int) Config.WRIST_positionUp);
    wristMotor.configReverseSoftLimitThreshold((int) Config.WRIST_positionLoad);
    
    wristMotor.selectProfileSlot(Config.WRIST_kSlotIdx, Config.WRIST_kPIDLoopIdx);
		wristMotor.config_kF(Config.WRIST_kSlotIdx, Config.WRIST_kF, Config.WRIST_kTimeoutMs);
		wristMotor.config_kP(Config.WRIST_kSlotIdx, Config.WRIST_kP, Config.WRIST_kTimeoutMs);
		wristMotor.config_kI(Config.WRIST_kSlotIdx, Config.WRIST_kI, Config.WRIST_kTimeoutMs);
		wristMotor.config_kD(Config.WRIST_kSlotIdx, Config.WRIST_kD, Config.WRIST_kTimeoutMs);

    wristMotor.configMotionCruiseVelocity(getCruiseVelocity(), Config.WRIST_kTimeoutMs);
    wristMotor.configMotionAcceleration(getAcceleration(), Config.WRIST_kTimeoutMs);
  }

  public void updateMotionMagic() {
    wristMotor.config_kF(Config.WRIST_kSlotIdx, Config.WRIST_kF, Config.WRIST_kTimeoutMs);
		wristMotor.config_kP(Config.WRIST_kSlotIdx, Config.WRIST_kP, Config.WRIST_kTimeoutMs);
		wristMotor.config_kI(Config.WRIST_kSlotIdx, Config.WRIST_kI, Config.WRIST_kTimeoutMs);
		wristMotor.config_kD(Config.WRIST_kSlotIdx, Config.WRIST_kD, Config.WRIST_kTimeoutMs);

    wristMotor.configMotionCruiseVelocity(getCruiseVelocity(), Config.WRIST_kTimeoutMs);
    wristMotor.configMotionAcceleration(getAcceleration(), Config.WRIST_kTimeoutMs);
  }

  private int getCruiseVelocity() {
    return (int)Config.WRIST_kCruiseVelocity;
  }

  private int getAcceleration() {
    return (int)Math.round(getCruiseVelocity() / Config.WRIST_kAccelerationNumSec);
  }
  
  private double getAngle() {
    return getWristPosition() / getStatePosition(States.LOAD) * RAD_IN_90;
  }

  private double getFeedForward() {
    return Math.sin(getAngle()) * Config.WRIST_holdSpeedLoad;
  }

  // END magic motion code
  private double getAutoSpeed() {
    double currPosition = getWristPosition();
    double targetPosition = getTargetPosition();

    double diff = targetPosition - currPosition;
    if (Math.abs(diff) <= Config.WRIST_acceptableError) {
      // Close enough
      direction = Direction.HOLD;
      autoSpeed = stateData.get(targetState).holdSpeed;
    }
    else if (diff < 0) {
      direction = Direction.DOWN;
      autoSpeed = Config.WRIST_lowerSpeed;
    }
    else {  
      direction = Direction.UP;
      autoSpeed = Config.WRIST_raiseSpeed;
    }

    return autoSpeed;
  }

  public void raise() {
    States nextState; 
    if (Robot.cargoIntake.isCargoDetected()) {
      nextState = States.UP;
      logger.log("Cargo detected while raising");
    } else {
      nextState = stateData.get(targetState).raiseState;
    }
    if (targetState != nextState) {
      logger.log("Raising wrist to: " + nextState.toString() + " from " + targetState.toString());
    }
    targetState = nextState;
  }

  public void lower() {
    States nextState; 
    if (Config.WRIST_skipDown && targetState != States.FLAT && !Robot.cargoIntake.isCargoDetected()) {
      nextState = States.FLAT; 
      logger.log("No cargo detected while lowering. Skipping down to FLAT"); 
    }
    else {
      nextState = stateData.get(targetState).lowerState;
    } 
    if (targetState != nextState) {
      logger.log("Lowering wrist to: " + nextState.toString());
    }
    targetState = nextState;
  }

  public boolean isUpLimitHit() {
    return !upLimitSwitch.get();
  }

  private double getTargetPosition() {
    return getStatePosition(targetState);
  }

  private double getStatePosition(States s) {
    return stateData.get(s).targetPosition;
  }

  @Override
  public void logSmartDashboard() {
    SmartDashboard.putNumber("Wrist Speed", setSpeed);
    SmartDashboard.putNumber("Wrist AutoSpeed", autoSpeed);
    SmartDashboard.putString("Wrist Target State", targetState.toString());
    SmartDashboard.putString("Wrist Motor Direction", direction.toString());
    SmartDashboard.putNumber("Wrist Raw Position", getWristPosition());
    SmartDashboard.putNumber("Wrist Target Position", getTargetPosition());
    SmartDashboard.putBoolean("Wrist Up Limit Hit", isUpLimitHit());
    // Instrum.Process(wristMotor, new StringBuilder());
    // SmartDashboard.putNumber("Cruise velocity", getCruiseVelocity());
    // SmartDashboard.putNumber("accel", getAcceleration());
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new WristControlCommand());
  }

  public void zeroEncoder() {
    targetState = States.ZEROING; 
    wristMotor.configForwardSoftLimitEnable(false);
    wristMotor.configReverseSoftLimitEnable(false);
    if (!isUpLimitHit()) {
      setSpeed = Config.WRIST_raiseSpeed; 
      wristMotor.set(setSpeed); 
    } 
    else {
      wristMotor.setSelectedSensorPosition(0, 0, 0);
      targetState = States.MANUAL;
    }
  }

  public static class Instrum {
    /* Tracking variables for instrumentation */
    private static int _loops = 0;
    private static int _timesInMotionMagic = 0;
  
    public static void Process(WPI_TalonSRX tal, StringBuilder sb) {
      /* Smart dash plots */
      SmartDashboard.putNumber("SensorVel", tal.getSelectedSensorVelocity(Config.WRIST_kPIDLoopIdx));
      SmartDashboard.putNumber("SensorPos", tal.getSelectedSensorPosition(Config.WRIST_kPIDLoopIdx));
      SmartDashboard.putNumber("MotorOutputPercent", tal.getMotorOutputPercent());
      SmartDashboard.putNumber("MotorOutput", tal.get());
      SmartDashboard.putNumber("ClosedLoopError", tal.getClosedLoopError(Config.WRIST_kPIDLoopIdx));
      /* Check if Talon SRX is performing Motion Magic */
      if (tal.getControlMode() == ControlMode.MotionMagic) {
        ++_timesInMotionMagic;
      } else {
        _timesInMotionMagic = 0;
      }
  
      if (_timesInMotionMagic > 10) {
        /* Print the Active Trajectory Point Motion Magic is servoing towards */
        SmartDashboard.putNumber("ClosedLoopTarget", tal.getClosedLoopTarget(Config.WRIST_kPIDLoopIdx));
          SmartDashboard.putNumber("ActTrajVelocity", tal.getActiveTrajectoryVelocity());
          SmartDashboard.putNumber("ActTrajPosition", tal.getActiveTrajectoryPosition());
      }
  
      /* Periodically print to console */
      if (++_loops >= 20) {
        _loops = 0;
        System.out.println(sb.toString());
      }
  
      /* Reset created string for next loop */
      sb.setLength(0);
    }
  }
}
