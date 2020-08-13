package spider65.ebike.tsdz2_esp32.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.data.LineDataSet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import spider65.ebike.tsdz2_esp32.R;
import spider65.ebike.tsdz2_esp32.TSDZBTService;
import spider65.ebike.tsdz2_esp32.TSDZConst;
import spider65.ebike.tsdz2_esp32.data.TSDZ_Config;
import spider65.ebike.tsdz2_esp32.databinding.ActivityTorqueSensorSetupBinding;


public class TorqueSensorCalibrationActivity extends AppCompatActivity {

    private static final String TAG = "TorqueSetupActivity";
    private TSDZ_Config cfg = new TSDZ_Config();
    private IntentFilter mIntentFilter = new IntentFilter();
    private ActivityTorqueSensorSetupBinding binding;
    private LineChart chart;

    EditText firstNum_1, secondNum_1, firstNum_2, secondNum_2,firstNum_3, secondNum_3;
    EditText firstNum_4, secondNum_4,firstNum_5, secondNum_5, firstNum_6, secondNum_6;



     @Override
		protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView(this,R.layout.activity_torque_sensor_setup);
         binding.setHandler(this);

         Toolbar toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

         mIntentFilter.addAction(TSDZBTService.TSDZ_CFG_READ_BROADCAST);
         mIntentFilter.addAction(TSDZBTService.TSDZ_CFG_WRITE_BROADCAST);
         TSDZBTService service = TSDZBTService.getBluetoothService();
         if (service != null && service.getConnectionStatus() == TSDZBTService.ConnectionState.CONNECTED)
             service.readCfg();
         else {
             Toast.makeText(TorqueSensorCalibrationActivity.this,
                     "Bike is not connected!", Toast.LENGTH_LONG).show();
         }

         ActionBar actionBar = getSupportActionBar();
         if (actionBar != null)
             actionBar.setDisplayShowTitleEnabled(false);

         firstNum_1 = findViewById(R.id.firstNum_1);
         secondNum_1 = findViewById(R.id.secondNum_1);
         firstNum_2 = findViewById(R.id.firstNum_2);
         secondNum_2 = findViewById(R.id.secondNum_2);
         firstNum_3 = findViewById(R.id.firstNum_3);
         secondNum_3 = findViewById(R.id.secondNum_3);
         firstNum_4 = findViewById(R.id.firstNum_4);
         secondNum_4 = findViewById(R.id.secondNum_4);
         firstNum_5 = findViewById(R.id.firstNum_5);
         secondNum_5 = findViewById(R.id.secondNum_5);
         firstNum_6 = findViewById(R.id.firstNum_6);
         secondNum_6 = findViewById(R.id.secondNum_6);

         chart = findViewById(R.id.chart);

         View.OnFocusChangeListener listener;

         listener = new View.OnFocusChangeListener() {
             public void onFocusChange(View v, boolean hasFocus) {
                 if (!hasFocus)
                     initGraph();
                     //Toast.makeText(TorqueSensorCalibrationActivity.this,
                       //      "OK", Toast.LENGTH_LONG).show();

             }
         };

         firstNum_1. setOnFocusChangeListener(listener);
         secondNum_1. setOnFocusChangeListener(listener);
         firstNum_2. setOnFocusChangeListener(listener);
         secondNum_2. setOnFocusChangeListener(listener);
         firstNum_3. setOnFocusChangeListener(listener);
         secondNum_3. setOnFocusChangeListener(listener);
         firstNum_4. setOnFocusChangeListener(listener);
         secondNum_4. setOnFocusChangeListener(listener);
         firstNum_5. setOnFocusChangeListener(listener);
         secondNum_5. setOnFocusChangeListener(listener);
         firstNum_6. setOnFocusChangeListener(listener);
         secondNum_6. setOnFocusChangeListener(listener);
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

