package driver.gosedot.Kelas;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SedotBiasa implements Serializable {
    public String namaLayanan;
    public String idLayanan;
    public String harga;
    public String urlGambar;

    public SedotBiasa(){}

    public SedotBiasa(String idLayanan,String namaLayanan, String harga, String urlGambar) {
        this.namaLayanan = namaLayanan;
        this.idLayanan = idLayanan;
        this.harga = harga;
        this.urlGambar = urlGambar;
    }

    public String getIdLayanan() {
        return idLayanan;
    }

    public void setIdLayanan(String idLayanan) {
        this.idLayanan = idLayanan;
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public void setNamaLayanan(String namaLayanan) {
        this.namaLayanan = namaLayanan;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getUrlGambar() {
        return urlGambar;
    }

    public void setUrlGambar(String urlGambar) {
        this.urlGambar = urlGambar;
    }
}
