package raidzero.robot.wrappers;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.Pigeon2;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

public class SendablePigeon extends PigeonIMU implements Sendable {

    protected double[] ypr = new double[3];

    public SendablePigeon(int deviceNumber) {
        super(deviceNumber);
    }
    
    public double getHeading() {
        getYawPitchRoll(ypr);
        return ypr[0];
    }

    public double getNegatedHeading() {
        return -getHeading();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Gyro");
        builder.addDoubleProperty("Value", this::getNegatedHeading, null);
    }
}
