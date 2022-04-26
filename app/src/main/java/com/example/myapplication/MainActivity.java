package com.example.myapplication;

import static com.example.myapplication.receiver.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.receiver.NetworkStateChangeReceiver;

public class MainActivity extends AppCompatActivity {
    private Button stringGenerateButton;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        textView = (TextView) findViewById(R.id.textView);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {



                // Perintah Intent Explicit pindah halaman ke activity_detail
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(intent);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Perintah Intent Explicit pindah halaman ke activity_detail
                startActivity(new Intent(MainActivity.this, MainActivity4.class));
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Perintah Intent Explicit pindah halaman ke activity_detail
                startActivity(new Intent(MainActivity.this, MainActivity11.class));
            }
        });

        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE,false);
                String networkStatus = isNetworkAvailable ? "connected":"disconnected";


                Toast.makeText(MainActivity.this, "Network Status" + networkStatus, Toast.LENGTH_SHORT).show();

            }
        }, intentFilter);

    }


   }