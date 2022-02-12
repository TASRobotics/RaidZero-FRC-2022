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
import raidzero.robot.Constants.ThroatLongConstants;

public class ThroatLong extends Submodule {

    private static ThroatLong instance = null;

    public static ThroatLong getInstance() {
        if (instance == null) {
            instance = new ThroatLong();
        }
        return instance;
    }

    private ThroatLong() {
    }

    private LazyTalonSRX conveyorMotor;

    private double outputOpenLoop = 0.0;

    @Override
    public void onInit() {
        conveyorMotor = new LazyTalonSRX(ThroatLongConstants.MOTOR_ID);
        conveyorMotor.configFactoryDefault();
        conveyorMotor.setNeutralMode(ThroatLongConstants.NEUTRAL_MODE);
        conveyorMotor.setInverted(ThroatLongConstants.MOTOR_INVERSION);
        conveyorMotor.setSensorPhase(ThroatLongConstants.SENSOR_PHASE);

        // TODO(jimmy): Tune PID constants
        TalonSRXConfiguration config = new TalonSRXConfiguration();
        config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
        config.slot0.kF = ThroatLongConstants.KF;
        config.slot0.kP = ThroatLongConstants.KP;
        config.slot0.kI = ThroatLongConstants.KI;
        config.slot0.kD = ThroatLongConstants.KD;
        config.slot0.integralZone = ThroatLongConstants.IZONE;

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
