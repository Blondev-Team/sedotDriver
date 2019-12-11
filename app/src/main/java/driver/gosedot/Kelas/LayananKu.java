package driver.gosedot.Kelas;

import java.io.Serializable;

public class LayananKu implements Serializable {
    public String idpengangkutan;
    public String kodepengangkutan;
    public String waktuangkut;
    public String totalpesanan;
    public String totaltransaksi;
    public String nmdriver;
    public String nopolisi;
    public String status;

    public LayananKu(String idpengangkutan, String kodepengangkutan, String waktuangkut, String totalpesanan, String totaltransaksi, String nmdriver, String nopolisi, String status) {
        this.idpengangkutan = idpengangkutan;
        this.kodepengangkutan = kodepengangkutan;
        this.waktuangkut = waktuangkut;
        this.totalpesanan = totalpesanan;
        this.totaltransaksi = totaltransaksi;
        this.nmdriver = nmdriver;
        this.nopolisi = nopolisi;
        this.status = status;
    }

    public String getIdpengangkutan() {
        return idpengangkutan;
    }

    public void setIdpengangkutan(String idpengangkutan) {
        this.idpengangkutan = idpengangkutan;
    }

    public String getKodepengangkutan() {
        return kodepengangkutan;
    }

    public void setKodepengangkutan(String kodepengangkutan) {
        this.kodepengangkutan = kodepengangkutan;
    }

    public String getWaktuangkut() {
        return waktuangkut;
    }

    public void setWaktuangkut(String waktuangkut) {
        this.waktuangkut = waktuangkut;
    }

    public String getTotalpesanan() {
        return totalpesanan;
    }

    public void setTotalpesanan(String totalpesanan) {
        this.totalpesanan = totalpesanan;
    }

    public String getTotaltransaksi() {
        return totaltransaksi;
    }

    public void setTotaltransaksi(String totaltransaksi) {
        this.totaltransaksi = totaltransaksi;
    }

    public String getNmdriver() {
        return nmdriver;
    }

    public void setNmdriver(String nmdriver) {
        this.nmdriver = nmdriver;
    }

    public String getNopolisi() {
        return nopolisi;
    }

    public void setNopolisi(String nopolisi) {
        this.nopolisi = nopolisi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
