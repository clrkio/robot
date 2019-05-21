/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.config.Config;

public class ControlledDriveCommand extends Command {
  protected double speed; 
  protected double rotation; 
  protected boolean quickTurn = true; 

  public ControlledDriveCommand() {
    requires((Robot.drivetrain));
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    setSpeed();
    setTurn();
  }

  protected void setSpeed() {
    double forwardValue = Robot.driveGamepad.getRawAxis(Config.GAMEPAD_driveForwardAxisId);
    double reverseValue = Robot.driveGamepad.getRawAxis(Config.GAMEPAD_driveReverseAxisId);
    
    speed = forwardValue - reverseValue; 
  }

  protected void setTurn() {
    double driveTurnAxisId = Robot.driveGamepad.getRawAxis(Config.GAMEPAD_driveTurnAxisId);
    double turnModifer = speed > 0 ? -1 : 1;
    rotation = turnModifer * driveTurnAxisId;
  }

  protected void setQuickTurn() {
    quickTurn = Robot.driveGamepad.getRawButton(Config.GAMEPAD_driveQuickTurnButton); 
  }

  protected void updateDrivetrain() {
    Robot.drivetrain.robotDrive.curvatureDrive(speed, rotation, quickTurn);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }
}
