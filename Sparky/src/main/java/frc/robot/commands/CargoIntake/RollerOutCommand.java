/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.CargoIntake;

import frc.robot.Robot;
import frc.robot.config.Config;
import edu.wpi.first.wpilibj.command.Command;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.*;

public class RollerOutCommand extends Command {
  public RollerOutCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.cargoIntake);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {   
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(Config.LIMELIGHT_LED_OFF);  
    Robot.cargoIntake.rollerMotor.set(Config.CARGO_INTAKE_rollerSpeedOut); 
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }
}
