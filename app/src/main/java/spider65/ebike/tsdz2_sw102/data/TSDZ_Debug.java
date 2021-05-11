package spider65.ebike.tsdz2_sw102.data;

import android.util.Log;

import static spider65.ebike.tsdz2_sw102.TSDZConst.DEBUG_ADV_SIZE;
// Debug Characteristic Notification
// According to the BLE specification the notification length can be max ATT_MTU - 3.
// The 3 bytes subtracted is the 3-byte header(OP-code (operation, 1 byte) and the attribute handle (2 bytes))
// In BLE 4.1 the ATT_MTU is 23 bytes, but in BLE 4.2 the ATT_MTU can be negotiated up to 247 bytes
// -> Max payload for BT 4.1 is 20 bytes
public class TSDZ_Debug {

    private static final String TAG = "TSDZ_Debug";

    public short dutyCycle; // D
    public int motorERPS; // D
    public short  focAngle; // D
    public int torqueSensorValue; // D ADC torque sensor
    public short adcThrottle; // value from ADC Throttle/Temperature
    public short throttle; // Throttled mapped to 0-255
    public float pTorque; // Torque in Nm
    public float battery_energy_h_km_x10; // wh/km
    public float pedalWeight;


    /*
    #pragma pack(1)
    typedef struct _tsdz_debug
    {
      volatile uint8_t ui8_adc_throttle;
      volatile uint8_t ui8_throttle;
      volatile uint16_t ui16_adc_pedal_torque_sensor;
      volatile uint8_t ui8_duty_cycle;
      volatile uint16_t ui16_motor_speed_erps;
      volatile uint8_t ui8_foc_angle;
      volatile uint16_t ui16_pedal_torque_x100;
      volatile uint16_t ui16_cadence_sensor_pulse_high_percentage_x10;
      volatile int16_t i16_pcb_temperaturex10;
    } struct_tsdz_debug;
     */

    public boolean setData(byte[] data) {
        if (data.length != DEBUG_ADV_SIZE) {
            Log.e(TAG, "Wrong Debug BT message size!");
            return false;
        }
        adcThrottle = (short)(data[1] & 255);
        throttle = (short)(data[2] & 255);
        torqueSensorValue = ((data[4] & 255) << 8) + ((data[3] & 255));
        dutyCycle = (short)(data[5] & 255);
        motorERPS = ((data[7] & 255) << 8) + ((data[6] & 255));
        focAngle = (short)(data[8] & 255);
        pTorque = (float)(((data[10] & 255) << 8) + (data[9] & 255)) / 100;
        battery_energy_h_km_x10 = (float)(((data[12] & 255) << 8) + (data[11] & 255)) / 10;
        this.pedalWeight = (float)(((data[14] & 255) << 8) + (data[13] & 255)) / 10;
        return true;
    }

}
