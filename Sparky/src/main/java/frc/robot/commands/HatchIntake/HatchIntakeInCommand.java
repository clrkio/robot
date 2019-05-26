/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.HatchIntake;

import frc.robot.Robot;
import frc.robot.config.Config; 
import edu.wpi.first.wpilibj.command.Command;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class HatchIntakeInCommand extends Command {
  public HatchIntakeInCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.hatchIntake);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (Robot.hatchIntake.getLeftSwitchStatus() && Robot.hatchIntake.getRightSwitchStatus()) {
      Robot.hatchIntake.rollerMotor.set(Config.HATCH_INTAKE_rollerSpeedIn); 
    } else {
      Robot.hatchIntake.rollerMotor.stopMotor(); 
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }
}
