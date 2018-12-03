package com.doovj.absensiunsil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AbsenActivity extends AppCompatActivity {

    EditText editTextNip, editTextNama, editTextTanggal, editTextJam, editTextLatitude, editTextLongitude;
    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);

        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);

        editTextNip = (EditText) findViewById(R.id.editTextNip);
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextTanggal = (EditText) findViewById(R.id.editTextTanggal);
        editTextJam = (EditText) findViewById(R.id.editTextJam);
        editTextLatitude = (EditText) findViewById(R.id.editTextLatitude);
        editTextLongitude = (EditText) findViewById(R.id.editTextLongitude);

        SimpleDateFormat formatjam = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formattanggal = new SimpleDateFormat("dd/MM/yyyy");

        Bundle b = getIntent().getExtras();
        Double latitud = b.getDouble("lat");
        Double longitud = b.getDouble("lon");

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        editTextNip.setText(String.valueOf(user.getNip()));
        editTextNama.setText(String.valueOf(user.getNama()));
        editTextTanggal.setText(formattanggal.format(new Date()));
        editTextJam.setText(formatjam.format(new Date()));
        editTextLatitude.setText(String.valueOf(latitud));
        editTextLongitude.setText(String.valueOf(longitud));

        findViewById(R.id.BtAbsen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button absen
                progressBar2.setVisibility(View.VISIBLE);
                Absen();
            }
        });

    }
    private void Absen() {
        final String nip = editTextNip.getText().toString().trim();
        final String nama = editTextNama.getText().toString().trim();
        final String tanggal = editTextTanggal.getText().toString().trim();
        final String jam = editTextJam.getText().toString().trim();
        final String latitude = editTextLatitude.getText().toString().trim();
        final String longitude = editTextLongitude.getText().toString().trim();

        //first we will do the validations

        if (TextUtils.isEmpty(nip)) {
            editTextNip.setError("tidak ada NIP");
            editTextNip.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nama)) {
            editTextNama.setError("Tidak ada Nama");
            editTextNama.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(tanggal)) {
            editTextTanggal.setError("Tidak ada Tanggal");
            editTextTanggal.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(jam)) {
            editTextJam.setError("Tidak ada Jam");
            editTextJam.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(latitude)) {
            editTextLatitude.setError("Tidak ada Latitude");
            editTextLatitude.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(longitude)) {
            editTextLongitude.setError("Tidak ada Longitude");
            editTextLongitude.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ABSEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject absenJson = obj.getJSONObject("absen");

                                //creating a new user object
                                Absen absen = new Absen(
                                        absenJson.getInt("id"),
                                        absenJson.getString("nip"),
                                        absenJson.getString("nama"),
                                        absenJson.getString("tanggal"),
                                        absenJson.getString("jam"),
                                        absenJson.getString("latitude"),
                                        absenJson.getString("longitude")
                                );

                                //starting the profile activity
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                progressBar2.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar2.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nip", nip);
                params.put("nama", nama);
                params.put("tanggal", tanggal);
                params.put("jam", jam);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
