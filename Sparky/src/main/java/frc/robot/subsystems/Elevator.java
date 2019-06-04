/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.HashMap;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.IO;
import frc.robot.Logger;
import frc.robot.Robot;
import frc.robot.commands.Elevator.ElevatorControlCommand;
import frc.robot.config.Config;
import frc.robot.impls.SmartDashboardSubsystem;

/**
 * Add your docs here.
 */
public class Elevator extends SmartDashboardSubsystem {
  private static Logger logger = new Logger(Elevator.class.getSimpleName());
  enum Direction {
    UP, DOWN, HOLD
  }

  enum States {
    CARGO_LOAD,
    ROCKET_CARGO_LOW,
    HATCH_LOW,
    CARGO_SHIP_SCORE,
    ROCKET_CARGO_MID,
    HATCH_MID,
    ROCKET_CARGO_HIGH,
    HATCH_HIGH,
    MANUAL
  }

  class StateData {
    public double targetHeight;
    public States raiseState;
    public States lowerState;
    public States skipRaiseState; 
    public States skipLowerState; 

    public StateData(double t, States r, States l, States sr, States sl) {
      targetHeight = t;
      raiseState = r;
      lowerState = l;
      skipRaiseState = sr; 
      skipLowerState = sl; 
    }
  }

  private HashMap<States, StateData> stateData;

  public WPI_TalonSRX elevatorMasterMotor; // left motor
  public WPI_TalonSRX elevatorSlaveMotor; // right motor

  private DigitalInput topSwitch; 
  private DigitalInput bottomSwitch;
  private double setSpeed;
  private double autoSpeed;

  private States targetState;
  private Direction direction;

  public Elevator() {
    setupStateData();
    elevatorMasterMotor = new WPI_TalonSRX(Config.CAN_elevatorLeft); 
    elevatorSlaveMotor = new WPI_TalonSRX(Config.CAN_elevatorRight); 
    elevatorSlaveMotor.follow(elevatorMasterMotor);

    elevatorMasterMotor.setInverted(false);
    elevatorSlaveMotor.setInverted(InvertType.FollowMaster);

    elevatorMasterMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

    elevatorMasterMotor.setNeutralMode(NeutralMode.Brake); 
    elevatorSlaveMotor.setNeutralMode(NeutralMode.Brake); 

    topSwitch = new DigitalInput(Config.ELEVATOR_topSwitchDIO);
    bottomSwitch = new DigitalInput(Config.ELEVATOR_bottomSwitchDIO);
    setSpeed = 0;
    autoSpeed = 0;
    targetState = States.MANUAL;
    direction = Direction.HOLD;
  }

  private void setupStateData() {
    stateData = new HashMap<>();
    stateData.put(States.CARGO_LOAD,
      new StateData(Config.ELEVATOR_heightCargoLoadGround, 
                    States.ROCKET_CARGO_LOW, States.CARGO_LOAD, 
                    States.ROCKET_CARGO_LOW, States.CARGO_LOAD));

    stateData.put(States.ROCKET_CARGO_LOW, 
      new StateData(Config.ELEVATOR_heightCargoRocketLow, 
                    States.HATCH_LOW, States.CARGO_LOAD, 
                    States.CARGO_SHIP_SCORE, States.CARGO_LOAD));

    stateData.put(States.HATCH_LOW, 
      new StateData(Config.ELEVATOR_heightHatchLow, 
                    States.CARGO_SHIP_SCORE, States.ROCKET_CARGO_LOW, 
                    States.HATCH_MID, States.HATCH_LOW));

    stateData.put(States.CARGO_SHIP_SCORE, 
      new StateData(Config.ELEVATOR_heightCargoScore, 
                    States.ROCKET_CARGO_MID, States.HATCH_LOW, 
                    States.ROCKET_CARGO_MID, States.ROCKET_CARGO_LOW));

    stateData.put(States.ROCKET_CARGO_MID,
      new StateData(Config.ELEVATOR_heightCargoRocketMid, 
                    States.HATCH_MID, States.CARGO_SHIP_SCORE, 
                    States.ROCKET_CARGO_HIGH, States.CARGO_SHIP_SCORE));

    stateData.put(States.HATCH_MID,
      new StateData(Config.ELEVATOR_heightHatchMid, 
                    States.ROCKET_CARGO_HIGH, States.ROCKET_CARGO_MID, 
                    States.HATCH_HIGH, States.HATCH_LOW));

    stateData.put(States.ROCKET_CARGO_HIGH,
      new StateData(Config.ELEVATOR_heightCargoRocketHigh, 
                    States.HATCH_HIGH, States.HATCH_MID, 
                    States.ROCKET_CARGO_HIGH, States.ROCKET_CARGO_MID));

    stateData.put(States.HATCH_HIGH,
      new StateData(Config.ELEVATOR_heightHatchHigh, 
                    States.HATCH_HIGH, States.ROCKET_CARGO_HIGH, 
                    States.HATCH_HIGH, States.HATCH_MID));

    stateData.put(States.MANUAL, 
      new StateData(Config.ELEVATOR_heightHatchLow, 
                    States.HATCH_LOW, States.HATCH_LOW, 
                    States.HATCH_LOW, States.HATCH_LOW));
  }

