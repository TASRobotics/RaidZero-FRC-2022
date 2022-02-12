package raidzero.robot.teleop;

import edu.wpi.first.wpilibj.XboxController;

import raidzero.robot.submodules.Superstructure;
import raidzero.robot.submodules.Swerve;
import raidzero.robot.submodules.*;
import raidzero.robot.Constants.HoodConstants.HoodAngle;
import raidzero.robot.Constants.IntakeConstants;
import raidzero.robot.Constants.ClimbConstants;
import raidzero.robot.Constants.SwerveConstants;
import raidzero.robot.submodules.Intake;
import raidzero.robot.submodules.Shooter;
import raidzero.robot.submodules.ThroatLong;
import raidzero.robot.submodules.ThroatShort;
import raidzero.robot.submodules.Extension;
import raidzero.robot.submodules.Limelight;
import raidzero.robot.utils.JoystickUtils;

public class Teleop {

    private static Teleop instance = null;
    private static XboxController p1 = new XboxController(0);
    private static XboxController p2 = new XboxController(1);

    private static final Swerve swerve = Swerve.getInstance();
    private static final Climb climb = Climb.getInstance();
    private static final Intake intake = Intake.getInstance();
    private static final ThroatShort throatShort = ThroatShort.getInstance();
    private static final ThroatLong throatLong = ThroatLong.getInstance();
    private static final Extension extension = Extension.getInstance();
    private static final Shooter shooter = Shooter.getInstance();

    private static boolean shift1 = false;
    private static boolean shift2 = false;
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
            intake.intakeBalls(0.5);
        }
        else if (p.getRawButton(8)) {
            intake.intakeBalls(-0.5);
        }
        else {
            intake.intakeBalls(0.0);
        }
    }

    private void p2Loop(XboxController p) {
        shift2 = p.getLeftBumper();

        
        /**
         * Throat
         */
        if (p.getYButton()) {
            throatLong.moveBalls(1.0);
            throatShort.moveBalls(1.0);
        }
        else if (p.getBButton()) {
            throatLong.moveBalls(-1.0);
            throatShort.moveBalls(-1.0);
        }
        else {
            throatLong.moveBalls(0.0);
            throatShort.moveBalls(0.0);
        }

        /**
         * Extension
         */
        if (p.getRightBumper()) {
            extension.extendManual(1.0);
        }
        else if (p.getLeftBumper()) {
            extension.extendManual(-1.0);
        }
        else {
            extension.extendManual(0);
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

        /**
         * Climb
        */
        climb.climb(p.getRightTriggerAxis() - p.getLeftTriggerAxis());
        if (p.getStartButton()){
            climb.run();
        }else{
            climb.stop();
        }
    }
}
