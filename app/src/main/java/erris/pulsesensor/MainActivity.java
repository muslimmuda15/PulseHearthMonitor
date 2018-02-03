package erris.pulsesensor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnMonitor, btnPenyakit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeResources();
    }

    private void initializeResources() {
        btnMonitor  = (Button) findViewById(R.id.btnMonitor);
        btnPenyakit = (Button) findViewById(R.id.btnPenyakit);

        btnMonitor.setOnClickListener(this);
        btnPenyakit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMonitor:
                startActivity(new Intent(getBaseContext(), PulseMonitor.class));
                break;

            case R.id.btnPenyakit:
                startActivity(new Intent(getBaseContext(), MenuPasien.class));
                break;
        }
    }
}
