package iut.paci.paciapp;

        import android.app.Activity;
        import android.app.Service;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.StrictMode;
        import android.support.design.widget.Snackbar;
        import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.Date;

        import iut.paci.rpc.RpcClient;
        import iut.paci.rpc.RpcException;
        import iut.paci.rpc.RpcServer;
        import iut.paci.services.IServiceForum;
        import iut.paci.services.Message;
        import iut.paci.services.ServiceForum;

public class ChatActivity extends Activity {

    ListView chatListView;
    public ArrayAdapter<Comment> chatAdapter;
    final String Nom = "Billy";
    final int Port =12345;
    RpcServer rpcServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        LancerServeur();

        chatListView = (ListView) findViewById(R.id.chat_listView);
        chatAdapter = new ChatAdapter(getApplicationContext(), R.layout.row_chat);
        chatListView.setAdapter(chatAdapter);
        Intent intent = getIntent();
        String ip= intent.getStringExtra("IP");
        String nomTel= intent.getStringExtra("nomTel");
        final TextView tv_Destinataire = (TextView) findViewById(R.id.tv_Destinataire);
        final TextView tv_nomTel = (TextView) findViewById(R.id.tv_nomTel);
        tv_nomTel.setText(nomTel);
        tv_Destinataire.setText(ip);
/*
        chatListView = (ListView) findViewById(R.id.chat_listView);
        chatAdapter = new ArrayAdapter<Comment>(this, R.layout.row_chat);
        chatListView.setAdapter(chatAdapter);
*/


    }
        public void btnSend(View v) {
        final EditText editTextToSend= (EditText) findViewById(R.id.chat_text);
        if(editTextToSend.getText().toString().equals("") == false)
        {
            EnvoyerMessage();
            //   chatAdapter.add(new Comment(true, editTextToSend.getText().toString()));
        }
      }


    void LancerServeur()    {
        ServiceForum service = new ServiceForum(ChatActivity.this);
        rpcServer = new RpcServer();
            try
            {
                rpcServer.registerService("ServiceChat", service);
                rpcServer.start(Port);
                Log.i("Serveur","Server started..");
            }
            catch(RpcException e)
            {
                Log.e("Serveur",e.getMessage());
            }

    }


    void EnvoyerMessage()
    {
        Message m;
        try
        {

            final EditText editTextToSend= (EditText) findViewById(R.id.chat_text);

          //  String Destinataire = et_Destinataire.getText().toString();
            Intent intent = getIntent();
            String ip= intent.getStringExtra("IP");
            String nomTel= intent.getStringExtra("nomTel");
            String Contenu = editTextToSend.getText().toString();
            IServiceForum objetDistant = RpcClient.lookupService(ip, Port, "ServiceChat", IServiceForum.class);
            m = new Message(Nom,Contenu,new Date());
            objetDistant.sendMessage(m);
            chatAdapter.add(new Comment(false, editTextToSend.getText().toString()));
            editTextToSend.setText("");
            chatAdapter.notifyDataSetChanged();
            //Snackbar.make(this.findViewById(android.R.id.content),nomTel,Snackbar.LENGTH_SHORT).show();

        }
        catch(RpcException e)
        {
            Toast.makeText(this, "Erreur connexion", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rpcServer.kill();
        Log.e("Serveur","Serveur stopped");
    }
}
