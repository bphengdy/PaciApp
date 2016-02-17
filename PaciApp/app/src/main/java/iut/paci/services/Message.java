package iut.paci.services;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Chaouche
 */
public class Message implements Serializable{

    public String Nom;
    public String contenu;
    public Date moment;

    public Message(String Nom, String contenu, Date moment) {
        this.Nom = Nom;
        this.contenu = contenu;
        this.moment = moment;
    }
    
}
