package driver.gosedot.activity;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import driver.gosedot.Kelas.SharedVariable;
import driver.gosedot.Kelas.Tangki;
import driver.gosedot.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<Tangki> listTangki = new ArrayList<>();
    private HttpResponse response;
    private SweetAlertDialog pDialogLoading,pDialodInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading..");
        pDialogLoading.setCancelable(false);
        pDialogLoading.show();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        getDataTangki();

    }

    public void getDataTangki(){
        final String url = SharedVariable.serverURL+"master/tangki";

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialogLoading.dismiss();
                try {

                    JSONArray jsonArray2 = new JSONArray(response);
                    Log.d("jmlLowongan",""+jsonArray2.length());

                    listTangki.clear();

                    for (int d=0;d<jsonArray2.length();d++){
                        JSONObject jojo = jsonArray2.getJSONObject(d);
                        Log.d("arrayNya:",""+jojo.toString());

                        String idtangki = jojo.getString("idtangki");
                        String nmtangki = jojo.getString("nmtangki");
                        String latitude = jojo.getString("latitude");
                        String longitude = jojo.getString("longitude");

                        Tangki tangki = new Tangki(
                                idtangki,
                                nmtangki,
                                latitude,
                                longitude
                        );
                        listTangki.add(tangki);
                    }

                    for (int c = 0;c < listTangki.size(); c++){
                        Tangki tangki = listTangki.get(c);
                        double lat = Double.valueOf(tangki.getLatitude());
                        double lon = Double.valueOf(tangki.getLongitude());
                        LatLng posisi = new LatLng(lat,lon);

                        mMap.addMarker(new MarkerOptions().position(posisi).title(tangki.getNamaTangki()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisi,17));
                    }

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
