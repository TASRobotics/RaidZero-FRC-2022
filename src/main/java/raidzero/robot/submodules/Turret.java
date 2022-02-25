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
import raidzero.robot.Constants.TurretConstants;
import raidzero.robot.dashboard.Tab;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import raidzero.robot.wrappers.LazyCANSparkMax;


public class Turret extends Submodule {

    private static Turret instance = null;

    public static Turret getInstance() {
        if (instance == null) {
            instance = new Turret();
        }
        return instance;
    }

    private Turret() {
    }

    private LazyCANSparkMax turret;

    public static enum ControlState {
        OPEN_LOOP, POSITION
    };
    // private LazyTalonFX shooterMotor;

    private double outputPercentSpeed = 0.0;

    private double outputOpenLoop = 0.0;
    private double outputPosition = 0.0;
    private ControlState controlState = ControlState.OPEN_LOOP;

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
        turret = new LazyCANSparkMax(TurretConstants.MOTOR_ID, MotorType.kBrushless);
        turret.restoreFactoryDefaults();
        turret.setIdleMode(TurretConstants.NEUTRAL_MODE);
        turret.setInverted(TurretConstants.INVERSION);

    }

    @Override
    public void onStart(double timestamp) {
        outputPercentSpeed = 0.0;
    }

    @Override
    public void run() {
        turret.set(outputPercentSpeed);
    }

    @Override
    public void stop() {
        outputPercentSpeed = 0.0;
        turret.set(outputPercentSpeed);
    }

    public void spin(double percentOutput) {
        outputPercentSpeed = percentOutput;
    }

     /**
     * Rotates the turret to the specified angle using closed-loop control.
     * 
     * @param angle the angle to rotate to
     */
    public void rotateToAngle(double angle) {
        controlState = ControlState.POSITION;
        outputPosition = -angle * TurretConstants.TICKS_PER_DEGREE;
    }

    /**
     * Rotates the turret using open-loop control.
     * 
     * Note: Positive (+) is clockwise
     * 
     * @param percentOutput the percent output in [-1, 1]
     */
    public void rotateManual(double percentOutput) {
        controlState = ControlState.OPEN_LOOP;
        outputOpenLoop = percentOutput * TurretConstants.MANUAL_COEF;
    }

    public boolean isInOpenLoop() {
        return controlState == ControlState.OPEN_LOOP;
    }

    public boolean isAtPosition() {
        return controlState == ControlState.POSITION &&
               Math.abs(turret.getEncoder().getPosition()) < TurretConstants.TOLERANCE;
    }
}
