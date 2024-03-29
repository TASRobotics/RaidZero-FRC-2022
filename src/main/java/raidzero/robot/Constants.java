package raidzero.robot;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax.IdleMode;

import org.apache.commons.math3.util.FastMath;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import raidzero.robot.utils.InterpolatingDouble;
import raidzero.robot.utils.InterpolatingTreeMap;

public class Constants {
    /**
     * Swerve Constants
     */
    public static final class SwerveConstants {
        public static final int MODULE_ID_TOP_RIGHT = 1;
        public static final int MODULE_ID_TOP_LEFT = 3;
        public static final int MODULE_ID_BOTTOM_LEFT = 5;
        public static final int MODULE_ID_BOTTOM_RIGHT = 7;
        public static final double[] INIT_MODULES_DEGREES = new double[] {
            (349.893 + 90) % 360.0, 
            (149.854 + 90) % 360.0, 
            (6.416 + 90) % 360.0, 
            (182.637 + 90) % 360.0
        };

        // Ugly Bot
        // (195.820 + 90) % 360.0, 
        // (278.789 + 90) % 360.0, 
        // (180.000 + 90) % 360.0, 
        // (107.139 + 90) % 360.0

        //new double[] {(57.832 + 0) % 360.0, (205.576 + 0) % 360.0, (212.520 + 0) % 360.0, (308.232 + 0) % 360.0};

        // Robot dimensions
        public static final double ROBOT_WIDTH_INCHES = 25.25; // 23.0 inches on ugly bot
        public static final double ROBOT_HALF_WIDTH_METERS = Units.inchesToMeters(ROBOT_WIDTH_INCHES) / 2.0;
        public static final double ROBOT_RADIUS_INCHES = FastMath.hypot(ROBOT_WIDTH_INCHES, ROBOT_WIDTH_INCHES) / 2.0;
        public static final double WHEEL_DIAMETER_INCHES = 4.0;
        public static final double WHEEL_DIAMETER_METERS = Units.inchesToMeters(WHEEL_DIAMETER_INCHES);

        public static final Translation2d MODULE_TOP_LEFT_POSITION = new Translation2d(ROBOT_HALF_WIDTH_METERS, ROBOT_HALF_WIDTH_METERS);
        public static final Translation2d MODULE_TOP_RIGHT_POSITION = new Translation2d(ROBOT_HALF_WIDTH_METERS, -ROBOT_HALF_WIDTH_METERS);
        public static final Translation2d MODULE_BOTTOM_LEFT_POSITION = new Translation2d(-ROBOT_HALF_WIDTH_METERS, ROBOT_HALF_WIDTH_METERS);
        public static final Translation2d MODULE_BOTTOM_RIGHT_POSITION = new Translation2d(-ROBOT_HALF_WIDTH_METERS, -ROBOT_HALF_WIDTH_METERS);

        public static final double FALCON_TICKS = 2048.0;

        // This changes depending on the swerve module used - swerveX has several.  2022 we're using 5.50:1
        // was 7.2 on ugly bot
        public static final double MOTOR_RATIO = 5.5;
        public static final double MOTOR_TICKS_TO_METERS = Math.PI * WHEEL_DIAMETER_METERS / (FALCON_TICKS * MOTOR_RATIO);

        public static final double CANCODER_TO_DEGREES = 360.0 / 4096.0;

        public static final double MAX_SPEED_MPS = 5.0;  //was 4.0
        public static final double MAX_ANGULAR_SPEED_RPS = 2 * Math.PI;

        public static final boolean MOTOR_INVERSION = false;
        public static final boolean ROTOR_INVERSION = true;
        public static final boolean ROTOR_INVERT_SENSOR_PHASE = true;

        // PID constants
        public static final int PID_PRIMARY_SLOT = 0;
        public static final int PID_AUX_SLOT = 1;

        public static final double MOTOR_MAX_VELOCITY_TICKS_PER_100MS = 21397.0;
        public static final double MOTOR_MAX_VELOCITY_EFFECTIVE_MPS = 3.0;
        public static final double MOTOR_KF = 1.0 * 1023 / MOTOR_MAX_VELOCITY_TICKS_PER_100MS;
        public static final double MOTOR_KP = 0.08;
        public static final double MOTOR_KD = 0.2;

