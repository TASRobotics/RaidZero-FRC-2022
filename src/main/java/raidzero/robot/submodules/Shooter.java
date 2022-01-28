package raidzero.robot.submodules;

import raidzero.robot.wrappers.LazyTalonFX;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import raidzero.robot.wrappers.LazyCANSparkMax;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

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

    private LazyCANSparkMax motorLeft;
    private SparkMaxPIDController motorLeftPidController;

    private LazyCANSparkMax motorRight;
    private SparkMaxPIDController motorRightPidController;

    // private LazyTalonFX shooterMotor;

    private double outputPercentSpeed = 0.0;

    // private NetworkTableEntry shooterVelocityEntry =
    //         Shuffleboard.getTab(Tab.MAIN).add("Shooter Vel", 0).withWidget(BuiltInWidgets.kTextView)
    //                 .withSize(1, 1).withPosition(0, 2).getEntry();
    // private NetworkTableEntry shooterUpToSpeedEntry = Shuffleboard.getTab(Tab.MAIN)
    //         .add("Up To Speed", false).withWidget(BuiltInWidgets.kBooleanBox).withSize(1, 1)
    //         .withPosition(1, 2).getEntry();

    @Override
    public void onInit() {
        // shooterMotor = new LazyTalonFX(ShooterConstants.MOTOR_ID);
        // shooterMotor.configFactoryDefault();
        // shooterMotor.setNeutralMode(ShooterConstants.NEUTRAL_MODE);
        // shooterMotor.setInverted(ShooterConstants.INVERSION);

        // TalonFXConfiguration config = new TalonFXConfiguration();
        // config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
        // config.slot0.kF = ShooterConstants.K_F;
        // config.slot0.kP = ShooterConstants.K_P;
        // config.slot0.kI = ShooterConstants.K_I;
        // config.slot0.kD = ShooterConstants.K_D;
        // config.slot0.integralZone = ShooterConstants.K_INTEGRAL_ZONE;

        // shooterMotor.configAllSettings(config);

        /**
         * motorLeft config
         */
        motorLeft = new LazyCANSparkMax(NewShooterConstants.LEFT_MOTOR_ID, MotorType.kBrushless);
        motorLeft.restoreFactoryDefaults();
        motorLeft.setIdleMode(NewShooterConstants.NEUTRAL_MODE);
        motorLeft.setInverted(NewShooterConstants.LEFT_INVERSION);
        
        motorLeftPidController = motorLeft.getPIDController();

        motorLeftPidController.setFF(NewShooterConstants.KF);
        motorLeftPidController.setP(NewShooterConstants.KP);
        motorLeftPidController.setI(NewShooterConstants.KI);
        motorLeftPidController.setD(NewShooterConstants.KD);
        motorLeftPidController.setIZone(NewShooterConstants.IZONE);
        // motorLeftPidController.setOutputRange(NewShooterConstants.MINOUT, NewShooterConstants.MAXOUT);
        motorLeftPidController.setFeedbackDevice(motorLeft.getEncoder());

        /**
         * motorRight config
         */
        motorRight = new LazyCANSparkMax(NewShooterConstants.RIGHT_MOTOR_ID, MotorType.kBrushless);
        motorRight.restoreFactoryDefaults();
        motorRight.setIdleMode(NewShooterConstants.NEUTRAL_MODE);
        motorRight.setInverted(NewShooterConstants.RIGHT_INVERSION);
        
        motorRightPidController = motorRight.getPIDController();

        motorRightPidController.setFF(NewShooterConstants.KF);
        motorRightPidController.setP(NewShooterConstants.KP);
        motorRightPidController.setI(NewShooterConstants.KI);
        motorRightPidController.setD(NewShooterConstants.KD);
        motorRightPidController.setIZone(NewShooterConstants.IZONE);
        // motorRightPidController.setOutputRange(NewShooterConstants.MINOUT, NewShooterConstants.MAXOUT);
        motorRightPidController.setFeedbackDevice(motorRight.getEncoder());
    }

    @Override
    public void onStart(double timestamp) {
        outputPercentSpeed = 0.0;
        zero();
    }

    // @Override
    // public void update(double timestamp) {
    //     shooterVelocityEntry.setNumber(shooterMotor.getSelectedSensorVelocity());
    //     shooterUpToSpeedEntry.setBoolean(isUpToSpeed());
    // }

    @Override
    public void run() {
        // motorLeftPidController.setReference(outputPercentSpeed, ControlType.kVelocity);
        // motorRightPidController.setReference(outputPercentSpeed, ControlType.kVelocity);
        motorLeft.set(outputPercentSpeed);
        motorRight.set(outputPercentSpeed);
    }

    @Override
    public void stop() {
        outputPercentSpeed = 0.0;
        // motorLeftPidController.setReference(0.0, ControlType.kVelocity);
        // motorRightPidController.setReference(0.0, ControlType.kVelocity);
        motorLeft.set(0);
        motorRight.set(0);
    }

    @Override
    public void zero() {
        motorLeft.getEncoder();
        motorRight.getEncoder();
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
