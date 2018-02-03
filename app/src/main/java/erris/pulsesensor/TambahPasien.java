package erris.pulsesensor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import erris.pulsesensor.database.PasienTable;
import erris.pulsesensor.kmeans.Kmeans;
import erris.pulsesensor.models.Pasien;

public class TambahPasien extends AppCompatActivity {

    EditText txtNama, txtUmur, txtTinggi, txtBB, txtKeluhan;
    CheckBox chkX1, chkX2, chkX3, chkX4, chkX5, chkX6, chkX7, chkX8, chkX9, chkX10, chkX11;
    Button btnSimpan, btnHapus;

    int id;
    int nilai_pjk, nilai_pja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pasien);

        String tmp_id = getIntent().getStringExtra("id");
        id = Integer.parseInt(tmp_id);

        initializeResources();

        if ( id > 0 ) {
            loadData();
        }
    }

    private void loadData() {
        PasienTable pasienTable = new PasienTable();
        Pasien pasien = pasienTable.selectPasienById(id);

        txtNama.setText(pasien.getNama());
        txtUmur.setText(String.valueOf(pasien.getUmur()));
        txtTinggi.setText(String.valueOf(pasien.getTinggi()));
        txtBB.setText(String.valueOf(pasien.getBb()));
        txtKeluhan.setText(pasien.getKeluhan());

        if ( pasien.getX1() == 1 )
            chkX1.setChecked(true);
        if ( pasien.getX2() == 1 )
            chkX2.setChecked(true);
        if ( pasien.getX3() == 1 )
            chkX3.setChecked(true);
        if ( pasien.getX4() == 1 )
            chkX4.setChecked(true);
        if ( pasien.getX5() == 1 )
            chkX5.setChecked(true);
        if ( pasien.getX6() == 1 )
            chkX6.setChecked(true);
        if ( pasien.getX7() == 1 )
            chkX7.setChecked(true);
        if ( pasien.getX8() == 1 )
            chkX8.setChecked(true);
        if ( pasien.getX9() == 1 )
            chkX9.setChecked(true);
        if ( pasien.getX10() == 1 )
            chkX10.setChecked(true);
        if ( pasien.getX11() == 1 )
            chkX11.setChecked(true);
    }

    private void initializeResources() {
        txtNama    = (EditText) findViewById(R.id.txtNamaPasien);
        txtUmur    = (EditText) findViewById(R.id.txtUmurPasien);
        txtTinggi  = (EditText) findViewById(R.id.txtTinggiPasien);
        txtBB      = (EditText) findViewById(R.id.txtBbPasien);
        txtKeluhan = (EditText) findViewById(R.id.txtKeluhanPasien);

        chkX1     = (CheckBox) findViewById(R.id.chkX1);
        chkX2     = (CheckBox) findViewById(R.id.chkX2);
        chkX3     = (CheckBox) findViewById(R.id.chkX3);
        chkX4     = (CheckBox) findViewById(R.id.chkX4);
        chkX5     = (CheckBox) findViewById(R.id.chkX5);
        chkX6     = (CheckBox) findViewById(R.id.chkX6);
        chkX7     = (CheckBox) findViewById(R.id.chkX7);
        chkX8     = (CheckBox) findViewById(R.id.chkX8);
        chkX9     = (CheckBox) findViewById(R.id.chkX9);
        chkX10    = (CheckBox) findViewById(R.id.chkX10);
        chkX11    = (CheckBox) findViewById(R.id.chkX11);

        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnHapus  = (Button) findViewById(R.id.btnHapus);

        if ( id == 0 )
            btnHapus.setVisibility(View.GONE);

        btnSimpan.setOnClickListener(new ButtonSimpanListener());
        btnHapus.setOnClickListener(new ButtonHapusListener());
    }

    private class ButtonSimpanListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            PasienTable pasienTable = new PasienTable();

            if ( id > 0 ) {
                pasienTable.delete(id);
            }

            if ( txtNama.getText().toString().equals("") || txtUmur.getText().toString().equals("") || txtTinggi.getText().toString().equals("") || txtBB.getText().toString().equals("") || txtKeluhan.getText().toString().equals("") ) {
                Toast.makeText(getBaseContext(), "Silahkan lengkapi form terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            Dialog loadingDialog;
            loadingDialog = ProgressDialog.show(TambahPasien.this, "Harap Tunggu", "Menyimpan Data Pasien...");
            loadingDialog.setCanceledOnTouchOutside(true);

            String nama    = txtNama.getText().toString();
            int umur       = Integer.valueOf(txtUmur.getText().toString());
            int tinggi     = Integer.valueOf(txtTinggi.getText().toString());
            int bb         = Integer.valueOf(txtBB.getText().toString());
            String keluhan = txtKeluhan.getText().toString();

            nilai_pjk = 0; nilai_pja = 0;
            int x1 = 0;
            if ( chkX1.isChecked() ) {
                x1 = 1;
                nilai_pja++; nilai_pjk++;
            }

            int x2 = 0;
            if ( chkX2.isChecked() ) {
                x2 = 1;
                nilai_pja++; nilai_pjk++;
            }

            int x3 = 0;
            if ( chkX3.isChecked() ) {
                x3 = 1;
                nilai_pja++; nilai_pjk++;
            }

            int x4 = 0;
            if ( chkX4.isChecked() ) {
                x4 = 1;
                nilai_pjk++;
            } else {
                nilai_pja++;
            }

            int x5 = 0;
            if ( chkX5.isChecked() ) {
                x5 = 1;
                nilai_pjk++;
            } else {
                nilai_pja++;
            }

            int x6 = 0;
            if ( chkX6.isChecked() ) {
                x6 = 1;
                nilai_pja++;
            } else {
                nilai_pjk++;
            }

            int x7 = 0;
            if ( chkX7.isChecked() ) {
                x7 = 1;
                nilai_pja++;
            } else {
                nilai_pjk++;
            }

            int x8 = 0;
            if ( chkX8.isChecked() ) {
                x8 = 1;
                nilai_pja++;
            } else {
                nilai_pjk++;
            }

            int x9 = 0;
            if ( chkX9.isChecked() ) {
                x9 = 1;
                nilai_pja++; nilai_pjk++;
            }

            int x10 = 0;
            if ( chkX10.isChecked() ) {
                x10 = 1;
                nilai_pja++;
            } else {
                nilai_pjk++;
            }

            int x11 = 0;
            if ( chkX11.isChecked() ) {
                x11 = 1;
                nilai_pjk++;
            } else {
                nilai_pja++;
            }

            String kp = "";
            if ( nilai_pjk >= nilai_pja ) {
                kp = "PJK";
            } else {
                kp = "PJA";
            }

            long tanggal = getTime();

            loadingDialog.dismiss();

            Pasien pasien = new Pasien("", nama, tanggal, umur, bb, tinggi, keluhan, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, kp, "", 0, "");
            id = pasienTable.insert(pasien);

//            if ( id > 0 ) {
//                Toast.makeText(getBaseContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getBaseContext(), PulseMonitor.class);
//                intent.putExtra("id", id);
//                finish();
//                startActivity(intent);
//            }
            finish();
        }
    }

    private class ButtonHapusListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            PasienTable pasienTable = new PasienTable();
            pasienTable.delete(id);
            Toast.makeText(getBaseContext(), "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private long getTime() {
        return System.currentTimeMillis();
    }
}
