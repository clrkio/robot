/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Wrist;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Logger;
import frc.robot.Robot;

public class WristRaiseCommand extends Command {
  private static Logger logger = new Logger(WristRaiseCommand.class.getSimpleName());

  public WristRaiseCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.wrist);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    logger.log("Raised");
    Robot.wrist.raise();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }
}
