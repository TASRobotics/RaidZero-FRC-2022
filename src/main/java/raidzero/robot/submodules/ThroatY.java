package raidzero.robot.submodules;

import raidzero.robot.wrappers.LazyTalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import raidzero.robot.Constants.ThroatYConstants;

public class ThroatY extends Submodule {

    private static ThroatY instance = null;

    public static ThroatY getInstance() {
        if (instance == null) {
            instance = new ThroatY();
        }
        return instance;
    }

    private ThroatY() {
    }

    private LazyTalonFX conveyorMotor;

    private double outputOpenLoop = 0.0;

    @Override
    public void onInit() {
        conveyorMotor = new LazyTalonFX(ThroatYConstants.MOTOR_ID);
        conveyorMotor.configFactoryDefault();
        conveyorMotor.setNeutralMode(ThroatYConstants.NEUTRAL_MODE);
        conveyorMotor.setInverted(ThroatYConstants.MOTOR_INVERSION);

        TalonFXConfiguration config = new TalonFXConfiguration();
        config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
        config.slot0.kF = ThroatYConstants.KF;
        config.slot0.kP = ThroatYConstants.KP;
        config.slot0.kI = ThroatYConstants.KI;
        config.slot0.kD = ThroatYConstants.KD;
        config.slot0.integralZone = ThroatYConstants.IZONE;

        conveyorMotor.configAllSettings(config);
        conveyorMotor.selectProfileSlot(0, 0);

        SupplyCurrentLimitConfiguration currentConfig = new SupplyCurrentLimitConfiguration(true, 35, 35, 0);
        conveyorMotor.configSupplyCurrentLimit(currentConfig);

    }

    @Override
    public void onStart(double timestamp) {
        outputOpenLoop = 0.0;
    }

    @Override
    public void run() {
        conveyorMotor.set(ControlMode.PercentOutput,  outputOpenLoop);//*ConveyorConstants.MAXSPEED);
    }

    @Override
    public void stop() {
        outputOpenLoop = 0.0;
        conveyorMotor.set(ControlMode.PercentOutput, 0.0);
    }

    /**
     * Spins the conveyor using open-loop control
     * 
     * @param percentOutput the percent output is [-1, 1]
     */
    public void moveBalls(double output) {
        outputOpenLoop = output;
    }

}