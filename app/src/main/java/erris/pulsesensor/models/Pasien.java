package erris.pulsesensor.models;

public class Pasien {
    public static final String TABLE = "Pasien";

    // Labels Table Columns names
    public static final String COLUMN_ID = "pasienId";
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_TANGGAL = "tanggal";
    public static final String COLUMN_UMUR = "umur";
    public static final String COLUMN_KELAMIN = "jenis_kelamin";
    public static final String COLUMN_BB = "bb";
    public static final String COLUMN_TINGGI = "tinggi";
    public static final String COLUMN_KELUHAN = "keluhan";
    public static final String COLUMN_X1 = "x1";
    public static final String COLUMN_X2 = "x2";
    public static final String COLUMN_X3 = "x3";
    public static final String COLUMN_X4 = "x4";
    public static final String COLUMN_X5 = "x5";
    public static final String COLUMN_X6 = "x6";
    public static final String COLUMN_X7 = "x7";
    public static final String COLUMN_X8 = "x8";
    public static final String COLUMN_X9 = "x9";
    public static final String COLUMN_X10 = "x10";
    public static final String COLUMN_X11 = "x11";
    public static final String COLUMN_KP = "kelas_penyakit";
    public static final String COLUMN_KK = "kelas_kmeans";
    public static final String COLUMN_DETAK = "detak_jantung";
    public static final String COLUMN_KONDISI = "kondisi_jantung";

    private String pasienId;
    private String nama;
    private long tanggal;
    private int umur;
    private String kelamin;
    private int bb;
    private int tinggi;
    private String keluhan;
    private int x1;
    private int x2;
    private int x3;
    private int x4;
    private int x5;
    private int x6;
    private int x7;
    private int x8;
    private int x9;
    private int x10;
    private int x11;
    private String kelas_penyakit;
    private String kelas_kmeans;
    private int detak_jantung;
    private String kondisi_jantung;

    public Pasien() {}

    public Pasien(String pasienId, String nama, long tanggal, int umur, String jenisKelamin, int bb, int tinggi, String keluhan,
                  int x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8, int x9, int x10, int x11,
                  String kelas_penyakit, String kelas_kmeans, int detak_jantung, String kondisi_jantung) {
        this.pasienId = pasienId;
        this.nama = nama;
        this.tanggal = tanggal;
        this.umur = umur;
        this.kelamin = jenisKelamin;
        this.bb = bb;
        this.tinggi = tinggi;
        this.keluhan = keluhan;
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.x5 = x5;
        this.x6 = x6;
        this.x7 = x7;
        this.x8 = x8;
        this.x9 = x9;
        this.x10 = x10;
        this.x11 = x11;
        this.kelas_penyakit = kelas_penyakit;
        this.kelas_kmeans = kelas_kmeans;
        this.detak_jantung = detak_jantung;
        this.kondisi_jantung = kondisi_jantung;
    }

    public String getPasienId() {
        return pasienId;
    }

    public void setPasienId(String pasienId) {
        this.pasienId = pasienId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public long getTanggal() {
        return tanggal;
    }

    public void setTanggal(long tanggal) {
        this.tanggal = tanggal;
    }

    public int getUmur() {
        return umur;
    }

    public void setKelamin(String kelamin){ this.kelamin = kelamin; }

    public String getKelamin() { return this.kelamin; }

    public void setUmur(int umur) {
        this.umur = umur;
    }

    public int getBb() {
        return bb;
    }

    public void setBb(int bb) {
        this.bb = bb;
    }

    public int getTinggi() {
        return tinggi;
    }

    public void setTinggi(int tinggi) {
        this.tinggi = tinggi;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getX3() {
        return x3;
    }

    public void setX3(int x3) {
        this.x3 = x3;
    }

    public int getX4() {
        return x4;
    }

    public void setX4(int x4) {
        this.x4 = x4;
    }

    public int getX5() {
        return x5;
    }

    public void setX5(int x5) {
        this.x5 = x5;
    }

    public int getX6() {
        return x6;
    }

    public void setX6(int x6) {
        this.x6 = x6;
    }

    public int getX7() {
        return x7;
    }

    public void setX7(int x7) {
        this.x7 = x7;
    }

    public int getX8() {
        return x8;
    }

    public void setX8(int x8) {
        this.x8 = x8;
    }

    public int getX9() {
        return x9;
    }

    public void setX9(int x9) {
        this.x9 = x9;
    }

    public int getX10() {
        return x10;
    }

    public void setX10(int x10) {
        this.x10 = x10;
    }

    public int getX11() {
        return x11;
    }

    public void setX11(int x11) {
        this.x11 = x11;
    }

    public String getKelas_penyakit() {
        return kelas_penyakit;
    }

    public void setKelas_penyakit(String kelas_penyakit) {
        this.kelas_penyakit = kelas_penyakit;
    }

    public String getKelas_kmeans() {
        return kelas_kmeans;
    }

    public void setKelas_kmeans(String kelas_kmeans) {
        this.kelas_kmeans = kelas_kmeans;
    }

    public int getDetak_jantung() {
        return detak_jantung;
    }

    public void setDetak_jantung(int detak_jantung) {
        this.detak_jantung = detak_jantung;
    }

    public String getKondisi_jantung() {
        return kondisi_jantung;
    }

    public void setKondisi_jantung(String kondisi_jantung) {
        this.kondisi_jantung = kondisi_jantung;
    }
}
