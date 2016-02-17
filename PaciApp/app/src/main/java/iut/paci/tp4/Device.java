package iut.paci.tp4;

import org.mapsforge.core.model.LatLong;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by John on 01/02/2016.
 */
public class Device implements Serializable {
    String Nom;
    String ip;
    Date date;

    public Device(String n, String ip) {
        Nom = n;
        this.ip = ip;
        date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
