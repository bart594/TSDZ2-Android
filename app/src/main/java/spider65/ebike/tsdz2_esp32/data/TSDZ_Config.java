package spider65.ebike.tsdz2_esp32.data;
import android.util.Log;

public class TSDZ_Config  {


    private static final String TAG = "TSDZ_Config";
    private static final int CFG_SIZE = 20;

    public enum TempControl {
        none (0),
        tempADC (1);

        private final int value;
        TempControl(int value) {
            this.value = value;
        }

        public static TempControl fromValue(int value) {
            switch (value) {
                case 0:
                    return none;
                case 1:
                    return tempADC;
            }
            return null;
        }

        public int getValue() {
            return value;
        }
    }

    public int ui8_motor_type;
    public int ui8_motor_temperature_min_value_to_limit;
    public int ui8_motor_temperature_max_value_to_limit;
    public int ui8_motor_acceleration;
    public boolean advanced_cadence_sensor_mode;
    public int ui16_cadence_sensor_pulse_high_percentage_x10;
    public int ui8_pedal_torque_per_10_bit_ADC_step_x100;
    public TempControl temperature_control;
    public boolean throttleEnabled;
    public int ui8_assist_without_pedal_rotation_threshold;
    public int ui8_lights_configuration;
    public int ui16_wheel_perimeter;
    public boolean ui8_walk_mode_enabled;
    public int ui16_battery_voltage_reset_wh_counter_x10;
    public int ui8_battery_max_current;
    public int ui8_target_max_battery_power_div25;
    public int ui16_battery_pack_resistance_x1000;
    public int ui16_battery_low_voltage_cut_off_x10;
    public boolean ui8_street_mode_enabled;
    public boolean ui8_street_mode_power_limit_enabled;
    public boolean ui8_street_mode_throttle_enabled;
    public int ui8_street_mode_power_limit_div25;
    public int ui8_street_mode_speed_limit;
    public int ui8_eMTB_assist_level;
    public int[] ui8_motor_acceleration_level = new int[5];
    public int[] ui8_power_assist_level = new int[5];
    public int[] ui8_torque_assist_level = new int[5];
    public int[] ui8_target_peak_battery_power_div25 = new int[5];
    public int[] ui8_walk_assist_level = new int[5];
    public int[][] ui16_torque_sensor_calibration = new int[6][2];
    public int ui32_wh_x10_100_percent;
    public int ui32_wh_x10_offset;
    public int ui8_battery_soc_enable;
    public boolean ui8_torque_sensor_calibration_feature_enabled;
    public boolean ui8_field_weakening_enabled;
    public int ui8_field_weakening_current;
    public int ui8_cadence_RPM_limit;
    public boolean ui8_soft_start_feature_enabled;
    public int  ui8_number_of_assist_levels;

    private boolean first_data_chunk = true;
    private boolean second_data_chunk = true;
    private boolean third_data_chunk = true;
    private boolean fourth_data_chunk = true;
    private boolean fifth_data_chunk = true;
    /*
    #pragma pack(1)
    typedef struct _tsdz_cfg
    {
      volatile uint8_t ui8_motor_type;
      volatile uint8_t ui8_motor_temperature_min_value_to_limit;
      volatile uint8_t ui8_motor_temperature_max_value_to_limit;
      volatile uint8_t ui8_motor_acceleration;
      volatile uint8_t ui8_cadence_sensor_mode;
      volatile uint16_t ui16_cadence_sensor_pulse_high_percentage_x10;
      volatile uint8_t ui8_pedal_torque_per_10_bit_ADC_step_x100;
      volatile uint8_t ui8_optional_ADC_function;
      volatile uint8_t ui8_lights_configuration;
      volatile uint16_t ui16_wheel_perimeter;
      volatile uint8_t ui8_cruise_enabled;
      volatile uint16_t ui16_battery_voltage_reset_wh_counter_x10;
      volatile uint8_t ui8_battery_max_current;
      volatile uint8_t ui8_target_max_battery_power_div25;
      volatile uint16_t ui16_battery_pack_resistance_x1000;
      volatile uint16_t ui16_battery_low_voltage_cut_off_x10;
      volatile uint8_t ui8_street_mode_enabled;
      volatile uint8_t ui8_street_mode_power_limit_enabled;
      volatile uint8_t ui8_street_mode_throttle_enabled;
      volatile uint8_t ui8_street_mode_power_limit_div25;
      volatile uint8_t ui8_street_mode_speed_limit;
      volatile uint8_t ui8_esp32_temp_control;
      volatile uint8_t ui8_cadence_assist_level[4];
      volatile uint8_t ui8_power_assist_level[4];
      volatile uint8_t ui8_torque_assist_level[4];
      volatile uint8_t ui8_walk_assist_level[4];
    } struct_tsdz_cfg;
    */

