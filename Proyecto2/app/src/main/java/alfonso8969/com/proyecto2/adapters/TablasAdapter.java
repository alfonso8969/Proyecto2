package alfonso8969.com.proyecto2.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import alfonso8969.com.proyecto2.clases.Tablas;
import alfonso8969.com.proyecto2.R;

/**
 * Creado por alfonso en fecha 02/10/2016.
 */


public class
TablasAdapter extends ArrayAdapter<Tablas> {
    private Context context;
    private int LayoutResourceID;
    private Tablas[] datos = null;



    public TablasAdapter(Context context, int resource, Tablas[] datos) {
        super(context, resource, datos);
        this.context = context;
        this.LayoutResourceID = resource;
        this.datos = datos;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
    View row = convertView;
    TablasHolder holder ;
        if (row==null){
            LayoutInflater inflater =((Activity)context).getLayoutInflater();
            row=inflater.inflate(LayoutResourceID,parent,false);

            holder = new TablasHolder();
            holder.imagen=(ImageView)row.findViewById(R.id.imagen);
            holder.texto=(TextView)row.findViewById(R.id.textViewNombreTabla);
            row.setTag(holder);
        }
        else {
        holder=(TablasHolder) row.getTag();
        }

        Tablas tablas = datos[position];
        holder.texto.setText(tablas.nomTabla);
        holder.imagen.setImageResource(tablas.icon);

      return  row;
    }

    private static class TablasHolder{
        ImageView imagen;
        TextView texto;
    }
}
