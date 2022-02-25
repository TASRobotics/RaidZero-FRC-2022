package raidzero.robot.submodules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import raidzero.robot.Constants.ClimbConstants;
import raidzero.robot.wrappers.InactiveDoubleSolenoid;
import raidzero.robot.wrappers.LazyTalonFX;


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
    
    private LazyTalonFX extensionMotor;
    private InactiveDoubleSolenoid solenoidLeft;
    private InactiveDoubleSolenoid solenoidRight;

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