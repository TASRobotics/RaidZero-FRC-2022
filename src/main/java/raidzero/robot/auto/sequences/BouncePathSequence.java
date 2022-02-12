package raidzero.robot.auto.sequences;

import java.util.Arrays;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

import raidzero.robot.auto.actions.*;
import raidzero.robot.pathing.Path;
import raidzero.robot.submodules.*;

public class BouncePathSequence extends AutoSequence {

    private static final Path PATH = Path.fromWaypoints(
        Arrays.asList(
            new Pose2d(
                Units.inchesToMeters(0), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(20), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(50), Units.inchesToMeters(60),
                Rotation2d.fromDegrees(90)
            )
        ),
        false, 3.5, 3.5
    );
    private static final Path PATH2 = Path.fromWaypoints(
        Arrays.asList(
            new Pose2d(
                Units.inchesToMeters(50), Units.inchesToMeters(60),
                Rotation2d.fromDegrees(-90)
            ),
            new Pose2d(
                Units.inchesToMeters(50), Units.inchesToMeters(30),
                Rotation2d.fromDegrees(-90)
            ),
            new Pose2d(
                Units.inchesToMeters(75), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(-60)
            ),
            new Pose2d(
                Units.inchesToMeters(110), Units.inchesToMeters(-60),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(145), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(90)
            ),
            new Pose2d(
                Units.inchesToMeters(145), Units.inchesToMeters(80),
                Rotation2d.fromDegrees(90)
            )
        ),
        false, 3.5, 3.5
    );
    private static final Path PATH3 = Path.fromWaypoints(
        Arrays.asList(
            new Pose2d(
                Units.inchesToMeters(145), Units.inchesToMeters(60),
                Rotation2d.fromDegrees(-90)
            ),
            new Pose2d(
                Units.inchesToMeters(145), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(-90)
            ),
            new Pose2d(
                Units.inchesToMeters(170), Units.inchesToMeters(-50),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(220), Units.inchesToMeters(-40),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(243), Units.inchesToMeters(40),
                Rotation2d.fromDegrees(90)
            ),
            new Pose2d(
                Units.inchesToMeters(243), Units.inchesToMeters(90),
                Rotation2d.fromDegrees(90)
            )
        ),
        false, 3.5, 3.5
    );
    private static final Path PATH4 = Path.fromWaypoints(
        Arrays.asList(
            new Pose2d(
                Units.inchesToMeters(243), Units.inchesToMeters(90),
                Rotation2d.fromDegrees(-90)
            ),
            new Pose2d(
                Units.inchesToMeters(243), Units.inchesToMeters(50),
                Rotation2d.fromDegrees(-90)
            ),
            new Pose2d(
                Units.inchesToMeters(300), Units.inchesToMeters(30),
                Rotation2d.fromDegrees(0)
            )
        ),
        false, 3.5, 3.5
    );
    private static final Swerve swerve = Swerve.getInstance();

    public BouncePathSequence() {
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
                        new Rotation2d()
                    )
                );
            }),
            new DrivePath(PATH),
            new DrivePath(PATH2),
            new DrivePath(PATH3),
            new DrivePath(PATH4)
        )));
        System.out.println("Added actions.");
    }

    @Override
    public void onEnded() {
        System.out.println("BouncePathSequence ended!");
    }

    @Override
    public String getName() {
        return "Bounce Pathing Sequence";
    }
}