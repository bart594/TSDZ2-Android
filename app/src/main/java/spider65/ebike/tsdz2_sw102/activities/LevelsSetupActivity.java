package spider65.ebike.tsdz2_sw102.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import spider65.ebike.tsdz2_sw102.R;
import spider65.ebike.tsdz2_sw102.TSDZBTService;
import spider65.ebike.tsdz2_sw102.data.TSDZ_Config;
import spider65.ebike.tsdz2_sw102.databinding.ActivityLevelsSetupBinding;

public class LevelsSetupActivity extends AppCompatActivity {

    private static final String TAG = "LevelsSetupActivity";
    private TSDZ_Config cfg = new TSDZ_Config();
    private IntentFilter mIntentFilter = new IntentFilter();
    private ActivityLevelsSetupBinding binding;
    SeekBar.OnSeekBarChangeListener mlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_levels_setup);
        binding.setClickHandler(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SeekBar seekBarAssistLevelsNumber = findViewById(R.id.seekBarAssistLevelsNumber);
        TextView SeekBarValueAssistLevelsNumber = findViewById(R.id.TxtviewSeekBarAssistLevelsNumber);
        SeekBar seekBareMTBLevel = findViewById(R.id.seekBareMTBLevel);
        TextView SeekBarValueeMTBLevel = findViewById(R.id.TxtviewSeekBareMTBLevel);
        SeekBar SeekBarADCMinCurrent = findViewById(R.id.SeekBarADCMinCurrent);
        TextView SeekBarValueADCMinCurrent = findViewById(R.id.TxtviewSeekBarADCMinCurrent);


        SeekBar seekBarAssistLevel1 = findViewById(R.id.seekBarAssistLevel1);
        TextView SeekBarValueAssistLevel1 = findViewById(R.id.TxtviewSeekBarAssistLevel1);
        SeekBar seekBarTorqueAssistLevel1 = findViewById(R.id.seekBarTorqueAssistLevel1);
        TextView SeekBarValueTorqueAssistLevel1 = findViewById(R.id.TxtviewSeekBarTorqueAssistLevel1);
        SeekBar seekBarAccelerationLevel1 = findViewById(R.id.seekBarAccelerationLevel1);
        TextView SeekBarValueAccelerationLevel1 = findViewById(R.id.TxtviewSeekBarAccelerationLevel1);
        SeekBar seekBarPeakPowerLevel1 = findViewById(R.id.seekBarPeakPowerLevel1);
        TextView SeekBarValuePeakPowerLevel1 = findViewById(R.id.TxtviewSeekBarPeakPowerLevel1);

        SeekBar seekBarAssistLevel2 = findViewById(R.id.seekBarAssistLevel2);
        TextView SeekBarValueAssistLevel2 = findViewById(R.id.TxtviewSeekBarAssistLevel2);
        SeekBar seekBarTorqueAssistLevel2 = findViewById(R.id.seekBarTorqueAssistLevel2);
        TextView SeekBarValueTorqueAssistLevel2 = findViewById(R.id.TxtviewSeekBarTorqueAssistLevel2);
        SeekBar seekBarAccelerationLevel2 = findViewById(R.id.seekBarAccelerationLevel2);
        TextView SeekBarValueAccelerationLevel2 = findViewById(R.id.TxtviewSeekBarAccelerationLevel2);
        SeekBar seekBarPeakPowerLevel2 = findViewById(R.id.seekBarPeakPowerLevel2);
        TextView SeekBarValuePeakPowerLevel2 = findViewById(R.id.TxtviewSeekBarPeakPowerLevel2);

        SeekBar seekBarAssistLevel3 = findViewById(R.id.seekBarAssistLevel3);
        TextView SeekBarValueAssistLevel3 = findViewById(R.id.TxtviewSeekBarAssistLevel3);
        SeekBar seekBarTorqueAssistLevel3 = findViewById(R.id.seekBarTorqueAssistLevel3);
        TextView SeekBarValueTorqueAssistLevel3 = findViewById(R.id.TxtviewSeekBarTorqueAssistLevel3);
        SeekBar seekBarAccelerationLevel3 = findViewById(R.id.seekBarAccelerationLevel3);
        TextView SeekBarValueAccelerationLevel3 = findViewById(R.id.TxtviewSeekBarAccelerationLevel3);
        SeekBar seekBarPeakPowerLevel3 = findViewById(R.id.seekBarPeakPowerLevel3);
        TextView SeekBarValuePeakPowerLevel3 = findViewById(R.id.TxtviewSeekBarPeakPowerLevel3);

        SeekBar seekBarAssistLevel4 = findViewById(R.id.seekBarAssistLevel4);
        TextView SeekBarValueAssistLevel4 = findViewById(R.id.TxtviewSeekBarAssistLevel4);
        SeekBar seekBarTorqueAssistLevel4 = findViewById(R.id.seekBarTorqueAssistLevel4);
        TextView SeekBarValueTorqueAssistLevel4 = findViewById(R.id.TxtviewSeekBarTorqueAssistLevel4);
        SeekBar seekBarAccelerationLevel4 = findViewById(R.id.seekBarAccelerationLevel4);
        TextView SeekBarValueAccelerationLevel4 = findViewById(R.id.TxtviewSeekBarAccelerationLevel4);
        SeekBar seekBarPeakPowerLevel4 = findViewById(R.id.seekBarPeakPowerLevel4);
        TextView SeekBarValuePeakPowerLevel4 = findViewById(R.id.TxtviewSeekBarPeakPowerLevel4);

        SeekBar seekBarAssistLevel5 = findViewById(R.id.seekBarAssistLevel5);
        TextView SeekBarValueAssistLevel5 = findViewById(R.id.TxtviewSeekBarAssistLevel5);
        SeekBar seekBarTorqueAssistLevel5 = findViewById(R.id.seekBarTorqueAssistLevel5);
        TextView SeekBarValueTorqueAssistLevel5 = findViewById(R.id.TxtviewSeekBarTorqueAssistLevel5);
        SeekBar seekBarAccelerationLevel5 = findViewById(R.id.seekBarAccelerationLevel5);
        TextView SeekBarValueAccelerationLevel5 = findViewById(R.id.TxtviewSeekBarAccelerationLevel5);
        SeekBar seekBarPeakPowerLevel5 = findViewById(R.id.seekBarPeakPowerLevel5);
        TextView SeekBarValuePeakPowerLevel5 = findViewById(R.id.TxtviewSeekBarPeakPowerLevel5);

        SeekBar seekBarWalkAssistLevel1 = findViewById(R.id.seekBarWalkAssistLevel1);
        TextView SeekBarValueWalkAssistLevel1 = findViewById(R.id.TxtviewSeekBarWalkAssistLevel1);
        SeekBar seekBarWalkAssistLevel2 = findViewById(R.id.seekBarWalkAssistLevel2);
        TextView SeekBarValueWalkAssistLevel2 = findViewById(R.id.TxtviewSeekBarWalkAssistLevel2);
        SeekBar seekBarWalkAssistLevel3 = findViewById(R.id.seekBarWalkAssistLevel3);
        TextView SeekBarValueWalkAssistLevel3 = findViewById(R.id.TxtviewSeekBarWalkAssistLevel3);
        SeekBar seekBarWalkAssistLevel4 = findViewById(R.id.seekBarWalkAssistLevel4);
        TextView SeekBarValueWalkAssistLevel4 = findViewById(R.id.TxtviewSeekBarWalkAssistLevel4);
        SeekBar seekBarWalkAssistLevel5 = findViewById(R.id.seekBarWalkAssistLevel5);
        TextView SeekBarValueWalkAssistLevel5 = findViewById(R.id.TxtviewSeekBarWalkAssistLevel5);

        mlistener = new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (seekBar.getId()) {
                    case R.id.seekBarAssistLevelsNumber:
                        SeekBarValueAssistLevelsNumber.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBareMTBLevel:
                        SeekBarValueeMTBLevel.setText(String.valueOf(progress));
                        break;
                    case R.id.SeekBarADCMinCurrent:
                        SeekBarValueADCMinCurrent.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarAssistLevel1:
                        int assist_progress_custom_1 =  progress * 10;
                        SeekBarValueAssistLevel1.setText(String.valueOf(assist_progress_custom_1));
                        break;
                    case R.id.seekBarTorqueAssistLevel1:
                        SeekBarValueTorqueAssistLevel1.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarAccelerationLevel1:
                        SeekBarValueAccelerationLevel1.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarPeakPowerLevel1:
                        int progress_custom1 =  progress * 25;
                        SeekBarValuePeakPowerLevel1.setText(String.valueOf(progress_custom1));
                        break;
                    case R.id.seekBarAssistLevel2:
                        int assist_progress_custom_2 =  progress * 10;
                        SeekBarValueAssistLevel2.setText(String.valueOf(assist_progress_custom_2));
                        break;
                    case R.id.seekBarTorqueAssistLevel2:
                        SeekBarValueTorqueAssistLevel2.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarAccelerationLevel2:
                        SeekBarValueAccelerationLevel2.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarPeakPowerLevel2:
                        int progress_custom2 =  progress * 25;
                        SeekBarValuePeakPowerLevel2.setText(String.valueOf(progress_custom2));
                        break;
                    case R.id.seekBarAssistLevel3:
                        int assist_progress_custom_3 =  progress * 10;
                        SeekBarValueAssistLevel3.setText(String.valueOf(assist_progress_custom_3));
                        break;
                    case R.id.seekBarTorqueAssistLevel3:
                        SeekBarValueTorqueAssistLevel3.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarAccelerationLevel3:
                        SeekBarValueAccelerationLevel3.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarPeakPowerLevel3:
                        int progress_custom3 =  progress * 25;
                        SeekBarValuePeakPowerLevel3.setText(String.valueOf(progress_custom3));
                        break;
                    case R.id.seekBarAssistLevel4:
                        int assist_progress_custom_4 =  progress * 10;
                        SeekBarValueAssistLevel4.setText(String.valueOf(assist_progress_custom_4));
                        break;
                    case R.id.seekBarTorqueAssistLevel4:
                        SeekBarValueTorqueAssistLevel4.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarAccelerationLevel4:
                        SeekBarValueAccelerationLevel4.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarPeakPowerLevel4:
                        int progress_custom4 =  progress * 25;
                        SeekBarValuePeakPowerLevel4.setText(String.valueOf(progress_custom4));
                        break;
                    case R.id.seekBarAssistLevel5:
                        int assist_progress_custom_5 =  progress * 10;
                        SeekBarValueAssistLevel5.setText(String.valueOf(assist_progress_custom_5));
                        break;
                    case R.id.seekBarTorqueAssistLevel5:
                        SeekBarValueTorqueAssistLevel5.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarAccelerationLevel5:
                        SeekBarValueAccelerationLevel5.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarPeakPowerLevel5:
                        int progress_custom5 =  progress * 25;
                        SeekBarValuePeakPowerLevel5.setText(String.valueOf(progress_custom5));
                        break;
                    case R.id.seekBarWalkAssistLevel1:
                        SeekBarValueWalkAssistLevel1.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarWalkAssistLevel2:
                        SeekBarValueWalkAssistLevel2.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarWalkAssistLevel3:
                        SeekBarValueWalkAssistLevel3.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarWalkAssistLevel4:
                        SeekBarValueWalkAssistLevel4.setText(String.valueOf(progress));
                        break;
                    case R.id.seekBarWalkAssistLevel5:
                        SeekBarValueWalkAssistLevel5.setText(String.valueOf(progress));
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        seekBarAssistLevelsNumber.setOnSeekBarChangeListener(mlistener);
        seekBareMTBLevel.setOnSeekBarChangeListener(mlistener);
        SeekBarADCMinCurrent.setOnSeekBarChangeListener(mlistener);


        seekBarAssistLevel1.setOnSeekBarChangeListener(mlistener);
        seekBarTorqueAssistLevel1.setOnSeekBarChangeListener(mlistener);
        seekBarAccelerationLevel1.setOnSeekBarChangeListener(mlistener);
        seekBarPeakPowerLevel1.setOnSeekBarChangeListener(mlistener);

        seekBarAssistLevel2.setOnSeekBarChangeListener(mlistener);
        seekBarTorqueAssistLevel2.setOnSeekBarChangeListener(mlistener);
        seekBarAccelerationLevel2.setOnSeekBarChangeListener(mlistener);
        seekBarPeakPowerLevel2.setOnSeekBarChangeListener(mlistener);

        seekBarAssistLevel3.setOnSeekBarChangeListener(mlistener);
        seekBarTorqueAssistLevel3.setOnSeekBarChangeListener(mlistener);
        seekBarAccelerationLevel3.setOnSeekBarChangeListener(mlistener);
        seekBarPeakPowerLevel3.setOnSeekBarChangeListener(mlistener);

        seekBarAssistLevel4.setOnSeekBarChangeListener(mlistener);
        seekBarTorqueAssistLevel4.setOnSeekBarChangeListener(mlistener);
        seekBarAccelerationLevel4.setOnSeekBarChangeListener(mlistener);
        seekBarPeakPowerLevel4.setOnSeekBarChangeListener(mlistener);

        seekBarAssistLevel5.setOnSeekBarChangeListener(mlistener);
        seekBarTorqueAssistLevel5.setOnSeekBarChangeListener(mlistener);
        seekBarAccelerationLevel5.setOnSeekBarChangeListener(mlistener);
        seekBarPeakPowerLevel5.setOnSeekBarChangeListener(mlistener);

        seekBarWalkAssistLevel1.setOnSeekBarChangeListener(mlistener);
        seekBarWalkAssistLevel2.setOnSeekBarChangeListener(mlistener);
        seekBarWalkAssistLevel3.setOnSeekBarChangeListener(mlistener);
        seekBarWalkAssistLevel4.setOnSeekBarChangeListener(mlistener);
        seekBarWalkAssistLevel5.setOnSeekBarChangeListener(mlistener);

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
        if (view.getId() == R.id.okButton)
            saveCfg();
        else if (view.getId() == R.id.exitButton)
            finish();
    }

    private void saveCfg() {

        cfg.ui8_number_of_assist_levels =  binding.seekBarAssistLevelsNumber.getProgress();
        cfg.ui8_eMTB_assist_level =  binding.seekBareMTBLevel.getProgress();
        cfg.ui8_motor_current_min_adc = binding.SeekBarADCMinCurrent.getProgress();


        cfg.ui8_power_assist_level[0] = binding.seekBarAssistLevel1.getProgress();
        cfg.ui8_torque_assist_level[0] = binding.seekBarTorqueAssistLevel1.getProgress();
        cfg.ui8_motor_acceleration_level[0] = binding.seekBarAccelerationLevel1.getProgress();
        cfg.ui8_target_peak_battery_power_div25[0] = binding.seekBarPeakPowerLevel1.getProgress();

        cfg.ui8_power_assist_level[1] = binding.seekBarAssistLevel2.getProgress();
        cfg.ui8_torque_assist_level[1] = binding.seekBarTorqueAssistLevel2.getProgress();
        cfg.ui8_motor_acceleration_level[1] = binding.seekBarAccelerationLevel2.getProgress();
        cfg.ui8_target_peak_battery_power_div25[1] = binding.seekBarPeakPowerLevel2.getProgress();

        cfg.ui8_power_assist_level[2] = binding.seekBarAssistLevel3.getProgress();
        cfg.ui8_torque_assist_level[2] = binding.seekBarTorqueAssistLevel3.getProgress();
        cfg.ui8_motor_acceleration_level[2] = binding.seekBarAccelerationLevel3.getProgress();
        cfg.ui8_target_peak_battery_power_div25[2] = binding.seekBarPeakPowerLevel3.getProgress();

        cfg.ui8_power_assist_level[3] = binding.seekBarAssistLevel4.getProgress();
        cfg.ui8_torque_assist_level[3] = binding.seekBarTorqueAssistLevel4.getProgress();
        cfg.ui8_motor_acceleration_level[3] = binding.seekBarAccelerationLevel4.getProgress();
        cfg.ui8_target_peak_battery_power_div25[3] = binding.seekBarPeakPowerLevel4.getProgress();

        cfg.ui8_power_assist_level[4] = binding.seekBarAssistLevel5.getProgress();
        cfg.ui8_torque_assist_level[4] = binding.seekBarTorqueAssistLevel5.getProgress();
        cfg.ui8_motor_acceleration_level[4] = binding.seekBarAccelerationLevel5.getProgress();
        cfg.ui8_target_peak_battery_power_div25[4] = binding.seekBarPeakPowerLevel5.getProgress();


        cfg.ui8_walk_assist_level[0] = binding.seekBarWalkAssistLevel1.getProgress();
        cfg.ui8_walk_assist_level[1] = binding.seekBarWalkAssistLevel2.getProgress();
        cfg.ui8_walk_assist_level[2] = binding.seekBarWalkAssistLevel3.getProgress();
        cfg.ui8_walk_assist_level[3] = binding.seekBarWalkAssistLevel4.getProgress();
        cfg.ui8_walk_assist_level[4] = binding.seekBarWalkAssistLevel5.getProgress();


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

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive " + intent.getAction());
        if (intent.getAction() == null)
            return;
        switch (intent.getAction()) {
            case TSDZBTService.TSDZ_CFG_READ_BROADCAST:
                if (cfg.setData(intent.getByteArrayExtra(TSDZBTService.VALUE_EXTRA)))
                    binding.setCfg(cfg);
                    //TSDZBTService.getBluetoothService().writeCommand(new byte[] {TSDZConst.CMD_NO_DATA});
                break;
            case TSDZBTService.TSDZ_CFG_WRITE_BROADCAST:
                if (intent.getBooleanExtra(TSDZBTService.VALUE_EXTRA,false))
                    finish();
                else
                    showDialog(getString(R.string.error), getString(R.string.write_cfg_error));
                break;
         }
        }
    };
}