  public void move(double speed) {
    if (Math.abs(speed) > 0.05) {
      targetState = States.MANUAL;
      setSpeed = speed;
    }
    else if (targetState != States.MANUAL) {
      setSpeed = getAutoSpeed();
    }

    if (setSpeed > 0 && isTopSwitchHit()) {
      setSpeed = 0;
      logger.log("Hit top limit switch!");
    }
    else if (setSpeed < 0 && isBottomSwitchHit()) {
      setSpeed = Config.ELEVATOR_holdSpeed;
      logger.log("Hit bottom limit switch!");
    }

    // Actually set the speed
    elevatorMasterMotor.set(setSpeed);
  }

  public boolean isTopSwitchHit() {
    return !topSwitch.get();
  }

  public boolean isBottomSwitchHit() {
    return !bottomSwitch.get();
  }

  public double getPosition() { 
    return elevatorMasterMotor.getSelectedSensorPosition(); 
  }

  private double getAutoSpeed() {
    double currPosition = getPosition();
    double targetPosition = getTargetPosition();

    double diff = targetPosition - currPosition;
    if (Math.abs(diff) <= Config.ELEVATOR_acceptableError) {
      // Close enough
      hold();
    }
    else if (diff > 0) {
      driveUp();
    }
    else {
      driveDown();
    }

    return autoSpeed;
  }

  private void driveUp() {
    direction = Direction.UP;
    autoSpeed = Config.ELEVATOR_upSpeed;
  }

  private void driveDown() {
    direction = Direction.DOWN;
    autoSpeed = Config.ELEVATOR_downSpeed;
  }

  private void hold() {
    direction = Direction.HOLD;
    autoSpeed = Config.ELEVATOR_holdSpeed;
  }

  private boolean safeToLower() {
    return !(targetState == States.HATCH_LOW && Robot.hatchIntake.isHatchExtended()); 
  }

  public void raise() {
    States nextState = stateData.get(targetState).raiseState;
    if (targetState != nextState) {
      logger.log("Raising elevator to: " + nextState.toString());
    }
    targetState = nextState;
  }

  public void lower() {
    States nextState = stateData.get(targetState).lowerState;
    if (!safeToLower()) {
      nextState = targetState;
      logger.log("Hatch is extended. Keeping elevator at: " + targetState.toString());
    }
    if (targetState != nextState) {
      logger.log("Lowering elevator to: " + nextState.toString());
    }
    targetState = nextState;
  }

  public void cargoSkipRaise() {
    States nextState = stateData.get(getSnapToCargoState(targetState)).skipRaiseState; 
    if (targetState == States.CARGO_LOAD || targetState == States.ROCKET_CARGO_LOW || targetState == States.HATCH_LOW) {
      nextState = States.CARGO_SHIP_SCORE; 
    } 
    if (targetState != nextState) {
      logger.log("Raising elevator to: " + nextState.toString());
    }
    targetState = nextState; 
  }

