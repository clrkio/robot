/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Logger;
import frc.robot.Robot;
import frc.robot.commands.Drivetrain.ControlledDriveCommand;
import frc.robot.config.Config;
import frc.robot.impls.SmartDashboardSubsystem;

/**
 * Add your docs here.
 */
public class Drivetrain extends SmartDashboardSubsystem {
  private static Logger logger = new Logger(Drivetrain.class.getSimpleName());
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private CANSparkMax leftMotorPrimary;
  private CANSparkMax leftMotorSlaveA;
  private CANSparkMax leftMotorSlaveB;
  private CANSparkMax rightMotorPrimary;
  private CANSparkMax rightMotorSlaveA;
  private CANSparkMax rightMotorSlaveB;

  private CANEncoder rightEncoder; 
  private CANEncoder leftEncoder; 

  private DoubleSolenoid speedShifterSolenoid;
  private boolean isHighSpeed;
  private boolean isBrakeMode;
  private boolean isToddlerMode; 
  private boolean isFastTurnMode; 
  private boolean isTurnInPlaceMode;

  public DifferentialDrive robotDrive;

  private double speed;
  private double rotation;

  public Drivetrain() {
    shift(Config.DRIVE_startInHighSpeed);
    setIdleMode(Config.DRIVE_startInBrakeMode);

    leftMotorPrimary = new CANSparkMax(Config.CAN_leftDrivePrimary, MotorType.kBrushless);
    leftMotorSlaveA = new CANSparkMax(Config.CAN_leftDriveFollowerA, MotorType.kBrushless);
    leftMotorSlaveB = new CANSparkMax(Config.CAN_leftDriveFollowerB, MotorType.kBrushless);
    leftEncoder = leftMotorPrimary.getEncoder();

    rightMotorPrimary = new CANSparkMax(Config.CAN_rightDrivePrimary, MotorType.kBrushless);
    rightMotorSlaveA = new CANSparkMax(Config.CAN_rightDriveFollowerA, MotorType.kBrushless);
    rightMotorSlaveB = new CANSparkMax(Config.CAN_rightDriveFollowerB, MotorType.kBrushless);
    rightEncoder = rightMotorPrimary.getEncoder();

    leftMotorSlaveA.follow(leftMotorPrimary);
    leftMotorSlaveB.follow(leftMotorPrimary);

    rightMotorSlaveA.follow(rightMotorPrimary);
    rightMotorSlaveB.follow(rightMotorPrimary);

    robotDrive = new DifferentialDrive(leftMotorPrimary, rightMotorPrimary);

    // Forward = High Speed
    // Reverse = Low Speed
    speedShifterSolenoid = new DoubleSolenoid(Config.SOLENOID_driveHighSpeed, Config.SOLENOID_driveLowSpeed);

    speed = 0;
    rotation = 0;
    isToddlerMode = false; 
    isFastTurnMode = false; 
    isTurnInPlaceMode = false;
  }

  public void stop() {
    unSetAllPressedModes(); 
    set(0, 0);
  }

  public double getLeftPosition() {
    return leftEncoder.getPosition(); 
  }

  public double getRightPosition() {
    return rightEncoder.getPosition(); 
  }

  public boolean isHighSpeed() {
    return isHighSpeed;
  }

  public boolean isBrakeMode() {
    return isBrakeMode;
  }

  public boolean isToddlerMode() {
    return isToddlerMode; 
  }

  public void set(double requestedSpeed, double requestedRotation) {
    if (isFastTurnMode) {
      double slowSpeed = requestedSpeed * Config.DRIVE_fastTurnSpeedMultiplier; 
      double fastSpeedMultiplier = Math.abs(requestedRotation) + Config.DRIVE_fastTurnTurnConstant; 
      double fastSpeed = slowSpeed * fastSpeedMultiplier; 
        if (requestedRotation < 0) {
          robotDrive.tankDrive(fastSpeed, slowSpeed);
        } else {
          robotDrive.tankDrive(slowSpeed, fastSpeed);
        }
    } else {
      double directionModifer = (!isTurnInPlaceMode && requestedSpeed > 0) ? -1 : 1;
      double turnModifer = isTurnInPlaceMode ? Config.DRIVE_turnInPlaceMultiplier : Config.DRIVE_turnMultiplier; 
      rotation = requestedRotation * directionModifer * turnModifer; 

      double speedModifier = isTurnInPlaceMode ? Config.DRIVE_turnInPlaceSpeedMultiplier : Config.DRIVE_driveMultiplier; 
      speed = requestedSpeed * speedModifier; 

      if (isToddlerMode) {
        speed *= Config.DRIVE_toddlerModeSpeedMultiplier; 
        rotation *= Config.DRIVE_toddlerModeTurnMultiplier; 
      }
      if (speed > Config.DRIVE_isFastSpeed) {
        Robot.compressor.stop();
      } else if (!Robot.compressor.enabled()) {
        Robot.compressor.start();
      }
      robotDrive.curvatureDrive(speed, rotation, isTurnInPlaceMode); 
    }
  }

