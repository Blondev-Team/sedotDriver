package driver.salaman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import driver.salaman.R;
import driver.salaman.fragment.FragmentHome;
import driver.salaman.fragment.FragmentMyPesanan;
import driver.salaman.fragment.FragmentProfil;
import driver.salaman.fragment.FragmentSedotBiasa;

public class HomeActivity extends AppCompatActivity {

    FragmentHome fragmentHome;
    FragmentMyPesanan fragmentMyPesanan;
    FragmentProfil fragmentProfil;
    FragmentSedotBiasa fragmentSedotBiasa;
    Intent intent;
    String loadFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    goToFragment(fragmentHome,true);
                    return true;
                case R.id.navigation_mylayanan:
                    goToFragment(fragmentMyPesanan,true);
                    return true;
                case R.id.navigation_profil:
                    goToFragment(fragmentProfil,true);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        intent = getIntent();
        loadFragment = intent.getStringExtra("loadFragment");

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentHome        = new FragmentHome();
        fragmentMyPesanan   = new FragmentMyPesanan();
        fragmentProfil      = new FragmentProfil();
        fragmentSedotBiasa  = new FragmentSedotBiasa();

        if (loadFragment == null || loadFragment.equals("")){
            goToFragment(fragmentHome,true);
        }else{
           loadSpesificFragment(loadFragment);
        }



    }

    public void loadSpesificFragment(String fragmentName){

        if (fragmentName.equals("home")){
            goToFragment(fragmentHome,true);
        }else if (fragmentName.equals("layanan")){
            goToFragment(fragmentMyPesanan,true);
        }else if (fragmentName.equals("profil")){
            goToFragment(fragmentProfil,true);
        }
    }

    void goToFragment(Fragment fragment, boolean isTop) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if (!isTop)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void goToSedotBiasa(){
        goToFragment(fragmentSedotBiasa,true);
    }

}
