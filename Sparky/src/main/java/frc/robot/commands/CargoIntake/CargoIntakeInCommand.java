/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.CargoIntake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Logger;
import frc.robot.Robot;

public class CargoIntakeInCommand extends Command {
  private static Logger logger = new Logger(CargoIntakeInCommand.class.getSimpleName());

  public CargoIntakeInCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.cargoIntake);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.cargoIntake.activateRollerIn();
    if (Robot.cargoIntake.isCargoDetected()) {
      Robot.wrist.raise();
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }
}
