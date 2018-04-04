package translet.transletapp;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import io.socket.client.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InviteActivity extends AppCompatActivity {

    private static String TAG = "Activity.Invite";
    private List<String> mEmails = new ArrayList<String>();
    private EditText mEmail;
    private ArrayAdapter<String> mInviteAdapter;
    private ListView mEmailsListView;
    private Socket mSocket;

    private String mUid;
    private String mSessionid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        Intent intent = getIntent();

        mUid = intent.getStringExtra("uid");

        Button btnAdd = (Button)findViewById(R.id.btnAdd);
        Button btnInvite = (Button)findViewById(R.id.btnInvite);

        TransLetApp app = (TransLetApp)this.getApplication();
        mSocket = app.getSocket();
        mSocket.connect();

        mEmail = (EditText) findViewById(R.id.editEmail);
        mEmailsListView = (ListView)findViewById(R.id.listEmails);

         mInviteAdapter = new ArrayAdapter<String>(this, R.layout.item_email);

        mEmailsListView.setAdapter(mInviteAdapter);

        /*
        mEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.editEmail || id == EditorInfo.IME_NULL) {
                    addToList();
                    return true;
                }
                return false;
            }
        }); */

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Add click encountered");
                addToList();
            }
        });
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Invite click received");
                inviteAttendees();
            }
        });

    }

    private void inviteAttendees() {
        JSONObject data = new JSONObject();
        JSONArray emails = new JSONArray(mEmails);

        try {
            Intent intent = new Intent();
            data.put("uid", mUid);
            data.put("invite", emails);
            mSocket.emit(Constants.EVENT_CREATE_SESSION, data);
            intent.putExtra("Users", mEmails.size());
            setResult(RESULT_OK, intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finish();
    }

    private void addToList() {
        String email = mEmail.getText().toString();
        mEmail.setText("");
        Log.d(TAG, "field:"+email);
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
        } else if (!email.contains("@")) {
            mEmail.setError(getString(R.string.error_invalid_email));

        } else {
            mEmails.add(email);
            Log.d(TAG, mEmails.toString());
            mInviteAdapter.add(email);
            mInviteAdapter.notifyDataSetChanged();
        }
        mEmail.requestFocus();
        //mInviteAdapter.add(email);
    }
}