  public void hatchSkipRaise() {
    States nextState = stateData.get(getSnapToHatchState(targetState)).skipRaiseState; 
    if (targetState != nextState) {
      logger.log("Raising elevator to: " + nextState.toString());
    }
    targetState = nextState; 
  }

  public void setState(int state) {
    States nextState; 
    switch(state) {
      case Config.ELEVATOR_cargoLoad: 
        nextState = States.CARGO_LOAD; 
        break; 
      case Config.ELEVATOR_rocketCargoLow: 
        nextState = States.ROCKET_CARGO_LOW; 
        break; 
      case Config.ELEVATOR_hatchLow: 
        nextState = States.HATCH_LOW; 
        break; 
      case Config.ELEVATOR_cargoShipScore: 
        nextState = States.CARGO_SHIP_SCORE; 
        break; 
      case Config.ELEVATOR_rocketCargoMid: 
        nextState = States.ROCKET_CARGO_MID; 
        break; 
      case Config.ELEVATOR_hatchMid: 
        nextState = States.HATCH_MID; 
        break; 
      case Config.ELEVATOR_rocketCargoHigh: 
        nextState = States.ROCKET_CARGO_HIGH; 
        break; 
      case Config.ELEVATOR_hatchHigh: 
        nextState = States.HATCH_HIGH; 
        break; 
      default: 
        nextState = States.MANUAL; 
    } 
    if (!safeToLower()) {
      nextState = targetState;
      logger.log("Hatch is extended. Keeping elevator at: " + targetState.toString());
    } 
    if (targetState != nextState) {
      logger.log("Moving elevator to: " + nextState.toString());
    }
    targetState = nextState;
  }

  private States getSnapToCargoState(States state) {
    States snapState; 
    switch(state) {
      case HATCH_LOW: 
        snapState = States.ROCKET_CARGO_LOW; 
        break; 
      case HATCH_MID: 
        snapState = States.ROCKET_CARGO_MID; 
        break; 
      case HATCH_HIGH:
        snapState = States.ROCKET_CARGO_HIGH; 
        break; 
      default:  
        snapState = state; 
    }
    return snapState; 
  }

  private States getSnapToHatchState(States state) {
    States snapState; 
    switch(state) {
      case CARGO_LOAD: 
        snapState = States.MANUAL; 
        break; 
      case ROCKET_CARGO_LOW: 
        snapState = States.MANUAL; 
        break;
      case CARGO_SHIP_SCORE: 
        snapState = States.HATCH_LOW; 
        break; 
      case ROCKET_CARGO_MID: 
        snapState = States.HATCH_LOW; 
        break; 
      case ROCKET_CARGO_HIGH: 
        snapState = States.HATCH_MID; 
        break; 
      default: 
        snapState = state; 
    }
    return snapState; 
  }

  private double getTargetPosition() {
    return getStateTargetPosition(targetState);
  }

  private double getStateTargetPosition(States state) {
    return stateData.get(state).targetHeight * Config.ELEVATOR_encoderPerInch;
  }
  
  public boolean isSafeForHatch() {
    return getPosition() >= getStateTargetPosition(States.HATCH_LOW);
  }

  @Override
  public void logSmartDashboard() {
    SmartDashboard.putNumber("Elevator speed", setSpeed);
    SmartDashboard.putNumber("Elevator raw position", getPosition());
    SmartDashboard.putNumber("Elevator target position", getTargetPosition());
    SmartDashboard.putBoolean("Elevator isTopSwitchHit", isTopSwitchHit());
    SmartDashboard.putBoolean("Elevator isBottomSwitchHit", isBottomSwitchHit());
    
    SmartDashboard.putNumber("Elevator autoSpeed", autoSpeed);
    SmartDashboard.putString("Elevator Target State", targetState.toString());
    SmartDashboard.putString("Elevator Motor Direction", direction.toString());
  }
  
  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ElevatorControlCommand());
  }

  public void zeroEncoder() {
    elevatorMasterMotor.setSelectedSensorPosition(0, 0, 0);
  }

}
