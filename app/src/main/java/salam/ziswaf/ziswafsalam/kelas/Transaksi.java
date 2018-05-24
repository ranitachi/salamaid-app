package salam.ziswaf.ziswafsalam.kelas;


public class Transaksi extends Koneksi{
    String URL = "http://keuangan.sekolahalambogor.id/json/json_update_transaksi/";
    String url = "";
    String response = "";

    public String updateBiodata(String kwitansi) {
        try {
            url = URL + kwitansi;
            System.out.println("URL Update Transaksi : " + url);
            response = call(url);
        } catch (Exception e) {
        }
        return response;
    }
}
