package driver.gosedot.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import driver.gosedot.R;

public class JenisPelangganActivity extends AppCompatActivity {

    Button btnNonNiaga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jenis_pelanggan);

        btnNonNiaga = findViewById(R.id.btnNonNiaga);
        btnNonNiaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),PemesananPenyedotanActivity.class);
                startActivity(i);
            }
        });
    }
}
