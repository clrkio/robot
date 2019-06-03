/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.HatchIntake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class HatchRetractCommand extends Command {
  public HatchRetractCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.hatchIntake);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.hatchIntake.retractHatch();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }
}
