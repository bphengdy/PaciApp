package iut.paci.tp4;

import java.util.ArrayList;
import java.util.List;

import iut.paci.paciapp.R;
import iut.paci.shortestpath.Graph;
import iut.paci.shortestpath.Node;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TwoNodesActivity extends Activity {

	Spinner spinner1 ,spinner2;
	Graph routeGraph; String URLMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two_nodes);

		Intent intent = getIntent();
		String numEtage = intent.getStringExtra("numEtage");
		switch(Integer.parseInt(numEtage)) //Quel etage ?
		{
			case 2: URLMap = "/Smartcampus/Route2.osm";
				break;
			case 3: URLMap = "/Smartcampus/Route3.osm";
				break;
			case 4: URLMap = "/Smartcampus/Route4.osm";
				break;
		}
		routeGraph = Graph.getGraphFromOsm(Environment.getExternalStorageDirectory() + URLMap);
		List<String> list = new ArrayList<String>();
		for (Node node : routeGraph.Nodes) {
			if((node.name.equals("1")==false) && (node.name.equals("node") == false)) // Ne pas afficher dans le spinner
				list.add(node.name);
		}
       
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
                      
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
        spinner2.setAdapter(dataAdapter);
        
	}

	
	public void search(View v){


		Intent intent = new Intent(getApplicationContext(),MapActivity.class);
/*		intent.putExtra("depart", spinner1.getSelectedItem().toString());
		intent.putExtra("arrivee", spinner2.getSelectedItem().toString());*/

		Node A =routeGraph.getNodeByName(spinner1.getSelectedItem().toString()); //Depart
		Node B =routeGraph.getNodeByName(spinner2.getSelectedItem().toString()); //Arrivée

		if(A!=B)
		{
			ArrayList<Node> chemin = routeGraph.shorterPath(A, B); //Fonction pour trouver le chemin le + court

			Intent intent1 = getIntent();
			String numEtage = intent1.getStringExtra("numEtage"); //getNumEtage
			intent.putExtra("numEtage",numEtage); //SetEtage

			Bundle bundleObject = new Bundle();
			bundleObject.putSerializable("listNode", chemin); //Clé valeur de la liste de node a envoyer
			intent.putExtras(bundleObject);

			//	new MapActivity().DessinerChemin(chemin);
			startActivity(intent);
		}
		else
			Snackbar.make(v,"Départ et arrivé identique !",Snackbar.LENGTH_SHORT).show();

	}

}
