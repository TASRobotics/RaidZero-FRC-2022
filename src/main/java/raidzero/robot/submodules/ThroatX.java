package raidzero.robot.submodules;

import raidzero.robot.wrappers.LazyCANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import raidzero.robot.Constants.ThroatXConstants;;

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

    private double outputOpenLoop = 0.0;

    @Override
    public void onInit() {
        conveyorMotor = new LazyCANSparkMax(ThroatXConstants.MOTOR_ID, MotorType.kBrushless);
        conveyorMotor.restoreFactoryDefaults();
        conveyorMotor.setIdleMode(ThroatXConstants.NEUTRAL_MODE);
        conveyorMotor.setInverted(ThroatXConstants.MOTOR_INVERSION);
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