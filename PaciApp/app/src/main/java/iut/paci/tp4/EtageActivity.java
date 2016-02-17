package iut.paci.tp4;

import iut.paci.paciapp.R;
import iut.paci.shortestpath.Graph;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;


public class EtageActivity extends Activity {

    Graph routeGraph; int numEtage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etage);

    }


    public void gotoEtage(View v)
    {
        switch(v.getId())
        {
            case(R.id.btn_2):
                numEtage = 2;
                break;
            case(R.id.btn_4):
                numEtage = 4;
                break;
        }
        Intent i = new Intent(this,TwoNodesActivity.class);
        i.putExtra("numEtage",String.valueOf(numEtage));
        startActivity(i);
    }

}
