package frc.robot.limelight; 

import frc.robot.config.*;
import frc.robot.common.*;
import edu.wpi.first.networktables.*;

public class AutoAlign {
    
    public DriveConstants getLimelightTracking() {
        // These numbers must be tuned for your Robot!  Be careful!
        // final double STEER_K = 0.03;                    // how hard to turn toward the target
        // final double DRIVE_K = 0.26;                    // how hard to drive fwd toward the target
        // final double DESIRED_TARGET_AREA = 13.0;        // Area of the target when the robot reaches the wall
        // final double MAX_DRIVE = 0.7;                   // Simple speed limit so we don't drive too fast
        
        final double STEER_K = -0.02;                    // how hard to turn toward the target
        final double DRIVE_K = 0.26;                    // how hard to drive fwd toward the target
        final double DESIRED_TARGET_AREA = 4.5;        // Area of the target when the robot reaches the wall

        double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
        double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        // double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
        double ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
        double ts = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ts").getDouble(0);
System.out.println("tv: " + tv + " tx: " + tx + " ta: " + ta + " ts: " + ts);
        if (tv < 1.0)
        {
          return null;
        }
        
        // Start with proportional steering
        double steer_cmd = tx * STEER_K;

        // try to drive forward until the target area reaches our desired area
        double drive_cmd = (DESIRED_TARGET_AREA - ta) * DRIVE_K;
        System.out.println("Attempted speed " + drive_cmd);

        // don't let the robot drive too fast into the goal
        drive_cmd = Math.min(drive_cmd, Config.DRIVE_AUTO_maxSpeed);
        System.out.println("steer: " + steer_cmd + " drive: " + drive_cmd);
        
        return new DriveConstants(drive_cmd, steer_cmd, 0, 0); 
  }
}