package frc.robot.controls;

import edu.wpi.first.wpilibj.Joystick;

public class ButtonAction {

    public interface Action
    {
        void run() ;
    }

    private int buttonBounce ;
    private Joystick gamepad;
    private int buttonId;

    public ButtonAction(Joystick gamepad, int buttonId)
    {
        this.gamepad = gamepad;
        this.buttonId = buttonId;

        buttonBounce = 0;
    }

    public void check(Action press, Action release)
    {
        if ( gamepad.getRawButton(buttonId) ) {
            buttonBounce++ ;
            if ( 4 == buttonBounce ) { press.run(); }
        }
        else
        {
            if ( buttonBounce != 0 ) { release.run(); }
            buttonBounce = 0 ;
        }
    }

    public void check(Action press)
    {
        check(press, () -> {});
    }
}
