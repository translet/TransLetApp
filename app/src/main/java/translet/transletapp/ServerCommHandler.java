package translet.transletapp;

import android.net.Uri;
import android.util.Log;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ServerCommHandler {

    private String accessURL = null;
    private String TAG = "ServerCommHandler";

    public ServerCommHandler(String host, String port) {
        this.accessURL = new String(host + ":" + port);
    }

    public ServerCommHandler() {
        this.accessURL = Constants.SERVER_URL;
    }


    protected String auth(String uname, String password) {
        HttpURLConnection conn = null;

        String ln;
        StringBuffer buf = new StringBuffer();
        BufferedWriter w = null;
        OutputStream os = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("email", uname);
            params.put("password", password);

            Uri.Builder b = new Uri.Builder();
            b.appendQueryParameter("email", uname)
                    .appendQueryParameter("password", password);
            String q = b.build().toString();
            URL url = new URL(this.accessURL + q);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "text/plain");
            os = conn.getOutputStream();
            w = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            w.write(q);
            w.flush();
            w.close();
            os.close();
            int respCode = conn.getResponseCode();
            if (respCode == 200) {
                InputStreamReader isreader = new InputStreamReader(conn.getInputStream());
                BufferedReader bReader = new BufferedReader(isreader);
                while ((ln = bReader.readLine()) != null) {
                    buf.append(ln);
                }
            } else {
                Log.e(this.TAG,"Invalid resp(code:"+ respCode+")");
                buf.append("Auth failed");
            }
        } catch (MalformedURLException e) {
            Log.e(this.TAG, e.getMessage());
            return "Auth failed";
        } catch (IOException e) {
            Log.e(this.TAG, e.getMessage());
            return "Auth failed";
        }

        return buf.toString();
    }
}