    public boolean setData(byte[] data) {
        if (data.length != CFG_SIZE) {
            Log.e(TAG, "setData: wrong data size");
            return false;
        }
        if (data[1] == 1) {
            ui8_motor_type = (data[2] & 255);
            ui8_motor_temperature_min_value_to_limit = (data[3] & 255);
            ui8_motor_temperature_max_value_to_limit = (data[4] & 255);
            ui8_motor_acceleration = (data[5] & 255);
            ui8_number_of_assist_levels = (data[6] & 255);
            ui8_eMTB_assist_level = (data[7] & 255);
            ui8_torque_sensor_calibration_feature_enabled = (data[8] & 255) != 0;
            ui8_pedal_torque_per_10_bit_ADC_step_x100 = (data[9] & 255);
            throttleEnabled = (data[10] & 255) == 2;

            if ((data[10] & 255) == 1) {
                temperature_control = TempControl.tempADC; // Temperature controlled by Controller sensor
            } else
                temperature_control = TempControl.none; // No motorTemperature control

            ui8_assist_without_pedal_rotation_threshold = (data[11] & 255);
            ui8_lights_configuration = (data[12] & 255);
            ui16_wheel_perimeter = (data[13] & 255) + ((data[14] & 255) << 8);
            ui8_walk_mode_enabled = (data[15] & 255) != 0;
            ui16_battery_voltage_reset_wh_counter_x10 = (data[16] & 255) + ((data[17] & 255) << 8);
            ui8_battery_max_current = (data[18] & 255);
            ui8_target_max_battery_power_div25 = (data[19] & 255);
            return false;
        }else if(data[1] == 2) {
            ui16_battery_pack_resistance_x1000 = (data[2] & 255) + ((data[3] & 255) << 8);
            ui16_battery_low_voltage_cut_off_x10 = (data[4] & 255) + ((data[5] & 255) << 8);
            ui8_street_mode_enabled = (data[6] & 255) != 0;
            ui8_street_mode_throttle_enabled = (data[7] & 255) != 0;
            ui8_street_mode_power_limit_div25 = (data[8] & 255) * 25;
            ui8_street_mode_speed_limit = (data[9] & 255);
            ui8_field_weakening_enabled = (data[10] & 255) != 0;
            ui8_field_weakening_current = (data[11] & 255);
            ui8_cadence_RPM_limit = (data[12] & 255);
            ui8_soft_start_feature_enabled = (data[13] & 255) != 0;
            ui8_battery_soc_enable = (data[14] & 255);
            return false;
        }else if(data[1] == 3) {
            for (int i = 0; i < 5; i++)
                ui8_target_peak_battery_power_div25[i] = (data[2 + i] & 255);
            for (int i = 0; i < 5; i++)
                ui8_motor_acceleration_level[i] = (data[7 + i] & 255);
            for (int i = 0; i < 5; i++)
                ui8_walk_assist_level[i] = (data[12 + i] & 255);
            return false;
        }else if(data[1] == 4) {
            for (int i = 0; i < 5; i++)
                ui8_power_assist_level[i] = (data[2 + i] & 255);
            for (int i = 0; i < 5; i++)
                ui8_torque_assist_level[i] = (data[7 + i] & 255);
                ui32_wh_x10_100_percent = (data[12] & 255) + ((data[13] & 255) << 8)  + ((data[14] & 255) << 16);
                ui32_wh_x10_offset = (data[15] & 255) + ((data[16] & 255) << 8)  + ((data[17] & 255) << 16);
            return false;
        }else if(data[1] == 5) {
            for (int i = 0; i < 6; i++)
                ui16_torque_sensor_calibration[i][0] = (data[2 + i] & 255); //kg

                ui16_torque_sensor_calibration[0][1] = (data[8] & 255) + ((data[9] & 255) << 8); //adc
                ui16_torque_sensor_calibration[1][1] = (data[10] & 255) + ((data[11] & 255) << 8);
                ui16_torque_sensor_calibration[2][1] = (data[12] & 255) + ((data[13] & 255) << 8);
                ui16_torque_sensor_calibration[3][1] = (data[14] & 255) + ((data[15] & 255) << 8);
                ui16_torque_sensor_calibration[4][1] = (data[16] & 255) + ((data[17] & 255) << 8);
                ui16_torque_sensor_calibration[5][1] = (data[18] & 255) + ((data[19] & 255) << 8);
        }
        return true;
    }

