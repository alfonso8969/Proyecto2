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
 * Creado por alfonso en fecha 07/12/2016.
 */

public class BarCodeListaAdapter  extends RecyclerView.Adapter<BarCodeListaAdapter.ViewHolder>{

    private Context myContext;
    private CursorAdapter myCursorAdapter;

    public BarCodeListaAdapter(Context context, Cursor cursor){

        myContext=context;
        myCursorAdapter=new CursorAdapter(myContext,cursor,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(context);
                return inflater.inflate(R.layout.barcodeitems,parent,false);
            }
            @Override
            public void bindView(View view, final Context context, Cursor cursor) {
                AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
                TextView textViewId = (TextView) view.findViewById(R.id.idBarcode);
                TextView tvNombreProducto = (TextView) view.findViewById(R.id.edtNombreProducto);
                TextView tvFormato = (TextView)view.findViewById(R.id.tvFormato);
                TextView tvResultado = (TextView)view.findViewById(R.id.tvResultado);

                textViewId.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0))));
                String nombreProducto=(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
                String resultado=(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
                String tipo =  (cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));
                tvNombreProducto.setText(nombreProducto);
                tvFormato.setText(tipo);
                tvResultado.setText(resultado);
            }
        };

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.barcodeitems, parent, false);

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
        TextView textViewId, tvNombreProducto,tvFormato,tvResultado;


        ViewHolder(View itemView) {
            super(itemView);

            textViewId = (TextView) itemView.findViewById(R.id.idBarcode);
            tvNombreProducto = (TextView) itemView.findViewById(R.id.edtNombreProducto);
            tvFormato = (TextView)itemView.findViewById(R.id.tvFormato);
            tvResultado = (TextView)itemView.findViewById(R.id.tvResultado);

        }
    }
}
