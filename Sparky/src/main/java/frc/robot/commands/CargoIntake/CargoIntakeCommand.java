/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.CargoIntake;

import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.config.Config; 
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.networktables.*;

enum Direction {
  STOP, IN, OUT 
}

public class CargoIntakeCommand extends Command {
  private double speed; 
  private double startTime; 
  private Direction direction; 
  public CargoIntakeCommand() {
    requires(Robot.cargoIntake);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    startTime = System.currentTimeMillis(); 
    speed = 0; 
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    SmartDashboard.putNumber("photoelelectricValue", Robot.cargoIntake.getPhotoeletricValue()); 
    if (isStalling()) {
      direction = Direction.STOP; 
      speed = 0; 
      Robot.cargoIntake.rollerMotor.stopMotor(); 
      SmartDashboard.putString("cargoIntakeStatus: ", "Rollers Stalling");
    } else {
      direction = Direction.IN; 
      speed = OI.playerGamepad.getRawAxis(Config.GAMEPAD_cargoIntakeRollerIn)*Config.CARGO_INTAKE_rollerSpeedInMultiplier;  
      if (speed == 0) {
        direction = Direction.OUT; 
        speed = OI.playerGamepad.getRawAxis(Config.GAMEPAD_cargoIntakeRollerOut)*Config.CARGO_INTAKE_rollerSpeedOutMultiplier; 
        SmartDashboard.putString("cargoIntakeStatus: ", "Cargo Out Normally"); 
      } if (speed > 0 && Robot.cargoIntake.getPhotoeletricValue() < Config.CARGO_INTAKE_photoelectricThreshold) {
        // check if cargo has been loaded. if it has, do not let the rollers intake any further 
        direction = Direction.STOP; 
        speed = 0; 
        Robot.cargoIntake.rollerMotor.stopMotor(); 
        SmartDashboard.putString("cargoIntakeStatus: ", "Photoelectric Triggered"); 
      } else if (speed > 0) {
        SmartDashboard.putString("cargoIntakeStatus: ", "Cargo In Normally");
      }
      SmartDashboard.putNumber("cargoIntakeRollerSpeed", speed); 
      SmartDashboard.putString("cargoIntakeRollerDirection", directionToString(direction));
      SmartDashboard.putNumber("cargoIntakePosition", Robot.cargoIntake.getRollerPosition()); 
      Robot.cargoIntake.rollerMotor.set(speed); 
    }
  }

  private String directionToString(Direction direction) {
    switch (direction) {
      case STOP: 
        return "STOP"; 
      case IN: 
        return "IN"; 
      case OUT:
        return "OUT"; 
      default:
        return "UNKNOWN"; 
    }
  }

  private boolean isStalling(){ 
    double currentTime = System.currentTimeMillis(); 
    return false; 
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
