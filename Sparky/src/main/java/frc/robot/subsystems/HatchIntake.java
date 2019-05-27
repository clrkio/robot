/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.config.*;
import frc.robot.commands.HatchIntake.HatchIntakeCommand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * Add your docs here.
 */
public class HatchIntake extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public WPI_TalonSRX rollerMotor; 
  public Solenoid leftSolenoid; 
  public Solenoid rightSolenoid; 
  public DigitalInput leftSwitch; 
  public DigitalInput rightSwitch; 

  public HatchIntake() {
    rollerMotor = new WPI_TalonSRX(Config.CAN_hatchIntake); 
    leftSwitch = new DigitalInput(Config.HATCH_INTAKE_leftSwitchPort);
    rightSwitch = new DigitalInput(Config.HATCH_INTAKE_rightSwitchPort); 
  }

  public boolean getLeftSwitchStatus() {
    return leftSwitch.get(); 
  }

  public boolean getRightSwitchStatus() {
    return rightSwitch.get(); 
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new HatchIntakeCommand());
  }
}
