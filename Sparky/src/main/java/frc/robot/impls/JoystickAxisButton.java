/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.impls;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;
import frc.robot.config.Config;

/**
 * Add your docs here.
 */
public class JoystickAxisButton extends Button {
    private final GenericHID m_joystick;
    private final int m_axisNumber;
  
    /**
     * Create a joystick axis button for triggering commands.
     *
     * @param joystick     The GenericHID object that has the button (e.g. Joystick, KinectStick,
     *                     etc)
     * @param buttonNumber The button number (see {@link GenericHID#getRawButton(int) }
     */
    public JoystickAxisButton(GenericHID joystick, int axisNumber) {
      m_joystick = joystick;
      m_axisNumber = axisNumber;
    }
  
    /**
     * Returns pressed if the absolute value of the axis is greater than 0.1
     *
     * @return The value of the joystick button
     */
    @Override
    public boolean get() {
      return Math.abs(m_joystick.getRawAxis(m_axisNumber)) > Config.MIN_AXIS_VALUE_FOR_PRESS;
    }


}
