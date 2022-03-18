package raidzero.robot.auto.sequences;

import java.util.Arrays;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;

import raidzero.robot.auto.actions.*;
import raidzero.robot.pathing.Path;
import raidzero.robot.submodules.*;

public class TestRotationSequence extends AutoSequence {

    private static final Path PATH = Path.fromWaypoints(
        Arrays.asList(
            new Pose2d(
                Units.inchesToMeters(0), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(55.7), Units.inchesToMeters(-31.3),
                Rotation2d.fromDegrees(0)
            )
        ),
        false, 2.69, 2.69
    );

    private static final Path PATH2 = Path.fromWaypoints(
        Arrays.asList(  
            new Pose2d(
                Units.inchesToMeters(55.7), Units.inchesToMeters(-31.3),
                Rotation2d.fromDegrees(0)
            ),         
            new Pose2d(
                Units.inchesToMeters(36.0), Units.inchesToMeters(-125.0),
                Rotation2d.fromDegrees(0)
            )
        ),
        false, 2.69, 2.69
    );

    private static final Path PATH3 = Path.fromWaypoints(
        Arrays.asList(  
            new Pose2d(
                Units.inchesToMeters(36.0), Units.inchesToMeters(-125.0),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(107.8), Units.inchesToMeters(-176.2),
                Rotation2d.fromDegrees(0)
            )
        ),
        false, 2.33, 2.33 
    );

    private static final Path PATH4 = Path.fromWaypoints(
        Arrays.asList(  
            new Pose2d(
                Units.inchesToMeters(107.8), Units.inchesToMeters(-176.2),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(36.0), Units.inchesToMeters(-125.0),
                Rotation2d.fromDegrees(0)
            )
        ),
        true, 2.33, 2.33 
    );

    // Last two points for grabbing excess preload (if any)
    private static final Path PATH5 = Path.fromWaypoints(
        Arrays.asList(  
            new Pose2d(
                Units.inchesToMeters(25.0), Units.inchesToMeters(-167.0),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(25.0), Units.inchesToMeters(-247.5),
                Rotation2d.fromDegrees(0)
            )
        ),
        true, 2.33, 2.33 
    );

    private static final Path PATH6 = Path.fromWaypoints(
        Arrays.asList(  
            new Pose2d(
                Units.inchesToMeters(25.0), Units.inchesToMeters(-247.5),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(25.0), Units.inchesToMeters(-167.0),
                Rotation2d.fromDegrees(0)
            )
        ),
        true, 2.33, 2.33 
    );

    private static final Swerve swerve = Swerve.getInstance();
    private static final Intake intake = Intake.getInstance();
    private static final Shooter shooter = Shooter.getInstance();
    private static final Superstructure autoaim = Superstructure.getInstance();
    private static final ThroatX throatX = ThroatX.getInstance();
    private static final ThroatY throatY = ThroatY.getInstance();
    private static final Turret turret = Turret.getInstance();


    public TestRotationSequence() {
    }

    @Override
    public void sequence() {
        addAction(new SeriesAction(Arrays.asList(
            new LambdaAction(() -> {
                swerve.zero();
                swerve.setPose(
                    new Pose2d(
                        Units.inchesToMeters(0.0),
                        Units.inchesToMeters(0.0), 
                        new Rotation2d(24)
                    )
                );
                autoaim.setAiming(true);
                intake.setSolenoid(false);
                intake.intakeBalls(1.0);
                throatX.moveBalls(0.295);                
                // turret.moveToPosition(-18);
            }),

            new DrivePath(PATH, Rotation2d.fromDegrees(-27)),

            new LambdaAction(() -> {
                intake.intakeBalls(1.0);
                throatX.moveBalls(0.295); 
            }),

            new DrivePath(PATH2, Rotation2d.fromDegrees(-56.5)),

            new LambdaAction(() -> {
                intake.intakeBalls(1.0);
                throatX.moveBalls(0.295);
                Timer.delay(0.5);
                throatY.moveBalls(0.7);
            }),
            new LambdaAction(() -> {
                Timer.delay(2);
                throatY.moveBalls(0);
            }),

            new DrivePath(PATH3, Rotation2d.fromDegrees((-56.5))),

            new LambdaAction(() -> {
                intake.intakeBalls(1.0);
                throatX.moveBalls(0.295);
                Timer.delay(1);
            }),

            new DrivePath(PATH4, Rotation2d.fromDegrees((-90))),

            new LambdaAction(() -> {
                intake.intakeBalls(1.0);
                throatX.moveBalls(0.295);
                Timer.delay(0.5);
                throatY.moveBalls(0.7);
            }),
            new LambdaAction(() -> {
                Timer.delay(2);
                autoaim.setAiming(false);
                throatY.moveBalls(0);
                throatX.moveBalls(0);
                intake.intakeBalls(0);
            })

            // new LambdaAction(() -> {
            //     shooter.shoot(0.0, false);
            // }),
            // new LambdaAction(() -> {
            //     Timer.delay(2);
            //     shooter.shoot(0.0, false);
            // })
        
        )));
        System.out.println("Added actions.");
    }

    @Override
    public void onEnded() {
        System.out.println("TestRotationSequence ended!");
    }

    @Override
    public String getName() {
        return "Test Rotation Sequence";
    }
}