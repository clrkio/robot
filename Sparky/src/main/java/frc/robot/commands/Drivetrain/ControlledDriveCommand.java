/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.IO;
import frc.robot.Logger;
import frc.robot.Robot;
import frc.robot.config.Config;

public class ControlledDriveCommand extends Command {
  private static Logger logger = new Logger(ControlledDriveCommand.class.getSimpleName());
  protected double speed;
  protected double rotation; 
  protected boolean quickTurn = true; 
  protected boolean fastTurn; 

  public ControlledDriveCommand() {
    requires((Robot.drivetrain));
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    setQuickTurn();
    setFastTurn(); 
    setSpeed();
    setTurn();
    updateDrivetrain();
  }

  protected void setSpeed() {
    double forwardValue = IO.driverGamepad.getRawAxis(Config.GAMEPAD_driveForwardAxisId);
    double reverseValue = IO.driverGamepad.getRawAxis(Config.GAMEPAD_driveReverseAxisId);

    speed = (forwardValue - reverseValue)*Config.DRIVE_driveMultiplier;
    if (quickTurn) {
      speed *= Config.DRIVE_quickTurnSpeedMultiplier; 
    }
  }

  protected void setTurn() {
    double driveTurnAxisId = IO.driverGamepad.getRawAxis(Config.GAMEPAD_driveTurnAxisId);
    double directionModifer = (!quickTurn && speed > 0) ? -1 : 1;
    double turnModifer = quickTurn ? Config.DRIVE_quickTurnMultiplier : 
                        (fastTurn ? Config.DRIVE_fastTurnMultiplier : Config.DRIVE_turnMultiplier); 
    rotation = driveTurnAxisId * directionModifer * turnModifer;
  }

  protected void setQuickTurn() {
    quickTurn = IO.driverGamepad.getRawButton(Config.GAMEPAD_driveQuickTurnButton);
  }
  
  protected void setFastTurn() {
    fastTurn = IO.driverGamepad.getRawButton(Config.GAMEPAD_driveFastTurnButton); 
  }

  protected void updateDrivetrain() {
    Robot.drivetrain.set(speed, rotation, quickTurn);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }
}