    public byte[] toByteArray() {
        byte[] data = new byte[CFG_SIZE];

        if(first_data_chunk) {

            first_data_chunk = false;

            data[0] = 0x53;                         // S
            data[1] = 1;                            //first chunk of config data
            data[2] = (byte) (ui8_motor_type & 0xff);
            data[3] = (byte) ui8_motor_temperature_min_value_to_limit;
            data[4] = (byte) ui8_motor_temperature_max_value_to_limit;
            data[5] = (byte) ui8_motor_acceleration;
            data[6] = (byte) ui8_number_of_assist_levels;
            data[7] = (byte) ui8_eMTB_assist_level;
            data[8] = (byte) (ui8_torque_sensor_calibration_feature_enabled ? 1 : 0);
            data[9] = (byte) ui8_pedal_torque_per_10_bit_ADC_step_x100;
            data[10] = (byte) (throttleEnabled ? 2 : 0); // ui8_optional_ADC_function
            data[10] = temperature_control == TempControl.tempADC ? (byte) 1 : data[10]; // ui8_optional_ADC_function
            data[11] = (byte) ui8_assist_without_pedal_rotation_threshold;
            data[12] = (byte) ui8_lights_configuration;
            data[13] = (byte) ui16_wheel_perimeter;
            data[14] = (byte) (ui16_wheel_perimeter >>> 8);
            data[15] = (byte) (ui8_walk_mode_enabled ? 1 : 0);
            data[16] = (byte) ui16_battery_voltage_reset_wh_counter_x10;
            data[17] = (byte) (ui16_battery_voltage_reset_wh_counter_x10 >>> 8);
            data[18] = (byte) ui8_battery_max_current;
            data[19] = (byte) (ui8_target_max_battery_power_div25);

            return data;

        }else if(second_data_chunk) {

            second_data_chunk = false;

            data[0] = 0x53;
            data[1] = 2;                            //second chunk of config data
            data[2] = (byte) ui16_battery_pack_resistance_x1000;
            data[3] = (byte) (ui16_battery_pack_resistance_x1000 >>> 8);
            data[4] = (byte) ui16_battery_low_voltage_cut_off_x10;
            data[5] = (byte) (ui16_battery_low_voltage_cut_off_x10 >>> 8);
            data[6] = (byte) (ui8_street_mode_enabled ? 1 : 0);
            data[7] = (byte) (ui8_street_mode_throttle_enabled ? 1 : 0);
            data[8] = (byte) (ui8_street_mode_power_limit_div25 / 25);
            data[9] = (byte) ui8_street_mode_speed_limit;
            data[10] = (byte) (ui8_field_weakening_enabled? 1 : 0);
            data[11] = (byte) ui8_field_weakening_current;
            data[12] = (byte) ui8_cadence_RPM_limit;
            data[13] = (byte) (ui8_soft_start_feature_enabled ? 1 : 0);
            data[14] = (byte) ui8_battery_soc_enable;
            return data;

        }else if (third_data_chunk)  {

            third_data_chunk = false;

            data[0] = 0x53;
            data[1] = 3;                            //third chunk of config data
            for (int i = 0; i < 5; i++)
                data[2 + i] = (byte) ui8_target_peak_battery_power_div25[i];
            for (int i = 0; i < 5; i++)
                data[7 + i] = (byte) ui8_motor_acceleration_level[i];
            for (int i = 0; i < 5; i++)
                data[12 + i] = (byte) ui8_walk_assist_level[i];
            return data;

        }else if (fourth_data_chunk)  {

            fourth_data_chunk = false;

            data[0] = 0x53;
            data[1] = 4;                            //fourth chunk of config data
            for (int i = 0; i < 5; i++)
                data[2 + i] = (byte) ui8_power_assist_level[i];
            for (int i = 0; i < 5; i++)
                data[7 + i] = (byte) ui8_torque_assist_level[i];
            data[12] = (byte) ui32_wh_x10_100_percent;
            data[13] = (byte) (ui32_wh_x10_100_percent >>> 8);
            data[14] = (byte) (ui32_wh_x10_100_percent >>> 16);
            data[15] = (byte) ui32_wh_x10_offset;
            data[16] = (byte) (ui32_wh_x10_offset >>> 8);
            data[17] = (byte) (ui32_wh_x10_offset >>> 16);

            return data;

        }else if (fifth_data_chunk)  {

        data[0] = 0x53;
        data[1] = 5;                            //fifth chunk of config data

        for (int i = 0; i < 6; i++)
        data[2 + i] = (byte) ui16_torque_sensor_calibration[i][0]; //kg

        data[8] = (byte) ui16_torque_sensor_calibration[0][1];
        data[9] = (byte) (ui16_torque_sensor_calibration[0][1] >>> 8);
        data[10] = (byte) ui16_torque_sensor_calibration[1][1];
        data[11] = (byte) (ui16_torque_sensor_calibration[1][1] >>> 8);
        data[12] = (byte) ui16_torque_sensor_calibration[2][1];
        data[13] = (byte) (ui16_torque_sensor_calibration[2][1] >>> 8);
        data[14] = (byte) ui16_torque_sensor_calibration[3][1];
        data[15] = (byte) (ui16_torque_sensor_calibration[3][1] >>> 8);
        data[16] = (byte) ui16_torque_sensor_calibration[4][1];
        data[17] = (byte) (ui16_torque_sensor_calibration[4][1] >>> 8);
        data[18] = (byte) ui16_torque_sensor_calibration[5][1];
        data[19] = (byte) (ui16_torque_sensor_calibration[5][1] >>> 8);

        first_data_chunk = true;
        second_data_chunk = true;
        third_data_chunk = true;
        fourth_data_chunk = true;
        }
         return data;
    }
}
