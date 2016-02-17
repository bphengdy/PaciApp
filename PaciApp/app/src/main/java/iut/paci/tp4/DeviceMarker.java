package iut.paci.tp4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.overlay.Marker;

import iut.paci.paciapp.ChatActivity;
import iut.paci.paciapp.R;

/**
 * Created by John on 01/02/2016.
 */
public class DeviceMarker extends Marker {

    private Device dev;
    private Context ctx;

    public DeviceMarker(LatLong latLong, Bitmap bitmap, int horizontalOffset, int verticalOffset, Device dev,Context ctx) {
        super(latLong, bitmap, horizontalOffset, verticalOffset);
        this.dev = dev;
        this.ctx = ctx;
    }

    public boolean onTap(LatLong geoPoint, Point viewPosition, Point tapPoint) {
        if(contains(viewPosition, tapPoint)) {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(ctx);
            alertDialog2.setTitle("Informations");
            alertDialog2.setMessage("Nom du téléphone : " + getDev().getNom() + "\nIP : " + getDev().getIp() + "\nChatter avec ?");
            //_____ Oui
            alertDialog2.setPositiveButton("Oui",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            Intent intent = new Intent(ctx, ChatActivity.class);
                            intent.putExtra("IP", getDev().getIp());
                            intent.putExtra("nomTel", getDev().getNom());
                            ctx.startActivity(intent);
                        }
                    });
            //_____ Non
            alertDialog2.setNegativeButton("Non",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            dialog.cancel();
                        }
                    });
            alertDialog2.show();
            return true;
        }
        return false;
    }

    public Device getDev() {
        return dev;
    }
}
