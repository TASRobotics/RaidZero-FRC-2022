package raidzero.robot.submodules;

import raidzero.robot.wrappers.LazyTalonFX;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import raidzero.robot.Constants;
import raidzero.robot.Constants.ShooterConstants;
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

    private LazyTalonFX motorLeft;
    private LazyTalonFX motorRight;

    private double outputPercentSpeed = 0.0;

    private NetworkTableEntry shooterVelocityEntry =
            Shuffleboard.getTab(Tab.MAIN).add("Shooter Vel", 0).withWidget(BuiltInWidgets.kTextView)
                    .withSize(1, 1).withPosition(0, 2).getEntry();
    private NetworkTableEntry shooterUpToSpeedEntry = Shuffleboard.getTab(Tab.MAIN)
            .add("Up To Speed", false).withWidget(BuiltInWidgets.kBooleanBox).withSize(1, 1)
            .withPosition(0, 3).getEntry();

    @Override
    public void onInit() {
        motorLeft = new LazyTalonFX(ShooterConstants.MOTOR_LEFT_ID);
        motorLeft.configFactoryDefault();
        motorLeft.setNeutralMode(ShooterConstants.NEUTRAL_MODE);
        motorLeft.setInverted(ShooterConstants.INVERSION);
        
        motorRight = new LazyTalonFX(ShooterConstants.MOTOR_RIGHT_ID);
        motorRight.configFactoryDefault();
        motorRight.setNeutralMode(ShooterConstants.NEUTRAL_MODE);
        motorRight.follow(motorLeft);
        motorRight.setInverted(InvertType.OpposeMaster);
        
        TalonFXConfiguration config = new TalonFXConfiguration();
        config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
        config.slot0.kF = ShooterConstants.K_F;
        config.slot0.kP = ShooterConstants.K_P;
        config.slot0.kI = ShooterConstants.K_I;
        config.slot0.kD = ShooterConstants.K_D;
        config.slot0.integralZone = ShooterConstants.K_INTEGRAL_ZONE;

        motorLeft.configAllSettings(config);
        motorRight.configAllSettings(config);
    }

    @Override
    public void onStart(double timestamp) {
        outputPercentSpeed = 0.0;
        zero();
    }

    @Override
    public void update(double timestamp) {
        shooterVelocityEntry.setNumber(motorLeft.getSelectedSensorVelocity());
        shooterUpToSpeedEntry.setBoolean(isUpToSpeed());
    }

    @Override
    public void run() {
        if (Math.abs(outputPercentSpeed) < 0.1) {
            stop();
        } else {
            motorLeft.set(ControlMode.Velocity,
                    outputPercentSpeed * ShooterConstants.FAKE_MAX_SPEED);
        }
    }

    @Override
    public void stop() {
        outputPercentSpeed = 0.0;
        motorLeft.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void zero() {
        motorLeft.getSensorCollection().setIntegratedSensorPosition(0.0, Constants.TIMEOUT_MS);
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
    public boolean isUpToSpeed() {
        return Math.abs(outputPercentSpeed) > 0.1
                && Math.abs(motorLeft.getClosedLoopError()) < ShooterConstants.ERROR_TOLERANCE;
    }
}
