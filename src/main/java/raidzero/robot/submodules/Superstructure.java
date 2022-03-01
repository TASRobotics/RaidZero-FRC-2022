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

    private TurnToGoal autoaim;
    public double shooterVelocity;

    @Override
    public void onStart(double timestamp) {
        autoaim = new TurnToGoal();
    }

    @Override
    public void update(double timestamp) {
        autoaim.update();
    }

    @Override
    public void stop() {
    }

    public double getspeed(){
        return autoaim.getShooterSpeed();
    }
    
}