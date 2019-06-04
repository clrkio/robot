/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Logger;
import frc.robot.IO;
import frc.robot.commands.CargoIntake.CargoIntakeCommand;
import frc.robot.config.Config;
import frc.robot.impls.SmartDashboardSubsystem;

/**
 * Add your docs here.
 */
public class CargoIntake extends SmartDashboardSubsystem {
  private static Logger logger = new Logger(CargoIntake.class.getSimpleName());
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  enum Direction {
    STOP, IN, OUT, DETECTED, HOLD
  }

  private CANSparkMax rollerMotor;
  private DigitalInput photoelectricSensor; 

  private Direction direction;

  public CargoIntake() {
    rollerMotor = new CANSparkMax(Config.CAN_cargoIntake, MotorType.kBrushed);
    rollerMotor.setInverted(true);
    rollerMotor.setIdleMode(IdleMode.kBrake);

    photoelectricSensor = new DigitalInput(Config.CARGO_INTAKE_photoelectricDIO); 
    stop();
  } 

  public boolean isCargoDetected() {
    return !photoelectricSensor.get() && !IO.playerGamepad.getRawButton(Config.GAMEPAD_ignorePhotoelectric);
  }

  public void activateRollerIn() {
    if (!isCargoDetected()) {
      rollerMotor.set(Config.CARGO_INTAKE_rollerInSpeed);
      direction = Direction.IN;
    }
    else {
      holdIn();
    }
  }

  public void activateRollerOut() {
    rollerMotor.set(Config.CARGO_INTAKE_rollerOutSpeed);
    direction = Direction.OUT;
  }

  public void holdIn() {
    rollerMotor.set(Config.CARGO_INTAKE_holdSpeed);
    direction = Direction.HOLD;
  }

  public void stop() {
    if (isCargoDetected()) {
      holdIn();
    }
    else {
      rollerMotor.set(0);
      direction = Direction.STOP;
    }
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new CargoIntakeCommand());
  }

  @Override
  public void logSmartDashboard() {
    // Smart dashboard cargo roll in/out/hold/stop
    SmartDashboard.putString("CargoIntake RollerState", direction.toString());
    
    // Smart dashboard cargo loaded and photoelectric value
    SmartDashboard.putBoolean("CargoIntake IsCargoDetected", isCargoDetected());
    SmartDashboard.putBoolean("CargoIntake IgnorePhotoelectric", IO.playerGamepad.getRawButton(Config.GAMEPAD_ignorePhotoelectric));
  }
}
