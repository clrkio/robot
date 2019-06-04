/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

//http://10.90.20.11:5801/
package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;
import frc.robot.Robot;
import frc.robot.config.*;

public class LimeLightAutoAlignCommand extends Command {
  protected double speed; 
  protected double rotation; 
  protected boolean turnInPlace = true; 
  
  public LimeLightAutoAlignCommand() {
    requires(Robot.drivetrain);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (!validTarget()) {
      Robot.drivetrain.stop(); 
      return; 
    }
    setLedOn();
    setValues();
    updateDrivetrain();
  }

  private boolean validTarget() {
    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    SmartDashboard.putNumber("tv", tv);
    return tv == 1.0; 
  }

  private void setValues() {
    final double STEER_K = -0.05; // how hard to turn toward the target
    final double DRIVE_K = 0.20; // how hard to drive fwd toward the target
    final double DESIRED_TARGET_AREA = 35; // Area of the target when the robot reaches the wall

    //horizontal offset 
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    //vertical offset 
    double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    //target area 0%-100%
    double ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    //skew
    double ts = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ts").getDouble(0);
    //System.out.println("tv: " + tv + " tx: " + tx + " ta: " + ta + " ts: " + ts);

    double distance = DESIRED_TARGET_AREA - ta; 
    // Start with proportional steering
    rotation = distance * tx * STEER_K * Config.DRIVE_autoDriveMultiplier;

    // try to drive forward until the target area reaches our desired area
    System.out.println("ta: " + ta); 
    speed = distance * DRIVE_K * Config.DRIVE_autoDriveMultiplier;

    // don't let the robot drive too fast into the goal
    speed = Math.min(speed, Config.DRIVE_autoMaxSpeed);
    speed = Math.max(speed, Config.DRIVE_autoMinSpeed); 
    if (speed != 0) {
      System.out.println("speed: " + speed + " rotation: " + rotation);
    }
  }

  protected void updateDrivetrain() {
    Robot.drivetrain.robotDrive.curvatureDrive(speed, rotation, false);
  }

  protected void setLedOn() {
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3); 
  }

  protected void setLedOff() {
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1); 
  }

  protected void setLedDefault() {
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(0); 
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
