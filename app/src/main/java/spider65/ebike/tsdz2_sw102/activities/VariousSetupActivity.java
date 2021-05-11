package spider65.ebike.tsdz2_sw102.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import spider65.ebike.tsdz2_sw102.databinding.ActivityVariousSetupBinding;

public class VariousSetupActivity extends AppCompatActivity {

    private static final String TAG = "VariousSetupActivity";
    private TSDZ_Config cfg = new TSDZ_Config();
    private IntentFilter mIntentFilter = new IntentFilter();
    private ActivityVariousSetupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_various_setup);
        binding.setClickHandler(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SeekBar seekBarEnLvL = findViewById(R.id.seekBarEnLvl);
        TextView seekBarValueEnLvl = findViewById(R.id.TxtviewSeekBarEnLvl);
        SeekBar seekBarPwrOff = findViewById(R.id.seekBarPwrOff);
        TextView seekBarValuePwrOff = findViewById(R.id.TxtviewSeekBarPwrOff);


        seekBarEnLvL.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValueEnLvl.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarPwrOff.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValuePwrOff.setText(String.valueOf(progress));
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
            case R.id.exitButton:
                finish();
                break;
        }
    }

    private void saveCfg() {

        int selected = binding.timeField.getSelectedItemPosition();
        cfg.ui8_time_field_enable = selected;

        selected = binding.unitsTP.getSelectedItemPosition();
        cfg.ui8_units_type = selected;

        selected = binding.plusLP.getSelectedItemPosition();
        cfg.ui8_plus_long_press_switch = selected;

            cfg.ui8_energy_saving_mode_level = binding.seekBarEnLvl.getProgress();
            cfg.ui8_lcd_power_off_time_minutes = binding.seekBarPwrOff.getProgress();


        TSDZBTService service = TSDZBTService.getBluetoothService();
        if (service != null && service.getConnectionStatus() == TSDZBTService.ConnectionState.CONNECTED)
            service.writeCfg(cfg);
        else {
            showDialog(getString(R.string.error), getString(R.string.connection_error));
        }
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
            switch (intent.getAction()) {
                case TSDZBTService.TSDZ_CFG_READ_BROADCAST:
                    if (cfg.setData(intent.getByteArrayExtra(TSDZBTService.VALUE_EXTRA))) {
                        binding.setCfg(cfg);
                        binding.timeField.setSelection(cfg.ui8_time_field_enable);
                        binding.unitsTP.setSelection(cfg.ui8_units_type);
                        binding.plusLP.setSelection(cfg.ui8_plus_long_press_switch);
                        //TSDZBTService.getBluetoothService().writeCommand(new byte[] {TSDZConst.CMD_NO_DATA});
                    }
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
