package gachon.mp.livre_bottom_navigation.pushNoti;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.Timestamp;

import org.json.JSONObject;



import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendNotification {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static void sendNotification(String regToken, String title, String messsage){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... parms) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Log.d("Send Notification", "send notification");
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();

                    dataJson.put("body", messsage);
                    dataJson.put("title", title);
                    json.put("notification", dataJson);
                    json.put("to", regToken);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + "AAAALoHT6dc:APA91bHeEnUdqM-imWPv2vUZDhJJfO8kzj_uFWMRcJIZQN9qEv45YbCFiCnOCsTJGp0OYxqPrZymS9WjSZznAzWfVZ0jS2Fyyz6o12wtcOZAZhXgT50ON1Ec74b5VufZVndp16PgGhKC")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();

                }catch (Exception e){
                    Log.d("error", e+"");
                }
                return  null;
            }
        }.execute();
    }
}
