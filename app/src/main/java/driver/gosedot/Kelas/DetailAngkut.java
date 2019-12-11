package driver.gosedot.Kelas;

import java.io.Serializable;

public class DetailAngkut implements Serializable {
    String idpesanan,kodepesanan,nmuser,nmjenis,latitude,longitude,alamat,biaya,paket,metodebayar,petugas;


    public DetailAngkut(String idpesanan, String kodepesanan, String nmuser, String nmjenis, String latitude, String longitude, String alamat, String biaya, String paket, String metodebayar, String petugas) {
        this.idpesanan = idpesanan;
        this.kodepesanan = kodepesanan;
        this.nmuser = nmuser;
        this.nmjenis = nmjenis;
        this.latitude = latitude;
        this.longitude = longitude;
        this.alamat = alamat;
        this.biaya = biaya;
        this.paket = paket;
        this.metodebayar = metodebayar;
        this.petugas = petugas;
    }

    public String getIdpesanan() {
        return idpesanan;
    }

    public void setIdpesanan(String idpesanan) {
        this.idpesanan = idpesanan;
    }

    public String getKodepesanan() {
        return kodepesanan;
    }

    public void setKodepesanan(String kodepesanan) {
        this.kodepesanan = kodepesanan;
    }

    public String getNmuser() {
        return nmuser;
    }

    public void setNmuser(String nmuser) {
        this.nmuser = nmuser;
    }

    public String getNmjenis() {
        return nmjenis;
    }

    public void setNmjenis(String nmjenis) {
        this.nmjenis = nmjenis;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public String getPaket() {
        return paket;
    }

    public void setPaket(String paket) {
        this.paket = paket;
    }

    public String getMetodebayar() {
        return metodebayar;
    }

    public void setMetodebayar(String metodebayar) {
        this.metodebayar = metodebayar;
    }

    public String getPetugas() {
        return petugas;
    }

    public void setPetugas(String petugas) {
        this.petugas = petugas;
    }
}
