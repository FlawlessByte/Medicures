package co.realinventor.medicures.AdminMod;

import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.Common.FeedbackManager;
import co.realinventor.medicures.Common.NotificationManager;
import co.realinventor.medicures.R;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class AdminReplyActivity extends AppCompatActivity {
    private EditText editTextReply;
    private DatabaseReference ref;
    private String senderName, uid, to, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reply);

        editTextReply = findViewById(R.id.editTextReply);

        to = getIntent().getStringExtra("to");
        uid = "admin@medicure";
        senderName = "Admin";


    }

    public void replySendButtonPressed(View view){
        String msg = editTextReply.getText().toString();

        if(msg.equals("")){
            Toast.makeText(this, "Empty Reply!", Toast.LENGTH_SHORT).show();
        }
        else{

            new NotificationManager().makeNotification(msg,to,uid,senderName);
            Toast.makeText(this, "Your reply has been sent to user!", Toast.LENGTH_LONG).show();
            finish();

        }
    }
}
