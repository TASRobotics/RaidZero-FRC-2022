package raidzero.robot.auto.sequences;

import java.util.Arrays;


import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

import raidzero.robot.auto.actions.*;
import raidzero.robot.pathing.Path;
import raidzero.robot.submodules.*;

public class BarrelRacingPathSequence extends AutoSequence {

    private static final Path PATH = Path.fromWaypoints(
        Arrays.asList(
            new Pose2d(
                Units.inchesToMeters(0), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(120), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(150), Units.inchesToMeters(-40),
                Rotation2d.fromDegrees(-90)
            ),
            new Pose2d(
                Units.inchesToMeters(115), Units.inchesToMeters(-60),
                Rotation2d.fromDegrees(-180)
            ),
            new Pose2d(
                Units.inchesToMeters(70), Units.inchesToMeters(-30),
                Rotation2d.fromDegrees(-270)
            ),
            new Pose2d(
                Units.inchesToMeters(180), Units.inchesToMeters(10),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(240), Units.inchesToMeters(30),
                Rotation2d.fromDegrees(90)
            ),
            new Pose2d(
                Units.inchesToMeters(200), Units.inchesToMeters(80),
                Rotation2d.fromDegrees(180)
            ),
            new Pose2d(
                Units.inchesToMeters(170), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(270)
            ),
            new Pose2d(
                Units.inchesToMeters(240), Units.inchesToMeters(-50),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(320), Units.inchesToMeters(-30),
                Rotation2d.fromDegrees(90)
            ),
            new Pose2d(
                Units.inchesToMeters(300), Units.inchesToMeters(30),
                Rotation2d.fromDegrees(180)
            ),
            new Pose2d(
                Units.inchesToMeters(0), Units.inchesToMeters(20),
                Rotation2d.fromDegrees(180)
            )
        ),
        false, 4.5, 4.5
    );
    private static final Swerve swerve = Swerve.getInstance();

    public BarrelRacingPathSequence() {
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
            new DrivePath(PATH)
        )));
        System.out.println("Added actions.");
    }

    @Override
    public void onEnded() {
        System.out.println("BarrelRacingPathSequence ended!");
    }

    @Override
    public String getName() {
        return "Barrel Racing Pathing Sequence";
    }
}