package raidzero.robot.teleop;

import edu.wpi.first.wpilibj.XboxController;

import raidzero.robot.submodules.Swerve;
import raidzero.robot.submodules.*;
import raidzero.robot.Constants.SwerveConstants;
import raidzero.robot.Constants.IntakeConstants;
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

    private static boolean intakeshift = false;
    private static double intakeOut = 0;

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
            JoystickUtils.deadband(p.getLeftY()) * SwerveConstants.MAX_SPEED_MPS * (p.getRawButton(1) ? 1 : 0.5),
            JoystickUtils.deadband(p.getLeftX()) * SwerveConstants.MAX_SPEED_MPS * (p.getRawButton(1) ? 1 : 0.5),
            (turning) ? JoystickUtils.deadband(p.getRawAxis(2)) * (p.getRawButton(1) ? -1 : -1) : 0,
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
        if (p.getRawButton(2)) {
            swerve.zero();
            return;
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

        /**
         * Turret
        */
        if (p.getRawButton(3)) {
            turret.spin(0.2);
        }
        else if (p.getRawButton(4)) {
            turret.spin(-0.2);
        }
        else {
            turret.spin(0.0);
        }

        /**
         * Climb Hook
        */
        if (p.getRawButton(9)){
            climb.setSolenoid(true);
        }
        else if(p.getRawButton(10)){
            climb.setSolenoid(false);
        }

        /**
         * Climb
        */
        if (p.getRawButton(7))
        {
            climb.climb(0.5);
        }
        else if (p.getRawButton(8))
        {
            climb.climb(-0.5);
        }
        else
        {
            climb.climb(0.0);
        }


    }

    private void p2Loop(XboxController p) {

        /**
         * Shooter
         */
        if (p.getAButton()) {
            shooter.shoot(0.4, false);
        }
        else if (p.getAButtonReleased()) {
            shooter.shoot(0.0, false);
        }

        /**
         * Fire
         */
        if (shooter.isUpToSpeed() && p.getXButton()){
            throaty.moveBalls(1.0);
        }
        else if (p.getRawButton(13)){
            throaty.moveBalls(-0.3);
        }
        else{
            throaty.moveBalls(0.0);
        }


        /**
         * Intake
        */
        intakeshift =  p.getRawButton(6);
        intakeOut = ((p.getRawButton(5) || intakeshift) ? 1 : 0) * ((-p.getRawAxis(3))+1) / 2;
        System.out.println("intake: " + intakeOut);
        if (p.getRawButton(5)) {
            intake.intakeBalls((IntakeConstants.CONTROL_SCALING_FACTOR * intakeOut));
            throatx.moveBalls(0.7);
        }
        else if (p.getRawButton(6)) {
            intake.intakeBalls(-1*(IntakeConstants.CONTROL_SCALING_FACTOR * intakeOut));
            throatx.moveBalls(-0.7);
        }
        else {
            intake.intakeBalls(0.0);
            throatx.moveBalls(0.0);
        }

        /**
         * Intake Release
        */
        if (p.getRawButton(7)){
            intake.setSolenoid(true);
        }
        else if(p.getRawButton(8)){
            intake.setSolenoid(false);
        }

        // /**
        //  * ThroatX
        //  */
        // if (p.getYButton()){
        //     throatx.moveBalls(0.7);
        // }
        // else if (p.getBButton()){
        //     throaty.moveBalls(-0.7);
        // }
        // else{
        //     throaty.moveBalls(0.0);
        // }



    }
}
