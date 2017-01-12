package alfonso8969.com.proyecto2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import alfonso8969.com.proyecto2.R;

/**
 * Creado por alfonso en fecha 08/11/2016.
 */

public class ProveedoresListaAdapter extends RecyclerView.Adapter<ProveedoresListaAdapter.ViewHolder> {

    private Context myContext;
    private CursorAdapter myCursorAdapter;
    public ProveedoresListaAdapter(Context context, Cursor cursor){

        myContext=context;
        myCursorAdapter=new CursorAdapter(myContext,cursor,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(context);
                return inflater.inflate(R.layout.proveedoresitems,parent,false);
            }
            @Override
            public void bindView(View view, final Context context, Cursor cursor) {
                AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
                TextView textViewId = (TextView) view.findViewById(R.id.idProveedor);
                TextView proveedorNombreCompañia = (TextView) view.findViewById(R.id.textproveedorNombreCompañia);
                TextView proveedorNombreContacto = (TextView)view.findViewById(R.id.textproveedorNombreContacto);

                textViewId.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0))));
                String nombreCompañia=(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
                String proveedorNombreContact=(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
                proveedorNombreCompañia.setText(nombreCompañia);
                proveedorNombreContacto.setText(proveedorNombreContact);

            }
        };
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.proveedoresitems, parent, false);

        ViewHolder viewHolder;
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        myCursorAdapter.getCursor().moveToPosition(position);
        myCursorAdapter.bindView(holder.itemView, myContext, myCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return myCursorAdapter.getCount();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewId, proveedorNombreCompañia,proveedorNombreContacto;


        ViewHolder(View itemView) {
            super(itemView);

            textViewId = (TextView) itemView.findViewById(R.id.idCliente);
            proveedorNombreCompañia = (TextView) itemView.findViewById(R.id.textproveedorNombreCompañia);
            proveedorNombreContacto = (TextView)itemView.findViewById(R.id.textproveedorNombreContacto);
        }
    }


}
