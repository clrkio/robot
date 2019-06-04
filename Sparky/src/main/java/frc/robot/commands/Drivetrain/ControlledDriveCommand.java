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

  public ControlledDriveCommand() {
    requires((Robot.drivetrain));
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    setTurnInPlace();
    setFastTurnMode(); 
    setToddlerMode(); 
    setBrakeMode(); 
    setSpeed();
    setTurn();
    updateDrivetrain();
  }

  protected void setSpeed() {
    double forwardValue = IO.driverGamepad.getRawAxis(Config.GAMEPAD_driveForwardAxisId);
    double reverseValue = IO.driverGamepad.getRawAxis(Config.GAMEPAD_driveReverseAxisId);

    speed = (forwardValue - reverseValue);
  }

  protected void setTurn() {
    double driveTurnAxisId = IO.driverGamepad.getRawAxis(Config.GAMEPAD_driveTurnAxisId);
    rotation = driveTurnAxisId;
  }

  protected void setTurnInPlace() {
    Robot.drivetrain.setTurnInPlace(IO.driverGamepad.getRawButton(Config.GAMEPAD_driveTurnInPlaceModeButton));
  }

  protected void setFastTurnMode() {
    Robot.drivetrain.setFastTurn(IO.driverGamepad.getRawButton(Config.GAMEPAD_driveFastTurnModeButton));
  }

  protected void setToddlerMode() {
    Robot.drivetrain.setToddlerMode(IO.driverGamepad.getRawButton(Config.GAMEPAD_driveToddlerModeButton)); 
  }

  protected void setBrakeMode() {
    Robot.drivetrain.setIdleMode(IO.driverGamepad.getRawButton(Config.GAMEPAD_driveBrakeModeButton));
  }
  
  protected void updateDrivetrain() {
    Robot.drivetrain.set(speed, rotation);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }
}
