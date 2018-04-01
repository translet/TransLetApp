package translet.transletapp;

import android.app.Application;
import android.util.Log;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class TransLetApp extends Application {
    private Socket mSocket;
    private String TAG = "application.TransLetApp";
    @Override
    public void onCreate() {
        try {
            mSocket = IO.socket(Constants.SERVER_URL);
            Log.d(TAG, "connecting to"+Constants.SERVER_URL);
            mSocket.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
            return mSocket;
        }
}
