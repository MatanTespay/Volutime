package network.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Matan on 21/02/2017.
 */

public class NetworkConnector {
    private  final String BASE_URL = "http://192.168.14.79:8080/projSrv/";
    private List<NetworkResListener> listeners = Collections.synchronizedList(new ArrayList<NetworkResListener>());
    private Context ctx;
    private static NetworkConnector instance;

    public static final String GET_VOLUNTEERS_REQ = "8";
    public static final String GET_ORGNIZATIONS_REQ = "9";
    public static final String GET_VOLEVENTS_REQ = "10";
    public static final String REQ = "req";
    private final int RETRY_TIMES = 2;
    public static String CURRENT_REQ = "-1";
    public static final String RESOURCE_FAIL_TAG = "{\"result_code\":0}";
    public static final String RESOURCE_SUCCESS_TAG = "{\"result_code\":1}";

    private NetworkConnector()
    {
        super();
    }

    public static NetworkConnector getInstance(){
        if(instance==null){
            instance = new NetworkConnector();

        }
        return instance;
    }

    public void setContext(Context ctx){
        this.ctx = ctx;
    }

    public static void releaseInstance() {
        if (instance != null) {
            instance.clean();
            instance = null;
        }
    }

    private void clean() {
        listeners.clear();
    }

    public boolean unregisterListener(NetworkResListener listener){
        boolean result = false;
        if(listener!=null){
            if(listeners.contains(listener)){
                result= listeners.remove(listener);
            }
        }
        return result;
    }

    public void registerListener(NetworkResListener listener) {
        if(listener!=null){
            if(!listeners.contains(listener)){
                listeners.add(listener);
            }
        }
    }

    private void sendQuery(){
        NetworkTask networkTask = new NetworkTask();
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter(REQ , CURRENT_REQ);
        String query = builder.build().getEncodedQuery();
        networkTask.execute(query);
    }

    public void getVolunteers(){
        CURRENT_REQ = GET_VOLUNTEERS_REQ;
        sendQuery();
        /*NetworkTask networkTask = new NetworkTask();

        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter(REQ , GET_VOLUNTEERS_REQ);
        String query = builder.build().getEncodedQuery();

        networkTask.execute(query);*/
    }

    public void getOrganizations(){
        CURRENT_REQ = GET_ORGNIZATIONS_REQ;
        sendQuery();
    }
    public void getVolevents(){
        CURRENT_REQ = GET_VOLEVENTS_REQ;
        sendQuery();
    }

    private  void notifyPreUpdateListeners() {

        Handler handler = new Handler(ctx.getMainLooper());


        Runnable myRunnable = new Runnable() {

            @Override
            public void run() {
                try{
                    String resource = "";
                    if(CURRENT_REQ.equals("8"))
                        resource = "Volunteers";
                    else if(CURRENT_REQ.equals("9"))
                        resource = "organizations";
                    else if(CURRENT_REQ.equals("10"))
                        resource = "volevents";
                    for (NetworkResListener listener : listeners) {
                        listener.onPreUpdate(resource);
                    }
                }
                catch(Throwable t){
                    t.printStackTrace();
                }
            }
        };
        handler.post(myRunnable);

    }

    private  void notifyPostUpdateListeners(final byte[] res, final ResStatus status) {

        Handler handler = new Handler(ctx.getMainLooper());

        Runnable myRunnable = new Runnable() {

            @Override
            public void run() {
                try{
                    for (NetworkResListener listener : listeners) {
                        listener.onPostUpdate(res, status, CURRENT_REQ);
                    }
                }
                catch(Throwable t){
                    t.printStackTrace();
                }
            }
        };
        handler.post(myRunnable);

    }

    private byte[] getResFromServer(String query, int retry){

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = null;

        try {

            URL url = new URL(BASE_URL+"projres");


            int timeoutConnection = 10000;

            int timeoutSocket = 10000;


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeoutConnection);
            conn.setReadTimeout(timeoutSocket);

            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();

            in = new BufferedInputStream(conn.getInputStream());

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                byte[] buffer = new byte[4096];
                int n = -1;

                while ((n = in.read(buffer)) != -1) {
                    if (n > 0) {
                        out.write(buffer, 0, n);
                    }
                }
            }
            else{
                retry=0;
                return new byte[0];
            }


        } catch (Throwable e) {
            e.printStackTrace();
            if(retry==0){
                return new byte[0];
            }
            return getResFromServer(query, retry - 1);
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (Throwable e) {}
        }
        return   out.toByteArray();

    }

    private class NetworkTask extends AsyncTask<String, Void, byte[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notifyPreUpdateListeners();
        }

        @Override
        protected byte[] doInBackground(String... params) {

            byte[] res= getResFromServer(params[0],RETRY_TIMES);
            return res;

        }

        @Override
        protected void onPostExecute(byte[] res) {

            super.onPostExecute(res);

            if(res!=null && res.length>0){
                String resp = new String(res);
                if(!resp.equals(RESOURCE_FAIL_TAG)) {
                    notifyPostUpdateListeners(res, ResStatus.SUCCESS);
                }
            }
            else{
                notifyPostUpdateListeners(res, ResStatus.FAIL);
            }

        }

    }
}
