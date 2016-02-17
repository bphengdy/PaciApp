package iut.paci.paciapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import iut.paci.rpc.RpcClient;
import iut.paci.rpc.RpcException;
import iut.paci.services.IServiceForum;
import iut.paci.services.Message;

/**
 * Created by John on 22/01/2016.
 */
public class ForumActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void BtnSend(View v)
    {
        EditText tv_IP = (EditText) findViewById(R.id.et_IP);
        EditText tv_Port = (EditText) findViewById(R.id.et_Port);
        EditText tv_Service = (EditText) findViewById(R.id.et_Service);
        EditText tv_Nom = (EditText) findViewById(R.id.et_Nom);
        EditText tv_Contenu = (EditText) findViewById(R.id.et_Contenu);

        String IP = tv_IP.getText().toString(); //10.30.24.252
        int Port = Integer.parseInt(tv_Port.getText().toString()); //12345
        String Service = tv_Service.getText().toString();
        String Nom = tv_Nom.getText().toString();
        String Contenu = tv_Contenu.getText().toString();
        try
        {
            IServiceForum objetDistant = RpcClient.lookupService(IP, Port, Service, IServiceForum.class);
            Message m = new Message(Nom,Contenu,new Date());
            objetDistant.sendMessage(m);
        }
        catch(RpcException e)
        {
            Toast.makeText(this,"Erreur connexion",Toast.LENGTH_LONG);
        }

    }
}