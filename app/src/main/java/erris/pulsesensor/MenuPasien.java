package erris.pulsesensor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import erris.pulsesensor.database.PasienTable;
import erris.pulsesensor.kmeans.Kmeans;
import erris.pulsesensor.models.Pasien;

public class MenuPasien extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listView;
    FloatingActionButton floatingActionButton, fabProses;
    SimpleAdapter adapter;
    List<HashMap<String, String>> fillMaps;
    List<String> listId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pasien);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Daftar Pasien");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeResources();
        initializeListview();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_pasien) {
        } else if (id == R.id.nav_riwayat) {
            startActivity(new Intent(getBaseContext(), RekamMedis.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadData() {
        PasienTable pasienTable = new PasienTable();
        List<Pasien> pasienList = pasienTable.selectAll();
        listId = new ArrayList<>();
        fillMaps.clear();

        int i = 1;
        for ( Pasien pasien : pasienList ) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("No", String.valueOf(i));
            hm.put("Nama", pasien.getNama());
            hm.put("Jenis Kelamin", pasien.getKelamin());
            hm.put("Umur", Integer.toString(pasien.getUmur()));
//            hm.put("Berat Badan", Integer.toString(pasien.getBb()));
//            hm.put("Tinggi Badan", Integer.toString(pasien.getTinggi()));
            hm.put("Kelas Penyakit", pasien.getKelas_penyakit());
            hm.put("Kelas K-Means", pasien.getKelas_kmeans());
//            hm.put("Detak Jantung", String.valueOf(pasien.getDetak_jantung()));
//            hm.put("Kondisi Jantung", pasien.getKondisi_jantung());
            fillMaps.add(hm);
            listId.add(pasien.getPasienId());
            i++;
        }

        adapter.notifyDataSetChanged();
    }

    private void initializeListview() {
//        String[] from = new String[] {"No", "Nama", "Kelas Penyakit", "Kelas K-Means", "Detak Jantung", "Kondisi Jantung"};
//        String[] from = new String[] {"No", "Nama", "Berat Badan", "Tinggi Badan", "Kelas Penyakit", "Kelas K-Means"};
        String[] from = new String[] {"No", "Nama", "Umur", "Jenis Kelamin", "Kelas Penyakit", "Kelas K-Means"};
//        int[] to = new int[] { R.id.no, R.id.nama, R.id.kelas_penyakit, R.id.kelas_kmeans, R.id.detak_jantung, R.id.kondisi_jantung};
//        int[] to = new int[] { R.id.no, R.id.nama, R.id.berat_badan, R.id.tinggi_badan, R.id.kelas_penyakit, R.id.kelas_kmeans};
        int[] to = new int[] { R.id.no, R.id.nama, R.id.umur, R.id.jenis_kelamin, R.id.kelas_penyakit, R.id.kelas_kmeans};
        fillMaps = new ArrayList<HashMap<String, String>>();
        adapter = new SimpleAdapter(this, fillMaps, R.layout.grid_pasien, from, to);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), TambahPasien.class);
                intent.putExtra("id", listId.get(i));
                startActivity(intent);
            }
        });

    }

    private void initializeResources() {
        listView = (ListView) findViewById(R.id.lvPasien);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fabNewPasien);
        fabProses = (FloatingActionButton) findViewById(R.id.fabProses);

        floatingActionButton.setOnClickListener(new TambahPasienButtonListener());
        fabProses.setOnClickListener(new ProsesButtonListener());
    }

    private class TambahPasienButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getBaseContext(), TambahPasien.class);
            intent.putExtra("id", "0");
            startActivity(intent);
        }
    }

    private class ProsesButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Dialog loadingDialog;
            loadingDialog = ProgressDialog.show(MenuPasien.this, "Harap Tunggu", "Menyimpan Data Pasien...");
            loadingDialog.setCanceledOnTouchOutside(true);

            PasienTable pasienTable = new PasienTable();
            List<Pasien> pasienList = pasienTable.selectAll();
            int jml_pasien = 0; int kelas_sama = 0; Double persen;

            for ( Pasien pasien : pasienList ) {
                if ( pasien.getKelas_kmeans().equals("") ) {
                    Kmeans kmeans = new Kmeans();
                    String kk = kmeans.lloyd();

                    pasienTable.updateKK(pasien.getPasienId(), kk);

                    if ( kk.equals(pasien.getKelas_penyakit()) ) {
                        kelas_sama++;
                    }
                    jml_pasien++;
                }
            }
            persen = (Double.valueOf(kelas_sama) / Double.valueOf(jml_pasien)) * 100;

            loadingDialog.dismiss();

            AlertDialog dialogSetting = new AlertDialog.Builder(MenuPasien.this)
                    .setTitle("Proses K-Means")
                    .setMessage("Proses K-Means Selesai. Akurasi ("+ kelas_sama +"/"+ jml_pasien +") : "+ persen +"%")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loadData();
                        }
                    }).create();
            dialogSetting.show();
        }
    }
}
