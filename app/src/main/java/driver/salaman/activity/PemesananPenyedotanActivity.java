package driver.salaman.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.location.LocationListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

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

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import driver.salaman.Kelas.SedotBiasa;
import driver.salaman.Kelas.SharedVariable;
import driver.salaman.Kelas.UserPreference;
import driver.salaman.R;

public class PemesananPenyedotanActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{

    private GoogleMap mMap;
    SedotBiasa sedotBiasa;
    Intent intent;
    TextView tvHarga, tvGolongan,tvAlamat;
    ImageView getMyLocation,btnBack;
    double latitude;
    double longitude;
    Double lat,lon;
    Marker currentMarker;
    private SweetAlertDialog pDialogLoading,pDialodInfo;

    LocationRequest request;
    GoogleApiClient client;
    Button btnPilih,btnBatal,btnPesan;
    EditText etAlamat;
    List<Address> addresses;

    BottomSheetBehavior sheetBehavior;
    LinearLayout bottomSheetLayout;

    private HttpResponse response;
    private String alamatKirim,metodeBayar,paket;
    UserPreference mUserPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan_penyedotan);

        intent = getIntent();
        sedotBiasa = (SedotBiasa) intent.getSerializableExtra("sedotbiasa");
        metodeBayar = intent.getStringExtra("metodeBayar");
        paket       = intent.getStringExtra("paket");

        bottomSheetLayout = findViewById(R.id.bottomSheetLayout);
        sheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        mUserPref = new UserPreference(this);

        tvHarga = findViewById(R.id.tvHarga);
        tvAlamat = findViewById(R.id.tvAlamat);
        //tvGolongan = findViewById(R.id.tvGolongan);
        getMyLocation = findViewById(R.id.getMyLocation);
        btnPilih = findViewById(R.id.btnPilih);
        //btnBatal = findViewById(R.id.btnBatal);
        btnPesan = findViewById(R.id.btnPesan);
        btnBack = findViewById(R.id.btnBack);

        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setTitleText("Loading..");
        pDialogLoading.setCancelable(false);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ENGLISH);
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Double myHarga = Double.valueOf(sedotBiasa.getHarga());
        tvHarga.setText(""+formatRupiah.format((double) myHarga));

        tvGolongan.setText(sedotBiasa.getNamaLayanan());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double selectedLat = mMap.getCameraPosition().target.latitude;
                Double selectedLon = mMap.getCameraPosition().target.longitude;

                LatLng selectedLocation = new LatLng(selectedLat,selectedLon);
                String alamat = getAdress(selectedLocation);
                tvAlamat.setText(alamat);
                tvAlamat.setVisibility(View.VISIBLE);

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvAlamat.setVisibility(View.GONE);
                alamatKirim = "";

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        getMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lat != null){

                    LatLng myLocation = new LatLng(lat,lon);
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_user);

                    MarkerOptions options = new MarkerOptions();
                    options.position(myLocation);
                    options.icon(icon);
                    options.title("Lokasi Saya");

                    if(currentMarker == null)
                    {
                        currentMarker = mMap.addMarker(options);
                    }

                    else
                    {
                        currentMarker.setPosition(myLocation);
                    }


                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(myLocation, 15);
                    mMap.moveCamera(update);
                }
            }
        });


    }

    private void checkValidation() {


        if (lat == null || lat == 0.0
                || alamatKirim == null || alamatKirim.equals("") ) {

            new SweetAlertDialog(PemesananPenyedotanActivity.this,SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Anda belum memilih lokasi")
                    .show();
        }
        else{
           pDialogLoading.show();
            String latitude     = ""+lat;
            String longitude    = ""+lon;

            addPesanan(mUserPref.getIdUser(),
                    sedotBiasa.getIdLayanan(),
                    latitude,
                    longitude,
                    alamatKirim,
                    sedotBiasa.getHarga(),
                    metodeBayar,
                    paket
            );
        }
    }

    public String getAdress(LatLng lokasi){
        String alamat = "";
        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lokasi.latitude, lokasi.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            Log.d("pemesanan:",e.toString());
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        alamat = address;
        alamatKirim = alamat;
        return alamat;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng dinasPerkim = new LatLng(-5.714188, 105.586646);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dinasPerkim, 17));

        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        client.connect();

    }

    private void addPesanan(final String iduser,final String idjenis,final String latitude
        ,final String longitude,final String alamat,final String biaya,final String metodeBayar
        ,final String paket){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {


            @Override
            protected String doInBackground(String... strings) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("iduser", iduser));
                nameValuePairs.add(new BasicNameValuePair("idjenis", idjenis));
                nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
                nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
                nameValuePairs.add(new BasicNameValuePair("alamat", alamat));
                nameValuePairs.add(new BasicNameValuePair("biaya", biaya));
                nameValuePairs.add(new BasicNameValuePair("metodeBayar", metodeBayar));
                nameValuePairs.add(new BasicNameValuePair("paket", paket));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            SharedVariable.serverURL+"pesanan/addPesanan");
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

                    new SweetAlertDialog(PemesananPenyedotanActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                            .setContentText("Pemesanan Berhasil")
                            .show();

                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    intent.putExtra("loadFragment","layanan");
                    startActivity(intent);
                    finish();

                }else {
                    new SweetAlertDialog(PemesananPenyedotanActivity.this,SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Terjadi kesalahan, coba lagi nanti")
                            .show();
                }



            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(iduser,idjenis,latitude,longitude,alamat,biaya,metodeBayar,paket);

    }


    public boolean isServiceRunning(Context c, Class<?> serviceClass)
    {
        ActivityManager activityManager = (ActivityManager)c.getSystemService(Context.ACTIVITY_SERVICE);


        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);



        for(ActivityManager.RunningServiceInfo runningServiceInfo : services)
        {
            if(runningServiceInfo.service.getClassName().equals(serviceClass.getName()))
            {
                return true;
            }
        }

        return false;


    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest().create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(4000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(getApplicationContext(), "Lokasi Kamu Tidak Dapat Ditemukan", Toast.LENGTH_LONG).show();
        }else{
            lat = location.getLatitude();
            lon = location.getLongitude();
           // Toast.makeText(getApplicationContext(), "lat : "+lat+" & lon:"+lon, Toast.LENGTH_SHORT).show();
        }
    }
}
