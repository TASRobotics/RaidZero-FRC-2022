package raidzero.robot.auto.actions;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.math.MathUtil;
import raidzero.robot.Constants;
import raidzero.robot.Constants.LimelightConstants;
import raidzero.robot.Constants.TurretConstants;
import raidzero.robot.dashboard.Tab;
import raidzero.robot.submodules.Limelight;
import raidzero.robot.submodules.Turret;
import raidzero.robot.submodules.Shooter;
import raidzero.robot.submodules.Swerve;
import raidzero.robot.submodules.Limelight.CameraMode;
import raidzero.robot.submodules.Limelight.LedMode;

import raidzero.lib.util.TimedBoolean;

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
    
    private double robotVx = 0.0;
    private double robotVy = 0.0;


    private PIDController pidController;
    private double headingError;
    private DefaultMode defaultMode = DefaultMode.STOP;
    private NetworkTableInstance inst = NetworkTableInstance.getDefault();

    private TimedBoolean onTarget = new TimedBoolean(LimelightConstants.AIM_ON_TARGET_DURATION);

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

        robotVx = swerve.getChassisSpeed().vxMetersPerSecond;
        robotVy = swerve.getChassisSpeed().vyMetersPerSecond;

        double output = MathUtil.clamp(
            pidController.calculate(headingError),
            -TurretConstants.MAX_INPUT_PERCENTAGE,
            TurretConstants.MAX_INPUT_PERCENTAGE
        );
        System.out.println(headingError);
        turret.rotateManual(output);
        shooter.shoot(this.calculateSpeed(), false);
        // hood.moveToTick(this.calculateHood());
        
        onTarget.update(pidController.atSetpoint());
    }

    @Override
    public void done() {
        System.out.println("[Auto] Action '" + getClass().getSimpleName() + "' finished!");
        //limelight.setLedMode(LedMode.Off);
        turret.stop();
        shooter.shoot(0.0, false);
        // hood.adjust(0.0);

        double offset = inst.getTable("limelight").getEntry("<tx>").getDouble(0);
        Shuffleboard.getTab("Limelight").add("Value Offset", offset);
    }

    private int calculateSpeed() {
        return 0;
    }

    private int calculateHood() {
        return (int)limelight.getTy();
    }

    public double getAngleOffset() {
        double chassisVx = swerve.getChassisSpeed().vxMetersPerSecond;
        double chassisVy = swerve.getChassisSpeed().vyMetersPerSecond;
        double strafeSpeed = Math.pow(Math.pow(chassisVx,2)+Math.pow(chassisVy,2),.5);
        double targetAngleFromRobot = Math.atan2(chassisVy,chassisVx)+Math.toRadians(turret.getAngle())+Math.toRadians(limelight.getTy()*Constants.LimelightConstants.PIXELS_TO_DEGREES);
        double newShooterSpeed = Math.pow(Math.pow(strafeSpeed,2)+Math.pow(getShooterSpeed(),2)-2*strafeSpeed*getDirectShooterSpeed()*Math.cos(targetAngleFromRobot),0.5);
        return Math.asin(strafeSpeed/newShooterSpeed*Math.sin(targetAngleFromRobot));
    }

    private double getDirectShooterSpeed() {
        //Magic Number 32m/s is max speed shooter can go
        double speedOffset = swerve.getChassisSpeed().vxMetersPerSecond*Math.cos(Math.toRadians(turret.getAngle()))
            + swerve.getChassisSpeed().vyMetersPerSecond*Math.sin(Math.toRadians(turret.getAngle()));
        double a = 7.956e-05;
        double b = 2.554;
        double c = 0.3877; //3.823 3.886
        return a * Math.pow(Math.abs(limelight.getTy()), b) + c - speedOffset/Constants.ShooterConstants.FLYWHEEL_TO_BALL_VELOCITY;
    }

    public double getShooterSpeed() {
        return getDirectShooterSpeed()/Math.cos(Math.toRadians(headingError));
    }
}