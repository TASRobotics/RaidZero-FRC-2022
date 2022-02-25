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
import raidzero.robot.Constants.IntakeConstants;
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
    private LazyCANSparkMax motorRight;


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
        motorLeft = new LazyCANSparkMax(IntakeConstants.LEFT_MOTOR_ID, MotorType.kBrushless);
        motorLeft.restoreFactoryDefaults();
        motorLeft.setIdleMode(IntakeConstants.NEUTRAL_MODE);
        motorLeft.setInverted(IntakeConstants.LEFT_INVERSION);

        motorRight = new LazyCANSparkMax(IntakeConstants.RIGHT_MOTOR_ID, MotorType.kBrushless);
        motorRight.restoreFactoryDefaults();
        motorRight.setIdleMode(IntakeConstants.NEUTRAL_MODE);
        // motorRight.setInverted(IntakeConstants.RIGHT_INVERSION);
        motorRight.follow(motorLeft, true);
    }

    @Override
    public void onStart(double timestamp) {
        outputPercentSpeed = 0.0;
    }

    @Override
    public void run() {
        motorLeft.set(outputPercentSpeed);
    }

    @Override
    public void stop() {
        outputPercentSpeed = 0.0;
        motorLeft.set(outputPercentSpeed);
    }

    /**
     * Spins the intake using open-loop control
     * 
     * @param percentOutput the percent output is [-1, 1]
     */
    public void intakeBalls(double percentOutput) {
        outputPercentSpeed = percentOutput;
    }
}
