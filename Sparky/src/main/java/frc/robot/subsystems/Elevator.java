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

    public StateData(double t, States r, States l) {
      targetHeight = t;
      raiseState = r;
      lowerState = l;
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
    targetState = States.CARGO_LOAD;
    direction = Direction.HOLD;
  }

  private void setupStateData() {
    stateData = new HashMap<>();
    stateData.put(States.CARGO_LOAD,
      new StateData(Config.ELEVATOR_heightCargoLoadGround, States.ROCKET_CARGO_LOW, States.CARGO_LOAD));

    stateData.put(States.ROCKET_CARGO_LOW, 
      new StateData(Config.ELEVATOR_heightCargoRocketLow, States.HATCH_LOW, States.CARGO_LOAD));

    stateData.put(States.HATCH_LOW, 
      new StateData(Config.ELEVATOR_heightHatchLow, States.CARGO_SHIP_SCORE, States.ROCKET_CARGO_LOW));

    stateData.put(States.CARGO_SHIP_SCORE, 
      new StateData(Config.ELEVATOR_heightCargoScore, States.ROCKET_CARGO_MID, States.HATCH_LOW));

    stateData.put(States.ROCKET_CARGO_MID,
      new StateData(Config.ELEVATOR_heightCargoRocketMid, States.HATCH_MID, States.CARGO_SHIP_SCORE));

    stateData.put(States.HATCH_MID,
      new StateData(Config.ELEVATOR_heightHatchMid, States.ROCKET_CARGO_HIGH, States.ROCKET_CARGO_MID));

    stateData.put(States.ROCKET_CARGO_HIGH,
      new StateData(Config.ELEVATOR_heightCargoRocketHigh, States.HATCH_HIGH, States.HATCH_MID));

    stateData.put(States.HATCH_HIGH,
      new StateData(Config.ELEVATOR_heightHatchHigh, States.HATCH_HIGH, States.ROCKET_CARGO_HIGH));

    stateData.put(States.MANUAL, 
      new StateData(Config.ELEVATOR_heightHatchLow, States.HATCH_LOW, States.HATCH_LOW));
  }

  public void move(double speed) {
    if (Math.abs(speed) > 0.05) {
      targetState = States.MANUAL;
    }
    else {
      speed = 0;
    }
    setSpeed = speed;

    autoMove();
    double position = getPosition(); 
    if ((position < getStateTargetPosition(States.CARGO_LOAD)) && setSpeed < 0) {
      setSpeed = 0;
      logger.log("Too low");
    }
    if ((position > getStateTargetPosition(States.HATCH_HIGH)) && setSpeed > 0) {
      setSpeed = 0;
      logger.log("Too high");
    }
    // if (setSpeed > 0 && isTopSwitchHit()) {
    //   setSpeed = 0;
    // }
    // else if (setSpeed < 0 && isBottomSwitchHit()) {
    //   setSpeed = 0;
    // }
    // Pretty sure code goes here for actually moving
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

  private void autoMove() {
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

    setSpeed = autoSpeed;
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

  public void raise() {
    States nextState = stateData.get(targetState).raiseState;
    if (targetState != nextState) {
      logger.log("Raising elevator to: " + nextState.toString());
    }
    targetState = nextState;
  }

  public void lower() {
    States nextState = stateData.get(targetState).lowerState;
    if (targetState == States.HATCH_LOW && Robot.hatchIntake.isHatchExtended()) {
      nextState = targetState;
      logger.log("Hatch is extended. Keeping elevator at: " + targetState.toString());
    }
    if (targetState != nextState) {
      logger.log("Lowering elevator to: " + nextState.toString());
    }
    targetState = nextState;
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
