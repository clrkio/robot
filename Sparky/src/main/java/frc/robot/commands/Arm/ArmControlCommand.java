/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Arm;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.OI;
import frc.robot.config.Config;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class ArmControlCommand extends Command {
  public ArmControlCommand() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    super(); 
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double position = Robot.arm.getArmPosition(); 
    if (position <= Config.ARM_armDownPosition || position >= Config.ARM_armUpPosition) {
      Robot.arm.armMotor.set(Config.ARM_armStop); 
    }
    double speed = OI.driverGamepad.getRawAxis(Config.GAMEPAD_cargoIntakeArmAxisId)*Config.ARM_armMultiplier;
    if (speed > 0) {
        speed = Math.max(speed, Config.ARM_maxArmMinSpeed); 
        speed = Math.min(speed, Config.ARM_maxArmMaxSpeed); 
    } else {
        speed = Math.min(speed, -Config.ARM_maxArmMaxSpeed);
        speed = Math.max(speed, -Config.ARM_maxArmMinSpeed); 
    }
    Robot.arm.armMotor.set(speed); 
  }


  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
