/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.HatchIntake;

import frc.robot.config.Config;
import frc.robot.OI;
import frc.robot.Robot;
import edu.wpi.first.networktables.*; 
import edu.wpi.first.wpilibj.command.Command;

public class HatchIntakeCommand extends Command {
  public HatchIntakeCommand() {
    requires(Robot.hatchIntake);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (OI.driverGamepad.getPOV() == Config.HATCH_INTAKE_in) {
      System.out.println(Robot.hatchIntake.leftSwitch.get());
      if (Robot.hatchIntake.leftSwitchStatus()) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(Config.LIMELIGHT_LED_ON); 
      } else {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(Config.LIMELIGHT_LED_OFF); 
      }
    }
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
