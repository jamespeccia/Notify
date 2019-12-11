package com.jamespeccia.notify;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NotificationService extends NotificationListenerService {
    private static OkHttpClient okHttpClient;

    private PackageManager packageManager;
    private SharedPreferences selectedApplications, serverInformation;

    @Override
    public void onCreate() {
        super.onCreate();
        selectedApplications = getSharedPreferences("selected_applications", MODE_PRIVATE);
        serverInformation = getSharedPreferences("server_information", MODE_PRIVATE);
        packageManager = getApplicationContext().getPackageManager();
        okHttpClient = new OkHttpClient();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        String packageName = statusBarNotification.getPackageName();

        String applicationLabel = null;
        //Tries to get the name of the application from the package name
        try {
            applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //Checks if notification is from a selected application
        if (applicationLabel != null && selectedApplications.getBoolean(applicationLabel, false)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("label", applicationLabel);
                jsonObject.put("title", statusBarNotification.getNotification().extras.getCharSequence("android.title").toString());
                jsonObject.put("text", statusBarNotification.getNotification().extras.getCharSequence("android.text").toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String url = "http://" + serverInformation.getString("host", "localhost") + ":" + serverInformation.getInt("port", 80);
            post(url, jsonObject.toString());
        }
        super.onNotificationPosted(statusBarNotification);
    }

    /**
     * Builds a POST request containing the details of the notification to the saved server
     *
     * @param url      url of the server
     * @param jsonText String representation of the JSON that contains details about the notification
     */
    private void post(String url, String jsonText) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonText);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        //Creates a new AsyncTask to send the POST request to the server
        new AsyncPost().execute(request);
    }

    /**
     * Sends POST requests asynchronously after a request has been built
     */
    private static class AsyncPost extends AsyncTask<Request, Void, Void> {
        @Override
        protected Void doInBackground(Request... requests) {
            try {
                okHttpClient.newCall(requests[0]).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

