package erris.pulsesensor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import erris.pulsesensor.database.PasienTable;
import erris.pulsesensor.models.Pasien;

public class RekamMedis extends AppCompatActivity {

    ListView listView;
    SimpleAdapter adapter;
    List<HashMap<String, String>> fillMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekam_medis);

        getSupportActionBar().setTitle("Riwayat Rekam Medis");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initializeResources();
        initializeListview();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        PasienTable pasienTable = new PasienTable();
        List<Pasien> pasienList = pasienTable.selectAll();
        fillMaps.clear();

        int i = 1;
        for ( Pasien pasien : pasienList ) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("No", String.valueOf(i));
            hm.put("Nama", pasien.getNama());
            hm.put("Tanggal", getDate(pasien.getTanggal(), "dd/MM/yyyy hh:mm:ss"));
            hm.put("Keluhan", pasien.getKeluhan());
            fillMaps.add(hm);
            i++;
        }

        adapter.notifyDataSetChanged();
    }

    private void initializeListview() {
        String[] from = new String[] {"No", "Nama", "Tanggal", "Keluhan"};
        int[] to = new int[] { R.id.noRiwayat, R.id.namaRiwayat, R.id.tanggalRiwayat, R.id.keluhan};
        fillMaps = new ArrayList<HashMap<String, String>>();
        adapter = new SimpleAdapter(this, fillMaps, R.layout.grid_riwayat, from, to);
        listView.setAdapter(adapter);
    }

    private void initializeResources() {
        listView = (ListView) findViewById(R.id.lvPasienRiwayat);
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
