package translet.transletapp;

import android.net.Uri;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ServerCommHandler {
    protected String buildAuthURL(String uname, String password) {
        HttpURLConnection conn = null;

        String ln;
        StringBuffer buf = new StringBuffer();
        BufferedWriter w = null;
        OutputStream os = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("email", uname);
            params.put("password", password);
            String accessURL = "http://192.168.5.22:9090/auth/";

            Uri.Builder b = new Uri.Builder();
            b.appendQueryParameter("email", uname)
                    .appendQueryParameter("password", password);
            String q = b.build().toString();
            URL url = new URL(accessURL + q);

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
                buf.append("Invalid");
            }
        } catch (MalformedURLException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }

        return buf.toString();
    }
}
