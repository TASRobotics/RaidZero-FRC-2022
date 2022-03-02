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
                Units.inchesToMeters(27.7), Units.inchesToMeters(-55.0),
                Rotation2d.fromDegrees(0)
            )
        ),
        false, 2.69, 2.69
    );

    private static final Path PATH2 = Path.fromWaypoints(
        Arrays.asList(  
            new Pose2d(
                Units.inchesToMeters(25.0), Units.inchesToMeters(-57.0),
                Rotation2d.fromDegrees(0)
            ),         
            new Pose2d(
                Units.inchesToMeters(25.0), Units.inchesToMeters(-167.0),
                Rotation2d.fromDegrees(0)
            )
        ),
        false, 2.69, 2.69
    );

    private static final Path PATH3 = Path.fromWaypoints(
        Arrays.asList(  
            new Pose2d(
                Units.inchesToMeters(25.0), Units.inchesToMeters(-167.0),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(64.0), Units.inchesToMeters(-209.5),
                Rotation2d.fromDegrees(0)
            )
        ),
        false, 2.33, 2.33 
    );

    private static final Path PATH4 = Path.fromWaypoints(
        Arrays.asList(  
            new Pose2d(
                Units.inchesToMeters(64.0), Units.inchesToMeters(-209.5),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(25.0), Units.inchesToMeters(-167.0),
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
    private static final ThroatX throatX = ThroatX.getInstance();
    private static final ThroatY throatY = ThroatY.getInstance();


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
                intake.setSolenoid(false);
            }),

            new DrivePath(PATH),

            new LambdaAction(() -> {
                intake.intakeBalls(0.3);
                throatX.moveBalls(0.2);
                throatY.moveBalls(0.2);
                shooter.shoot(1.0, false);
            }),
            // new LambdaAction(() -> {
            //     Timer.delay(2);
            //     shooter.shoot(0.0, false);
            // }),

            new DrivePath(PATH2, Rotation2d.fromDegrees(57.67)),

            new LambdaAction(() -> {
                Timer.delay(2);
                shooter.shoot(0.0, false);
            })

            // new DrivePath(PATH3),

            // new LambdaAction(() -> {
            //     intake.intakeBalls(0.3);
            //     throatX.moveBalls(0.3);
            //     throatY.moveBalls(0.2);
            //     shooter.shoot(1.0, false);
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