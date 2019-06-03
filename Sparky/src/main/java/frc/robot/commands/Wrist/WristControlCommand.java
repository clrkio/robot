/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Wrist;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.IO;
import frc.robot.Robot;
import frc.robot.config.Config;

public class WristControlCommand extends Command {
  public WristControlCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.wrist);
  }


  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double speed = IO.playerGamepad.getRawAxis(Config.GAMEPAD_wristAxisId) * Config.WRIST_speedMultiplier;
    Robot.wrist.move(speed);
  }


  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }
}
