/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.config.*;
import frc.robot.commands.CargoIntake.CargoIntakeRestCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * Add your docs here.
 */
public class CargoIntake extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public WPI_TalonSRX rollerMotor; //LB/RB
  public WPI_TalonSRX armMotor; //right joystick 

  public CargoIntake() {
    rollerMotor = new WPI_TalonSRX(Config.CARGO_INTAKE_rollerPort); 
    armMotor = new WPI_TalonSRX(Config.CARGO_INTAKE_armPort); 
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    setDefaultCommand(new CargoIntakeRestCommand());
  }
}
