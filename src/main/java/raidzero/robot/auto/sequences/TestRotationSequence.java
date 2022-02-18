package raidzero.robot.auto.sequences;

import java.util.Arrays;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

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
                Units.inchesToMeters(42.37), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(0)
            )
        ),
        false, 2.5, 2.5
    );

    private static final Path PATH2 = Path.fromWaypoints(
        Arrays.asList(
            new Pose2d(
                Units.inchesToMeters(42.37), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(15.37), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(0)
            )
        ),
        true, 2.5, 2.5
    );

    private static final Path PATH3 = Path.fromWaypoints(
        Arrays.asList(  
            new Pose2d(
                Units.inchesToMeters(15.37), Units.inchesToMeters(-170.8),
                Rotation2d.fromDegrees(0)
            ),   
                
            new Pose2d(
                Units.inchesToMeters(15.37), Units.inchesToMeters(-242.8),
                Rotation2d.fromDegrees(0)
            )
        
        ),
        false, 2.0, 2.0
    );

    private static final Path PATH4 = Path.fromWaypoints(
        Arrays.asList(  
            new Pose2d(
                Units.inchesToMeters(15.37), Units.inchesToMeters(-242.8),
                Rotation2d.fromDegrees(0)
            ),   
                
            new Pose2d(
                Units.inchesToMeters(0.07), Units.inchesToMeters(-277.9),
                Rotation2d.fromDegrees(0)
            )
        
        ),
        false, 2.0, 2.0
    );

    private static final Swerve swerve = Swerve.getInstance();
    private static final Intake intake = Intake.getInstance();
    private static final Shooter shooter = Shooter.getInstance();
    private static final ThroatLong throatlong = ThroatLong.getInstance();
    private static final ThroatShort throatshort = ThroatShort.getInstance();

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
                        new Rotation2d()
                    )
                );
            }),
            new DrivePath(PATH),
            new DrivePath(PATH2, Rotation2d.fromDegrees(-57.67)),
            new DrivePath(PATH3, Rotation2d.fromDegrees(-57.67)),
            new DrivePath(PATH4, Rotation2d.fromDegrees(-90.00))

            // new LambdaAction(() -> {
            //     intake.intakeBalls(0.3);
            //    // throatlong.moveBalls(0.3);
            //     throatshort.moveBalls(0.5);
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