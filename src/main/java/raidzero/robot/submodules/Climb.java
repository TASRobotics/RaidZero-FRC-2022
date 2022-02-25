package raidzero.robot.submodules;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
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
    private InactiveDoubleSolenoid solenoid;

    @Override
    public void onInit() {
        extensionMotor = new LazyTalonFX(ClimbConstants.MOTOR_ID);
        extensionMotor.configFactoryDefault();
        extensionMotor.setNeutralMode(ClimbConstants.NEUTRAL_MODE);
        extensionMotor.setInverted(ClimbConstants.INVERSION);

        solenoid = new InactiveDoubleSolenoid(0, 1);
    }

    @Override
    public void onStart(double timestamp) {
        outputOpenLoop = 0.0;
        solenoid.setActive(true);
        solenoid.set(Value.kForward);
    }

    @Override
    public void update(double timestamp) {
        if (extensionMotor.isRevLimitSwitchClosed() == 1) {
            zero();
        }
    }

    @Override
    public void run() {
        extensionMotor.set(ControlMode.PercentOutput, outputOpenLoop);
    }

    @Override
    public void stop() {
        outputOpenLoop = 0.0;
        extensionMotor.set(ControlMode.PercentOutput, 0.0);
        solenoid.setActive(false);
    }

    @Override
    public void zero() {
        extensionMotor.setSelectedSensorPosition(0.0);
    }

    /**
     * Climbs using open-loop control..
     * 
     * @param percentOutput percent output in [-1, 1]
     */
    public void climb(double percentOutput) {
        outputOpenLoop = percentOutput;
    }

    public void setSolenoid(boolean value)
    {
        if (value)
            solenoid.set(Value.kForward);
        else 
            solenoid.set(Value.kReverse);
    }
}