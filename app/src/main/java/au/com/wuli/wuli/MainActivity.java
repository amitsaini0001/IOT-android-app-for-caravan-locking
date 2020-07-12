package au.com.wuli.wuli;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    boolean doubleBackToExitPressedOnce = false;
    GoogleMap mmap;
    SupportMapFragment SupportMapFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.maindrawerid);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.na_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            SupportMapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.mainfrag);
            SupportMapFragment.getMapAsync(this);






    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }

        }, 2000);



    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(toggle.onOptionsItemSelected(item)){

            return true;}


        return super.onOptionsItemSelected(item);
    }



    public void clickEvent(MenuItem v) {
//        if (v.getItemId() == R.id.nav_help) {
//            Toast.makeText(MainActivity.this, "you click on button1",
//                    Toast.LENGTH_SHORT).show();
//        }

        if (v.getItemId() == R.id.nav_home) {
            try{
                Intent intent1 = new Intent(this,MainActivity.class);
                this.startActivity(intent1);}
            finally {
                //this.finish();
            }
        }
        if (v.getItemId() == R.id.nav_logs) {
            try{
            Intent intent1 = new Intent(this,LogsActivity.class);
            this.startActivity(intent1);}
            finally {
               // this.finish();
            }
        }
        if (v.getItemId() == R.id.nav_lcation) {
            try{
            Intent intent1 = new Intent(this,LocationActivity.class);
            this.startActivity(intent1);}
            finally {
               // this.finish();
            }
        }
        if (v.getItemId() == R.id.nav_connect) {
            try{
            Intent intent1 = new Intent(this,BluetoothActivity.class);
            this.startActivity(intent1);}
            finally {
                //this.finish();
            }
        }

        if (v.getItemId() == R.id.nav_share) {
            try{
            Intent intent1 = new Intent(this,ShareActivity.class);
            this.startActivity(intent1);}
            finally {
                //this.finish();
            }
        }
        if (v.getItemId() == R.id.nav_help) {
            try{

            Intent intent1 = new Intent(this,HelpActivity.class);
            this.startActivity(intent1);}
            finally {
               // this.finish();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        float zoomLevel = 16.0f;
        LatLng sydney = new LatLng(-27.477, 153.028);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Wuli Test Device"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,zoomLevel));

    }

}
