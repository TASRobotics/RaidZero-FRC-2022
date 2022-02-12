package raidzero.robot.submodules;

import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.SparkMaxLimitSwitch.Type;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import raidzero.robot.wrappers.LazyCANSparkMax;

import raidzero.robot.Constants.ExtensionConstants;;

public class Extension extends Submodule {

    /**
     * 63:1
     */
    public static enum ControlState {
        OPEN_LOOP, POSITION
    };

    private static Extension instance = null;

    public static Extension getInstance() {
        if (instance == null) {
            instance = new Extension();
        }
        return instance;
    }

    private Extension() {
    }

    private LazyCANSparkMax turretMotor;
    private SparkMaxPIDController turretPidController;

    private double outputOpenLoop = 0.0;
    private double outputPosition = 0.0;
    private ControlState controlState = ControlState.OPEN_LOOP;

    @Override
    public void onInit() {
        turretMotor = new LazyCANSparkMax(ExtensionConstants.MOTOR_ID, MotorType.kBrushless);
        turretMotor.restoreFactoryDefaults();
        turretMotor.setIdleMode(ExtensionConstants.NEUTRAL_MODE);
        turretMotor.setInverted(ExtensionConstants.INVERSION);
        
        turretPidController = turretMotor.getPIDController();

        // turretPidController.setFF(ExtensionConstants.KF);
        // turretPidController.setP(ExtensionConstants.KP);
        // turretPidController.setI(ExtensionConstants.KI);
        // turretPidController.setD(ExtensionConstants.KD);
        // turretPidController.setIZone(ExtensionConstants.IZONE);
        // turretPidController.setOutputRange(ExtensionConstants.MINOUT, ExtensionConstants.MAXOUT);
        // turretPidController.setFeedbackDevice(turretMotor.getEncoder());
    }

    @Override
    public void onStart(double timestamp) {
        controlState = ControlState.OPEN_LOOP;
        outputOpenLoop = 0.0;
        outputPosition = 0.0;
        zero();
    }

    // @Override
    // public void update(double timestamp) {
    //     if (turretMotor.getForwardLimitSwitch(Type.kNormallyClosed).isPressed()) {
    //         zero();
    //     }
    // }

    @Override
    public void run() {
        switch (controlState) {
            case OPEN_LOOP:
                turretMotor.set(outputOpenLoop);
                break;
            case POSITION:
                turretPidController.setReference(outputPosition, ControlType.kPosition);
                break;
        }
    }

    @Override
    public void stop() {
        controlState = ControlState.OPEN_LOOP;
        outputOpenLoop = 0.0;
        outputPosition = 0.0;
        turretMotor.set(0);
    }

    @Override
    public void zero() {
        turretMotor.getEncoder();   
    }

    /**
     * @param percentOutput the percent output in [-1, 1]
     */
    public void extendManual(double percentOutput) {
        controlState = ControlState.OPEN_LOOP;
        outputOpenLoop = percentOutput;
    }
}
