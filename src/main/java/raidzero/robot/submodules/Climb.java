package raidzero.robot.submodules;

import com.ctre.phoenix.motorcontrol.ControlMode;

import raidzero.robot.Constants.ClimbConstants;
import raidzero.robot.wrappers.LazyTalonSRX;
import raidzero.robot.wrappers.InactiveCompressor;

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

    private double outputOpenLoop = 0.0;

    private LazyTalonSRX extensionMotor;
    private InactiveCompressor leftCompressor;
    private InactiveCompressor rightCompressor;

    @Override
    public void onInit() {
        
    }

    @Override
    public void onStart(double timestamp) {

    }

    @Override
    public void update(double timestamp) {

    }

    @Override
    public void run() {
    }

    @Override
    public void stop() {
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