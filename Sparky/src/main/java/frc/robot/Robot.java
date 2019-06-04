/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Preferences;
import frc.robot.config.Config;
import frc.robot.impls.SmartDashboardSubsystem;
import frc.robot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static Logger logger = new Logger(Robot.class.getSimpleName());

  public static IO m_oi;

  public static Drivetrain drivetrain = new Drivetrain(); 
  public static Wrist wrist = new Wrist(); 
  public static CargoIntake cargoIntake = new CargoIntake(); 
  public static HatchIntake hatchIntake = new HatchIntake(); 
  public static Elevator elevator = new Elevator(); 

  Command m_autonomousCommand = null;
  SendableChooser<Boolean> m_compressorState = new SendableChooser<>();

  private Preferences prefs;

  public static Compressor compressor;
  

  private SmartDashboardSubsystem[] subsystems =
    {drivetrain, wrist, cargoIntake, hatchIntake, elevator};

  private void logSmartDashboard() {
    for (SmartDashboardSubsystem s : subsystems) {
      s.logSmartDashboard();
    }
  }
  

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    logger.log("robotInit");
    prefs = Preferences.getInstance();

    Config.updateConfig(prefs);
    m_oi = new IO();
    m_compressorState.setDefaultOption("On", true);
    m_compressorState.addOption("Off", false);

    compressor = new Compressor(0);
    
    SmartDashboard.putData("Compress on", m_compressorState);
    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(); 
    camera.setResolution((int)Config.CAMERA_resX, (int)Config.CAMERA_resY);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    logSmartDashboard();
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
    logger.log("disabledInit");
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  
  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString code to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons to
   * the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    logger.log("autonomousInit");
    teleopInit();

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    teleopPeriodic();
  }

  @Override
  public void teleopInit() {
    logger.log("teleopInit");
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    Config.updateConfig(prefs);
    boolean isCompressorOn = m_compressorState.getSelected();
    compressor.setClosedLoopControl(isCompressorOn);
    logger.log("Compressor is " + (isCompressorOn ? "ON" : "OFF"));
    
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
