package translet.transletapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserAccountActivity extends AppCompatActivity {

    private static final int REQUEST_LOGIN = 0;
    private static final int REQUEST_INVITE = 1;
    private static final int REQUEST_SESSION = 2;
    private static final String TAG = "UserAccountActivity";
    private static String mUsername;
    private static String mSessionid;
    private TextView mSessionInfo;
    private Button mStartSession;
    private Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        TransLetApp app = (TransLetApp)getApplication();
        mSocket = app.getSocket();

        mSessionInfo = (TextView)findViewById(R.id.tvSessionInfo);
        mSessionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join_session();
            }
        });

        mStartSession = (Button)findViewById(R.id.btnCreateSession);
        mStartSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_new_session();
            }
        });
        mSocket.on(Constants.EVENT_SESSION_INVITE, onSessionInvite);
        mSocket.on(Constants.EVENT_CREATE_SESSION, onSessionCreated);
        startSignIn();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off(Constants.EVENT_SESSION_INVITE, onSessionInvite);
        mSocket.off(Constants.EVENT_CREATE_SESSION, onSessionCreated);
    }

    private void start_new_session() {
        startInviteActivity();
    }

    private void join_session() {
        JSONObject jsonOut = new JSONObject();
        try {
            jsonOut.put("uid", mUsername);
            jsonOut.put("sessionid", mSessionid);
            mSocket.emit(Constants.EVENT_JOIN_SESSION, jsonOut);
            startMainActivity();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startInviteActivity() {
        Intent intent = new Intent(getApplicationContext(), InviteActivity.class);
        intent.putExtra("uid", mUsername);
        startActivityForResult(intent, REQUEST_INVITE);
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("uid", mUsername);
        intent.putExtra("sessionid", mSessionid);
        startActivityForResult(intent, REQUEST_SESSION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK != resultCode) {
            finish();
            return;
        }
        //String activityTag = data.getStringExtra(Constants.ACTIVITY_TAG);
        switch(requestCode) {
            case REQUEST_LOGIN:
                mUsername = data.getStringExtra("uid");
                Log.d(TAG, new String("Received uid"+mUsername));
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.message_welcome)+mUsername, Toast.LENGTH_SHORT).show();
                TextView uid = (TextView)findViewById(R.id.tvuid);
                uid.setText(mUsername);
                uid.setVisibility(1);
                uid.setTextColor(R.color.colorPrimary);
                break;
            case REQUEST_SESSION:
                break;
            case REQUEST_INVITE:
                int userCount = data.getIntExtra("Users", 0);
                Toast.makeText(getApplicationContext(),
                        new String("Invite sent to "+String.valueOf(userCount)), Toast.LENGTH_LONG).show();

                break;
        }

    }

    private void startSignIn() {
        mUsername = null;
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    private Emitter.Listener onSessionInvite = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            UserAccountActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        mSessionid = data.getString("sessionid");
                        String message = data.getString("message");
                        Toast.makeText(getApplicationContext(),
                                message, Toast.LENGTH_LONG).show();
                        mSessionInfo.setText(message);
                        mSessionInfo.setTypeface(Typeface.DEFAULT_BOLD);
                        mSessionInfo.setBackgroundResource(R.color.colorAccent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onSessionCreated = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            UserAccountActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                     try {
                         mSessionid = data.getString("sessionid");
                         Toast.makeText(getApplicationContext(),
                                 new String("Session " + mSessionid + "created"), Toast.LENGTH_LONG).show();
                         startMainActivity();
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }
            });
        }
    };

}
