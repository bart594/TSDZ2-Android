package spider65.ebike.tsdz2_sw102.data;

import android.util.Log;

import static spider65.ebike.tsdz2_sw102.TSDZConst.TRIP_ADV_SIZE;

// Debug Characteristic Notification
// According to the BLE specification the notification length can be max ATT_MTU - 3.
// The 3 bytes subtracted is the 3-byte header(OP-code (operation, 1 byte) and the attribute handle (2 bytes))
// In BLE 4.1 the ATT_MTU is 23 bytes, but in BLE 4.2 the ATT_MTU can be negotiated up to 247 bytes
// -> Max payload for BT 4.1 is 20 bytes
public class TSDZ_Trip {

    private static final String TAG = "TSDZ_Trip";

    public int trip_time;
    public float trip_distance;
    public int battery_power_avg;
    public float battery_current_avg;
    public int pedal_power_avg;
    public float trip_avg_speed;
    public float trip_max_speed;
    public float battery_energy_h_km_avg; // wh/km
    public short pedal_cadence_avg;
    public String g_trip_time = "0:00";


    public boolean setData(byte[] data) {
        if (data.length != TRIP_ADV_SIZE) {
            Log.e(TAG, "Wrong Trip BT message size!");
           return false;
        }
        trip_time = (data[1] & 255) + ((data[2] & 255) << 8)  + ((data[3] & 255) << 16);

        int temp = trip_time % 86399; // 86399 = seconds in 1 day minus 1s
        // Calculate trip time
        int hours = temp / 3600;
        int minutes = (temp % 3600) / 60;
        int seconds = (temp % 3600) % 60;

        if(hours > 0){
            g_trip_time = String.format("%d:%02d", hours, minutes);
        }else{
            g_trip_time = String.format("%d:%02d", minutes, seconds);
        }

        trip_distance =  (float) ((data[4] & 255) + ((data[5] & 255) << 8)  + ((data[6] & 255) << 16)) / 100 ;
        trip_avg_speed = (float) ((data[7] & 255) + ((data[8] & 255) << 8)) / 10;
        trip_max_speed = (float) ((data[9] & 255) + ((data[10] & 255) << 8)) / 10;
        battery_current_avg = (float) ((data[11] & 255) + ((data[12] & 255) << 8)) / 10;
        battery_power_avg =  ((data[13] & 255) + ((data[14] & 255) << 8));
        pedal_power_avg = ((data[15] & 255) + ((data[16] & 255) << 8));
        pedal_cadence_avg = (short) (data[17] & 255);
        battery_energy_h_km_avg = (float) ((data[18] & 255) + ((data[19] & 255) << 8)) / 100;
        return true;
    }

}