        public static final double ROTOR_MAX_VELOCITY_TICKS_PER_100MS = 3600.0;
        public static final double ROTOR_KF = 1.0 * 1023 / ROTOR_MAX_VELOCITY_TICKS_PER_100MS;
        public static final double ROTOR_KP = 0.8; //1.35
        public static final double ROTOR_KD = 3.0; //0.2
        public static final double ROTOR_TARG_VELO = 1.0 * ROTOR_MAX_VELOCITY_TICKS_PER_100MS;
        public static final double ROTOR_TARG_ACCEL = 10 * ROTOR_TARG_VELO;
    }

    /**
     * Path Constants
     */
    public class PathConstants {
        public static final int BASE_TRAJ_PERIOD_MS = 0;
        public static final int MIN_POINTS_IN_TALON = 10;
        public static final int TRANSMIT_PERIOD_MS = 20;
    }

    /**
     * Intake Constants
     */
    public static final class IntakeConstants {
        public static final int LEFT_MOTOR_ID = 16;
        public static final int RIGHT_MOTOR_ID = 15;

        public static final IdleMode NEUTRAL_MODE = IdleMode.kCoast;

        public static final boolean LEFT_INVERSION = true;
        public static final boolean RIGHT_INVERSION = false;

        public static final double CONTROL_SCALING_FACTOR = 2;
    }

    /**
     * Climb Constants
     */
    public static final class ClimbConstants {
        public static final int MOTOR_ID = 27;

        public static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake;
        public static final InvertType INVERSION = InvertType.None;
    }


    public static final class ThroatXConstants {
        public static final int MOTOR_ID = 25; 

        public static final IdleMode NEUTRAL_MODE = IdleMode.kBrake;
        public static final boolean MOTOR_INVERSION = true;

        public static final double KF = 0.010;
        public static final double KP = 0.00022;
        public static final double KI = 0;
        public static final double KD = 0.003;
    
        public static final double IZONE = 0;
        public static final double MINOUT = -1;
        public static final double MAXOUT = 1;

        public static final double MAXSPEED = 5000 * 2048;
    }

    public static final class ThroatYConstants {
        public static final int MOTOR_ID = 26;

        public static final NeutralMode NEUTRAL_MODE = NeutralMode.Coast;
        public static final boolean MOTOR_INVERSION = false;

        public static final double KF = 0.010;
        public static final double KP = 0.00022;
        public static final double KI = 0;
        public static final double KD = 0.003;
    
        public static final double IZONE = 0;
        public static final double MINOUT = -1;
        public static final double MAXOUT = 1;

        public static final double MAXSPEED = 5000 * 2048;
    }

    public static final class TurretConstants {
        public static final int MOTOR_ID = 40;

        public static final IdleMode NEUTRAL_MODE = IdleMode.kBrake;
        public static final boolean INVERSION = true;

        public static final double TICKS_PER_DEGREE = 10732 / 90;

        public static final double MAX_INPUT_PERCENTAGE = 0.2;

        public static final double KF = 1/11000;
        public static final double KP = 0.0;
        public static final double KI = 0.0;
        public static final double KD = 0.0;
        public static final int IZONE = 0;

        public static final double MINOUT = -1;
        public static final double MAXOUT = 1;

        public static final int TOLERANCE = 1000;
        public static final double MANUAL_COEF = 0.3;
        public static final double AT_SETPOINT_DURATION = 0.05;

        //SmartMotion Coefficients
        public static final double MAXVEL = 2000;
        public static final double MAXACC = 4000;
    }

    public static final class HoodConstants {
        public static final int MOTOR_ID = 30;

        public static final IdleMode IDLE_MODE = IdleMode.kBrake;
        public static final boolean INVERSION = false;

        public static final double GEAR_RATIO = 45.0;

        public static final int FULLY_EXTENDED_TICKS = 82;

        public static final double[] LOGISTFIT = new double[]{-2.317,-1.246,78.78};
        // The names refer to the angle of ball release
        //For limelight, the following are areas:
        //Green zone: 4.04
        //Yellow zone: 2.6
        //Blue zone: 1.55
        //Red zone: 0.91
        public static enum HoodAngle {
            // +-500 for extra tolerance, limit switches should do its thing
            RETRACTED(70.6), HIGH(46.02), MEDIUM(22.55), LOW(11.02);
            
