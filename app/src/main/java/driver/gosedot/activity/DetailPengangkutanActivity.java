package driver.gosedot.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import driver.gosedot.Kelas.DetailAngkut;
import driver.gosedot.Kelas.SharedVariable;
import driver.gosedot.Kelas.UserPreference;
import driver.gosedot.R;
import driver.gosedot.adapter.AdapterDetailAngkut;

public class DetailPengangkutanActivity extends AppCompatActivity {

    ImageView btnBack;
    Button btnSelesai,btnTutup,btnKeSelesai,btnNavigasi;
    TextView tvAlamat,tvJenis,tvHarga,tvNIK,tvPetugas;
    private String alamatKirim;
    BottomSheetBehavior sheetBehavior;
    LinearLayout bottomSheetLayout;

    UserPreference mUserPref;
    RecyclerView rvAngkut;
    AdapterDetailAngkut adapter;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    private HttpResponse response;
    private List<DetailAngkut> angkutList;
    private RequestQueue requestQueue;
    String latTujuan,longTujuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengangkutan);
        mUserPref = new UserPreference(DetailPengangkutanActivity.this);
        final Intent intent = getIntent();
        //Toast.makeText(getApplication(),"Data Angkut "+intent.getStringExtra("kodeangkut"),Toast.LENGTH_LONG).show();
        btnBack = findViewById(R.id.btnBack);
        btnTutup = findViewById(R.id.btnTutup);
        btnSelesai = findViewById(R.id.btnSelesai);
        btnKeSelesai = findViewById(R.id.btnKeSelesai);
        btnNavigasi = findViewById(R.id.btnNavigasi);
        //rvAngkut = findViewById(R.id.rvPemesanan);
        tvHarga = findViewById(R.id.tvHarga);
        tvJenis = findViewById(R.id.tvNama);
        tvNIK = findViewById(R.id.tvNIK);
        tvPetugas = findViewById(R.id.tvPetugas);

        bottomSheetLayout = findViewById(R.id.bottomSheetLayout);
        sheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        pDialogLoading = new SweetAlertDialog(DetailPengangkutanActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        angkutList = new ArrayList<>();
        adapter     = new AdapterDetailAngkut(getApplication(),angkutList);
        /*rvAngkut.setLayoutManager(new LinearLayoutManager(getApplication()));
        rvAngkut.setHasFixedSize(true);
        rvAngkut.setItemAnimator(new DefaultItemAnimator());
        rvAngkut.setAdapter(adapter);
*/
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnKeSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        btnTutup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //tvAlamat.setVisibility(View.GONE);

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        btnNavigasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+latTujuan+","+longTujuan));
                startActivity(intent);
            }
        });


        getAngkutan(mUserPref.getIdUser(),intent.getStringExtra("kodeangkut"));

        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAngkut(intent.getStringExtra("kodeangkut"));
            }
        });

    }


    public void getAngkutan(final String iddriver, final String kodeangkut){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("kodepengangkutan",kodeangkut));
                nameValuePairs.add(new BasicNameValuePair("iddriver", iddriver));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            SharedVariable.serverURL+"pengangkutan/selectPesananAngkut");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    Log.d("errorFilter:",e.getMessage());

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("errorFilter:",e.getMessage());
                }

                //look at this
                return "success";
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                pDialogLoading.dismiss();
                int statusCode = response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();


                String responData = null;
                try {

                    responData = EntityUtils.toString(entity);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("responRest:",""+statusCode);
                Log.d("responData:",""+responData);


                if (statusCode == 200){
                    JSONArray jsonArray2 = null;
                    try {
                        jsonArray2 = new JSONArray(responData);
                        Log.d("jmlFiltered",""+jsonArray2.length());

                        angkutList.clear();

                        for (int d=0;d<jsonArray2.length();d++){
                            JSONObject jojo = jsonArray2.getJSONObject(d);
                            Log.d("arrayAngkut:",""+jojo.toString());

                            String idpesanan = jojo.getString("idpesanan");
                            String kodepesanan = jojo.getString("kodepesanan");
                            String nmuser = jojo.getString("nmuser");
                            String nmjenis = jojo.getString("nmjenis");
                            String latitude = jojo.getString("latitude");
                            String longitude = jojo.getString("longitude");
                            String alamat = jojo.getString("alamat");
                            String biaya = jojo.getString("biaya");
                            String paket = jojo.getString("paket");
                            String metodebayar = jojo.getString("metodebayar");
                            String petugas = jojo.getString("petugas");
                            String status_angkut = jojo.getString("status_angkut");

                            tvHarga.setText(biaya);
                            tvJenis.setText(nmjenis);
                            tvNIK.setText(nmuser);
                            tvPetugas.setText(petugas);

                            latTujuan = latitude;
                            longTujuan = longitude;

                            if (status_angkut.equals("Selesai")){
                                disableTombolSelesai();
                            }


                            DetailAngkut angkut = new DetailAngkut(
                              idpesanan,
                              kodepesanan,
                              nmuser,
                              nmjenis,
                              latitude,
                              longitude,
                              alamat,
                              biaya,
                              paket,
                              metodebayar,
                              petugas
                            );

                            angkutList.add(angkut);
                        }
                        adapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else {
                    new SweetAlertDialog(DetailPengangkutanActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Terjadi kesalahan, coba lagi nanti")
                            .setTitleText("Gagal")
                            .show();
                }

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(iddriver);
    }


    private void finishAngkut(final String kodeangkut){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {


            @Override
            protected String doInBackground(String... strings) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("kodepengangkutan", kodeangkut));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            SharedVariable.serverURL+"pengangkutan/finishAngkut");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    Log.d("errorLogin:",e.getMessage());

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("errorLogin:",e.getMessage());
                }


                //look at this
                return "success";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                pDialogLoading.dismiss();
                int statusCode = response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();


                String responData = null;
                try {

                    responData = EntityUtils.toString(entity);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("responRest:",""+statusCode);
                Log.d("responData:",""+responData);

                if (statusCode == 200){
                    responData =  responData.replace("\"", "");
                    Log.d("responData:",""+responData);

                    new SweetAlertDialog(DetailPengangkutanActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                            .setContentText("Angkutan Diselesaikan")
                            .show();
                    disableTombolSelesai();

                }else {
                    new SweetAlertDialog(DetailPengangkutanActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Terjadi kesalahan, coba lagi nanti")
                            .setTitleText("Proses Gagal")
                            .show();
                }

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(kodeangkut);
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(DetailPengangkutanActivity.this);
        return requestQueue;
    }

    void disableTombolSelesai(){
        btnKeSelesai.setText("Angkutan Selesai");
        btnKeSelesai.setEnabled(false);

        btnSelesai.setText("Angkutan Selesai");
        btnSelesai.setEnabled(false);
    }


    public void addToRequestQueue(Request request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }


}
