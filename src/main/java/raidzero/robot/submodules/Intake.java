package raidzero.robot.submodules;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import raidzero.robot.Constants;
import raidzero.robot.Constants.IntakeConstants;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import raidzero.robot.wrappers.LazyCANSparkMax;


public class Intake extends Submodule {

    private static Intake instance = null;

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }
        return instance;
    }

    private Intake() {
    }

    private double outputPercentSpeed = 0.0;

    private LazyCANSparkMax motorLeft;
    private LazyCANSparkMax motorRight;

    private DoubleSolenoid s_intake;


    // private NetworkTableEntry shooterVelocityEntry =
    //         Shuffleboard.getTab(Tab.MAIN).add("Shooter Vel", 0).withWidget(BuiltInWidgets.kTextView)
    //                 .withSize(1, 1).withPosition(0, 2).getEntry();
    // private NetworkTableEntry shooterUpToSpeedEntry = Shuffleboard.getTab(Tab.MAIN)
    //         .add("Up To Speed", false).withWidget(BuiltInWidgets.kBooleanBox).withSize(1, 1)
    //         .withPosition(1, 2).getEntry();

    @Override
    public void onInit() {

        motorLeft = new LazyCANSparkMax(IntakeConstants.LEFT_MOTOR_ID, MotorType.kBrushless);
        motorLeft.restoreFactoryDefaults();
        motorLeft.setIdleMode(IntakeConstants.NEUTRAL_MODE);
        motorLeft.setInverted(IntakeConstants.LEFT_INVERSION);

        motorRight = new LazyCANSparkMax(IntakeConstants.RIGHT_MOTOR_ID, MotorType.kBrushless);
        motorRight.restoreFactoryDefaults();
        motorRight.setIdleMode(IntakeConstants.NEUTRAL_MODE);

        motorRight.follow(motorLeft, true);

        s_intake = new DoubleSolenoid(Constants.PNEUMATICS_MODULE_TYPE, 2, 3);
    }

    @Override
    public void onStart(double timestamp) {
        outputPercentSpeed = 0.0;

        s_intake.set(Value.kForward);
    }

    
    @Override
    public void update(double timestamp) {
    }

    @Override
    public void run() {
        motorLeft.set(outputPercentSpeed);
    }

    @Override
    public void stop() {
        outputPercentSpeed = 0.0;
        motorLeft.set(outputPercentSpeed);
    }

    @Override
    public void zero() {
    }

    /**
     * Spins the intake using open-loop control
     * 
     * @param percentOutput the percent output is [-1, 1]
     */
    public void intakeBalls(double percentOutput) {
        outputPercentSpeed = percentOutput;
    }
    
    public void setSolenoid(boolean value)
    {
        if (value){
            s_intake.set(Value.kForward);
        }
        else{
            s_intake.set(Value.kReverse);
        }
    }

    public void toggleSolenoid()
    {
        s_intake.toggle();
    }

    public boolean getSolenoid()
    {
        if(s_intake.get() == Value.kForward)
            return true;
        else
            return false;
    }
}
