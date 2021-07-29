package driver.salaman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import driver.salaman.R;

public class BerandaActivity extends AppCompatActivity {

    Button btnDataPelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        btnDataPelanggan = findViewById(R.id.btnDataPelanggan);

        btnDataPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),JenisPelangganActivity.class);
                startActivity(i);
            }
        });
    }
}
