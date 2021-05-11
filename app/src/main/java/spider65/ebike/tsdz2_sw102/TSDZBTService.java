package spider65.ebike.tsdz2_sw102;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.UUID;

import androidx.core.app.NotificationCompat;
import android.content.BroadcastReceiver;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import spider65.ebike.tsdz2_sw102.data.TSDZ_Config;

import static android.bluetooth.BluetoothDevice.TRANSPORT_LE;
import static spider65.ebike.tsdz2_sw102.TSDZConst.CMD_GET_CONFIG_DATA;
import static spider65.ebike.tsdz2_sw102.TSDZConst.CMD_STATUS_DATA;
import static spider65.ebike.tsdz2_sw102.TSDZConst.CMD_TRIP_DATA;
import static spider65.ebike.tsdz2_sw102.TSDZConst.CMD_SEND_CONFIG_DATA;
import static spider65.ebike.tsdz2_sw102.TSDZConst.CMD_DEBUG_DATA;
import static spider65.ebike.tsdz2_sw102.TSDZConst.CMD_MOTOR_DATA;
import static spider65.ebike.tsdz2_sw102.TSDZConst.CMD_NO_DATA;

public class TSDZBTService extends Service {

    private static final String TAG = "TSDZBTService";

    private NavinfoServiceReceiver NavinfoServiceReceiver = new NavinfoServiceReceiver();

    public static String TSDZ_UART_SERVICE = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
    public static String TSDZ_CHARACTERISTICS_RX = "6e400002-b5a3-f393-e0a9-e50e24dcca9e";
    public static String TSDZ_CHARACTERISTICS_TX = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";
    public static String CLIENT_CHARACTERISTIC_TSDZ2 = "00002902-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_TSDZ_SERVICE = UUID.fromString(TSDZ_UART_SERVICE);
    public final static UUID UUID_RCV_DATA_CHARACTERISTIC = UUID.fromString(TSDZ_CHARACTERISTICS_TX);
    public final static UUID UUID_SEND_DATA_CHARACTERISTIC = UUID.fromString(TSDZ_CHARACTERISTICS_RX);


    public static final String ADDRESS_EXTRA = "ADDRESS";
    public static final String VALUE_EXTRA = "VALUE";

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    public static final String SERVICE_STARTED_BROADCAST = "SERVICE_STARTED";
    public static final String SERVICE_STOPPED_BROADCAST = "SERVICE_STOPPED";
    public static final String CONNECTION_SUCCESS_BROADCAST = "CONNECTION_SUCCESS";
    public static final String CONNECTION_FAILURE_BROADCAST = "CONNECTION_FAILURE";
    public static final String CONNECTION_LOST_BROADCAST = "CONNECTION_LOST";
    public static final String TSDZ_STATUS_BROADCAST = "TSDZ_STATUS";
    public static final String TSDZ_DEBUG_BROADCAST = "TSDZ_DEBUG";
    public static final String TSDZ_NO_DATA_BROADCAST = "TSDZ_NO_DATA";
    public static final String TSDZ_MOTOR_DATA_BROADCAST = "TSDZ_MOTOR_DATA";
    public static final String TSDZ_TRIP_DATA_BROADCAST = "TSDZ_TRIP_DATA";
    public static final String TSDZ_CFG_READ_BROADCAST = "TSDZ_CFG_READ";
    public static final String TSDZ_CFG_WRITE_BROADCAST = "TSDZ_CFG_WRITE";

    private static final int MAX_CONNECTION_RETRY = 10;
    private static TSDZBTService mService = null;

    private BluetoothAdapter mBluetoothAdapter;
    private String address;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private ConnectionState mConnectionState = ConnectionState.DISCONNECTED;

    private boolean stopped = false;
    private int connectionRetry = 0;

    private BluetoothGattCharacteristic tsdz_tx_char = null;
    private BluetoothGattCharacteristic tsdz_rx_char = null;

    public static TSDZBTService getBluetoothService() {
        return mService;
    }

