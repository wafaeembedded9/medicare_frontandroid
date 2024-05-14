package com.ouafaa.medicare_connect;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;





public class surveillanceActivity2 extends AppCompatActivity {
    private static Button  save_flow;
    private Spinner spflowunit;
    private String[] flowunit = {

            "K","C"
    };

    TextView txvalue,textViewHR,textViewSPO2;
    private EditText ip;

    private Switch switchButton;
    public static String ip_address;
    Handler handler = new Handler();
    boolean statusdevice = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveillance2);
        ip=(EditText) findViewById(R.id.Adresseip);
        switchButton = findViewById(R.id.switch1);
        ImageView imageView20 = findViewById(R.id.imageView20);
        ImageView imageView21 = findViewById(R.id.imageView21);
        imageView20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(surveillanceActivity2.this, QstAnswer.class);
                startActivity(intent);
            }});
        imageView21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(surveillanceActivity2.this, Poids.class);
                startActivity(intent);
            }});

        // Ajouter un écouteur de changement d'état au bouton de commutation
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Le bouton est activé, récupérer l'adresse IP
                    ip_address = ip.getText().toString();
                } else {
                    // Le bouton est désactivé, annuler la récupération
                    txvalue.setText("Températur: 0");
                }
            }
        });

        spflowunit = (Spinner) findViewById(R.id.spinner);
        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, flowunit);

        // mengeset Array Adapter tersebut ke Spinner
        spflowunit.setAdapter(adapter);
        save_flow = (Button) findViewById(R.id.save_flow);

        save_flow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView txunit = findViewById(R.id.tx_unit);

                String setflowunit = String.valueOf(spflowunit.getSelectedItem());

                txunit.setText(setflowunit);

            }
        });



        txvalue = (TextView ) findViewById(R.id.tx_value);
        textViewHR = findViewById(R.id.textView19);
        textViewSPO2 = findViewById(R.id.textView20);
        handler.postDelayed(status_data,0);


    }

    private Runnable status_data = new Runnable() {
        @Override
        public void run() {
            if (statusdevice) {
                request_to_url("");
                handler.postDelayed(this, 2000);
                Log.d("Status", "Connectivity_esp32");
            }else {
                handler.removeCallbacks(status_data);
                Log.d("Status","Finalizado");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        statusdevice = false;
    }

    public void request_to_url (String command) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {

            new request_data().execute("http://" + ip_address + "/" + command);

        }else {
            Toast.makeText(surveillanceActivity2.this, "Not connected  ", Toast.LENGTH_LONG).show();

        }
    }

    private class request_data extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            return Connectivité.geturl(url[0]);

        }

        @Override
        protected void onPostExecute(String result_data) {
            String[] values = result_data.split(",");
            if (values.length >= 3) {
                txvalue.setText("Température: " + values[0]);
                textViewHR.setText("Rythme cardiaque: " + values[1]);
                textViewSPO2.setText("SpO2: " + values[2]);

            }else{

                txvalue.setText("Température: 0");
                textViewHR.setText("Rythme cardiaque: 0" );
                textViewSPO2.setText("SpO2: 0");
            }
        }
    }


}