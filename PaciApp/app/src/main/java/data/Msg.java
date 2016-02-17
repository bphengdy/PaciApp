package data;

/**
 * Created by John on 22/01/2016.
 */
public class Msg {
    private String Contenu;

    public boolean isType_Personne() {
        return Type_Personne;
    }

    public void setType_Personne(boolean type_Personne) {
        Type_Personne = type_Personne;
    }

    private boolean Type_Personne;

    public Msg(boolean Type_personne, String msg)
    {
        Contenu = msg;
        Type_Personne = Type_personne;
    }

    public String getContenu() {
        return Contenu;
    }

    public void setContenu(String contenu) {
        Contenu = contenu;
    }
}
