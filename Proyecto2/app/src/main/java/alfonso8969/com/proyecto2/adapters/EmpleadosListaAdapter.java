package alfonso8969.com.proyecto2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import alfonso8969.com.proyecto2.R;

/**
 * Creado por alfonso en fecha 13/10/2016.
 */

public class EmpleadosListaAdapter extends RecyclerView.Adapter<EmpleadosListaAdapter.ViewHolder> {
    private Context myContext;
    private CursorAdapter myCursorAdapter;

    public EmpleadosListaAdapter(Context context, Cursor cursor){

        myContext=context;
        myCursorAdapter=new CursorAdapter(myContext,cursor,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(context);
                return inflater.inflate(R.layout.empleadositems,parent,false);
            }
            @Override
            public void bindView(View view, final Context context, Cursor cursor) {
                AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
                TextView textViewId = (TextView) view.findViewById(R.id.id);
                TextView empleadoNombreApellido = (TextView) view.findViewById(R.id.textempleadoNombreCompleto);
                final ImageView empleadoImagen=(ImageView) view.findViewById(R.id.imagenEmpleado);


                textViewId.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0))));
                String nombre=(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
                String apellido=(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
                empleadoNombreApellido.setText(apellido+", "+nombre);
                String avatarUri =cursor.getString(cursor.getColumnIndex(cursor.getColumnName(16)));

                     /*  Glide
                                .with(context)
                                .load(Uri.parse("file:///android_asset/" + avatarUri))
                                .asBitmap()
                                .error(R.drawable.ic2_account_circle)
                                .centerCrop()
                                .into(new BitmapImageViewTarget(empleadoImagen) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable drawable
                                                = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                        drawable.setCircular(true);
                                        empleadoImagen.setImageDrawable(drawable);
                                    }
                                });*/
                Glide
                        .with(context)
                        .load(Uri.parse("file:///sdcard/Android/data/alfonso8969.com.proyecto2/cache/temp/" + avatarUri))
                        .asBitmap()
                        .error(R.drawable.ic_launcher2)
                        .centerCrop()
                        .into(new BitmapImageViewTarget(empleadoImagen) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable drawable
                                        = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                drawable.setCircular(true);
                                empleadoImagen.setImageDrawable(drawable);
                            }
                        });

            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.empleadositems, parent, false);

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
        TextView textViewId, empleadoNombreCompleto;
        ImageView empleadoImagen;

        ViewHolder(View itemView) {
            super(itemView);

            textViewId = (TextView) itemView.findViewById(R.id.id);
            empleadoNombreCompleto = (TextView) itemView.findViewById(R.id.textempleadoNombreCompleto);
            empleadoImagen = (ImageView) itemView.findViewById(R.id.imagenEmpleado);
        }
    }

}