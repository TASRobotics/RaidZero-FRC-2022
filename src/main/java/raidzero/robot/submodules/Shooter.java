package raidzero.robot.submodules;

import raidzero.robot.wrappers.LazyCANSparkMax;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import raidzero.robot.Constants;
import raidzero.robot.Constants.NewShooterConstants;
import raidzero.robot.dashboard.Tab;

public class Shooter extends Submodule {

    private static Shooter instance = null;

    public static Shooter getInstance() {
        if (instance == null) {
            instance = new Shooter();
        }
        return instance;
    }

    private Shooter() {
    }

    private LazyCANSparkMax shooterMotorRight;
    private LazyCANSparkMax shooterMotorLeft;
    private SparkMaxPIDController shooterPidController;

    private double outputPercentSpeed = 0.0;

    private NetworkTableEntry shooterVelocityEntry =
            Shuffleboard.getTab(Tab.MAIN).add("Shooter Vel", 0).withWidget(BuiltInWidgets.kTextView)
                    .withSize(1, 1).withPosition(0, 2).getEntry();

    // private NetworkTableEntry shooterUpToSpeedEntry = Shuffleboard.getTab(Tab.MAIN)
    //         .add("Up To Speed", false).withWidget(BuiltInWidgets.kBooleanBox).withSize(1, 1)
    //         .withPosition(0, 3).getEntry();

    @Override
    public void onInit() {
        shooterMotorRight = new LazyCANSparkMax(NewShooterConstants.RIGHT_MOTOR_ID, MotorType.kBrushless);
        shooterMotorLeft = new LazyCANSparkMax(NewShooterConstants.LEFT_MOTOR_ID, MotorType.kBrushless);

        shooterMotorRight.restoreFactoryDefaults();
        shooterMotorLeft.restoreFactoryDefaults();

        shooterMotorRight.setIdleMode(NewShooterConstants.NEUTRAL_MODE);
        shooterMotorRight.setIdleMode(NewShooterConstants.NEUTRAL_MODE);

        shooterMotorRight.setInverted(NewShooterConstants.RIGHT_INVERSION);
        shooterMotorRight.setInverted(NewShooterConstants.LEFT_INVERSION);

        shooterPidController = shooterMotorRight.getPIDController();

        shooterPidController.setFF(NewShooterConstants.KF);
        shooterPidController.setP(NewShooterConstants.KP);
        shooterPidController.setI(NewShooterConstants.KI);
        shooterPidController.setD(NewShooterConstants.KD);
        shooterPidController.setIZone(NewShooterConstants.IZONE);

        shooterMotorLeft.follow(shooterMotorRight);
    }

    @Override
    public void onStart(double timestamp) {
        outputPercentSpeed = 0.0;
        zero();
    }

    @Override
    public void update(double timestamp) {
        shooterVelocityEntry.setNumber(shooterMotorRight.getEncoder().getVelocity());
        // shooterUpToSpeedEntry.setBoolean(isUpToSpeed());
    }

    @Override
    public void run() {
        if (Math.abs(outputPercentSpeed) < 0.1) {
            stop();
        } else {
            shooterPidController.setReference(outputPercentSpeed * NewShooterConstants.FAKE_MAX_SPEED, ControlType.kVelocity);
        }
    }

    @Override
    public void stop() {
        outputPercentSpeed = 0.0;
        shooterMotorRight.set(0);
    }

    @Override
    public void zero() {
        // shooterMotor.getSensorCollection().setIntegratedSensorPosition(0.0, Constants.TIMEOUT_MS);
        shooterMotorRight.getEncoder().setPosition(0.0);
    }

    /**
     * Fires up the shooter.
     * 
     * @param percentSpeed speed of the shooter in [-1.0, 1.0]
     * @param freeze       whether to disregard the speed and keep the previous speed
     */
    public void shoot(double percentSpeed, boolean freeze) {
        if (freeze) {
            return;
        }
        outputPercentSpeed = percentSpeed;
    }

    /**
     * Returns whether the shooter is up to the setpoint speed.
     * 
     * @return whether the shooter is up to speed
     */
    // public boolean isUpToSpeed() {
    //     return Math.abs(outputPercentSpeed) > 0.1
    //             && Math.abs(shooterMotor.getClosedLoopError()) < ShooterConstants.ERROR_TOLERANCE;
    // }
}
