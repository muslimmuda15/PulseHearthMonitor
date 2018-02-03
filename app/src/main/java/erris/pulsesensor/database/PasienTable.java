package erris.pulsesensor.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import erris.pulsesensor.models.Pasien;

public class PasienTable {
    private Pasien data;

    public PasienTable(){
        data = new Pasien();
    }

    public static String createTable(){
        return "CREATE TABLE " + Pasien.TABLE + " (" +
                Pasien.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Pasien.COLUMN_NAMA + " TEXT, " +
                Pasien.COLUMN_TANGGAL + " TEXT, " +
                Pasien.COLUMN_UMUR + " NUMERIC, " +
                Pasien.COLUMN_BB + " NUMERIC, " +
                Pasien.COLUMN_TINGGI + " NUMERIC, " +
                Pasien.COLUMN_KELUHAN + " TEXT, " +
                Pasien.COLUMN_X1 + " NUMERIC, " +
                Pasien.COLUMN_X2 + " NUMERIC, " +
                Pasien.COLUMN_X3 + " NUMERIC, " +
                Pasien.COLUMN_X4 + " NUMERIC, " +
                Pasien.COLUMN_X5 + " NUMERIC, " +
                Pasien.COLUMN_X6 + " NUMERIC, " +
                Pasien.COLUMN_X7 + " NUMERIC, " +
                Pasien.COLUMN_X8 + " NUMERIC, " +
                Pasien.COLUMN_X9 + " NUMERIC, " +
                Pasien.COLUMN_X10 + " NUMERIC, " +
                Pasien.COLUMN_X11 + " NUMERIC, " +
                Pasien.COLUMN_KP + " TEXT, " +
                Pasien.COLUMN_KK + " TEXT, " +
                Pasien.COLUMN_DETAK + " NUMERIC, " +
                Pasien.COLUMN_KONDISI + " TEXT)";
    }

    public int insert(Pasien data) {
        int pasienId;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Pasien.COLUMN_NAMA, data.getNama());
        values.put(Pasien.COLUMN_TANGGAL, data.getTanggal());
        values.put(Pasien.COLUMN_UMUR, data.getUmur());
        values.put(Pasien.COLUMN_BB, data.getBb());
        values.put(Pasien.COLUMN_TINGGI, data.getTinggi());
        values.put(Pasien.COLUMN_KELUHAN, data.getKeluhan());
        values.put(Pasien.COLUMN_X1, data.getX1());
        values.put(Pasien.COLUMN_X2, data.getX2());
        values.put(Pasien.COLUMN_X3, data.getX3());
        values.put(Pasien.COLUMN_X4, data.getX4());
        values.put(Pasien.COLUMN_X5, data.getX5());
        values.put(Pasien.COLUMN_X6, data.getX6());
        values.put(Pasien.COLUMN_X7, data.getX7());
        values.put(Pasien.COLUMN_X8, data.getX8());
        values.put(Pasien.COLUMN_X9, data.getX9());
        values.put(Pasien.COLUMN_X10, data.getX10());
        values.put(Pasien.COLUMN_X11, data.getX11());
        values.put(Pasien.COLUMN_KP, data.getKelas_penyakit());
        values.put(Pasien.COLUMN_KK, data.getKelas_kmeans());
        values.put(Pasien.COLUMN_DETAK, data.getDetak_jantung());
        values.put(Pasien.COLUMN_KK, data.getKondisi_jantung());

        // Inserting Row
        pasienId = (int)db.insert(Pasien.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();

        return pasienId;
    }

    public void updateKK(String pasienId, String kelas_kmeans) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Pasien.COLUMN_KK, kelas_kmeans);

        db.update(Pasien.TABLE, values, Pasien.COLUMN_ID + "=" + pasienId, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void updateDetakJantung(int pasienId, int detak_jantung, String kondisi_jantung) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Pasien.COLUMN_DETAK, detak_jantung);
        values.put(Pasien.COLUMN_KONDISI, kondisi_jantung);

        db.update(Pasien.TABLE, values, Pasien.COLUMN_ID + "=" + pasienId, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void clear() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Pasien.TABLE,null,null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void delete(int id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Pasien.TABLE,Pasien.COLUMN_ID + "=" + id,null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<Pasien> selectAll(){
        List<Pasien> pasienList = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "SELECT * FROM " + Pasien.TABLE;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Pasien data = new Pasien();
                data.setPasienId(cursor.getString(cursor.getColumnIndex(Pasien.COLUMN_ID)));
                data.setNama(cursor.getString(cursor.getColumnIndex(Pasien.COLUMN_NAMA)));
                data.setTanggal(cursor.getLong(cursor.getColumnIndex(Pasien.COLUMN_TANGGAL)));
                data.setUmur(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_UMUR)));
                data.setBb(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_BB)));
                data.setTinggi(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_TINGGI)));
                data.setKeluhan(cursor.getString(cursor.getColumnIndex(Pasien.COLUMN_KELUHAN)));
                data.setX1(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X1)));
                data.setX2(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X2)));
                data.setX3(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X3)));
                data.setX4(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X4)));
                data.setX5(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X5)));
                data.setX6(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X6)));
                data.setX7(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X7)));
                data.setX8(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X8)));
                data.setX9(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X9)));
                data.setX10(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X10)));
                data.setX11(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X11)));
                data.setKelas_penyakit(cursor.getString(cursor.getColumnIndex(Pasien.COLUMN_KP)));
                data.setKelas_kmeans(cursor.getString(cursor.getColumnIndex(Pasien.COLUMN_KK)));
                data.setDetak_jantung(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_DETAK)));
                data.setKondisi_jantung(cursor.getString(cursor.getColumnIndex(Pasien.COLUMN_KONDISI)));

                pasienList.add(data);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return pasienList;
    }

    public Pasien selectPasienById(int id){
        Pasien data = new Pasien();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "SELECT * FROM " + Pasien.TABLE +
                             " WHERE " + Pasien.COLUMN_ID + "='" + id +"'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                data.setPasienId(cursor.getString(cursor.getColumnIndex(Pasien.COLUMN_ID)));
                data.setNama(cursor.getString(cursor.getColumnIndex(Pasien.COLUMN_NAMA)));
                data.setTanggal(cursor.getLong(cursor.getColumnIndex(Pasien.COLUMN_TANGGAL)));
                data.setUmur(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_UMUR)));
                data.setBb(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_BB)));
                data.setTinggi(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_TINGGI)));
                data.setKeluhan(cursor.getString(cursor.getColumnIndex(Pasien.COLUMN_KELUHAN)));
                data.setX1(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X1)));
                data.setX2(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X2)));
                data.setX3(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X3)));
                data.setX4(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X4)));
                data.setX5(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X5)));
                data.setX6(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X6)));
                data.setX7(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X7)));
                data.setX8(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X8)));
                data.setX9(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X9)));
                data.setX10(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X10)));
                data.setX11(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_X11)));
                data.setKelas_penyakit(cursor.getString(cursor.getColumnIndex(Pasien.COLUMN_KP)));
                data.setKelas_kmeans(cursor.getString(cursor.getColumnIndex(Pasien.COLUMN_KK)));
                data.setDetak_jantung(cursor.getInt(cursor.getColumnIndex(Pasien.COLUMN_DETAK)));
                data.setKondisi_jantung(cursor.getString(cursor.getColumnIndex(Pasien.COLUMN_KONDISI)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return data;
    }
}