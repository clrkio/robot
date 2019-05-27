/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.config.*; 
import frc.robot.commands.Arm.ArmControlCommand; 
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.SensorCollection; 
import com.ctre.phoenix.motorcontrol.FeedbackDevice; 
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * Add your docs here.
 */
public class Arm extends Subsystem {
    public WPI_TalonSRX armMotor; //right joystick 

  public Arm() {
    armMotor = new WPI_TalonSRX(Config.ARM_armPort); 
  }

  public void init() {
    armMotor.configFactoryDefault(); 
    armMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 1);
		armMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
  }  

  public double getArmPosition() {
    return armMotor.getSelectedSensorPosition();
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ArmControlCommand());
  }
}
