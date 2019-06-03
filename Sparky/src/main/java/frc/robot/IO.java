/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.POVButton;
import frc.robot.config.Config;
import frc.robot.impls.JoystickAxisButton;
import frc.robot.commands.Drivetrain.*;
import frc.robot.commands.Elevator.ElevatorLowerCommand;
import frc.robot.commands.Elevator.ElevatorRaiseCommand;
import frc.robot.commands.ZeroEncodersCommand;
import frc.robot.commands.CargoIntake.*;
import frc.robot.commands.HatchIntake.*;
import frc.robot.commands.Wrist.WristLowerCommand;
import frc.robot.commands.Wrist.WristRaiseCommand;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class IO {
  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  // Button button = new JoystickButton(stick, buttonNumber);

  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());
  public static Joystick driverGamepad; 
  public static Joystick playerGamepad;
  public static Compressor compressor;

  public IO() {
    driverGamepad = new Joystick(Config.GAMEPAD_driverJoystickId);
    playerGamepad = new Joystick(Config.GAMEPAD_playerJoystickId); 

    // Make compressor available and also set up it on
    compressor = new Compressor(0);
    compressor.setClosedLoopControl(false);

    //Driver Code Controls 
    Button driveHighSpeedShift = new JoystickButton(driverGamepad, Config.GAMEPAD_driveHighSpeedButton);
    Button driveLowSpeedShift = new JoystickButton(driverGamepad, Config.GAMEPAD_driveLowSpeedButton);
    Button driveIdleToggle = new JoystickButton(driverGamepad, Config.GAMEPAD_driveToggleIdleMode);

    Button limeLightAutoAlignButton = new JoystickButton(driverGamepad, Config.GAMEPAD_driveAutoButton); 
    Button ledOffButton = new JoystickButton(playerGamepad, Config.GAMEPAD_ledOff); 
    Button ledOnButton = new JoystickButton(playerGamepad, Config.GAMEPAD_ledOn); 

    // DRIVE CONTROLS
    driveHighSpeedShift.whenPressed(new HighSpeedShiftCommand());
    driveLowSpeedShift.whenPressed(new LowSpeedShiftCommand());
    driveIdleToggle.whenPressed(new ToggleIdleModeCommand());

    // limeLightAutoAlignButton.whileHeld(new LimeLightAutoAlignCommand());
    // ledOffButton.whenPressed(new LEDOffCommand());
    // ledOnButton.whenPressed(new LEDOnCommand());

    // ENCODER CONTROLS
    Button zeroEncodersButton = new JoystickButton(playerGamepad, Config.GAMEPAD_zeroEncoders);
    zeroEncodersButton.whenPressed(new ZeroEncodersCommand());

    // HATCH CONTROLS
    Button hatchExtendButton = new POVButton(playerGamepad, Config.GAMEPAD_hatchExtendPOV);
    Button hatchRetractButton = new POVButton(playerGamepad, Config.GAMEPAD_hatchRetractPOV);

    Button hatchIntakeInButton = new POVButton(playerGamepad, Config.GAMEPAD_hatchIntakeInPOV); 
    Button hatchIntakeOutButton = new POVButton(playerGamepad, Config.GAMEPAD_hatchIntakeOutPOV);
    
    hatchExtendButton.whenPressed(new HatchExtendCommand());
    hatchRetractButton.whenPressed(new HatchRetractCommand());
    hatchIntakeInButton.whileHeld(new HatchIntakeInCommand());
    hatchIntakeOutButton.whileHeld(new HatchIntakeOutCommand());

    // CARGO CONTROLS
    Button cargoIntakeInButton = new JoystickButton(playerGamepad, Config.GAMEPAD_cargoIntakeRollerInButton);
    Button cargoIntakeOutButton = new JoystickAxisButton(playerGamepad, Config.GAMEPAD_cargoIntakeRollerOutAxis);

    cargoIntakeInButton.whileHeld(new CargoIntakeInCommand());
    cargoIntakeOutButton.whileHeld(new CargoIntakeOutCommand());

    // WRIST CONTROLS
    Button wristRaiseButton = new JoystickButton(playerGamepad, Config.GAMEPAD_wristUpButton);
    Button wristLowerButton = new JoystickAxisButton(playerGamepad, Config.GAMEPAD_wristDownAxis);

    wristRaiseButton.whenPressed(new WristRaiseCommand());
    wristLowerButton.whenPressed(new WristLowerCommand());

    // ELEVATOR CONTROLS
    Button elevatorRaiseButton = new JoystickButton(playerGamepad, Config.GAMEPAD_elevatorUpButton);
    Button elevatorLowerButton = new JoystickButton(playerGamepad, Config.GAMEPAD_elevatorDownButton);

    elevatorRaiseButton.whenPressed(new ElevatorRaiseCommand());
    elevatorLowerButton.whenPressed(new ElevatorLowerCommand());
  }
}
