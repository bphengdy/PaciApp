package iut.paci.tp4;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.os.Vibrator;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import iut.paci.paciapp.ChatActivity;
import iut.paci.paciapp.R;
import iut.paci.shortestpath.Graph;
import iut.paci.shortestpath.Node;

/**
 * Created by John on 22/01/2016.
 */
public class MapActivity extends AppCompatActivity {

    MapView mapView; TileCache tileCache;  File file; TileRendererLayer tileRendererLayer;
    String URLMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        AndroidGraphicFactory.createInstance(this.getApplication());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        mapView= new MapView(this);
        setContentView(this.mapView);

        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.getMapZoomControls().setZoomLevelMin((byte) 17);
        mapView.getMapZoomControls().setZoomLevelMax((byte) 20);
        tileCache= AndroidUtil.createTileCache(this, "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor());
    }

    public void onStart() {
        super.onStart();
        mapView.getModel().mapViewPosition.setCenter(new LatLong(48.841751, 2.2684444)); //Center
        mapView.getModel().mapViewPosition.setZoomLevel((byte) 19); //ZoomLevel
        Intent intent = getIntent();
        String numEtage = intent.getStringExtra("numEtage"); //getNumEtage
        switch(Integer.parseInt(numEtage)) //Quel Etage ?
        {
            case 2: URLMap = "/Smartcampus/Salle2.map";
                break;
            case 3: URLMap = "/Smartcampus/Salle3.map";
                break;
            case 4:URLMap = "Smartcampus/Salle4.map";
                break;
        }

        file= new File(Environment.getExternalStorageDirectory(),URLMap);
        MapDataStore mapDataStore= new MapFile(file);
        Drawable drawable;
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP)
            drawable= getDrawable(R.drawable.marqueur);
        else
            drawable=getResources().getDrawable(R.drawable.marqueur);
        final Bitmap bitmap= AndroidGraphicFactory.convertToBitmap(drawable);
        bitmap.scaleTo(60, 100);
        Bundle bundleObject = getIntent().getExtras();
        ArrayList<Node> listNode = (ArrayList<Node>) bundleObject.getSerializable("listNode"); //Récup la liste de Node qui forme le chemin
        tileRendererLayer= new TileRendererLayer(tileCache,
                mapDataStore,mapView.getModel().mapViewPosition, false, true, AndroidGraphicFactory.INSTANCE)
        {
            public boolean onLongPress(LatLong geoPoint, Point viewPosition, Point tapPoint) {
                final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MapActivity.this);
                WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                final String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

                final DeviceMarker marker= new DeviceMarker(new LatLong(geoPoint.latitude, geoPoint.longitude),
                        bitmap,
                        0, -bitmap.getHeight() / 2,
                        new Device(Build.MODEL,ip),
                        MapActivity.this
                );

                addMarqueurBDD(marker);


                //    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                //    v.vibrate(1000);
                mapView.getLayerManager().getLayers().add(marker);
                Toast.makeText(getApplication(), "New marker " + ip, Toast.LENGTH_SHORT).show();
                return true;
            }
        };
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
        mapView.getLayerManager().getLayers().add(tileRendererLayer);

        //Création des points possible à partir d'ici
    /*    marker= new DeviceMarker(new LatLong(48.8416264, 2.2689432),
                bitmap,
                0, -bitmap.getHeight() / 2,
                new Device(Build.MODEL,"192"),
                getApplicationContext());
        mapView.getLayerManager().getLayers().add(marker);*/

        ArrayList<DeviceMarker> listMarkers = InitMarqueur(); //Récupération des marqueurs de la BDD
        for(DeviceMarker dm : listMarkers) //Ajout des marqueurs dans la mapView
        {
            Log.i("DM","IP marqueur : "+String.valueOf(dm.getDev().getIp()));
            mapView.getLayerManager().getLayers().add(dm);
        }
        //Creation chemin
        Paint paint= AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(Color.RED);paint.setStrokeWidth(3);paint.setStyle(Style.STROKE);
        Polyline polyline= new Polyline(paint, AndroidGraphicFactory.INSTANCE);
        List<LatLong> coordinateList= polyline.getLatLongs();
       for(Node n : listNode)
        {
            coordinateList.add(new LatLong(n.latitude,n.longitude));
        }
        mapView.getLayerManager().getLayers().add(polyline);

    }

    public void onDestroy() {
        super.onDestroy();
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        final String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        deleteMarqueurBDD(ip); //Delete tout les marqueurs de la session
        mapView.destroyAll();
    }

    //Si l'utilisateur est connecté à internet
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //Initialisation de tout les marqueurs déjà présent
    public ArrayList<DeviceMarker> InitMarqueur() {
        InputStream is=null;String result="";
        ArrayList<DeviceMarker> markers = new ArrayList<DeviceMarker>();
        if(isOnline())
        {
            try
            {
                HttpClient httpclient = new DefaultHttpClient(); //Instanciation HttpClient
                HttpPost httppost = new HttpPost("http://bphengdy.esy.es/SelectAllMarqueur.php"); //Req. HttpPost vers le fichier
                //  HttpPost httppost = new HttpPost("http://192.168.1.25/SelectTest.php");
                //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost); //Execution de la requete
                HttpEntity entity = response.getEntity(); //Récupération de la réponse
                is = entity.getContent(); //Ajout de la réponse dans l'InputStream
            }
            catch(Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                //Récup de la chaine de caractère
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

                result=sb.toString();
            }catch(Exception e){
                Log.e("log_tag", "Error converting result "+e.toString());
            }

            try{

                JSONArray jArray = new JSONArray(result); //Création JSON array
                DeviceMarker marker;
                for(int i=0;i<jArray.length();i++) //Récupération + création d'un marqueur pr chaque ligne du json Array
                {
                    JSONObject json_data = jArray.getJSONObject(i);
                    Drawable drawable;
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP)
                        drawable= getDrawable(R.drawable.marqueur);
                    else
                        drawable=getResources().getDrawable(R.drawable.marqueur);
                    final Bitmap bitmap= AndroidGraphicFactory.convertToBitmap(drawable);
                    bitmap.scaleTo(60, 100);

                    marker= new DeviceMarker(new LatLong(json_data.getDouble("latitude"), json_data.getDouble("longitude")),
                            bitmap,
                            0, -bitmap.getHeight() / 2,
                            new Device(json_data.getString("appareil"),json_data.getString("ip")),
                            MapActivity.this
                    );
                    Log.i("LatLong",json_data.getString("ip"));
                    Log.i("LatLong", String.valueOf(json_data.getDouble("latitude")));
                 //   Log.i("LatLong",json_data.getString("ip"));
                 // mapView.getLayerManager().getLayers().add(marker);
                    markers.add(marker);
                }

            }
            catch(JSONException e)
            {
                Log.e("log_tag", "Error parsing data "+e.toString());
                return null;
            }
            return markers;
        }
        else
            return null;
    }

    //Add marqueur a la BDD
    public void addMarqueurBDD(DeviceMarker dm) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); //ArrayList Clé - Valeur
        nameValuePairs.add(new BasicNameValuePair("appareil", dm.getDev().getNom()));
        nameValuePairs.add(new BasicNameValuePair("ip", dm.getDev().getIp()));
        nameValuePairs.add(new BasicNameValuePair("latitude", Double.toString(dm.getLatLong().latitude)));
        nameValuePairs.add(new BasicNameValuePair("longitude", Double.toString(dm.getLatLong().longitude)));
        InputStream is=null;
        try
        {
            HttpClient httpclient = new DefaultHttpClient(); //Instanciation HttpClient
            HttpPost httppost = new HttpPost("http://bphengdy.esy.es/InsertIntoMarqueur.php"); //Req. HttpPost vers le fichier
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpclient.execute(httppost);
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error in http connection "+e.toString());
        }
    }

    //Delete marqueur by IP
    public void deleteMarqueurBDD(String ip) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); //ArrayList Clé-Valeur
        nameValuePairs.add(new BasicNameValuePair("ip",String.valueOf(ip))); //Ajout de l'ip dans l'ArrayList
        InputStream is=null;
        Log.i("DM","Delete : "+String.valueOf(ip));
        try
        {
            HttpClient httpclient = new DefaultHttpClient(); //Instanciation HttpClient
            HttpPost httppost = new HttpPost("http://bphengdy.esy.es/DeleteMarqueurByIp.php"); //Req. HttpPost vers le fichier extérieur
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpclient.execute(httppost);
        }
        catch(Exception e)
        {
            Log.e("log_tag", "Error in http connection "+e.toString());
        }
    }


}
