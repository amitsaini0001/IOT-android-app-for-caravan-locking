package au.com.wuli.wuli;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    GoogleMap mmap;
    SupportMapFragment SupportMapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        drawerLayout = findViewById(R.id.locationdrawerid);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.na_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SupportMapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.mapfrag);
        SupportMapFragment.getMapAsync(this);


    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(toggle.onOptionsItemSelected(item)){

            return true;}


        return super.onOptionsItemSelected(item);
    }
    public void clickEvent(MenuItem v) {

        if (v.getItemId() == R.id.nav_home) {
            try{
                Intent intent1 = new Intent(this,MainActivity.class);
                this.startActivity(intent1);}
            finally {
                this.finish();
            }
        }
        if (v.getItemId() == R.id.nav_logs) {
            try{
                Intent intent1 = new Intent(this,LogsActivity.class);
                this.startActivity(intent1);}
            finally {
                this.finish();
            }
        }
        if (v.getItemId() == R.id.nav_lcation) {
            try{
                Intent intent1 = new Intent(this,LocationActivity.class);
                this.startActivity(intent1);}
            finally {
                this.finish();
            }
        }
        if (v.getItemId() == R.id.nav_connect) {
            try{
                Intent intent1 = new Intent(this,BluetoothActivity.class);
                this.startActivity(intent1);}
            finally {
                this.finish();
            }
        }

        if (v.getItemId() == R.id.nav_share) {
            try{
                Intent intent1 = new Intent(this,ShareActivity.class);
                this.startActivity(intent1);}
            finally {
                this.finish();
            }
        }
        if (v.getItemId() == R.id.nav_help) {
            try{
                Intent intent1 = new Intent(this,HelpActivity.class);
                this.startActivity(intent1);}
            finally {
                this.finish();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        float zoomLevel = 16.0f;
        LatLng sydney = new LatLng(-27.477, 153.028);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Wuli Test Device"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,zoomLevel));

    }
}