  public void shift(boolean toHighSpeed) {
    if (toHighSpeed == isHighSpeed) {
      return;
    }
    
    logger.log("Setting gears to " + (toHighSpeed ? "high" : "low") + " speed.");
    isHighSpeed = toHighSpeed;
    if (isHighSpeed) { // Forward is high speed
      speedShifterSolenoid.set(Value.kForward);
      if (Config.DRIVE_disableNeoInHighSpeed) {
        leftMotorSlaveB.follow(null);
        rightMotorSlaveB.follow(null);
      }
    }
    else {  // Reverse is low speed
      speedShifterSolenoid.set(Value.kReverse);
      if (Config.DRIVE_disableNeoInHighSpeed) {
        leftMotorSlaveB.follow(leftMotorPrimary);
        rightMotorSlaveB.follow(rightMotorPrimary);
      }
    }
  }

  public void setIdleMode(boolean toMode) {
    if (toMode == isBrakeMode) {
      return;
    }
    
    logger.log("Setting idle mode to " + (toMode ? "brake" : "coast") + " mode.");
    isBrakeMode = toMode;

    IdleMode toSet = isBrakeMode ? IdleMode.kBrake : IdleMode.kCoast;
    leftMotorPrimary.setIdleMode(toSet);
    leftMotorSlaveA.setIdleMode(toSet);
    leftMotorSlaveB.setIdleMode(toSet);
    rightMotorPrimary.setIdleMode(toSet);
    rightMotorSlaveA.setIdleMode(toSet);
    rightMotorSlaveB.setIdleMode(toSet);
  }

  public void setFastTurn(boolean toMode) {
    if (isTurnInPlaceMode && toMode) {
      isFastTurnMode = false; 
      logger.log("Cannot set both turnInPlace and fastTurn, defaulting to turnInPlace"); 
    }
    if (isFastTurnMode == toMode) {
      return; 
    } 
    logger.log("Setting fast turn mode to " + (toMode ? "fastTurn" : "normalTurn") + " mode."); 
    isFastTurnMode = toMode; 
  }

  public void setTurnInPlace(boolean toMode) {
    if (isTurnInPlaceMode == toMode) {
      return; 
    } 
    logger.log("Setting turn in place mode to " + (toMode ? "turnInPlace" : "normalTurn") + " mode."); 
    isTurnInPlaceMode = toMode; 
  }

  public void setToddlerMode(boolean toMode) {
    if (toMode == isToddlerMode) {
      return; 
    }
    logger.log("Setting toddler mode to " + (toMode ? "ON" : "OFF")); 
    isToddlerMode = toMode; 
    shift(!isToddlerMode);
  }

  private void unSetAllPressedModes() {
    isBrakeMode = false; 
    isToddlerMode = false; 
    isFastTurnMode = false; 
    isTurnInPlaceMode = false; 
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ControlledDriveCommand()); 
  }

  @Override
  public void logSmartDashboard() {
    SmartDashboard.putNumber("Drivetrain left encoder", getLeftPosition());
    SmartDashboard.putNumber("Drivetrain right encoder", getRightPosition());
    SmartDashboard.putNumber("Drivetrain speed", speed);
    SmartDashboard.putNumber("Drivetrain turn", rotation);
    SmartDashboard.putBoolean("Drivetrain quickturn", isTurnInPlaceMode);
    SmartDashboard.putString("Drivetrain idleMode", isBrakeMode() ? "BRAKE" : "COAST");
    SmartDashboard.putString("Drivetrain speedMode", isHighSpeed() ? "HIGH" : "LOW");
  }
}
