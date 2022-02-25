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
import com.revrobotics.CANSparkMax.IdleMode;
// import raidzero.robot.Constants.ConveyorConstants;
import raidzero.robot.Constants.ThroatShortConstants;

public class ThroatX extends Submodule {

    private static ThroatX instance = null;

    public static ThroatX getInstance() {
        if (instance == null) {
            instance = new ThroatX();
        }
        return instance;
    }

    private ThroatX() {
    }

    private LazyCANSparkMax conveyorMotor;
    // private LazyTalonSRX conveyorMotor;

    private double outputOpenLoop = 0.0;

    @Override
    public void onInit() {
        conveyorMotor = new LazyCANSparkMax(ThroatShortConstants.MOTOR_ID, MotorType.kBrushless);
        conveyorMotor.restoreFactoryDefaults();
        conveyorMotor.setIdleMode(ThroatShortConstants.NEUTRAL_MODE);
        conveyorMotor.setInverted(ThroatShortConstants.MOTOR_INVERSION);
        // conveyorMotor.setSensorPhase(ThroatShortConstants.SENSOR_PHASE);
    }

    @Override
    public void onStart(double timestamp) {
        outputOpenLoop = 0.0;
    }

    @Override
    public void run() {
        conveyorMotor.set(outputOpenLoop);//*ConveyorConstants.MAXSPEED);
    }

    @Override
    public void stop() {
        outputOpenLoop = 0.0;
        conveyorMotor.set(0.0);
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