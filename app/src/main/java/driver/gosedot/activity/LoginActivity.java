package driver.gosedot.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import driver.gosedot.Kelas.SharedVariable;
import driver.gosedot.Kelas.UserPreference;
import driver.gosedot.R;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    TextView tvDaftar;
    EditText etEmail,etPassword;
    private SweetAlertDialog pDialogLoading;
    DialogInterface.OnClickListener listener;
    private HttpResponse response;
    UserPreference mUserpref;
    private String iduser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        //tvDaftar = findViewById(R.id.tvDaftar);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        mUserpref = new UserPreference(this);

        pDialogLoading = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading");
        pDialogLoading.setCancelable(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DaftarActivity.class);
                startActivity(intent);
            }
        });


        if (mUserpref.getIsLoggedIn() != null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("loadFragment","home");
            startActivity(intent);
            finish();
        }

    }

    private void checkValidation() {
        String getEmailId = etEmail.getText().toString();
        String getPassword = etPassword.getText().toString();

        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Semua Field Harus diisi")
                    .show();
        }
        else {
            pDialogLoading.show();
            doLogin(getEmailId,getPassword);
        }

    }


    private void doLogin(final String getEmailId, final String getPassword){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("email", getEmailId));
                nameValuePairs.add(new BasicNameValuePair("password", getPassword));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            SharedVariable.serverURL+"driver/login");
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

                        //populate the adapter
                        for (int d=0;d<jsonArray2.length();d++) {
                            JSONObject jojo = jsonArray2.getJSONObject(d);
                            Log.d("arrayLogin:", "" + jojo.toString());

                            iduser = jojo.getString("iddriver");

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mUserpref.setIsLoggedIn("true");
                    mUserpref.setIdUser(iduser);

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Terjadi kesalahan, coba lagi nanti")
                            .setTitleText("Gagal Login")
                            .show();
                }

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(getEmailId,getPassword);
    }
}
