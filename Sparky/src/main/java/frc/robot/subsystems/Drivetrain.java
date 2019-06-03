/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

import frc.robot.config.*;
import frc.robot.impls.SmartDashboardSubsystem;
import frc.robot.Logger;
import frc.robot.commands.Drivetrain.ControlledDriveCommand;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder; 

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

  public DifferentialDrive robotDrive;

  private double setSpeed;
  private double setRotation;
  private boolean setQuickturn;
  

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

    setSpeed = 0;
    setRotation = 0;
    setQuickturn = false;
  }

  public void stop() {
    set(0, 0, false);
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

  public void set(double speed, double rotation, boolean quickTurn) {
    robotDrive.curvatureDrive(speed, rotation, quickTurn);
    setSpeed = speed;
    setRotation = rotation;
    setQuickturn = quickTurn;
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

  public void setIdleMode(boolean toBrakeMode) {
    if (toBrakeMode == isBrakeMode) {
      return;
    }
    
    logger.log("Setting idle mode to " + (toBrakeMode ? "brake" : "coast") + " mode.");
    isBrakeMode = toBrakeMode;

    IdleMode toSet = isBrakeMode ? IdleMode.kBrake : IdleMode.kCoast;
    leftMotorPrimary.setIdleMode(toSet);
    leftMotorSlaveA.setIdleMode(toSet);
    leftMotorSlaveB.setIdleMode(toSet);
    rightMotorPrimary.setIdleMode(toSet);
    rightMotorSlaveA.setIdleMode(toSet);
    rightMotorSlaveB.setIdleMode(toSet);
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ControlledDriveCommand()); 
  }

  @Override
  public void logSmartDashboard() {
    SmartDashboard.putNumber("Drivetrain left encoder", getLeftPosition());
    SmartDashboard.putNumber("Drivetrain right encoder", getRightPosition());
    SmartDashboard.putNumber("Drivetrain speed", setSpeed);
    SmartDashboard.putNumber("Drivetrain turn", setRotation);
    SmartDashboard.putBoolean("Drivetrain quickturn", setQuickturn);
    SmartDashboard.putString("Drivetrain idleMode", isBrakeMode() ? "BRAKE" : "COAST");
  }
}
