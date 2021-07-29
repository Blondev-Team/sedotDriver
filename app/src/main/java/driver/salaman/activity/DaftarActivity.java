package driver.salaman.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import driver.salaman.Kelas.SharedVariable;
import driver.salaman.R;

public class DaftarActivity extends AppCompatActivity {

    EditText etNama,etEmail,etPassword,etPhone,etPassword2;
    Spinner spKecamatan;
    Button btnDaftar;
    private SweetAlertDialog pDialogLoading;
    DialogInterface.OnClickListener listener;
    private HttpResponse response;
    List<String> listIdKecamatan =  new ArrayList<String>();
    List<String> listNamaKecamatan =  new ArrayList<String>();
    ArrayAdapter<String> adapterKecamatan;
    private String idSelectedKecamatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        etNama = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etPassword2 = findViewById(R.id.etPassword2);
        btnDaftar = findViewById(R.id.btnDaftar);
        spKecamatan = findViewById(R.id.spKecamatan);

        pDialogLoading = new SweetAlertDialog(DaftarActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading");
        pDialogLoading.setCancelable(false);

        adapterKecamatan = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, listNamaKecamatan);
        adapterKecamatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKecamatan.setAdapter(adapterKecamatan);

        spKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    String idkecamtan    = listIdKecamatan.get(position).toString();
                    String namakecamatan        = listNamaKecamatan.get(spKecamatan.getSelectedItemPosition());
                    Log.d("idkecamtan:",""+idkecamtan);
                    Log.d("namakecamatan:",namakecamatan);

                    idSelectedKecamatan = idkecamtan;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        getDataKecamatan();
    }

    private void getDataKecamatan(){
        String url = SharedVariable.serverURL+"master/kecamatan";
        listIdKecamatan.clear();
        listNamaKecamatan.clear();
        adapterKecamatan.notifyDataSetChanged();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialogLoading.dismiss();
                try {

                    JSONArray jsonArray2 = new JSONArray(response);
                    Log.d("jmlKecamatan",""+jsonArray2.length());


                    for (int d=0;d<jsonArray2.length();d++){
                        JSONObject jojo = jsonArray2.getJSONObject(d);
                        Log.d("arrayKecamtan:",""+jojo.toString());

                        String id_kecamatan = jojo.getString("idkecamatan");
                        String nama_kecamtan = jojo.getString("nmkecamatan");

                        listIdKecamatan.add(id_kecamatan);
                        listNamaKecamatan.add(nama_kecamtan);
                    }
                    adapterKecamatan.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialogLoading.dismiss();
                Log.d("gagalKecamtan:","eror "+error.getMessage().toString());
                Toast.makeText(getApplicationContext(),"Terjadi kesalahan, coba lagi nanti",Toast.LENGTH_SHORT).show();
            }
        }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void checkValidation() {
        String getEmailId = etEmail.getText().toString();
        String getPassword = etPassword.getText().toString();
        String getPassword2 = etPassword2.getText().toString();
        String getNama = etNama.getText().toString();
        String getPhone = etPhone.getText().toString();

        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getPassword2.equals("") || getPassword2.length() == 0
                || getNama.equals("") || getNama.length() == 0
                || getPhone.equals("") || getPhone.length() == 0
            ) {

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Semua Field Harus diisi")
                    .show();
        }else if (!getPassword.equals(getPassword2)){
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Konfirmasi password tidak valid")
                    .show();
        }
        else {
            pDialogLoading.show();
            register(getEmailId,getPassword,getNama,getPhone,idSelectedKecamatan);
        }

    }

    private void register(final String email, final String passwordUser, final String nama, final String phone,final String idSelectedKecamatan){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {


            @Override
            protected String doInBackground(String... strings) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("password", passwordUser));
                nameValuePairs.add(new BasicNameValuePair("nmuser", nama));
                nameValuePairs.add(new BasicNameValuePair("nohp", phone));
                nameValuePairs.add(new BasicNameValuePair("idkecamatan", idSelectedKecamatan));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            SharedVariable.serverURL+"user/register");
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

                    new SweetAlertDialog(DaftarActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                            .setContentText("Pendaftaran berhasil, silakan Login")
                            .show();
                    clearInput();

                }else {
                    new SweetAlertDialog(DaftarActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Terjadi kesalahan, coba lagi nanti")
                            .setTitleText("Daftar Gagal")
                            .show();
                }

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(email,passwordUser,nama,phone,idSelectedKecamatan);
    }

    private void clearInput(){
        etNama.setText("");
        etPassword.setText("");
        etPassword2.setText("");
        etEmail.setText("");
        etPhone.setText("");
    }
}
