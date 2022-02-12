package raidzero.robot.submodules;

import raidzero.robot.wrappers.LazyCANSparkMax;
import raidzero.robot.wrappers.LazyTalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
// import raidzero.robot.Constants.ConveyorConstants;
import raidzero.robot.Constants.ThroatShortConstants;

public class ThroatShort extends Submodule {

    private static ThroatShort instance = null;

    public static ThroatShort getInstance() {
        if (instance == null) {
            instance = new ThroatShort();
        }
        return instance;
    }

    private ThroatShort() {
    }

    private LazyTalonSRX conveyorMotor;

    private double outputOpenLoop = 0.0;

    @Override
    public void onInit() {
        conveyorMotor = new LazyTalonSRX(ThroatShortConstants.MOTOR_ID);
        conveyorMotor.configFactoryDefault();
        conveyorMotor.setNeutralMode(ThroatShortConstants.NEUTRAL_MODE);
        conveyorMotor.setInverted(ThroatShortConstants.MOTOR_INVERSION);
        conveyorMotor.setSensorPhase(ThroatShortConstants.SENSOR_PHASE);

        // TODO(jimmy): Tune PID constants
        TalonSRXConfiguration config = new TalonSRXConfiguration();
        config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
        config.slot0.kF = ThroatShortConstants.KF;
        config.slot0.kP = ThroatShortConstants.KP;
        config.slot0.kI = ThroatShortConstants.KI;
        config.slot0.kD = ThroatShortConstants.KD;
        config.slot0.integralZone = ThroatShortConstants.IZONE;

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
