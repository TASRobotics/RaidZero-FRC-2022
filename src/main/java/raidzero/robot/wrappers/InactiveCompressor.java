package raidzero.robot.wrappers;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class InactiveCompressor extends Compressor {

    private static InactiveCompressor instance = null;
    
    private boolean state = true;

    public static InactiveCompressor getInstance() {
        if (instance == null) {
            instance = new InactiveCompressor();
        }
        return instance;
    }

    public InactiveCompressor() {
        super(PneumaticsModuleType.CTREPCM);
    }

    public boolean getState() {
        return state;
    }

    public void changeState() {
        state = !state;
        if (state) {
            super.enableDigital();
        } else {
            super.disable();
        }
    }
}