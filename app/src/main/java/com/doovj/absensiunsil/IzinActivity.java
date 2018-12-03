package com.doovj.absensiunsil;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.Map;

import static com.doovj.absensiunsil.R.id.progressBar2;

public class IzinActivity extends AppCompatActivity implements View.OnClickListener{

    EditText EtNip, EtNama, EtTanggalmulai, EtTanggalberakhir, EtKeterangan;
    Button BtIzin;
    ProgressBar loadingizin;
    DatePickerDialog DatePickerDialogmulai;
    DatePickerDialog DatePickerDialogberakhir;
    SimpleDateFormat simpledate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin);

        simpledate = new SimpleDateFormat("dd/MM/yyyy");

        findViewsById();

        setDateTimeField();

        loadingizin = (ProgressBar) findViewById(R.id.loadingizin);
        loadingizin.setVisibility(View.GONE);

        EtNip = (EditText) findViewById(R.id.etNip);
        EtNama = (EditText) findViewById(R.id.etNama);
        EtKeterangan = (EditText) findViewById(R.id.etKeterangan);
        BtIzin = (Button) findViewById(R.id.BtIzin);

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        EtNip.setText(String.valueOf(user.getNip()));
        EtNama.setText(String.valueOf(user.getNama()));

        findViewById(R.id.BtIzin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button absen
                loadingizin.setVisibility(View.VISIBLE);
                Izin();
            }
        });

    }

    private void findViewsById() {
        EtTanggalmulai = (EditText) findViewById(R.id.etTanggalmulai);
        EtTanggalmulai.setInputType(InputType.TYPE_NULL);
        EtTanggalmulai.requestFocus();

        EtTanggalberakhir = (EditText) findViewById(R.id.etTanggalberakhir);
        EtTanggalberakhir.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        EtTanggalmulai.setOnClickListener(this);
        EtTanggalberakhir.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialogmulai = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                EtTanggalmulai.setText(simpledate.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        DatePickerDialogberakhir = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                EtTanggalberakhir.setText(simpledate.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {
        if(view == EtTanggalmulai) {
            DatePickerDialogmulai.show();
        } else if(view == EtTanggalberakhir) {
            DatePickerDialogberakhir.show();
        }
    }

    private void Izin() {
        final String nip = EtNip.getText().toString().trim();
        final String nama = EtNama.getText().toString().trim();
        final String tanggal_mulai = EtTanggalmulai.getText().toString().trim();
        final String tanggal_berakhir = EtTanggalberakhir.getText().toString().trim();
        final String keterangan = EtKeterangan.getText().toString().trim();

        //first we will do the validations

        if (TextUtils.isEmpty(nip)) {
            EtNip.setError("tidak ada NIP");
            EtNip.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nama)) {
            EtNama.setError("Tidak ada Nama");
            EtNama.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(tanggal_mulai)) {
            EtTanggalmulai.setError("Tidak ada Tanggal mulai");
            EtTanggalmulai.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(tanggal_berakhir)) {
            EtTanggalberakhir.setError("Tidak ada Tanggal berakhir");
            EtTanggalberakhir.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(keterangan)) {
            EtKeterangan.setError("Tidak ada Keterangan");
            EtKeterangan.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_IZIN,
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
                                JSONObject izinJson = obj.getJSONObject("izin");

                                //creating a new user object
                                Izin izin = new Izin(
                                        izinJson.getInt("id"),
                                        izinJson.getString("nip"),
                                        izinJson.getString("nama"),
                                        izinJson.getString("tanggal_mulai"),
                                        izinJson.getString("tanggal_berakhir"),
                                        izinJson.getString("keterangan")
                                );

                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                loadingizin.setVisibility(View.GONE);
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
                        loadingizin.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nip", nip);
                params.put("nama", nama);
                params.put("tanggal_mulai", tanggal_mulai);
                params.put("tanggal_berakhir", tanggal_berakhir);
                params.put("keterangan", keterangan);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
