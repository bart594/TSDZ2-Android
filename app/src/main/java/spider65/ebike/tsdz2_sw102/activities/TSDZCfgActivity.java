package spider65.ebike.tsdz2_sw102.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import spider65.ebike.tsdz2_sw102.R;
import spider65.ebike.tsdz2_sw102.TSDZBTService;
import spider65.ebike.tsdz2_sw102.TSDZConst;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class TSDZCfgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tsdzcfg);
        Button b;
        b = findViewById(R.id.motor_button);
        b.setOnClickListener((View) -> setupMotor());
        b = findViewById(R.id.battery_button);
        b.setOnClickListener((View) -> setupBattery());
        b = findViewById(R.id.levels_button);
        b.setOnClickListener((View) -> setupLevels());
        b = findViewById(R.id.temperature_control);
        b.setOnClickListener((View) -> setupTemperature());
        b = findViewById(R.id.various_setup);
        b.setOnClickListener((View) -> setupVarious());
        b = findViewById(R.id.torque_sensor);
        b.setOnClickListener((View) -> torqueSensorCalib());
        b = findViewById(R.id.hall_calib_button);
        b.setOnClickListener((View) -> hallCalibration());
        b = findViewById(R.id.reset_settings);
        b.setOnClickListener((View) -> resetSettings());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setupMotor() {
        Intent intent = new Intent(this, SystemSetupActivity.class);
        startActivity(intent);
    }

    private void setupBattery() {
        Intent intent = new Intent(this, BatterySetupActivity.class);
        startActivity(intent);
    }

    private void setupLevels() {
        Intent intent = new Intent(this, LevelsSetupActivity.class);
        startActivity(intent);    }

    private void setupTemperature() {
        Intent intent = new Intent(this, TemperatureSetupActivity.class);
         startActivity(intent);    }

    private void setupVarious() {
        Intent intent = new Intent(this, VariousSetupActivity.class);
        startActivity(intent);    }

    private void hallCalibration() {
       Intent intent = new Intent(this, HallCalibrationActivity.class);
       startActivity(intent);    }

    private void torqueSensorCalib() {
        Intent intent = new Intent(this, TorqueSensorCalibrationActivity.class);
        startActivity(intent);    }

    private void resetSettings() {
        showDialog("Are you sure ?", "All your settings will be overwritten with default values!");
  }

    private void showDialog (String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title != null)
            builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                TSDZBTService service = TSDZBTService.getBluetoothService();
                if (service != null && service.getConnectionStatus() == TSDZBTService.ConnectionState.CONNECTED) {
                    TSDZBTService.getBluetoothService().writeCommand(new byte[]{TSDZConst.CMD_GET_CONFIG_DATA, TSDZConst.CMD_RESET_CONFIG_DATA});
                    try {
                        //set time in mili
                        Thread.sleep(200);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    TSDZBTService.getBluetoothService().writeCommand(new byte[]{TSDZConst.CMD_GET_CONFIG_DATA, 0x00,});
                    Toast.makeText(getApplicationContext(), "Default settings restored", Toast.LENGTH_LONG).show();
                }
                return;
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        builder.show();
    }

}