            public final double ticks;

            private HoodAngle(double ticks) {
                this.ticks = ticks;
            }
        }

        public static final double K_F = 0;
        public static final double K_P = 0.04;
        public static final double K_I = 0;
        public static final double K_D = 0.01;
        public static final int K_INTEGRAL_ZONE = 0;

        public static final int TOLERANCE = 400;
        public static final double AT_SETPOINT_DURATION = 0.2;

        // distance to hood angle regression
        public static final double ATAN_COEFFICIENT = -600670000;// 6.0067*10^8
        public static final double DISTANCE_COEFFICIENT = -624343.7;
        public static final double ANGLE_CONSTANT = -943521338;
    }

    public static final class ShooterConstants {
        public static final int MOTOR_LEFT_ID = 21;
        public static final int MOTOR_RIGHT_ID = 20;

        public static final NeutralMode NEUTRAL_MODE = NeutralMode.Coast;
        public static final InvertType INVERSION = InvertType.None;

        public static final double MAX_SPEED = 7900; // in ticks per 100ms
        public static final double FAKE_MAX_SPEED = 17000; // in ticks per 100ms
        public static final double ERROR_TOLERANCE = 250;
        public static final double UP_TO_SPEED_DURATION = 0.5; // in seconds

        public static final double K_F = 1023.0 * 0.4 / MAX_SPEED;
        public static final double K_P = 0.1;
        public static final double K_I = 0; // Shouldn't be touched
        public static final double K_D = 10; // Shouldn't be touched
        public static final int K_INTEGRAL_ZONE = 0; // Shouldn't be touched
    }

    /**
     * Limelight Constants
     */
    public static final class LimelightConstants {
        public static final String NAME = "limelight";

        public static final double MOUNTING_ANGLE = 31.4; // in degrees
        public static final double MOUNTING_HEIGHT = 0.56; // in meters

        // TODO: Improve the constants
        public static final double AIM_KP = 0.0088;
        public static final double AIM_KI = 0;
        public static final double AIM_KD = 0.0;
        public static final double MIN_I = 0;
        public static final double MAX_I = 0;
        public static final double ANGLE_ADJUST_THRESHOLD = 2.0;

        public static final double AIM_ON_TARGET_DURATION = 0.2;

        public static InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> DIST_TO_TICK_ESTIMATOR = new InterpolatingTreeMap<>();
        static {
            DIST_TO_TICK_ESTIMATOR.put(new InterpolatingDouble(0.0), new InterpolatingDouble(0.0));
            DIST_TO_TICK_ESTIMATOR.put(new InterpolatingDouble(1.0), new InterpolatingDouble(1.0));
        }
    }

    public static final class ExtensionConstants {
        public static final int MOTOR_ID = 40;

        public static final IdleMode NEUTRAL_MODE = IdleMode.kBrake;
        public static final boolean INVERSION = false;

        public static final boolean FORWARD_LIMIT_ENABLE = true;
        public static final int MAX_EXTENSION = 387914;
        public static final double TICKS_PER_DEGREE = 10732 / 90;

        public static final double MAX_INPUT_PERCENTAGE = 1;

        public static final double KF = 0.0;
        public static final double KP = 0.07;
        public static final double KI = 0.0;
        public static final double KD = 0.001;
        public static final int IZONE = 0;

        public static final double MINOUT = -1;
        public static final double MAXOUT = 1;
        public static final double MAXRPM = 5000;

        public static final int TOLERANCE = 1000;
        public static final double MANUAL_COEF = 0.3;
        public static final double AT_SETPOINT_DURATION = 0.05;
    }

    public static final class TOFSensorConstants {
        public static final int SENSOR_ID = 0;   
    }

    /**
     * Universal constants
     */
    public static final double JOYSTICK_DEADBAND = 0.07;

    public static final int TIMEOUT_MS = 20;

    public static final double SQRTTWO = Math.sqrt(2);

    public static final String CANBUS_STRING = "seCANdary";

    public static final PneumaticsModuleType PNEUMATICS_MODULE_TYPE = PneumaticsModuleType.REVPH;
}
