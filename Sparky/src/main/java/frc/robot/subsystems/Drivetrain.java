/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import frc.robot.config.*;
import frc.robot.commands.Drivetrain.ControlledDriveCommand;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * Add your docs here.
 */
public class Drivetrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private CANSparkMax leftMotorPrimary;
  private CANSparkMax leftMotorSlaveA;
  private CANSparkMax leftMotorSlaveB;
  private CANSparkMax rightMotorPrimary;
  private CANSparkMax rightMotorSlaveA;
  private CANSparkMax rightMotorSlaveB;

  public DifferentialDrive robotDrive;

  public Drivetrain(){ 
    leftMotorPrimary = new CANSparkMax(Config.CAN_leftDrivePrimary, MotorType.kBrushless);
    leftMotorSlaveA = new CANSparkMax(Config.CAN_leftDriveFollowerA, MotorType.kBrushless);
    leftMotorSlaveB = new CANSparkMax(Config.CAN_leftDriveFollowerB, MotorType.kBrushless);

    rightMotorPrimary = new CANSparkMax(Config.CAN_rightDrivePrimary, MotorType.kBrushless);
    rightMotorSlaveA = new CANSparkMax(Config.CAN_rightDriveFollowerA, MotorType.kBrushless);
    rightMotorSlaveB = new CANSparkMax(Config.CAN_rightDriveFollowerB, MotorType.kBrushless);

    leftMotorSlaveA.follow(leftMotorPrimary);
    leftMotorSlaveB.follow(leftMotorPrimary);
    // leftMotorPrimary.setInverted(!Config.DRIVE_rightIsInverted);
    // leftMotorSlaveA.setInverted(!Config.DRIVE_rightIsInverted);
    // leftMotorSlaveB.setInverted(!Config.DRIVE_rightIsInverted);

    rightMotorSlaveA.follow(rightMotorPrimary);
    rightMotorSlaveB.follow(rightMotorPrimary);
    // rightMotorPrimary.setInverted(Config.DRIVE_rightIsInverted);
    // rightMotorSlaveA.setInverted(Config.DRIVE_rightIsInverted);
    // rightMotorSlaveB.setInverted(Config.DRIVE_rightIsInverted);

    robotDrive = new DifferentialDrive(leftMotorPrimary, rightMotorPrimary);

  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ControlledDriveCommand()); 
  }
}
