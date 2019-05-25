/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.config.*;
import frc.robot.commands.CargoIntake.CargoIntakeRestCommand;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.SensorCollection; 
import com.ctre.phoenix.motorcontrol.FeedbackDevice; 

/**
 * Add your docs here.
 */
public class CargoIntake extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public WPI_TalonSRX rollerMotor; //LB/RB
  public WPI_TalonSRX armMotor; //right joystick 
  private SensorCollection armEncoder; 
  public AnalogInput photoelectricSensor; 

  final int kTimeoutMs = 30; 
  final boolean kDiscontinuityPresent = true; 
  final int kBookEnd_0 = 910; 
  final int kBookEnd_1 = 1137; 

  public CargoIntake() {
    rollerMotor = new WPI_TalonSRX(Config.CARGO_INTAKE_rollerPort); 
    armMotor = new WPI_TalonSRX(Config.CARGO_INTAKE_armPort); 
    armEncoder = armMotor.getSensorCollection(); 
    photoelectricSensor = new AnalogInput(Config.CARGO_INTAKE_photoelectricPort); 

    /* Seed quadrature to be absolute and continuous */
		initQuadrature();
		
		/* Configure Selected Sensor for Talon */
    armMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,	// Feedback
                                        0, 											// PID ID
											                  kTimeoutMs);	
  }

  public int getPhotoeletricValue() {
    return photoelectricSensor.getValue(); 
  }

  public double getArmPosition() {
    /**
		 * Quadrature is selected for soft-lim/closed-loop/etc. initQuadrature()
		 * will initialize quad to become absolute by using PWD
		 */
		int pulseWidthWithoutOverflows = armEncoder.getPulseWidthPosition() & 0xFFF;

		/**
		 * Display how we've adjusted PWM to produce a QUAD signal that is
		 * absolute and continuous. Show in sensor units and in rotation
		 * degrees.
		 */


		System.out.println("pulseWidDeg:" + ToDeg(pulseWidthWithoutOverflows));
		return ToDeg(pulseWidthWithoutOverflows); 
  }

  /**
	 * Seed the quadrature position to become absolute. This routine also
	 * ensures the travel is continuous.
	 */
	public void initQuadrature() {
		/* get the absolute pulse width position */
		int pulseWidth = armEncoder.getPulseWidthPosition();

		/**
		 * If there is a discontinuity in our measured range, subtract one half
		 * rotation to remove it
		 */
		if (kDiscontinuityPresent) {

			/* Calculate the center */
			int newCenter;
			newCenter = (kBookEnd_0 + kBookEnd_1) / 2;
			newCenter &= 0xFFF;

			/**
			 * Apply the offset so the discontinuity is in the unused portion of
			 * the sensor
			 */
			pulseWidth -= newCenter;
		}

		/**
		 * Mask out the bottom 12 bits to normalize to [0,4095],
		 * or in other words, to stay within [0,360) degrees 
		 */
		pulseWidth = pulseWidth & 0xFFF;

		/* Update Quadrature position */
		armEncoder.setQuadraturePosition(pulseWidth, kTimeoutMs);
  }
  
  double ToDeg(int units) {
		double deg = units * 360.0 / 4096.0;

		/* truncate to 0.1 res */
		deg *= 10;
		deg = (int) deg;
		deg /= 10;

		return deg;
	}

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    setDefaultCommand(new CargoIntakeRestCommand());
  }
}
