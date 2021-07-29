package driver.salaman.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import driver.salaman.Kelas.SedotBiasa;
import driver.salaman.Kelas.SharedVariable;
import driver.salaman.R;
import driver.salaman.adapter.AdapterSedotBiasa;

public class SedotBiasaActivity extends AppCompatActivity {

    RecyclerView rvLayanan;
    AdapterSedotBiasa adapter;
    private List<SedotBiasa> sedotBiasaList;
    private SweetAlertDialog pDialogLoading,pDialodInfo;
    private HttpResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sedot_biasa);

        rvLayanan       = findViewById(R.id.rvLayanan);
        sedotBiasaList  = new ArrayList<>();
        adapter         = new AdapterSedotBiasa(getApplicationContext(),sedotBiasaList);

        rvLayanan.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvLayanan.setHasFixedSize(true);
        rvLayanan.setItemAnimator(new DefaultItemAnimator());
        rvLayanan.setAdapter(adapter);

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();


        getDataGolongan();
    }

    public void getDataGolongan(){
        final String url = SharedVariable.serverURL+"master/golongan";

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialogLoading.dismiss();
                try {

                    JSONArray jsonArray2 = new JSONArray(response);
                    Log.d("jmlLowongan",""+jsonArray2.length());

                    sedotBiasaList.clear();

                    for (int d=0;d<jsonArray2.length();d++){
                        JSONObject jojo = jsonArray2.getJSONObject(d);
                        Log.d("arrayNya:",""+jojo.toString());

                        String id_golongan = jojo.getString("idgolongan");
                        String nmgolongan = jojo.getString("nmgolongan");
                        String harga = jojo.getString("harga");
                        String urlFoto = jojo.getString("banner");

                        SedotBiasa sedotBiasa = new SedotBiasa(
                                id_golongan,
                                nmgolongan,
                                harga,
                                urlFoto
                        );
                        Log.d("Golongan:",nmgolongan);
                        sedotBiasaList.add(sedotBiasa);
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialogLoading.dismiss();
                Log.d("getlaporan:","eror "+error.getMessage().toString());
                Toast.makeText(getApplicationContext(),"Terjadi kesalahan, coba lagi nanti",Toast.LENGTH_SHORT).show();
            }
        }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
