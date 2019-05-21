/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.networktables.*;
import frc.robot.Robot;
import frc.robot.config.*;

public class LimeLightAutoAlignCommand extends Command {
  protected double speed; 
  protected double rotation; 
  protected boolean quickTurn = true; 
  
  public LimeLightAutoAlignCommand() {
    requires(Robot.drivetrain);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  private void setValues() {
    final double STEER_K = -0.02; // how hard to turn toward the target
    final double DRIVE_K = 0.26; // how hard to drive fwd toward the target
    final double DESIRED_TARGET_AREA = 4.5; // Area of the target when the robot reaches the wall

    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    // double ty =
    // NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    double ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    double ts = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ts").getDouble(0);
    System.out.println("tv: " + tv + " tx: " + tx + " ta: " + ta + " ts: " + ts);
    if (tv < 1.0) {
      return;
    }

    // Start with proportional steering
    rotation = tx * STEER_K;

    // try to drive forward until the target area reaches our desired area
    speed = (DESIRED_TARGET_AREA - ta) * DRIVE_K;
    System.out.println("Attempted speed " + speed);

    // don't let the robot drive too fast into the goal
    speed = Math.min(speed, Config.DRIVE_AUTO_maxSpeed);
    System.out.println("speed: " + speed + " rotation: " + rotation);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
