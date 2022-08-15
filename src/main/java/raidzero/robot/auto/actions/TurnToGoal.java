package raidzero.robot.auto.actions;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.MathUtil;

import raidzero.robot.Constants.LimelightConstants;
import raidzero.robot.Constants.TurretConstants;
import raidzero.robot.submodules.Limelight;
import raidzero.robot.submodules.Turret;
import raidzero.robot.submodules.Shooter;
import raidzero.robot.submodules.Swerve;
import raidzero.robot.submodules.Limelight.CameraMode;
import raidzero.robot.submodules.Limelight.LedMode;

import raidzero.robot.utils.TimerBoolean;

/**
 * Action for turning the turret towards the goal using vision.
 */
public class TurnToGoal implements Action {

    public static enum DefaultMode {
        STOP, CLOCKWISE, COUNTER_CLOCKWISE
    }

    private static final Turret turret = Turret.getInstance();
    private static final Shooter shooter = Shooter.getInstance();
    private static final Limelight limelight = Limelight.getInstance();
    private static final Swerve swerve = Swerve.getInstance();

    public static boolean isAuton = false;

    private PIDController pidController;
    private double headingError;
    private DefaultMode defaultMode = DefaultMode.STOP;

    private TimerBoolean onTarget = new TimerBoolean(LimelightConstants.AIM_ON_TARGET_DURATION);

    public TurnToGoal() {
        this(DefaultMode.STOP);
    }

    public TurnToGoal(DefaultMode defaultMode) {
        pidController = new PIDController(
            LimelightConstants.AIM_KP, 
            LimelightConstants.AIM_KI, 
            LimelightConstants.AIM_KD
        );
        this.defaultMode = defaultMode;
        pidController.setIntegratorRange(LimelightConstants.MAX_I, LimelightConstants.MIN_I);
        pidController.setTolerance(LimelightConstants.ANGLE_ADJUST_THRESHOLD);
    }

    @Override
    public boolean isFinished() {
        return onTarget.hasDurationPassed();
    }

    @Override
    public void start() {
        onTarget.reset();

        pidController.reset();
        pidController.setSetpoint(0.0);

        limelight.setLedMode(LedMode.On);
        limelight.setPipeline(0);
        limelight.setCameraMode(CameraMode.Vision);
        System.out.println("[Auto] Action '" + getClass().getSimpleName() + "' started!");
    }

    @Override
    public void update() {
        if (!limelight.hasTarget()) {
            onTarget.update(false);
            if (defaultMode == DefaultMode.STOP) {
                if (turret.isInOpenLoop()) {
                    if (isAuton)
                        turret.rotateManual(0.2);
                    else
                        turret.stop();
                    }
            } else {
                double output = TurretConstants.MAX_INPUT_PERCENTAGE;
                output *= (defaultMode == DefaultMode.CLOCKWISE) ? 1 : -1;
                turret.rotateManual(output);
            }
            return;
		}
        headingError = limelight.getTx();

        double output = MathUtil.clamp(
            pidController.calculate(headingError),
            -TurretConstants.MAX_INPUT_PERCENTAGE,
            TurretConstants.MAX_INPUT_PERCENTAGE
        );
        System.out.println(headingError);
        turret.rotateManual(output);
        
        onTarget.update(pidController.atSetpoint());
    }

    @Override
    public void done() {
        System.out.println("[Auto] Action '" + getClass().getSimpleName() + "' finished!");
        //limelight.setLedMode(LedMode.Off);
        turret.stop();
    }

    public double getShooterSpeed() {
        double a = 7.956e-05;
        double b = 2.554;
        double c = 0.3877; //3.823 3.886
        return a * Math.pow(Math.abs(limelight.getTy()), b) + c;
    }
}