package spider65.ebike.tsdz2_sw102;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;




public class NotificationService extends NotificationListenerService {

    Context context;
    private String title_lastdata = "";
    private String title = "";
    private String text = "";

    Intent nav_msg_rcv = new Intent("nav_msg");

    @Override
    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }
    @Override

    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();

        if (pack.equals("net.osmand")) {
            Bundle extras = sbn.getNotification().extras;

            if (extras.containsKey("android.title")) {
                title = extras.getString("android.title");

                if(title.equals("Nawigacja") || title.equals("Navigation")){
                    return;
                }

              //  if(title.contains("Trzymaj") || title.contains("Keep") || title.contains("halten")){
              //      return;
               // }

                if (!title.equals(title_lastdata)){
                    title_lastdata = title;
                }else{
                    return;
                }

            }

            if (extras.containsKey("android.bigText")) {
                text = extras.getString("android.bigText");
            }

            String[] arrOfStr = title.split("•");
            String dist = arrOfStr[0].trim();
            String command = arrOfStr[1].trim();

            if (command.contains("lewo") || command.contains("left") || command.contains("links") || command.contains("lewej")){
                if (command.contains("ostro") || command.contains("sharply") || command.contains("scharf"))
                {
                    command = "sharp_left";
                }else if(command.contains("łagodnie") || command.contains("slightly") || command.contains("halb")){
                    command = "slightly_left";
                }else if(title.contains("Trzymaj") || title.contains("Keep") || title.contains("halten")){
                    command = "keep_left";
                }else {
                    command = "left";
                }
            }else if(command.contains("prawo") || command.contains("right") || command.contains("rechts") || command.contains("prawej")){
                if (command.contains("ostro") || command.contains("sharply") || command.contains("scharf"))
                {
                    command = "sharp_right";
                }else if(command.contains("łagodnie") || command.contains("slightly") || command.contains("halb")){
                    command = "slightly_right";
                }else if(title.contains("Trzymaj") || title.contains("Keep") || title.contains("halten")){
                    command = "keep_right";
                }else {
                    command = "right";
                }
            }else if(command.contains("zjazd") || command.contains("exit") || command.contains("Ausfahrt")){
                if(command.contains("1"))
                    command = "roundabout_1";
                if(command.contains("2"))
                    command = "roundabout_2";
                if(command.contains("3"))
                    command = "roundabout_3";
                if(command.contains("4"))
                    command = "roundabout_4";
            }else if(command.contains("Zawróć") || command.contains("U-turn") || command.contains("Wenden")){
            command = "u-turn";
            }else if(command.contains("Prosto") || command.contains("Head") || command.contains("Geradeaus")){
            command = "head";
            }else{
                command = "N";
            }

            arrOfStr = text.split("\n");

            try {
                arrOfStr = arrOfStr[1].split("•");
            }
            catch(ArrayIndexOutOfBoundsException exception) {
                arrOfStr = arrOfStr[0].split("•");
            }

            /*if(title.contains("Prosto") || title.contains("Head") || title.contains("Geradeaus")){
                arrOfStr = arrOfStr[0].split("•");
            }else if(title.contains("Zawróć") || title.contains("U-turn") || title.contains("Wenden")){
                arrOfStr = arrOfStr[0].split("•");
            }else {
                arrOfStr = arrOfStr[1].split("•");
            }*/

            String total_dist = arrOfStr[0].trim();

            Log.d("Package", pack);
            Log.d("distance", dist);
            Log.d("total distance", total_dist);
            Log.d("command", command);

            //Intent nav_msg_rcv = new Intent("nav_msg");

            nav_msg_rcv.putExtra("distance", dist);
            nav_msg_rcv.putExtra("total_distance", total_dist);
            nav_msg_rcv.putExtra("command", command);

            LocalBroadcastManager.getInstance(context).sendBroadcast(nav_msg_rcv);
        }

    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();

        if (pack.equals("net.osmand")) {
            Bundle extras = sbn.getNotification().extras;

            if (extras.containsKey("android.title")) {
                title = extras.getString("android.title");

            }

            nav_msg_rcv.putExtra("distance", "S");
            nav_msg_rcv.putExtra("total_distance", "S");
            nav_msg_rcv.putExtra("command", "S");

            LocalBroadcastManager.getInstance(context).sendBroadcast(nav_msg_rcv);

        }

    }
}
