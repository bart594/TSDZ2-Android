package spider65.ebike.tsdz2_esp32.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import spider65.ebike.tsdz2_esp32.R;
import spider65.ebike.tsdz2_esp32.TSDZBTService;
import spider65.ebike.tsdz2_esp32.TSDZConst;
import spider65.ebike.tsdz2_esp32.data.TSDZ_Config;
import spider65.ebike.tsdz2_esp32.databinding.ActivitySystemSetupBinding;

public class SystemSetupActivity extends AppCompatActivity {

    private static final String TAG = "MotorSetupActivity";
    private TSDZ_Config cfg = new TSDZ_Config();
    private IntentFilter mIntentFilter = new IntentFilter();
    private ActivitySystemSetupBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_system_setup);
        binding.setHandler(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SeekBar seekBarAcceleration = findViewById(R.id.seekBarAcceleration);
        TextView seekBarValueAcceleration = findViewById(R.id.TxtviewSeekBarAcceleration);
        SeekBar seekBarMaxCurrent = findViewById(R.id.seekBarMaxCurrent);
        TextView seekBarValueMaxCurrent = findViewById(R.id.TxtviewSeekBarMaxCurrent);
        SeekBar seekBarMaxPower = findViewById(R.id.seekBarMaxPower);
        TextView seekBarValueMaxPower = findViewById(R.id.TxtviewSeekBarMaxPower);
        SeekBar seekBarCadThd = findViewById(R.id.seekBarCadThd);
        TextView seekBarValueCadThd = findViewById(R.id.TxtviewSeekBarCadThd);
        SeekBar seekBarTorqueAdcStep = findViewById(R.id.seekBarTorqueAdcStep);
        TextView seekBarValueTorqueAdcStep = findViewById(R.id.TxtviewSeekBarTorqueAdcStep);

        seekBarAcceleration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValueAcceleration.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarMaxCurrent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValueMaxCurrent.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarMaxPower.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progress_custom =  progress * 25;
                seekBarValueMaxPower.setText(String.valueOf(progress_custom));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarCadThd.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValueCadThd.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarTorqueAdcStep.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValueTorqueAdcStep.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mIntentFilter.addAction(TSDZBTService.TSDZ_CFG_READ_BROADCAST);
        mIntentFilter.addAction(TSDZBTService.TSDZ_CFG_WRITE_BROADCAST);
        TSDZBTService service = TSDZBTService.getBluetoothService();
        if (service != null && service.getConnectionStatus() == TSDZBTService.ConnectionState.CONNECTED)
            service.readCfg();
        else {
            showDialog(getString(R.string.error), getString(R.string.connection_error));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    public void onOkCancelClick(View view) {
        switch (view.getId()) {
            case R.id.okButton:
                saveCfg();
                break;
            case R.id.cancelButton:
                finish();
                break;
        }
    }

    //  invalidate all to hide/show the checkbox dependant fields
    public void onCheckedChanged(View view, boolean checked) {
        switch (view.getId()) {
            case R.id.streetPowerCB:
                binding.streetPowerET.setEnabled(checked);
                break;
            case R.id.FWModeCB:
                binding.FWCurrentET.setEnabled(checked);
                break;
        }
    }

    private void saveCfg() {
        Integer val;
        boolean checked;

        cfg.ui8_motor_type = binding.motorTypeSP.getSelectedItemPosition();

        cfg.ui8_motor_acceleration =  binding.seekBarAcceleration.getProgress();

        cfg.ui8_battery_max_current = binding.seekBarMaxCurrent.getProgress();

        cfg.ui8_target_max_battery_power_div25 = binding.seekBarMaxPower.getProgress();

        if (cfg.ui8_target_peak_battery_power_div25[0] > cfg.ui8_target_max_battery_power_div25)
            cfg.ui8_target_peak_battery_power_div25[0] = cfg.ui8_target_max_battery_power_div25;

        if (cfg.ui8_target_peak_battery_power_div25[1] > cfg.ui8_target_max_battery_power_div25)
            cfg.ui8_target_peak_battery_power_div25[1] = cfg.ui8_target_max_battery_power_div25;

        if (cfg.ui8_target_peak_battery_power_div25[2] > cfg.ui8_target_max_battery_power_div25)
            cfg.ui8_target_peak_battery_power_div25[2] = cfg.ui8_target_max_battery_power_div25;

        if (cfg.ui8_target_peak_battery_power_div25[3] > cfg.ui8_target_max_battery_power_div25)
            cfg.ui8_target_peak_battery_power_div25[3] = cfg.ui8_target_max_battery_power_div25;

        if (cfg.ui8_target_peak_battery_power_div25[4] > cfg.ui8_target_max_battery_power_div25)
            cfg.ui8_target_peak_battery_power_div25[4] = cfg.ui8_target_max_battery_power_div25;

        checked = binding.FWModeCB.isChecked();
        if (checked) {
            if ((val = checkRange(binding.FWCurrentET, 0, 25)) == null) {
                showDialog("FW Current", getString(R.string.range_error, 0, 25));
                return;
            }
            cfg.ui8_field_weakening_current = val;
        }
        cfg.ui8_field_weakening_enabled = checked;

        cfg.ui8_assist_without_pedal_rotation_threshold = binding.seekBarCadThd.getProgress();
        cfg.ui8_pedal_torque_per_10_bit_ADC_step_x100  = binding.seekBarTorqueAdcStep.getProgress();

        checked = binding.torquecalibModeCB.isChecked();
        cfg.ui8_torque_sensor_calibration_feature_enabled = checked;

        if ((val = checkRange(binding.wheelPerimeterET, 1000, 2500)) == null) {
            showDialog(getString(R.string.wheel_perimeter), getString(R.string.range_error, 1000, 2500));
            return;
        }
        cfg.ui16_wheel_perimeter = val;

        checked = binding.walkModeCB.isChecked();
        cfg.ui8_walk_mode_enabled = checked;

        checked = binding.streetPowerCB.isChecked();
        if (checked) {
            if ((val = checkRange(binding.streetPowerET, 50, 500)) == null) {
                showDialog(getString(R.string.max_power), getString(R.string.range_error, 50, 500));
                return;
            }
            cfg.ui8_street_mode_power_limit_div25 = val;
        }
        cfg.ui8_street_mode_power_limit_enabled = checked;

        checked = binding.streetThrottleCB.isChecked();
        cfg.ui8_street_mode_throttle_enabled = checked;

        cfg.ui8_lights_configuration = binding.lightConfigSP.getSelectedItemPosition();

        TSDZBTService service = TSDZBTService.getBluetoothService();
        if (service != null && service.getConnectionStatus() == TSDZBTService.ConnectionState.CONNECTED)
            service.writeCfg(cfg);
        else {
            showDialog(getString(R.string.error), getString(R.string.connection_error));
        }

    }

    Integer checkRange(EditText et, int min, int max) {
        int val = Integer.parseInt(et.getText().toString());
        if (val < min || val > max) {
            et.setError(getString(R.string.range_error, min, max));
            return null;
        }
        return val;
    }

    private void showDialog (String title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (title != null)
            builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive " + intent.getAction());
        if (intent.getAction() == null)
            return;
        switch (intent.getAction()) {
            case TSDZBTService.TSDZ_CFG_READ_BROADCAST:
                if (cfg.setData(intent.getByteArrayExtra(TSDZBTService.VALUE_EXTRA))) {
                    binding.setCfg(cfg);
                    binding.motorTypeSP.setSelection(cfg.ui8_motor_type);
                    binding.lightConfigSP.setSelection(cfg.ui8_lights_configuration);
                    TSDZBTService.getBluetoothService().writeCommand(new byte[] {TSDZConst.CMD_NO_DATA});
                }
                break;
            case TSDZBTService.TSDZ_CFG_WRITE_BROADCAST:
                if (intent.getBooleanExtra(TSDZBTService.VALUE_EXTRA,false)){
                    finish();}
                else
                    showDialog(getString(R.string.error), getString(R.string.write_cfg_error));
                break;
         }
        }
    };
}
