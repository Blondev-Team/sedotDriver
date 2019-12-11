package driver.gosedot.Kelas;

import java.io.Serializable;

public class Layanan implements Serializable {
    public String idPesanan;
    public String paket;
    public String alamat;
    public String nmJenis;
    public String nmGolongan;
    public String biaya;
    public String metodeBayar;
    public String waktu;
    public String status;

    public Layanan(String idPesanan, String paket, String alamat, String nmJenis, String nmGolongan, String biaya, String metodeBayar, String waktu, String status) {
        this.idPesanan = idPesanan;
        this.paket = paket;
        this.alamat = alamat;
        this.nmJenis = nmJenis;
        this.nmGolongan = nmGolongan;
        this.biaya = biaya;
        this.metodeBayar = metodeBayar;
        this.waktu = waktu;
        this.status = status;
    }

    public String getIdPesanan() {
        return idPesanan;
    }

    public void setIdPesanan(String idPesanan) {
        this.idPesanan = idPesanan;
    }

    public String getPaket() {
        return paket;
    }

    public void setPaket(String paket) {
        this.paket = paket;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNmJenis() {
        return nmJenis;
    }

    public void setNmJenis(String nmJenis) {
        this.nmJenis = nmJenis;
    }

    public String getNmGolongan() {
        return nmGolongan;
    }

    public void setNmGolongan(String nmGolongan) {
        this.nmGolongan = nmGolongan;
    }

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public String getMetodeBayar() {
        return metodeBayar;
    }

    public void setMetodeBayar(String metodeBayar) {
        this.metodeBayar = metodeBayar;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