    public void initGraph() {

        String firstInput_1 = firstNum_1.getText().toString();
        String secondInput_1 = secondNum_1.getText().toString();
        String firstInput_2 = firstNum_2.getText().toString();
        String secondInput_2 = secondNum_2.getText().toString();
        String firstInput_3 = firstNum_3.getText().toString();
        String secondInput_3 = secondNum_3.getText().toString();
        String firstInput_4 = firstNum_4.getText().toString();
        String secondInput_4 = secondNum_4.getText().toString();
        String firstInput_5 = firstNum_5.getText().toString();
        String secondInput_5 = secondNum_5.getText().toString();
        String firstInput_6 = firstNum_6.getText().toString();
        String secondInput_6 = secondNum_6.getText().toString();

        ArrayList<Entry> listData = new ArrayList<>();

        listData.add(new Entry(Float.valueOf(firstInput_1), Float.valueOf(secondInput_1)));
        listData.add(new Entry(Float.valueOf(firstInput_2), Float.valueOf(secondInput_2)));
        listData.add(new Entry(Float.valueOf(firstInput_3), Float.valueOf(secondInput_3)));
        listData.add(new Entry(Float.valueOf(firstInput_4), Float.valueOf(secondInput_4)));
        listData.add(new Entry(Float.valueOf(firstInput_5), Float.valueOf(secondInput_5)));
        listData.add(new Entry(Float.valueOf(firstInput_6), Float.valueOf(secondInput_6)));

        LineDataSet dataset = new LineDataSet(listData, "Torque Sensor Calibration Curve");
        LineData data = new LineData(dataset);
        chart.setData(data);
        chart.invalidate(); //refresh
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
                        TSDZBTService.getBluetoothService().writeCommand(new byte[]{TSDZConst.CMD_NO_DATA});
                        initGraph();
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

    private void saveCfg() {
        Integer val;

        if ((val = checkRange(binding.firstNum1, 0, 120)) == null) {
            showDialog(getString(R.string.torque_sensor_kg), getString(R.string.range_error, 0, 120));
            return;
        }
        cfg.ui16_torque_sensor_calibration[0][0] = val;

        if ((val = checkRange(binding.secondNum1, 100, 400)) == null) {
            showDialog(getString(R.string.torque_sensor_adc), getString(R.string.range_error, 100, 400));
            return;
        }
        cfg.ui16_torque_sensor_calibration[0][1] = val;

        if ((val = checkRange(binding.firstNum2, 0, 120)) == null) {
            showDialog(getString(R.string.torque_sensor_kg), getString(R.string.range_error, 0, 120));
            return;
        }
        cfg.ui16_torque_sensor_calibration[1][0] = val;

        if ((val = checkRange(binding.secondNum2, 100, 400)) == null) {
            showDialog(getString(R.string.torque_sensor_adc), getString(R.string.range_error, 100, 400));
            return;
        }
        cfg.ui16_torque_sensor_calibration[1][1] = val;

        if ((val = checkRange(binding.firstNum3, 0, 120)) == null) {
            showDialog(getString(R.string.torque_sensor_kg), getString(R.string.range_error, 0, 120));
            return;
        }
        cfg.ui16_torque_sensor_calibration[2][0] = val;

        if ((val = checkRange(binding.secondNum3, 100, 400)) == null) {
            showDialog(getString(R.string.torque_sensor_adc), getString(R.string.range_error, 100, 400));
            return;
        }
        cfg.ui16_torque_sensor_calibration[2][1] = val;

        if ((val = checkRange(binding.firstNum4, 0, 120)) == null) {
            showDialog(getString(R.string.torque_sensor_kg), getString(R.string.range_error, 0, 120));
            return;
        }
        cfg.ui16_torque_sensor_calibration[3][0] = val;

        if ((val = checkRange(binding.secondNum4, 100, 400)) == null) {
            showDialog(getString(R.string.torque_sensor_adc), getString(R.string.range_error, 100, 400));
            return;
        }
        cfg.ui16_torque_sensor_calibration[3][1] = val;

        if ((val = checkRange(binding.firstNum5, 0, 120)) == null) {
            showDialog(getString(R.string.torque_sensor_kg), getString(R.string.range_error, 0, 120));
            return;
        }
        cfg.ui16_torque_sensor_calibration[4][0] = val;

        if ((val = checkRange(binding.secondNum5, 100, 400)) == null) {
            showDialog(getString(R.string.torque_sensor_adc), getString(R.string.range_error, 100, 400));
            return;
        }
        cfg.ui16_torque_sensor_calibration[4][1] = val;

        if ((val = checkRange(binding.firstNum6, 0, 120)) == null) {
            showDialog(getString(R.string.torque_sensor_kg), getString(R.string.range_error, 0, 120));
            return;
        }
        cfg.ui16_torque_sensor_calibration[5][0] = val;

        if ((val = checkRange(binding.secondNum6, 100, 400)) == null) {
            showDialog(getString(R.string.torque_sensor_adc), getString(R.string.range_error, 100, 400));
            return;
        }
        cfg.ui16_torque_sensor_calibration[5][1] = val;

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

}