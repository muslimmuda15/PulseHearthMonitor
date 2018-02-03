package erris.pulsesensor;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(splash, 1000);
    }

    protected Runnable splash = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
        }
    };
}
