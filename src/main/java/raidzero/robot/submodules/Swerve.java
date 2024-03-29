package raidzero.robot.submodules;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;

import com.ctre.phoenix.sensors.WPI_Pigeon2;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrapezoidProfile;

import raidzero.robot.Constants;
import raidzero.robot.Constants.SwerveConstants;
import raidzero.robot.dashboard.Tab;

public class Swerve extends Submodule {

    private enum ControlState {
        OPEN_LOOP, PATHING
    };

    private static Swerve instance = null;

    public static Swerve getInstance() {
        if (instance == null) {
            instance = new Swerve();
        }
        return instance;
    }

    private Swerve() {
    }

    private SwerveModule topRightModule = new SwerveModule();
    private SwerveModule topLeftModule = new SwerveModule();
    private SwerveModule bottomLeftModule = new SwerveModule();
    private SwerveModule bottomRightModule = new SwerveModule();

    private WPI_Pigeon2 pigeon;

    private SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
        SwerveConstants.MODULE_TOP_LEFT_POSITION, 
        SwerveConstants.MODULE_TOP_RIGHT_POSITION,
        SwerveConstants.MODULE_BOTTOM_LEFT_POSITION, 
        SwerveConstants.MODULE_BOTTOM_RIGHT_POSITION
    );

    private SwerveDriveOdometry odometry;
    private Pose2d currentPose;
    private Pose2d prevPose;

    private HolonomicDriveController pathController;
    private Trajectory currentTrajectory;
    private Rotation2d targetAngle;
    private Timer timer = new Timer();

    private ControlState controlState = ControlState.OPEN_LOOP;

    private NetworkTableEntry xPositionEntry =
        Shuffleboard.getTab(Tab.MAIN).add("X (m)", 0).withWidget(BuiltInWidgets.kTextView)
                .withSize(1, 1).withPosition(5, 3).getEntry();
    private NetworkTableEntry yPositionEntry =
        Shuffleboard.getTab(Tab.MAIN).add("Y (m)", 0).withWidget(BuiltInWidgets.kTextView)
                .withSize(1, 1).withPosition(6, 3).getEntry();

    public void onStart(double timestamp) {
        controlState = ControlState.OPEN_LOOP;
    }

    public void onInit() {
        pigeon = new WPI_Pigeon2(0, Constants.CANBUS_STRING);
        Shuffleboard.getTab(Tab.MAIN).add("Pigey", pigeon).withSize(2, 2).withPosition(4, 4);

        topRightModule.onInit(
            SwerveConstants.MODULE_ID_TOP_RIGHT, 
            SwerveConstants.MODULE_ID_TOP_RIGHT + 1, 
            SwerveConstants.INIT_MODULES_DEGREES[0], 
            1
        );
        topLeftModule.onInit(
            SwerveConstants.MODULE_ID_TOP_LEFT, 
            SwerveConstants.MODULE_ID_TOP_LEFT + 1, 
            SwerveConstants.INIT_MODULES_DEGREES[1], 
            2
        );
        bottomLeftModule.onInit(
            SwerveConstants.MODULE_ID_BOTTOM_LEFT, 
            SwerveConstants.MODULE_ID_BOTTOM_LEFT + 1, 
            SwerveConstants.INIT_MODULES_DEGREES[2], 
            3
        );
        bottomRightModule.onInit(
            SwerveConstants.MODULE_ID_BOTTOM_RIGHT, 
            SwerveConstants.MODULE_ID_BOTTOM_RIGHT + 1, 
            SwerveConstants.INIT_MODULES_DEGREES[3], 
            4
        );

        odometry = new SwerveDriveOdometry(
            kinematics, Rotation2d.fromDegrees(pigeon.getAngle()));

        pathController = new HolonomicDriveController(
            new PIDController(1, 0, 0), 
            new PIDController(1, 0, 0),
            new ProfiledPIDController(1.5, 0, 0,
                new TrapezoidProfile.Constraints(6.28, 6.28)
            )
        );

        zero();
        prevPose = new Pose2d();
    }

    @Override
    public void update(double timestamp) {
        if (controlState == ControlState.PATHING) {
            updatePathing();
        }
        topRightModule.update(timestamp);
        topLeftModule.update(timestamp);
        bottomLeftModule.update(timestamp);
        bottomRightModule.update(timestamp);

        prevPose = currentPose;
        currentPose = updateOdometry();

        xPositionEntry.setDouble(currentPose.getX());
        yPositionEntry.setDouble(currentPose.getY());
    }

    /**
     * Runs components in the submodule that have continuously changing inputs.
     */
    @Override
    public void run() {
        topRightModule.run();
        topLeftModule.run();
        bottomLeftModule.run();
        bottomRightModule.run();
    }

    @Override
    public void stop() {
        controlState = ControlState.OPEN_LOOP;
        topRightModule.stop();
        topLeftModule.stop();
        bottomLeftModule.stop();
        bottomRightModule.stop();
    }

    /**
     * Resets the sensor(s) to zero.
     */
    @Override
    public void zero() {
        zeroHeading();
        setPose(new Pose2d());
        topRightModule.zero();
        topLeftModule.zero();
        bottomLeftModule.zero();
        bottomRightModule.zero();
    }

    /**
     * Zeroes the heading of the swerve.
     */
    public void zeroHeading() {
        pigeon.setYaw(0, Constants.TIMEOUT_MS);
    }

    public void setPose(Pose2d pose) {
        odometry.resetPosition(pose, Rotation2d.fromDegrees(pigeon.getAngle()));
    }

    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    public Pose2d getPrevPose() {
        return prevPose;
    }

    public SwerveDriveKinematics getKinematics() {
        return kinematics;
    }

    private Pose2d updateOdometry() { 
        return odometry.update(
            Rotation2d.fromDegrees(pigeon.getAngle()),
            topLeftModule.getState(),
            topRightModule.getState(),
            bottomLeftModule.getState(),
            bottomRightModule.getState()
        );
    }

    public void drive(double xSpeed, double ySpeed, double angularSpeed, boolean fieldOriented) {
        boolean ignoreAngle = false;
        if (Math.abs(xSpeed) < 0.1 && Math.abs(ySpeed) < 0.1 && Math.abs(angularSpeed) < 0.1) {
            ignoreAngle = true;
        }
        var targetState =
            kinematics.toSwerveModuleStates(
                fieldOriented
                ? ChassisSpeeds.fromFieldRelativeSpeeds(
                    xSpeed, ySpeed, angularSpeed, 
                    Rotation2d.fromDegrees(pigeon.getAngle())
                  )
                : new ChassisSpeeds(xSpeed, ySpeed, angularSpeed)
            );
        SwerveDriveKinematics.desaturateWheelSpeeds(targetState, SwerveConstants.MAX_SPEED_MPS);
        topLeftModule.setTargetState(targetState[0], ignoreAngle, true);
        topRightModule.setTargetState(targetState[1], ignoreAngle, true);
        bottomLeftModule.setTargetState(targetState[2], ignoreAngle, true);
        bottomRightModule.setTargetState(targetState[3], ignoreAngle, true);
    }

    public void followPath(Trajectory trajectory) {
        followPath(trajectory, new Rotation2d());
    }

    public void followPath(Trajectory trajectory, Rotation2d targetAngle) {
        if (controlState == ControlState.PATHING) {
            return;
        }
        controlState = ControlState.PATHING;
        currentTrajectory = trajectory;
        this.targetAngle = targetAngle;

        timer.reset();
        timer.start();
    }

    private void updatePathing() {
        var state = currentTrajectory.sample(timer.get());
        var chassisSpeed = pathController.calculate(currentPose, state, targetAngle);
        var targetState = kinematics.toSwerveModuleStates(chassisSpeed);
        topLeftModule.setTargetState(targetState[0], false, false);
        topRightModule.setTargetState(targetState[1], false, false);
        bottomLeftModule.setTargetState(targetState[2], false, false);
        bottomRightModule.setTargetState(targetState[3], false, false);
    }

    public boolean isFinishedPathing() {
        return timer.hasElapsed(currentTrajectory.getTotalTimeSeconds());
    }

    public void testModule(int quadrant, double motorOutput, double rotorOutput) {
        System.out.println("Testing Q" + quadrant + ": motor=" + motorOutput + " rotor=" + rotorOutput);
        if (quadrant == 1) {
            topRightModule.testMotorAndRotor(motorOutput, rotorOutput);
        } else if (quadrant == 2) {
            topLeftModule.testMotorAndRotor(motorOutput, rotorOutput);
        } else if (quadrant == 3) {
            bottomLeftModule.testMotorAndRotor(motorOutput, rotorOutput);
        } else { 
            bottomRightModule.testMotorAndRotor(motorOutput, rotorOutput);
        }
    }
}
