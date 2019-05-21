package frc.robot.drivetrain; 

import frc.robot.config.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain {
  
    private CANSparkMax leftMotorPrimary;
    private CANSparkMax leftMotorSlaveA;
    private CANSparkMax leftMotorSlaveB;
    private CANSparkMax rightMotorPrimary;
    private CANSparkMax rightMotorSlaveA;
    private CANSparkMax rightMotorSlaveB;
  
    public DifferentialDrive robotDrive;
  
    private final SendableChooser<String> m_chooser = new SendableChooser<>();

    public DriveTrain() {
    
    leftMotorPrimary = new CANSparkMax(Config.CAN_leftDrivePrimary, MotorType.kBrushless);
    leftMotorSlaveA = new CANSparkMax(Config.CAN_leftDriveFollowerA, MotorType.kBrushless);
    leftMotorSlaveB = new CANSparkMax(Config.CAN_leftDriveFollowerB, MotorType.kBrushless);

    rightMotorPrimary = new CANSparkMax(Config.CAN_rightDrivePrimary, MotorType.kBrushless);
    rightMotorSlaveA = new CANSparkMax(Config.CAN_rightDriveFollowerA, MotorType.kBrushless);
    rightMotorSlaveB = new CANSparkMax(Config.CAN_rightDriveFollowerB, MotorType.kBrushless);

    leftMotorSlaveA.follow(leftMotorPrimary);
    leftMotorSlaveB.follow(leftMotorPrimary);
    // leftMotorPrimary.setInverted(!Config.DRIVE_rightIsInverted);
    // leftMotorSlaveA.setInverted(!Config.DRIVE_rightIsInverted);
    // leftMotorSlaveB.setInverted(!Config.DRIVE_rightIsInverted);

    rightMotorSlaveA.follow(rightMotorPrimary);
    rightMotorSlaveB.follow(rightMotorPrimary);
    // rightMotorPrimary.setInverted(Config.DRIVE_rightIsInverted);
    // rightMotorSlaveA.setInverted(Config.DRIVE_rightIsInverted);
    // rightMotorSlaveB.setInverted(Config.DRIVE_rightIsInverted);

    robotDrive = new DifferentialDrive(leftMotorPrimary, rightMotorPrimary);
    }

    public void controlledDrive(double forwardValue, double reverseValue, double driveTurnAxisId, boolean quickTurn) {
        double speedValue = forwardValue - reverseValue;

        double turnModifer = speedValue > 0 ? - 1 : 1;
        double turnValue = turnModifer * driveTurnAxisId; 

        speedValue = Math.min(Config.DRIVE_maxSpeed, speedValue);
        speedValue = Math.max(Config.DRIVE_minSpeed, speedValue);
        robotDrive.curvatureDrive(speedValue, turnValue, quickTurn);
    }
}
