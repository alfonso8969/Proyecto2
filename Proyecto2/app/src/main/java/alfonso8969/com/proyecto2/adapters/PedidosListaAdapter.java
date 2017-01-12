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
 * Creado por alfonso en fecha 16/11/2016.
 */

public class PedidosListaAdapter  extends RecyclerView.Adapter<PedidosListaAdapter.ViewHolder> {

    private Context myContext;
    private CursorAdapter myCursorAdapter;

    public PedidosListaAdapter(Context context, Cursor cursor) {

        myContext = context;
        myCursorAdapter = new CursorAdapter(myContext, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(context);
                return inflater.inflate(R.layout.pedidositems, parent, false);
            }
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
                TextView textViewId = (TextView) view.findViewById(R.id.idPedido);
                TextView tvNumeroPedido = (TextView) view.findViewById(R.id.numeroPedido);
                TextView tvNombreCompañiaPedido = (TextView) view.findViewById(R.id.nombreCompañiaPedido);
                TextView tvFechaPedido = (TextView) view.findViewById(R.id.fechaPedido);
                TextView tvFechaEntrega = (TextView)view.findViewById(R.id.fechaEntrega);


                textViewId.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0))));
                tvNumeroPedido.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
                String strNombreCompañiaPedido = (cursor.getString(cursor.getColumnIndex(cursor.getColumnName(9))));
                tvNombreCompañiaPedido.setText(strNombreCompañiaPedido);
                String strFechaPedido = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4)));
                tvFechaPedido.setText(strFechaPedido);
                String strFechaEntrega = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6)));
                String messageFechaEntrega;
                if(strFechaEntrega==null)
                    messageFechaEntrega="Sin entregar";
                else
                    messageFechaEntrega=strFechaEntrega;
                tvFechaEntrega.setText(messageFechaEntrega);
            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pedidositems, parent, false);

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
        TextView textViewId, tvNombreCompañiaPedido, tvFechaPedido;


        ViewHolder(View itemView) {
            super(itemView);

            textViewId = (TextView) itemView.findViewById(R.id.idPedido);
            tvNombreCompañiaPedido = (TextView) itemView.findViewById(R.id.nombreCompañiaPedido);
            tvFechaPedido = (TextView) itemView.findViewById(R.id.fechaPedido);
        }

    }
}