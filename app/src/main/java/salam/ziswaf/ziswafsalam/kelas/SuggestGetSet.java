package salam.ziswaf.ziswafsalam.kelas;

public class SuggestGetSet {
    String nis,nama;
    public SuggestGetSet(String nis, String name){
        this.setNis(nis);
        this.setNama(nama);
    }
        public String getNis() {
        return nis;
    }

    public void setNis(String id) {
        this.nis = nis;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
