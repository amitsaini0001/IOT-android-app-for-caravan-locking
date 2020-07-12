package au.com.wuli.wuli;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final TextView intro = (TextView)findViewById(R.id.introtext);
//
//        Thread splashThread = new Thread(){
//
//            public void run(){
//                try {
//                    intro.setText("Prepairing your Wuli..");
//                    sleep(2000);
//                    intro.setText("Setting Up services..");
//                    sleep(2000);
//                    intro.setText("Loading..");
//                    sleep(1000);
//                    Intent intent =new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        };
//
//        splashThread.start();


        intro.setText("Prepairing your Wuli..");

        final Handler handler7 = new Handler();
        handler7.postDelayed(new Runnable() {
            @Override
            public void run() {
                intro.setText("Setting up services..");
                final Handler handler7 = new Handler();
                handler7.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intro.setText("Loading..");

                        final Handler handler7 = new Handler();
                        handler7.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();





                            }
                        }, 2000);



                    }
                }, 2000);



            }
        }, 2000);


    }

}
