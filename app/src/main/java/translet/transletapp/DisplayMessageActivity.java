package translet.transletapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Intent intent = getIntent();
        String uid = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.textUID);
        textView.setText(uid);
    }
}
