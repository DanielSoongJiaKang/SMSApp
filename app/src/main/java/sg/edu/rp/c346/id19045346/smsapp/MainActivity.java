package sg.edu.rp.c346.id19045346.smsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etnumber;
    EditText etcontent;
    private BroadcastReceiver br;
    Button btnsend;
    Button btnviamessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        btnsend = findViewById(R.id.buttonSend);
        etcontent = findViewById(R.id.editTextcontent);
        etnumber = findViewById(R.id.editTextNumber);
        btnviamessage = findViewById(R.id.buttonviamessage);
        br = new MessageReceiver();


        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] multiple = etnumber.getText().toString().split(" ,");
                for(int i = 0; i<multiple.length; i++) {
                    if(etnumber.getText().toString().isEmpty() || etcontent.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please input something", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(multiple[i], null, etcontent.getText().toString(), null, null);
                    }
                }

                }
        });

        btnviamessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etnumber.getText().toString().isEmpty() || etcontent.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill up blank spaces", Toast.LENGTH_SHORT).show();
                }
                else {
                  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",etnumber.getText().toString(),null));
                  intent.putExtra("sms_body",etcontent.getText().toString());
                  startActivity(intent);
                }
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br,filter);


    }

    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }

    }
}
