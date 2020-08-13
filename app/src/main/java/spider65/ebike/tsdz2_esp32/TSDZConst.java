package spider65.ebike.tsdz2_esp32;

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
	byte CMD_GET_STATUS_DATA  = 0x52;  //  R
    byte CMD_GET_DEBUG_DATA  = 0x44;   // D
    byte CMD_NO_DATA  = 0x4E;   // N
    byte CMD_CADENCE_CALIBRATION = 0x07;

    // size in bytes of the Status/Debug BT notifications
    int DEBUG_ADV_SIZE = 16;
    int STATUS_ADV_SIZE = 17;

    // limit values used in the LevelSetupActivity
    int PWM_DUTY_CYCLE_MAX = 254;
    int WALK_ASSIST_DUTY_CYCLE_MAX = 80;
}
