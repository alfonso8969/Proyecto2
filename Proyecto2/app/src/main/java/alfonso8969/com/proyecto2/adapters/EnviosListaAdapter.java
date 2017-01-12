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
 * Creado por alfonso en fecha 15/11/2016.
 */

public class EnviosListaAdapter extends RecyclerView.Adapter<EnviosListaAdapter.ViewHolder>  {

    private Context myContext;
    private CursorAdapter myCursorAdapter;


    public EnviosListaAdapter(Context context, Cursor cursor){

        myContext=context;
        myCursorAdapter=new CursorAdapter(myContext,cursor,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(context);
                return inflater.inflate(R.layout.enviositems,parent,false);
            }
            @Override
            public void bindView(View view, final Context context, Cursor cursor) {
                AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
                TextView textViewId = (TextView) view.findViewById(R.id.idEnvio);
                TextView envioNombreCompañia = (TextView) view.findViewById(R.id.textEnvioNombreCompañia);

                textViewId.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0))));
                String nombreCompañia=(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
                envioNombreCompañia.setText(nombreCompañia);


            }
        };
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.enviositems, parent, false);

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
        TextView textViewId, envioNombreCompañia;


        ViewHolder(View itemView) {
            super(itemView);

            textViewId = (TextView) itemView.findViewById(R.id.idEnvio);
            envioNombreCompañia = (TextView) itemView.findViewById(R.id.textEnvioNombreCompañia);

        }
    }
}
