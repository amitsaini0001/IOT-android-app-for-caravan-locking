package au.com.wuli.wuli;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogsActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        drawerLayout = findViewById(R.id.logsdrawerid);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.na_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView lv1 = (ListView) findViewById(R.id.logsheader);

        Button clearlogs = (Button) findViewById(R.id.clearlogs);
        clearlogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(LogsActivity.this);
                alert.setTitle("Delete Logs");
                alert.setMessage("This will delete the logs permanently !");
                alert.setIcon(R.drawable.ic_error_outline_red_24dp);

                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        SharedPreferences logs = getSharedPreferences("LOGS",0);
                        SharedPreferences.Editor editor = logs.edit();
                        Integer newind=0;
                        String insertnewindex = newind.toString();

                        editor.putString("index-", insertnewindex);
                        editor.apply();

                        Toast.makeText(LogsActivity.this, "Logs Cleared ",
                                Toast.LENGTH_SHORT).show();



                        }});

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

                alert.show();



            }
        });





        List<String>items =new ArrayList<String>();
        SharedPreferences logs = getSharedPreferences("LOGS",0);
        SharedPreferences.Editor editor = logs.edit();

        String indget =logs.getString("index-","");
        Integer indgetint= Integer.parseInt(indget);

        for(int i =0; i<=indgetint; i++){
            Integer setind = i;
            String setstring = setind.toString();
            String logget = logs.getString(setstring,"");
            items.add(logget);



        }

//        String logget = logs.getString("logs-","");
//        SharedPreferences.Editor editor = logs.edit();
//
//        String[]logitems=logget.split("@");
//
//        Toast.makeText(LogsActivity.this, "Total elements= "+logitems.length,
//                Toast.LENGTH_SHORT).show();
//        for(int i=0; i<logitems.length; i++){
//            items.add(logitems[i]);
//
//        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lv1.setAdapter(adapter);

















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
}
