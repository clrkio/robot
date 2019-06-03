/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.IO;
import frc.robot.Robot;
import frc.robot.config.Config;

public class ElevatorControlCommand extends Command {
  public ElevatorControlCommand() {
    requires(Robot.elevator);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double speed = IO.playerGamepad.getRawAxis(Config.GAMEPAD_elevatorAxisId); 
    speed *= Config.ELEVATOR_speedMultiplier;
    Robot.elevator.move(speed);
  }

  protected void setSpeed() {
    
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }
}
