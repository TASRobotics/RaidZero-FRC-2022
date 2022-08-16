package raidzero.robot.auto.actions;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import raidzero.robot.submodules.AdjustableHood;
import raidzero.robot.submodules.Swerve;
import raidzero.robot.submodules.Turret;

public class AutoAim implements Action {
    private static final Swerve swerve = Swerve.getInstance();
    private static final Turret turret = Turret.getInstance();
    private static final AdjustableHood hood = AdjustableHood.getInstance();

    @Override
    public void start() {
        
    }

    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void update() {
        // Constants
        double ballSpeed = 0.0;
        double hoodAngle = hood.getPosition();

        // Robot velocity
        Pose2d currentPose = swerve.getPose();
        Pose2d prevPose = swerve.getPrevPose();

        // mps
        Vector2D driveTrainVelocity = new Vector2D((currentPose.getX() - prevPose.getX()) * 50, 
                                                   (currentPose.getY() - prevPose.getX()) * 50);

        // TOF
        double tof = 0.0; // seconds

        // Estimate
        Translation2d estimatedGoalPoseAtTOF = new Translation2d(driveTrainVelocity.getX() * tof * -1, 
                                                                 driveTrainVelocity.getY() * tof * -1);

        // Calculate angle & dist
        double distToGoal = Math.hypot(currentPose.getX() - estimatedGoalPoseAtTOF.getX(), 
                                       currentPose.getY() - estimatedGoalPoseAtTOF.getY());
        Rotation2d angleToGoal = new Rotation2d(Math.atan2(currentPose.getX() - estimatedGoalPoseAtTOF.getX(), 
                                                           currentPose.getY() - estimatedGoalPoseAtTOF.getY()));
    }

    @Override
    public void done() {
        // TODO Auto-generated method stub
        
    }
}
