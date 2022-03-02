package raidzero.robot.submodules;

import raidzero.robot.auto.actions.TurnToGoal;
import raidzero.robot.submodules.Shooter;

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
    public Shooter shooter;

    // @Override
    // public void onInit(){

    // }

    @Override
    public void onStart(double timestamp) {
        autoaim = new TurnToGoal();
        shooter = Shooter.getInstance();
    }

    @Override
    public void update(double timestamp) {
        autoaim.update();
        shooter.shoot(autoaim.getShooterSpeed(), false);
    }

    @Override
    public void stop() {
    }

    public double getspeed(){
        return autoaim.getShooterSpeed();
    }

    
}