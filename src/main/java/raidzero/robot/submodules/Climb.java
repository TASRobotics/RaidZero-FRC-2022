package raidzero.robot.submodules;

import com.ctre.phoenix.motorcontrol.ControlMode;

import raidzero.robot.Constants.ClimbConstants;
import raidzero.robot.wrappers.LazyTalonSRX;

public class Climb extends Submodule {

    private static Climb instance = null;

    public static Climb getInstance() {
        if (instance == null) {
            instance = new Climb();
        }
        return instance;
    }

    private Climb() {
    }

    private LazyTalonSRX climbRight;
    private LazyTalonSRX climbLeft;

    private double outputOpenLoop = 0.0;

    @Override
    public void onInit() {
        climbRight = new LazyTalonSRX(ClimbConstants.MOTOR_ID_2);
        climbLeft = new LazyTalonSRX(ClimbConstants.MOTOR_ID);
        climbRight.configFactoryDefault();
        climbLeft.configFactoryDefault();

        climbRight.setNeutralMode(ClimbConstants.NEUTRAL_MODE);
        climbLeft.setNeutralMode(ClimbConstants.NEUTRAL_MODE);

        climbRight.setInverted(ClimbConstants.INVERSION);
        climbLeft.setInverted(false);
    }

    @Override
    public void onStart(double timestamp) {

    }

    @Override
    public void update(double timestamp) {

    }

    @Override
    public void run() {
        climbRight.set(ControlMode.PercentOutput, outputOpenLoop);
        climbLeft.set(ControlMode.PercentOutput, outputOpenLoop);
    }

    @Override
    public void stop() {
        climbRight.set(ControlMode.PercentOutput, 0);
        climbLeft.set(ControlMode.PercentOutput, 0);
    }


    /**
     * Climbs using open-loop control..
     * 
     * @param percentOutput percent output in [-1, 1]
     */
    public void climb(double percentOutput) {
        outputOpenLoop = percentOutput;
    }
}