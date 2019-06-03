
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * 
 */
public class Logger {
    private String logPrefix;

    public Logger(String _logPrefix) {
        logPrefix = _logPrefix;
    }

    public void log(String logString) {
        System.out.println(String.format("%s: %s", logPrefix, logString));
    }
}
