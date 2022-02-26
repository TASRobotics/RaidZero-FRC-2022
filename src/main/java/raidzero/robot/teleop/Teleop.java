package raidzero.robot.teleop;

import edu.wpi.first.wpilibj.XboxController;

import raidzero.robot.submodules.Swerve;
import raidzero.robot.submodules.*;
import raidzero.robot.Constants.SwerveConstants;
import raidzero.robot.submodules.Limelight;
import raidzero.robot.utils.JoystickUtils;

public class Teleop {

    private static Teleop instance = null;
    private static XboxController p1 = new XboxController(0);
    private static XboxController p2 = new XboxController(1);

    private static final Swerve swerve = Swerve.getInstance();
    private static final Climb climb = Climb.getInstance();
    private static final Intake intake = Intake.getInstance();
    private static final Shooter shooter = Shooter.getInstance();
    private static final ThroatX throatx = ThroatX.getInstance();
    private static final ThroatY throaty = ThroatY.getInstance();
    private static final AdjustableHood hood = AdjustableHood.getInstance();
    private static final Turret turret = Turret.getInstance();


    public static Teleop getInstance() {
        if (instance == null) {
            instance = new Teleop();
        }
        return instance;
    }

    public void onStart() {
        swerve.zero();
    }

    /**
     * Continuously loops in teleop.
     */
    public void onLoop() {
        /**
         * shared controls
         */

        /**
         * p1 controls
         */
        p1Loop(p1);
        /**
         * p2 controls
         */
        p2Loop(p2);

        //System.out.println("Current Area: "+limelight.getTa());
        //System.out.println("Hood Angle: "+hood.getPosition());
    }

    private void p1Loop(XboxController p) {
        /**
         * Drive
        */
        boolean turning = p.getRawButton(12);
        swerve.drive(
            JoystickUtils.deadband(-p.getLeftY()) * SwerveConstants.MAX_SPEED_MPS * (p.getRawButton(1) ? 1 : 0.5),
            JoystickUtils.deadband(-p.getLeftX()) * SwerveConstants.MAX_SPEED_MPS * (p.getRawButton(1) ? 1 : 0.5),
            (turning) ? JoystickUtils.deadband(p.getRawAxis(2)) * (p.getRawButton(1) ? 0.5 : 0.25) : 0,
            true
        );
        // swerve.fieldOrientedDrive(
        //     JoystickUtils.deadband(p.getLeftX() * (p.getRawButton(1) ? 1 : 0.5)),
        //     JoystickUtils.deadband(p.getLeftY() * (p.getRawButton(1) ? -1 : -0.5)),
        //     //JoystickUtils.deadband(p.getRightX()));
        //     (turning) ? JoystickUtils.deadband(p.getRawAxis(2)) * (p.getRawButton(1) ? 0.5 : 0.25) : 0);
        /**
         * DO NOT CONTINUOUSLY CALL THE ZERO FUNCTION its not that bad but the absolute encoders are
         * not good to PID off of so a quick setting of the relative encoder is better
         */
        if (p.getXButtonPressed()) {
            swerve.zero();
            return;
        }
        
        /**
         * Intake
        */
        if (p.getRawButton(7)) {
            intake.intakeBalls(0.3);
        }
        else if (p.getRawButton(8)) {
            intake.intakeBalls(-0.3);
        }
        else {
            intake.intakeBalls(0.0);
        }

        /**
         * Throat
         */
        if (p.getRawButton(9)) {
            throatx.moveBalls(0.3);
        //    throaty.moveBalls(0.3);
        }
        else if (p.getRawButton(10)) {
            throatx.moveBalls(-0.3);
        //    throaty.moveBalls(-0.3);
        }
        else {
            throatx.moveBalls(0.0);
        //    throaty.moveBalls(0.0);
        }

        if (shooter.isUpToSpeed() && p.getRawButton(1)){
            throaty.moveBalls(0.3);
        }
        else if (p.getRawButton(12)){
            throaty.moveBalls(-0.3);
        }
        else{
            throaty.moveBalls(0.0);
        }

        /**
         * Hood
        */
        if (p.getRawButton(5)) {
            hood.adjust(0.2);
        }
        else if (p.getRawButton(6)) {
            hood.adjust(-0.2);
        }
        else {
            hood.adjust(0.0);
        }
    }

    private void p2Loop(XboxController p) {
        
        /**
         * Turret
        */
        if (p.getRawButton(5)) {
            turret.spin(0.5);
        }
        else if (p.getRawButton(6)) {
            turret.spin(-0.5);
        }
        else {
            turret.spin(0.0);
        }

        /**
         * Shooter
         */
        if (p.getAButton()) {
            shooter.shoot(1.0, false);
        }
        else {
            shooter.shoot(0.0, false);
        }

        // /**
        //  * Climb
        // */
        if (p.getYButton())
        {
            climb.climb(0.5);
        }
        else if (p.getBButton())
        {
            climb.climb(-0.5);
        }
        else
        {
            climb.climb(0.0);
        }





    }
}
