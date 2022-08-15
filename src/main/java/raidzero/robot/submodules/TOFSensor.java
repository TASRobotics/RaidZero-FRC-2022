package raidzero.robot.submodules;
import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;

import raidzero.robot.Constants.TOFSensorConstants;


public class TOFSensor extends Submodule {
    
    private static TOFSensor instance = null;

    public static TOFSensor getInstance() {
        if (instance == null) {
            instance = new TOFSensor();
        }
        return instance;
    }

    public TOFSensor() {}

    private TimeOfFlight sensor; 

    @Override
    public void onInit() {
        sensor = new TimeOfFlight(TOFSensorConstants.SENSOR_ID);
        sensor.setRangingMode(RangingMode.Medium, 24);
        // sensor.setRangeOfInterest(topLeftX, topLeftY, bottomRightX, bottomRightY);
    }

    @Override
    public void stop() {}

    public boolean isDetecting()
    {
        return sensor.getRange() < 100.0;
    }

}
