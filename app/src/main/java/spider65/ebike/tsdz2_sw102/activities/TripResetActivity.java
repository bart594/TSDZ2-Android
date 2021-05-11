package spider65.ebike.tsdz2_sw102.activities;

import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import spider65.ebike.tsdz2_sw102.R;
import spider65.ebike.tsdz2_sw102.TSDZBTService;
import spider65.ebike.tsdz2_sw102.TSDZConst;


public class TripResetActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_trip_reset);
        Button reset_b = findViewById(R.id.trip_reset_button);


        reset_b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog("Are you sure ?", "Remaining trip data will be erased!");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                     TSDZBTService.getBluetoothService().writeCommand(new byte[]{TSDZConst.CMD_TRIP_DATA, TSDZConst.CMD_RESET_TRIP_DATA});
                     try {
                         //set time in mili
                         Thread.sleep(200);

                     }catch (Exception e){
                         e.printStackTrace();
                     }

                     TSDZBTService.getBluetoothService().writeCommand(new byte[]{TSDZConst.CMD_TRIP_DATA, 0x00,});
                     Toast.makeText(getApplicationContext(), "Trip data erased", Toast.LENGTH_LONG).show();
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
