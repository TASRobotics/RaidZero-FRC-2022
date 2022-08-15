package raidzero.robot.submodules;

import raidzero.robot.auto.actions.TurnToGoal;

public class Superstructure extends Submodule {

    private static Superstructure instance = null;

    public static Superstructure getInstance() {
        if (instance == null) {
            instance = new Superstructure();
        }
        return instance;
    }

    private Superstructure() {
        
    }

    private boolean isAiming = false;

    private TurnToGoal autoaim;
    public double shooterVelocity;
    private Shooter shooter;

    @Override
    public void onStart(double timestamp) {
        autoaim = new TurnToGoal();
        shooter = Shooter.getInstance(); 
    }

    @Override
    public void update(double timestamp) {
        if(isAiming) {
            autoaim.update();
            shooter.shoot(autoaim.getShooterSpeed(), false);
        }
    }

    @Override
    public void stop() {
        setAiming(false);
    }

    public boolean isAiming() {
        return isAiming;
    }

    public void setAiming(boolean status) {
        if (status == isAiming) {
            return;
        }
        isAiming = status;
        if (status) {
            // // Don't aim if the robot is aiming and shooting already
            // if (isAiming) {
            //     isAiming = false;
            //     return;
            // }
            autoaim.start();
        } else {
            autoaim.done();
            
        }
    }
}