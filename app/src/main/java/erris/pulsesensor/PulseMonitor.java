package erris.pulsesensor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import erris.pulsesensor.cardio.PathView;
import erris.pulsesensor.database.PasienTable;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class PulseMonitor extends AppCompatActivity {

    private static String TAG = PulseMonitor.class.getSimpleName();

    private static final String SPEED_TEST_SERVER_URI_UL = "http://2.testdebit.info/";
    private final static String SPEED_TEST_SERVER_URI_DL = "http://penyakitjantung.xyz/insert.php";
    private static final int FILE_SIZE = 100000;
    private static final int REPORT_INTERVAL = 1000;
    private static final int SPEED_TEST_DURATION = 15000;

    PathView pathView;
    Button button;
    TextView lblWaktuKirim, lblWaktuTerima, lblDelay, lblJitter, lblUp, lblDown;
    int id;
    Date terima_sebelumnya;
    int jml_data; int detak_jantung;
    int rata_rata_jantung;
    String kondisi_jantung;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    String last_key;

    final SpeedTestSocket upTestSocket = new SpeedTestSocket();
    final SpeedTestSocket dlTestSocket = new SpeedTestSocket();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse_monitor);

        id = getIntent().getExtras().getInt("id");

        initializeResources();
        initializePreferences();
        initializeUploadSocket();
        initializeDownloadSocket();

        Thread thread = new Thread(new Runnable(){
            public void run() {
                try {
                    upTestSocket.startFixedUpload(SPEED_TEST_SERVER_URI_UL, FILE_SIZE, SPEED_TEST_DURATION, REPORT_INTERVAL);
                    dlTestSocket.startFixedDownload(SPEED_TEST_SERVER_URI_DL, SPEED_TEST_DURATION, REPORT_INTERVAL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        terima_sebelumnya = null;
        jml_data = 0;
        detak_jantung = 0;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("bpm");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = "", bpm = "", waktu = "";

                for ( DataSnapshot childSnapshot : dataSnapshot.getChildren() ) {
                    int i = 0;
                    for ( DataSnapshot subChildSnapshot : childSnapshot.getChildren()) {
                        key = childSnapshot.getKey();

                        if (i == 0) {
                            bpm = subChildSnapshot.getValue(String.class);
                        } else {
                            waktu = subChildSnapshot.getValue(String.class);
                        }
                        i++;
                    }
                }

                if ( !last_key.equals(key) ) {
                    if (button.getVisibility() == View.GONE) {
                        pathView.setVisibility(View.VISIBLE);
                        button.setVisibility(View.VISIBLE);
                    }
                    savePreferences(key);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat indonesiaFormat  = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    Date kirim  = Calendar.getInstance().getTime();
                    Date terima = Calendar.getInstance().getTime();

                    try {
                        kirim  = simpleDateFormat.parse(waktu);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long delay  = terima.getTime() - kirim.getTime();
                    long jitter;
                    if ( terima_sebelumnya == null ) {
                        terima_sebelumnya = terima;
                        jitter = 0;
                    } else {
                        jitter = terima.getTime() - terima_sebelumnya.getTime();
                        terima_sebelumnya = terima;
                    }

                    button.setText(bpm);
                    lblWaktuKirim.setText("Waktu Kirim: " + indonesiaFormat.format(kirim));
                    lblWaktuTerima.setText("Waktu Terima: " + indonesiaFormat.format(terima));
                    lblDelay.setText("Delay: " + TimeUnit.SECONDS.convert(delay, TimeUnit.MILLISECONDS) + " detik");
                    lblJitter.setText("Jitter: " + TimeUnit.SECONDS.convert(jitter, TimeUnit.MILLISECONDS) + " detik");

                    detak_jantung += Integer.parseInt(bpm);
                    jml_data++;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.action_save:
/*                if ( rata_rata_jantung == 0 ) {
                    Toast.makeText(getBaseContext(), "Silahkan lakukan pengujian terlebih dahulu", Toast.LENGTH_SHORT).show();
                    break;
                }
*/
                try{
                    rata_rata_jantung = detak_jantung / jml_data;
                    if ( rata_rata_jantung <= 75 ) {
                        kondisi_jantung = "Rendah";
                    } else if ( rata_rata_jantung <= 110 ) {
                        kondisi_jantung = "Normal";
                    } else {
                        kondisi_jantung = "Tinggi";
                    }

                    PasienTable pasienTable = new PasienTable();
                    pasienTable.updateDetakJantung(id, rata_rata_jantung, kondisi_jantung);
                    Toast.makeText(getBaseContext(), "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
                catch(Exception ex){
                    Toast.makeText(getBaseContext(), "Silahkan cek detak jantung terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
/*                Dialog loadingDialog;
                loadingDialog = ProgressDialog.show(PulseMonitor.this, "Harap Tunggu", "Menyimpan Data Pasien...");
                loadingDialog.setCanceledOnTouchOutside(true);

                rata_rata_jantung = detak_jantung / jml_data;

                if ( rata_rata_jantung <= 75 ) {
                    kondisi_jantung = "Rendah";
                } else if ( rata_rata_jantung <= 110 ) {
                    kondisi_jantung = "Normal";
                } else {
                    kondisi_jantung = "Tinggi";
                }
                loadingDialog.dismiss();

                PasienTable pasienTable = new PasienTable();
                pasienTable.updateDetakJantung(id, rata_rata_jantung, kondisi_jantung);
                Toast.makeText(getBaseContext(), "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                finish();
*/                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeDownloadSocket() {
        dlTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onCompletion(final SpeedTestReport report) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lblDown.setText("Down: " + report.getTransferRateBit() + " bps");
                    }
                });
            }

            @Override
            public void onError(final SpeedTestError speedTestError, final String errorMessage) {
                Log.d(TAG, errorMessage);
            }

            @Override
            public void onProgress(final float percent, final SpeedTestReport downloadReport) {
            }
        });
    }

    private void initializeUploadSocket() {
        upTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onError(final SpeedTestError speedTestError, final String errorMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCompletion(final SpeedTestReport report) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lblUp.setText("Up: " + report.getTransferRateBit() + " bps");
                    }
                });
            }

            @Override
            public void onProgress(final float percent, final SpeedTestReport uploadReport) {
            }
        });
    }

    private void initializePreferences() {
        prefs = getSharedPreferences("app", Context.MODE_PRIVATE);
        last_key = prefs.getString("key", "");
    }

    private void initializeResources() {
        pathView       = (PathView) findViewById(R.id.pulse);
        button         = (Button) findViewById(R.id.btnBpm);
        lblWaktuKirim  = (TextView) findViewById(R.id.textView5);
        lblWaktuTerima = (TextView) findViewById(R.id.textView6);
        lblDelay       = (TextView) findViewById(R.id.textView7);
        lblJitter      = (TextView) findViewById(R.id.textView8);
        lblUp          = (TextView) findViewById(R.id.textView9);
        lblDown        = (TextView) findViewById(R.id.textView10);
    }

    private void savePreferences(String key) {
        editor = prefs.edit();
        editor.putString("key", key);
        editor.commit();
    }
}