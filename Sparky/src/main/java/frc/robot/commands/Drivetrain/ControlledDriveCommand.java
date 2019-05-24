/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.OI;
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
    setQuickTurn();
    setTurn();
    updateDrivetrain();
    SmartDashboard.putNumber("drivetrain left position", Robot.drivetrain.getLeftPosition()); 
  }

  protected void setSpeed() {
    double forwardValue = OI.driverGamepad.getRawAxis(Config.GAMEPAD_driveForwardAxisId);
    double reverseValue = OI.driverGamepad.getRawAxis(Config.GAMEPAD_driveReverseAxisId);

    speed = (forwardValue - reverseValue)*Config.DRIVE_driveMultiplier; 
    speed = Math.min(Config.DRIVE_maxSpeed, speed);
    speed = Math.max(Config.DRIVE_minSpeed, speed);    
  }

  protected void setTurn() {
    double driveTurnAxisId = OI.driverGamepad.getRawAxis(Config.GAMEPAD_driveTurnAxisId);
    double directionModifer = speed > 0 ? -1 : 1;
    double turnModifer = quickTurn ? Config.DRIVE_quickTurnMultiplier : Config.DRIVE_turnMultiplier; 
    rotation = driveTurnAxisId * directionModifer * turnModifer;
  }

  protected void setQuickTurn() {
    quickTurn = OI.driverGamepad.getRawButton(Config.GAMEPAD_driveQuickTurnButton);
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
