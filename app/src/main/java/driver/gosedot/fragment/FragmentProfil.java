package driver.gosedot.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
import driver.gosedot.activity.LoginActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfil extends Fragment {


    public FragmentProfil() {
        // Required empty public constructor
    }

    LinearLayout llKeluar;
    UserPreference mUserpref;
    TextView tvName,tvEmail,tvPhone;
    private HttpResponse response;
    private SweetAlertDialog pDialogLoading;
    ImageView ivProfPict;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_profil, container, false);

        llKeluar = view.findViewById(R.id.llKeluar);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        ivProfPict = view.findViewById(R.id.ivProfPict);

        mUserpref = new UserPreference(getActivity());

        pDialogLoading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading");
        pDialogLoading.setCancelable(false);

        llKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserpref.setIsLoggedIn(null);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        getDriver(mUserpref.getIdUser());
        return view;
    }

    public void getDriver(final String iddriver){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("iddriver", iddriver));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            SharedVariable.serverURL+"driver/selectDriver");
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

                        for (int d=0;d<jsonArray2.length();d++){
                            JSONObject jojo = jsonArray2.getJSONObject(d);
                            Log.d("arrayAngkut:",""+jojo.toString());

                            String  nmuser = jojo.getString("nmdriver");
                            String  email = jojo.getString("email");
                            String  nohp = jojo.getString("nohp");
                            String  alamat = jojo.getString("alamat");
                            //String  photo = jojo.getString("photo");

                            tvName.setText(nmuser);
                            tvEmail.setText(email);
                            tvPhone.setText(nohp);

                            /*if (photo.length() > 0){
                                Glide.with(getActivity())
                                        .load(photo)
                                        .into(ivProfPict);
                            }*/
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Terjadi kesalahan, coba lagi nanti")
                            .setTitleText("Gagal")
                            .show();
                }

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(iddriver);
    }

}
