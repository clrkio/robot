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

public class CargoIntakeCommand extends Command {
  private double speed; 
  private double startTime; 
  public CargoIntakeCommand() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
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
      speed = 0; 
      Robot.cargoIntake.rollerMotor.stopMotor(); 
      return; 
    }
    // check if cargo has been loaded. if it has, turn off the rollers 
    if (Robot.cargoIntake.getPhotoeletricValue() < 100) {
      speed = 0; 
      Robot.cargoIntake.rollerMotor.stopMotor(); 
    } else {
      speed = OI.playerGamepad.getRawAxis(Config.GAMEPAD_cargoIntakeRollerIn); 
      if (speed < .01) {
        speed = -OI.playerGamepad.getRawAxis(Config.GAMEPAD_cargoIntakeRollerOut); 
      } 
      SmartDashboard.putNumber("cargoIntakeRollerSpeed", speed); 
      Robot.cargoIntake.rollerMotor.set(speed); 
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
