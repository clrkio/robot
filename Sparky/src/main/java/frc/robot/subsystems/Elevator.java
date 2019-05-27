/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.config.*; 
import frc.robot.commands.Elevator.ElevatorControlCommand; 
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.SensorCollection; 
import com.ctre.phoenix.motorcontrol.FeedbackDevice; 
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.NeutralMode; 

/**
 * Add your docs here.
 */
public class Elevator extends Subsystem {
  public WPI_TalonSRX elevatorMasterMotor; //left motor 
  public WPI_TalonSRX elevatorSlaveMotor; //right motor 

  public Elevator() {
    elevatorMasterMotor = new WPI_TalonSRX(Config.CAN_elevatorRight); 
    elevatorSlaveMotor = new WPI_TalonSRX(Config.CAN_elevatorLeft); 
    elevatorSlaveMotor.follow(elevatorMasterMotor); 

    elevatorMasterMotor.setNeutralMode(NeutralMode.Brake); 
    elevatorSlaveMotor.setNeutralMode(NeutralMode.Brake); 

    elevatorMasterMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0); 
  }
  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new ElevatorControlCommand());
  }

  public void move(double speed) {
    elevatorMasterMotor.set(speed); 
  }

  public double getElevatorRawPosition(){ 
    return elevatorMasterMotor.getSensorCollection().getQuadraturePosition(); 
  }
}
