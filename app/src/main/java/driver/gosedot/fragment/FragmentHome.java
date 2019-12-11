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
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.RequestQueue;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {

    public FragmentHome() {
        // Required empty public constructor
    }

    UserPreference mUserPref;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    private HttpResponse response;
    private RequestQueue requestQueue;
    TextView tvDriver,tvTotalAngkut,tvJadwal;
    RelativeLayout rlDriver,rlAngkutan,rlJadwal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        tvDriver = view.findViewById(R.id.tvDriver);
        tvTotalAngkut = view.findViewById(R.id.tvTotalAngkut);
        tvJadwal = view.findViewById(R.id.tvJadwal);
        rlDriver = view.findViewById(R.id.rlDriver);
        rlAngkutan = view.findViewById(R.id.rlAngkutan);
        rlJadwal = view.findViewById(R.id.rlJadwal);
        mUserPref = new UserPreference(getActivity());
        pDialogLoading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();
        getDriver(mUserPref.getIdUser());

        rlDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FragmentProfil.class);
                startActivity(intent);
            }
        });


//        rlSedotBiasa        = view.findViewById(R.id.rlSedotBiasa);
//        rlLokasiTangki      = view.findViewById(R.id.rlLokasiTangki);
//        rlKirimBerjadwal    = view.findViewById(R.id.rlKirimBerjadwal);
//
//        rlSedotBiasa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), SedotBiasaActivity.class);
//                startActivity(intent);
//            }
//        });
//        rlLokasiTangki.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MapsActivity.class);
//                startActivity(intent);
//            }
//        });
//        rlKirimBerjadwal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogComingSoon();
//            }
//        });


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

                            String nmdriver = jojo.getString("nmdriver");
                            String totalangkutan = jojo.getString("totalangkutan");
                            String nextangkut = jojo.getString("nextangkut");
                            tvDriver.setText(nmdriver);
                            tvTotalAngkut.setText(totalangkutan+" Angkutan");
                            tvJadwal.setText(nextangkut);
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

    private void dialogComingSoon(){
        new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE)
                .setContentText("Coming soon, Stay Tuned :)")
                .show();
    }


}
