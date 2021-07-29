package driver.salaman.Kelas;

import java.io.Serializable;

public class Tangki implements Serializable {
    public String idTangki;
    public String namaTangki;
    public String latitude;
    public String longitude;

    public Tangki(String idTangki, String namaTangki, String latitude, String longitude) {
        this.idTangki = idTangki;
        this.namaTangki = namaTangki;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getIdTangki() {
        return idTangki;
    }

    public void setIdTangki(String idTangki) {
        this.idTangki = idTangki;
    }

    public String getNamaTangki() {
        return namaTangki;
    }

    public void setNamaTangki(String namaTangki) {
        this.namaTangki = namaTangki;
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
}
