/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.HashMap;

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

    upLimitSwitch = new DigitalInput(Config.WRIST_backstopDIO);

    targetState = States.MANUAL;
    direction = Direction.HOLD;
    setSpeed = 0;
    autoSpeed = 0;
    hold();
  }

  private void setupStateData() {
    stateData = new HashMap<>();
    stateData.put(States.LOAD,
      new StateData(Config.WRIST_positionLoad, States.FLAT, States.LOAD, Config.WRIST_holdSpeedLoad));

    stateData.put(States.FLAT, 
      new StateData(Config.WRIST_positionFlat, States.CARGO_SHIP, States.LOAD, Config.WRIST_holdSpeedFlat));

    stateData.put(States.CARGO_SHIP, 
      new StateData(Config.WRIST_positionCargo, States.UP, States.FLAT, Config.WRIST_holdSpeedCargo));

    stateData.put(States.UP, 
      new StateData(Config.WRIST_positionUp, States.UP, States.CARGO_SHIP, Config.WRIST_holdSpeedUp));
  
    stateData.put(States.MANUAL, 
      new StateData(Config.WRIST_positionUp, States.UP, States.FLAT, Config.WRIST_holdSpeedUp));

    stateData.put(States.ZEROING, 
      new StateData(Config.WRIST_positionUp, States.UP, States.FLAT, Config.WRIST_holdSpeedUp));
  }

  public void init() {
    wristMotor.configFactoryDefault(); 
    wristMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 1);
		wristMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
  }  

  public double getWristPosition() {
    return wristMotor.getSelectedSensorPosition();
  }

  public void move(double speed) {
    if (Math.abs(speed) > 0.05) {
      targetState = States.MANUAL;
      setSpeed = speed;
    } else if (targetState == States.ZEROING) {
      return;
    } else if (targetState != States.MANUAL) {
      setSpeed = getAutoSpeed();
    }

    double position = getWristPosition(); 
    if (isUpLimitHit() && setSpeed > 0) {
      setSpeed = 0;
      logger.log("Too high");
    }

    if (position > Config.WRIST_positionLoad && setSpeed < 0) {
      setSpeed = Config.WRIST_holdSpeedLoad;
      logger.log("Too low");
    }

    wristMotor.set(setSpeed);
  }

  private double getAutoSpeed() {
    double currPosition = getWristPosition();
    double targetPosition = getTargetPosition();

    double diff = targetPosition - currPosition;
    if (Math.abs(diff) <= Config.WRIST_acceptableError) {
      // Close enough
      hold();
    }
    else if (diff > 0) {
      driveDown();
    }
    else {
      driveUp();
    }

    return autoSpeed;
  }

  private void driveUp() {
    direction = Direction.UP;
    autoSpeed = Config.WRIST_raiseSpeed;
  }

  private void driveDown() {
    direction = Direction.DOWN;
    autoSpeed = Config.WRIST_lowerSpeed;
  }

  private void hold() {
    direction = Direction.HOLD;
    autoSpeed = stateData.get(targetState).holdSpeed;
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
    if (Config.WRIST_skipDown && !Robot.cargoIntake.isCargoDetected()) {
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
    return stateData.get(targetState).targetPosition;
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
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new WristControlCommand());
  }

  public void zeroEncoder() {
    targetState = States.ZEROING; 
    if (!isUpLimitHit()) {
      setSpeed = Config.WRIST_raiseSpeed; 
      wristMotor.set(setSpeed); 
    } 
    else {
      wristMotor.setSelectedSensorPosition(0, 0, 0);
      targetState = States.MANUAL;
    }
  }
}