    public enum ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        DISCONNECTING
    }

    public TSDZBTService() {
        Log.d(TAG, "TSDZBTService()");
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        throw new UnsupportedOperationException();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if(intent != null)
        {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case ACTION_START_FOREGROUND_SERVICE:
                        address = intent.getStringExtra(ADDRESS_EXTRA);
                        if ((address != null) && connect(address))
                            startForegroundService();
                        else {
                            disconnect();
                            stopped = true;
                            Intent bi = new Intent(CONNECTION_FAILURE_BROADCAST);
                            LocalBroadcastManager.getInstance(this).sendBroadcast(bi);
                            stopSelf();
                        }
                        break;
                    case ACTION_STOP_FOREGROUND_SERVICE:
                        stopped = true;
                        disconnect();
                        stopForegroundService();
                        break;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /* Used to build and start foreground service. */
    private void startForegroundService()
    {
        Log.d(TAG, "startForegroundService");

        // Create notification default intent.
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String channelId = getString(R.string.app_name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(channelId);
            NotificationManager service = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            service.createNotificationChannel(notificationChannel);
        }

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setContentTitle(getText(R.string.notification_title));
        //builder.setContentText(getText(R.string.notification_message));
        builder.setTicker(getText(R.string.notification_title));
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.ic_bike_notification);
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        builder.setContentIntent(pendingIntent);

        // Add Disconnect button intent in notification.
        Intent stopIntent = new Intent(this, TSDZBTService.class);
        stopIntent.setAction(ACTION_STOP_FOREGROUND_SERVICE);
        PendingIntent pendingStopIntent = PendingIntent.getService(this, 0, stopIntent, 0);
        NotificationCompat.Action prevAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Disconnect", pendingStopIntent);
        builder.addAction(prevAction);

        // Build the notification.
        Notification notification = builder.build();

        // Start foreground service.
        startForeground(1, notification);
        //Start notification service
        LocalBroadcastManager.getInstance(this).registerReceiver(NavinfoServiceReceiver, new IntentFilter("nav_msg"));

        Intent bi = new Intent(SERVICE_STARTED_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(bi);
        mService = this;
    }

    private void stopForegroundService()
    {
        Log.d(TAG, "stopForegroundService");

        // Stop foreground service and remove the notification.
        stopForeground(true);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(NavinfoServiceReceiver);

        Intent bi = new Intent(SERVICE_STOPPED_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(bi);
        mService = null;

        // Stop the foreground service.
        stopSelf();
    }

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                connectionRetry = 0;
                Log.i(TAG, "onConnectionStateChange: Connected");
                // Discover services after successful connection.
                mBluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mConnectionState = ConnectionState.DISCONNECTED;
                Log.i(TAG, "onConnectionStateChange: Disconnected");
                if (!stopped)
                    if (connectionRetry++ > MAX_CONNECTION_RETRY) {
                        disconnect();
                        stopForegroundService();
                    } else {
                        connect(address);
                        Intent bi = new Intent(CONNECTION_LOST_BROADCAST);
                        LocalBroadcastManager.getInstance(TSDZBTService.this).sendBroadcast(bi);
                    }
                else {
                    mBluetoothGatt.close();
                    mBluetoothGatt = null;
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                List<BluetoothGattService> services = gatt.getServices();
                Log.i(TAG, "Services: " + services.toString());
                for (BluetoothGattService s:services) {
                    if (s.getUuid().equals(UUID_TSDZ_SERVICE)) {
                        List<BluetoothGattCharacteristic> lc = s.getCharacteristics();
                        for (BluetoothGattCharacteristic c:lc) {
                            if (c.getUuid().equals(UUID_RCV_DATA_CHARACTERISTIC)) {
                                tsdz_tx_char = c;
                                Log.d(TAG, "UUID_STATUS_CHARACTERISTIC enable notifications");
                            } else if(c.getUuid().equals(UUID_SEND_DATA_CHARACTERISTIC)) {
                                tsdz_rx_char = c;
                                Log.d(TAG, "UUID_CONFIG_CHARACTERISTIC enable notifications");
                            }
                        }
                    }
                }
                if (tsdz_tx_char == null || tsdz_rx_char == null) {
                    Intent bi = new Intent(CONNECTION_FAILURE_BROADCAST);
                    // TODO bi.putExtra("MESSAGE", "Error Detail");
                    LocalBroadcastManager.getInstance(TSDZBTService.this).sendBroadcast(bi);
                    Log.e(TAG, "onServicesDiscovered Characteristic not found!");
                    disconnect();
                    return;
                }else if (mConnectionState == ConnectionState.CONNECTING) {
                mConnectionState = ConnectionState.CONNECTED;
                Intent bi = new Intent(CONNECTION_SUCCESS_BROADCAST);
                LocalBroadcastManager.getInstance(TSDZBTService.this).sendBroadcast(bi);
            }
            // setCharacteristicNotification is asynchronous. Before to make a new call we
                // must wait the end of the previous in the callback onDescriptorWrite
                setCharacteristicNotification(tsdz_tx_char,true);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                      int status) {
            //Log.d(TAG, "onDescriptorWrite:" + descriptor.getCharacteristic().getUuid().toString() +
            //        " - " + descriptor.getUuid().toString());
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            //Log.d(TAG, "onCharacteristicRead:" + characteristic.getUuid().toString());
            if (status == BluetoothGatt.GATT_SUCCESS) {
               // if (UUID_CONFIG_CHARACTERISTIC.equals(characteristic.getUuid())) {
               //     Intent bi = new Intent(TSDZ_CFG_READ_BROADCAST);
               //     bi.putExtra(VALUE_EXTRA, characteristic.getValue());
               //     LocalBroadcastManager.getInstance(TSDZBTService.this).sendBroadcast(bi);
               // }
            } else {
                Log.e(TAG, "Characteristic read Error: " + status);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            //Log.d(TAG, "onCharacteristicChanged:" + characteristic.getUuid().toString());
            byte [] data = characteristic.getValue();
            if (UUID_RCV_DATA_CHARACTERISTIC.equals(characteristic.getUuid())) {
                    if ((data [0] == CMD_STATUS_DATA)) {
                        Intent bi = new Intent(TSDZ_STATUS_BROADCAST);
                        bi.putExtra(VALUE_EXTRA, data);
                        LocalBroadcastManager.getInstance(TSDZBTService.this).sendBroadcast(bi);
                        Log.d(TAG, "STATUS");
                    }else if (data[0] == CMD_DEBUG_DATA) {
                        Intent bi = new Intent(TSDZ_DEBUG_BROADCAST);
                        bi.putExtra(VALUE_EXTRA, characteristic.getValue());
                        LocalBroadcastManager.getInstance(TSDZBTService.this).sendBroadcast(bi);
                        //Log.d(TAG, "DEBUG DATA");
                    } else if (data[0] == CMD_GET_CONFIG_DATA) {
                        Intent bi = new Intent(TSDZ_CFG_READ_BROADCAST);
                        bi.putExtra(VALUE_EXTRA, characteristic.getValue());
                        LocalBroadcastManager.getInstance(TSDZBTService.this).sendBroadcast(bi);
                        //Log.d(TAG, "read CFG");
                    } else if (data[0] == CMD_MOTOR_DATA) {
                        Intent bi = new Intent(TSDZ_MOTOR_DATA_BROADCAST);
                        bi.putExtra(VALUE_EXTRA, characteristic.getValue());
                        LocalBroadcastManager.getInstance(TSDZBTService.this).sendBroadcast(bi);
                        Log.d(TAG, "MOTOR");
                    } else if (data[0] == CMD_TRIP_DATA) {
                        Intent bi = new Intent(TSDZ_TRIP_DATA_BROADCAST);
                        bi.putExtra(VALUE_EXTRA, characteristic.getValue());
                        LocalBroadcastManager.getInstance(TSDZBTService.this).sendBroadcast(bi);
                        //Log.d(TAG, "TRIP DATA");
                    } else if (data[0] == CMD_NO_DATA) {
                        Intent bi = new Intent(TSDZ_NO_DATA_BROADCAST);
                        bi.putExtra(VALUE_EXTRA, characteristic.getValue());
                        LocalBroadcastManager.getInstance(TSDZBTService.this).sendBroadcast(bi);
                       Log.d(TAG, "NOP");
                    } else {
                        Log.e(TAG, "Wrong RX Advertising Data");
                    }
                }
            }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic,
                                         int status) {
            //Log.d(TAG, "onCharacteristicWrite:" + characteristic.getUuid().toString());
            if (UUID_SEND_DATA_CHARACTERISTIC.equals(characteristic.getUuid())) {
               byte[] w_data = characteristic.getValue();
               if ((w_data [0] == CMD_SEND_CONFIG_DATA)) {
                   Intent bi = new Intent(TSDZ_CFG_WRITE_BROADCAST);
                   bi.putExtra(VALUE_EXTRA, true);
                   LocalBroadcastManager.getInstance(TSDZBTService.this).sendBroadcast(bi);
               }
               //else if(w_data [0] == CMD_MOTOR_DATA) {
                //      Intent bi = new Intent(TSDZ_MOTOR_DATA_BROADCAST);
                 //     bi.putExtra(VALUE_EXTRA, true);
                  //    LocalBroadcastManager.getInstance(TSDZBTService.this).sendBroadcast(bi);

                //}
            }
        }
    };

    private boolean connect(String address) {
        Log.d(TAG, "connect");
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Previously connected device.  Try to reconnect.
        if (address.equals(mBluetoothDeviceAddress)  && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = ConnectionState.CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback, TRANSPORT_LE);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = ConnectionState.CONNECTING;
        return true;
    }

    private void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        if (mConnectionState == ConnectionState.CONNECTED) {
            mConnectionState = ConnectionState.DISCONNECTING;
            setCharacteristicNotification(tsdz_tx_char,false);
        } else
            mBluetoothGatt.disconnect();
    }

    public ConnectionState getConnectionStatus() {
        return mConnectionState;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     */
    public void readCfg() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null || tsdz_rx_char == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        TSDZBTService.getBluetoothService().writeCommand(new byte[] {TSDZConst.CMD_GET_CONFIG_DATA});
        mBluetoothGatt.readCharacteristic(tsdz_rx_char);
         }

    public void writeCfg(TSDZ_Config cfg) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null || tsdz_rx_char == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
   // send 6 chunks of data
        for (int i = 0; i < 6; i++) {
            tsdz_rx_char.setValue(cfg.toByteArray());
            try {
                //set time in mili
                Thread.sleep(200);

            }catch (Exception e){
                e.printStackTrace();
            }
            mBluetoothGatt.writeCharacteristic(tsdz_rx_char);
       }
       //TSDZBTService.getBluetoothService().writeCommand(new byte[] {TSDZConst.CMD_NO_DATA});
    }

    public void writeCommand(byte[] command) {
        //Log.d(TAG,"Sending command: " + Utils.bytesToHex(command));
        if (mBluetoothAdapter == null || mBluetoothGatt == null || tsdz_rx_char == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        tsdz_rx_char.setValue(command);
        mBluetoothGatt.writeCharacteristic(tsdz_rx_char) ;
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                UUID.fromString(CLIENT_CHARACTERISTIC_TSDZ2));
        if (enabled)
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        else
            descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);
    }

    public static class NavinfoServiceReceiver  extends BroadcastReceiver {

        private boolean dist_km = false;
        private boolean total_dist_km = false;
        int dist_data, total_dist_data;

        private byte[] data = new byte[10];

        @Override
        public void onReceive(Context context, Intent intent) {

            String dist = intent.getStringExtra("distance");
            String total_dist = intent.getStringExtra("total_distance");
            String command = intent.getStringExtra("command");

            if (command.equals("S") || dist.equals("S") || total_dist.equals("S")) {
                dist_data = 0x00;
                total_dist_data = 0x00;
            }
            else if (!command.equals("N") || !dist.equals("N") || !total_dist.equals("N")) {
                if (dist.contains("km")) {
                    dist_km = true;
                }else{
                    dist_km = false;
                }
                if (total_dist.contains("km")) {
                    total_dist_km = true;
                }else{
                    total_dist_km = false;
                }


                String arr_dist[] = dist.split(" ");
                String arr_total_dist[] = total_dist.split(" ");

                if (dist_km) {
                    arr_dist[0] = arr_dist[0].replace(".",",");
                    String dist_data_arr[] = arr_dist[0].split(",");
                    dist_data = Integer.parseInt(dist_data_arr[0]) * 1000 + ((Integer.parseInt(dist_data_arr[1]) * 10));
                } else {
                    dist_data = Integer.parseInt(arr_dist[0]);
                }

                if (total_dist_km) {
                    arr_total_dist[0] = arr_total_dist[0].replace(".",",");

                    String t_dist_data_arr[] = arr_total_dist[0].split(",");

                    try {
                        total_dist_data = (Integer.parseInt(t_dist_data_arr[0]) * 1000) + ((Integer.parseInt(t_dist_data_arr[1]) * 10));
                    }
                    catch(ArrayIndexOutOfBoundsException exception) {
                        total_dist_data = (Integer.parseInt(t_dist_data_arr[0]) * 1000);
                    }
                } else {
                    total_dist_data = Integer.parseInt(arr_total_dist[0]);
                }
            }else{
                dist_data = 0x00;
                total_dist_data = 0x00;
            }

            data[0] = 0x4F; //"O" Osmand
            data[1] = (byte) (dist_data & 255);
            data[2] = (byte) (dist_data >>> 8);
            data[3] = (byte) (dist_data >>> 16);
            data[4] = (byte) (total_dist_data & 255);
            data[5] = (byte) (total_dist_data >>> 8);
            data[6] = (byte) (total_dist_data >>> 16);

            if (command.equals("left")){
                data[7] = (byte) (4 & 255);
                data[8] = (byte) (100 & 255);
            }else if(command.equals("right")){
                data[7] = (byte) (6 & 255);
                data[8] =  0x00;
            }else if(command.equals("sharp_left")){
                data[7] = (byte) (1 & 255);
                data[8] = 0x00;
            }else if(command.equals("sharp_right")){
                data[7] = (byte) (3 & 255);
                data[8] = 0x00;
            }else if(command.equals("slightly_left")){
                data[7] = (byte) (7 & 255);
                data[8] = 0x00;
            }else if(command.equals("slightly_right")){
                data[7] = (byte) (9 & 255);
                data[8] = 0x00;
            }else if(command.equals("u-turn")){
                data[7] = (byte) (2 & 255);
            }else if(command.equals("head")){
                data[7] = (byte) (8 & 255);
                data[8] = 0x00;
            }else if(command.equals("roundabout_1")){
                data[7] = (byte) (5 & 255);
                data[8] = (byte) (1 & 255);
            }else if(command.equals("roundabout_2")){
                data[7] = (byte) (5 & 255);
                data[8] = (byte) (2 & 255);
            }else if(command.equals("roundabout_3")){
                data[7] = (byte) (5 & 255);
                data[8] = (byte) (3 & 255);
            }else if(command.equals("roundabout_4")){
                data[7] = (byte) (5 & 255);
                data[8] = (byte) (4 & 255);
            }else if(command.equals("keep_left")){
                data[7] = (byte) (7 & 255);
                data[8] = (byte) (5 & 255);
            }else if(command.equals("keep_right")){
                data[7] = (byte) (9 & 255);
                data[8] = (byte) (6 & 255);
            }else if(command.equals("S")){          //Navi ends
                data[7] = 0x00;
                data[8] = (byte) (255 & 255);
            }else{
                data[7] = 0x00;
                data[8] = 0x00;
            }

            data[9] = 0x00;

            TSDZBTService.mService.writeCommand(data);
            // perform action here.
        }
    }
}