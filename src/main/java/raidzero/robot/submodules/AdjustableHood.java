package raidzero.robot.submodules;

import java.util.Map;

import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxLimitSwitch.Type;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import raidzero.robot.wrappers.LazyCANSparkMax;
import raidzero.robot.Constants.HoodConstants;
import raidzero.robot.dashboard.Tab;
import raidzero.robot.utils.InterpolatingDouble;
import raidzero.robot.Constants.LimelightConstants;

public class AdjustableHood extends Submodule {

    private enum ControlState {
        OPEN_LOOP, POSITION
    };

    private static AdjustableHood instance = null;

    public static AdjustableHood getInstance() {
        if (instance == null) {
            instance = new AdjustableHood();
        }
        return instance;
    }

    private AdjustableHood() {
    }

    private static final Limelight limelight = Limelight.getInstance();
    private static final Swerve swerve = Swerve.getInstance();

    private LazyCANSparkMax hoodMotor;
    private SparkMaxPIDController pidController;
    private RelativeEncoder encoder;
    private SparkMaxLimitSwitch reverseLimitSwitch;
    private SparkMaxLimitSwitch forwardLimitSwitch;

    private double outputOpenLoop = 0.0;
    private double outputPosition = 0.0;
    private boolean zeroing = false;

    private ControlState controlState = ControlState.OPEN_LOOP;

    private NetworkTableEntry hoodPositionEntry =
            Shuffleboard.getTab(Tab.MAIN).add("Hood Position", 0).withWidget(BuiltInWidgets.kDial)
                    .withProperties(Map.of("min", 0, "max", 83)).withSize(2, 2).withPosition(0, 0)
                    .getEntry();

    @Override
    public void onInit() {
        hoodMotor = new LazyCANSparkMax(HoodConstants.MOTOR_ID, MotorType.kBrushless);
        hoodMotor.restoreFactoryDefaults();
        hoodMotor.setIdleMode(HoodConstants.IDLE_MODE);
        hoodMotor.setInverted(HoodConstants.INVERSION);

        encoder = hoodMotor.getEncoder();

        forwardLimitSwitch = hoodMotor.getForwardLimitSwitch(Type.kNormallyClosed);
        reverseLimitSwitch = hoodMotor.getReverseLimitSwitch(Type.kNormallyClosed);
        forwardLimitSwitch.enableLimitSwitch(true);
        reverseLimitSwitch.enableLimitSwitch(true);

        pidController = hoodMotor.getPIDController();
        pidController.setP(HoodConstants.K_P);
        pidController.setI(HoodConstants.K_I);
        pidController.setD(HoodConstants.K_D);
        pidController.setFF(HoodConstants.K_F);
        zeroing = false;
    }

    @Override
    public void onStart(double timestamp) {
        controlState = ControlState.OPEN_LOOP;

        outputOpenLoop = 0.0;
        outputPosition = 0.0;
    }

    @Override
    public void update(double timestamp) {
        if ((reverseLimitSwitch.isPressed() || forwardLimitSwitch.isPressed()) && zeroing) {
            zero();
            zeroing = false;
        }

        SmartDashboard.putNumber("Hood Angle", encoder.getPosition());
        hoodPositionEntry.setNumber(encoder.getPosition());
    }

    @Override
    public void run() {
        if(zeroing) {
            pidController.setReference(-0.6, ControlType.kDutyCycle);
            return;
        }
        switch (controlState) {
            case OPEN_LOOP:
                pidController.setReference(outputOpenLoop, ControlType.kDutyCycle);
                break;
            case POSITION:
                pidController.setReference(outputPosition, ControlType.kPosition);
                break;
        }
    }

    @Override
    public void stop() {
        controlState = ControlState.OPEN_LOOP;
        outputOpenLoop = 0.0;
        outputPosition = 0.0;
        pidController.setReference(0.0, ControlType.kDutyCycle);
    }

    @Override
    public void zero() {
        encoder.setPosition(0.0);
    }

    /**
     * Returns the position of the hood.
     * 
     * @return position in encoder ticks
     */
    public double getPosition() {
        return encoder.getPosition();
    }

    public void goToZero() {
        zeroing = true;
    }

    /**
     * Adjusts the hood using open-loop control.
     * 
     * @param percentOutput the percent output in [-1, 1]
     */
    public void adjust(double percentOutput) {
        controlState = ControlState.OPEN_LOOP;
        outputOpenLoop = percentOutput;
    }

    /**
     * Moves the hood to a position using closed-loop control.
     * 
     * @param position position in encoder units
     */
    public void moveToTick(double position) {
        controlState = ControlState.POSITION;
        outputPosition = position;
    }

    /**
     *  Automatically adjusts the hood 
     * 
     */
    public void autoPosition() {
        Pose2d currPose = swerve.getPose();
        Pose2d prevPose = swerve.getPrevPose();
        Pose2d deltaPose = new Pose2d(currPose.getX() - prevPose.getX(), 
                                      currPose.getY() - prevPose.getY(), 
                                      new Rotation2d(currPose.getRotation().getRadians() - prevPose.getRotation().getRadians()));
        

        moveToTick(LimelightConstants.DIST_TO_TICK_ESTIMATOR.getInterpolated(
            new InterpolatingDouble(limelight.getTy())).value
        );
    }

    /**
     * Returns whether the hood is at the target position in the position control mode.
     * 
     * @return if the hood is at the target position
     */
    public boolean isAtPosition() {
        return controlState == ControlState.POSITION
                && Math.abs(outputPosition - encoder.getPosition()) < HoodConstants.TOLERANCE;
    }
}
