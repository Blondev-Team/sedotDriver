package driver.salaman.fragment;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import driver.salaman.Kelas.LayananKu;
import driver.salaman.Kelas.SharedVariable;
import driver.salaman.Kelas.UserPreference;
import driver.salaman.R;
import driver.salaman.adapter.AdapterLayanan;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMyPesanan extends Fragment {


    public FragmentMyPesanan() {
        // Required empty public constructor
    }


    UserPreference mUserPref;
    RecyclerView rvLayanan;
    AdapterLayanan adapter;
    Button btnDetail;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    private HttpResponse response;
    private List<LayananKu> layananList;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_mylayanan, container, false);

        mUserPref = new UserPreference(getActivity());
        rvLayanan = view.findViewById(R.id.rvLayanan);

        layananList = new ArrayList<>();
        adapter     = new AdapterLayanan(getActivity(),layananList);
        rvLayanan.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvLayanan.setHasFixedSize(true);
        rvLayanan.setItemAnimator(new DefaultItemAnimator());
        rvLayanan.setAdapter(adapter);

        pDialogLoading = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();

        Log.d("iduser:",mUserPref.getIdUser());

        getLayananku(mUserPref.getIdUser());
        return view;
    }

    private void dialogComingSoon(){
        new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE)
                .setContentText("Coming soon, Stay Tuned :)")
                .show();
    }


    public void getLayananku(final String iddriver){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("iddriver", iddriver));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            SharedVariable.serverURL+"pengangkutan/selectDataAngkut");
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

                        layananList.clear();

                        if(jsonArray2.length()<1){
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                                    .setContentText("Belum ada data penyedotan")
                                    .setTitleText("Informasi !")
                                    .show();
                        }

                        for (int d=0;d<jsonArray2.length();d++){
                            JSONObject jojo = jsonArray2.getJSONObject(d);

                            String idpengangkutan = jojo.getString("idpengangkutan");
                            String kodepengangkutan = jojo.getString("kodepengangkutan");
                            String waktuangkut = jojo.getString("waktuangkut");
                            String totalpesanan = jojo.getString("totalpesanan");
                            String totaltransaksi = jojo.getString("totaltransaksi");
                            String nmdriver = jojo.getString("nmdriver");
                            String nopolisi = jojo.getString("nopolisi");
                            String status = jojo.getString("status");

                            LayananKu layananku = new LayananKu(
                                    idpengangkutan,
                                    kodepengangkutan,
                                    waktuangkut,
                                    totalpesanan+" Pesanan",
                                    totaltransaksi,
                                    nmdriver,
                                    nopolisi,
                                    status
                            );

                            /* Log.d("L:",nmgolongan);*/
                            layananList.add(layananku);
                        }
                        adapter.notifyDataSetChanged();


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

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getActivity());
        return requestQueue;
    }


    public void addToRequestQueue(Request request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }






}
