package spider65.ebike.tsdz2_sw102;

public interface TSDZConst {

    int NO_ERROR                                  = 0;
    int ERROR_MOTOR_BLOCKED                       = 1;
    int ERROR_TORQUE_SENSOR                       = 2;
    /*
    int ERROR_BRAKE_APPLIED_DURING_POWER_ON       = 3;  // currently not used
    int ERROR_THROTTLE_APPLIED_DURING_POWER_ON    = 4;  // currently not used
    int ERROR_NO_SPEED_SENSOR_DETECTED            = 5;  // currently not used
    */
    int ERROR_LOW_CONTROLLER_VOLTAGE              = 6;  // controller works with no less than 15 V so give error code if voltage is too low
    int ERROR_OVERVOLTAGE                         = 8;
    int ERROR_TEMPERATURE_LIMIT                   = 9;
    int ERROR_TEMPERATURE_MAX                     = 10;


    // BT Command request/notification codes
    byte CMD_GET_CONFIG_DATA = 0x43;  // C
	byte CMD_SEND_CONFIG_DATA  = 0x53;  // S
    byte CMD_RESET_CONFIG_DATA  = 0x63;  // c
	byte CMD_STATUS_DATA  = 0x52;  //  R
    byte CMD_DEBUG_DATA  = 0x44;   // D
    byte CMD_NO_DATA  = 0x65;     // e NOP
    byte CMD_TRIP_DATA  = 0x54;   // T
    byte CMD_RESET_TRIP_DATA  = 0x74;  //t
    byte CMD_MOTOR_DATA = 0x4D; // M
    byte TEST_STOP = 0x01; // second byte  command POWER MODE MODE
    byte TEST_START = 0x05; // second byte command CALIB MODE

    // size in bytes of the Status/Debug BT notifications
    int DEBUG_ADV_SIZE = 15;
    int STATUS_ADV_SIZE = 20;
    int TRIP_ADV_SIZE = 20;

    // Default Hall Counter Offset values
    int[] DEFAULT_HALL_OFFSET = {44,23,44,23,44,23};
    double DEFAULT_AVG_OFFSET = (double)Math.round(((44*3) + (23*3))/6D * 2) / 2.0; // average rounded to 0.5

    // Default motor Phase
    int DEFAULT_ROTOR_OFFSET = 4; // Rotor angle adjust regarding the Hall angle reference
    int DEFAULT_PHASE_ANGLE = 64; // Phase has 90 deg difference from rotor position

    // limit values used in the LevelSetupActivity
    int PWM_DUTY_CYCLE_MAX = 254;
    int WALK_ASSIST_DUTY_CYCLE_MAX = 80;
}
