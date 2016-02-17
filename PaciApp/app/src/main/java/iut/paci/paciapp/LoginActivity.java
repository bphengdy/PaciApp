package iut.paci.paciapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by John on 22/01/2016.
 */
public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



    }


    public void Connexion(View v)
    {
        TextInputLayout til_1 = (TextInputLayout) findViewById(R.id.til_1);
        TextInputLayout til_2 = (TextInputLayout) findViewById(R.id.til_2);
        EditText tf_identifiant = (EditText) findViewById(R.id.tf_identifiant);
        EditText tf_mdp = (EditText) findViewById(R.id.tf_mdp);
        if(("Billy".equals(tf_identifiant.getText().toString()) )&& ("Billy".equals(tf_mdp.getText().toString())))
        {
            Toast.makeText(this.getApplicationContext(), "Connexion...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, iut.paci.tp4.EtageActivity.class);
            intent.putExtra("Identifiant",tf_identifiant.getText().toString() );
            startActivity(intent);
        }
        if(tf_identifiant.getText().toString().equals("")) til_1.setError("L'identifiant est requis !");
        else til_1.setError(null);
        if(tf_mdp.getText().toString().equals("")) til_2.setError("Le mot de passe est requis !");
        else til_2.setError(null);
    }


}
