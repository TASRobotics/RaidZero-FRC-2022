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
import raidzero.robot.Constants.NewIntakeConstants;
import raidzero.robot.dashboard.Tab;



public class Intake extends Submodule {

    private static Intake instance = null;

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }
        return instance;
    }

    private Intake() {
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

        /**
         * motorLeft config
         */
        motorLeft = new LazyCANSparkMax(NewIntakeConstants.LEFT_MOTOR_ID, MotorType.kBrushless);
        motorLeft.restoreFactoryDefaults();
        motorLeft.setIdleMode(NewIntakeConstants.NEUTRAL_MODE);
        motorLeft.setInverted(NewIntakeConstants.LEFT_INVERSION);


        /**
         * motorRight config
         */
        motorRight = new LazyCANSparkMax(NewIntakeConstants.RIGHT_MOTOR_ID, MotorType.kBrushless);
        motorRight.restoreFactoryDefaults();
        motorRight.setIdleMode(NewIntakeConstants.NEUTRAL_MODE);
        motorRight.setInverted(NewIntakeConstants.RIGHT_INVERSION);
    }

    @Override
    public void onStart(double timestamp) {
        outputPercentSpeed = 0.0;
        zero();
    }

    @Override
    public void run() {
        motorLeft.set(outputPercentSpeed);
        motorRight.set(outputPercentSpeed);
    }

    @Override
    public void stop() {
        outputPercentSpeed = 0.0;
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
    public void intakeBalls(double percentSpeed) {
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
