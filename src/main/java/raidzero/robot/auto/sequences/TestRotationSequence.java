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
                Units.inchesToMeters(43.9), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(0)
            )
        ),
        false, 2.5, 2.5
    );

    private static final Path PATH2 = Path.fromWaypoints(
        Arrays.asList(
            new Pose2d(
                Units.inchesToMeters(43.9), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(0)
            ),
            new Pose2d(
                Units.inchesToMeters(17.8), Units.inchesToMeters(0),
                Rotation2d.fromDegrees(0)
            )
        ),
        true, 1.69, 1.69
    );

    private static final Path PATH3 = Path.fromWaypoints(
        Arrays.asList(  
            new Pose2d(
                Units.inchesToMeters(17.8), Units.inchesToMeters(-170.8),
                Rotation2d.fromDegrees(0)
            ),   
                
            new Pose2d(
                Units.inchesToMeters(18.7), Units.inchesToMeters(-242.8),
                Rotation2d.fromDegrees(0)
            )
        
        ),
        false, 1.69, 0.79
    );

    private static final Path PATH4 = Path.fromWaypoints(
        Arrays.asList(  
            new Pose2d( 
                Units.inchesToMeters(18.7), Units.inchesToMeters(-242.8),
                Rotation2d.fromDegrees(0)
            ),      
                
            new Pose2d(
                Units.inchesToMeters(1.06), Units.inchesToMeters(-266.2),
                Rotation2d.fromDegrees(0)
            )
        
        ),
        false, 1.33, 1.33 
    );

    private static final Swerve swerve = Swerve.getInstance();
    private static final Intake intake = Intake.getInstance();
    private static final Shooter shooter = Shooter.getInstance();
    private static final ThroatX throatx = ThroatX.getInstance();
    private static final ThroatY throaty = ThroatY.getInstance();


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
                intake.setSolenoid(false);
            }),

            new DrivePath(PATH),
            new LambdaAction(() -> {
                intake.intakeBalls(1.0);
                throatx.moveBalls(0.7);
                throaty.moveBalls(1.0);
                shooter.shoot(0.4, false);
            }),
            new LambdaAction(() -> {
                Timer.delay(2);
                shooter.shoot(0.0, false);
            }),

            new DrivePath(PATH2, Rotation2d.fromDegrees(-57.67)),
            new DrivePath(PATH3, Rotation2d.fromDegrees(-57.67)),

            new LambdaAction(() -> {
                intake.intakeBalls(0.3);
                // throatlong.moveBalls(0.2);
                // throatshort.moveBalls(0.2);
                shooter.shoot(1.0, false);
            }),
            new LambdaAction(() -> {
                Timer.delay(2);
                shooter.shoot(0.0, false);
            }),

            new DrivePath(PATH4, Rotation2d.fromDegrees(-72.79)),

            new LambdaAction(() -> {
                intake.intakeBalls(0.3);
                // throatlong.moveBalls(0.3);
                // throatshort.moveBalls(0.2);
                shooter.shoot(1.0, false);
            }),
            new LambdaAction(() -> {
                Timer.delay(2);
                shooter.shoot(0.0, false);
            })
        
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