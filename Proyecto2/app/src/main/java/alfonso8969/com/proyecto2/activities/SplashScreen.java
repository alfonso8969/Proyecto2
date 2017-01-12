package alfonso8969.com.proyecto2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import alfonso8969.com.proyecto2.R;

public class SplashScreen extends AppCompatActivity {
long retardo = 2000;
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);
        logo=(ImageView)findViewById(R.id.logo);
        logo.setImageResource(R.mipmap.logoempresa);
        TimerTask task =new TimerTask() {
            @Override
            public void run() {

                Intent mostrarMenuPrincipal;
                mostrarMenuPrincipal = new Intent(SplashScreen.this,Menu_principal.class);
                startActivity(mostrarMenuPrincipal);
                SplashScreen.this.finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,retardo);
    }
}
