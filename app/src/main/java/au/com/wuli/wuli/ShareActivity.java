package au.com.wuli.wuli;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        drawerLayout = findViewById(R.id.sharedrawerid);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.na_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        final TextView tv = (TextView) findViewById(R.id.textvisit);
        tv.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView av = (TextView) findViewById(R.id.abouttextview);
        av.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView fa = (TextView) findViewById(R.id.frequenttextview);
        fa.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView fb = (TextView) findViewById(R.id.liketextview);
        fb.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView yo = (TextView) findViewById(R.id.textViewyou);
        fb.setMovementMethod(LinkMovementMethod.getInstance());
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
