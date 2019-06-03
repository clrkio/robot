/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.config.*;
import frc.robot.impls.SmartDashboardSubsystem;
import frc.robot.Logger;
import frc.robot.Robot;
import frc.robot.commands.HatchIntake.HatchIntakeDefaultCommand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * Add your docs here.
 */
public class HatchIntake extends SmartDashboardSubsystem {
  private static Logger logger = new Logger(HatchIntake.class.getSimpleName());

  enum Direction {
    STOP, IN, OUT, HOLD
  }

  private WPI_TalonSRX rollerMotor;
  
  private DoubleSolenoid extenderSolenoid;
  private DigitalInput leftSwitch; 
  private DigitalInput rightSwitch; 

  private boolean isHatchExtended;
  private Direction direction;

  public HatchIntake() {
    rollerMotor = new WPI_TalonSRX(Config.CAN_hatchIntake);
    rollerMotor.setInverted(InvertType.InvertMotorOutput);

    extenderSolenoid = new DoubleSolenoid(Config.SOLENOID_hatchRetract, Config.SOLENOID_hatchExtend);
    
    leftSwitch = new DigitalInput(Config.HATCH_INTAKE_leftSwitchDIO);
    rightSwitch = new DigitalInput(Config.HATCH_INTAKE_rightSwitchDIO);
    
    // Initial states
    retractHatch();
    stop();
  }

  public boolean isLeftSwitchPressed() {
    return !leftSwitch.get(); 
  }

  public boolean isRightSwitchPressed() {
    return !rightSwitch.get(); 
  }

  public boolean isHatchLoaded() {
    return isLeftSwitchPressed() && isRightSwitchPressed();
  }

  public boolean isHatchExtended() {
    return isHatchExtended;
  }

  public void extendHatch() {
    if (isHatchExtended) {
      return; // Already extended
    }

    if (!Robot.elevator.isSafeForHatch()) {
      logger.log("Elevator not in safe position for hatch extension!");
      return;
    }

    logger.log("Extending hatch.");
    extenderSolenoid.set(Value.kReverse);
    isHatchExtended = true;
  }

  public void retractHatch() {
    if (!isHatchExtended) {
      return; // Already retracted
    }

    if (!Robot.elevator.isSafeForHatch()) {
      logger.log("Elevator not in safe position for hatch retraction!");
      return;
    }

    logger.log("Retracting hatch.");
    extenderSolenoid.set(Value.kForward);
    isHatchExtended = false;
  }

  public void feedIn() {
    if (!isHatchExtended() || isHatchLoaded()) {
      stop();
      return;
    }

    rollerMotor.set(Config.HATCH_INTAKE_rollerSpeedIn); 
    direction = Direction.IN;
  }

  public void feedOut() {
    if (!isHatchExtended()) {
      stop();
      return;
    }

    rollerMotor.set(Config.HATCH_INTAKE_rollerSpeedOut);
    direction = Direction.OUT;
  }

  public void holdIn() {
    rollerMotor.set(Config.HATCH_INTAKE_rollerSpeedHold);
    direction = Direction.HOLD;
  }

  public void stop() {
    if (isHatchExtended() && isHatchLoaded()) {
      holdIn();
      return;
    }

    rollerMotor.stopMotor();
    direction = Direction.STOP;
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new HatchIntakeDefaultCommand());
  }

  @Override
  public void logSmartDashboard() {
    SmartDashboard.putString("Hatch RollerState", direction.toString());

    SmartDashboard.putBoolean("Hatch Extended", isHatchExtended());
    SmartDashboard.putBoolean("Hatch LeftSwitchPressed", isLeftSwitchPressed());
    SmartDashboard.putBoolean("Hatch RightSwitchPressed", isRightSwitchPressed());
    SmartDashboard.putBoolean("Hatch Loaded", isHatchLoaded());
    SmartDashboard.putBoolean("Hatch ElevatorSafe", Robot.elevator.isSafeForHatch());
  }
}
