package alfonso8969.com.proyecto2.fragments.productos;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.IOException;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

/**
 * Creado por alfonso en fecha 09/11/2016.
 */

public class Productos_ItemsFragment extends Fragment {
    public TextView tvNombreProducto,tvProveedorId,tvNombreProveedor,tvCategoriaId,tvNombreCategoria,tvCantidadUnidad;
    public TextView tvPrecioUnidad,tvUnidadStock,tvUnidadPedido,tvNivelReaprovisionamiento,tvInterrumpido;
    public ImageView imagenProducto;
    public String productoId,strNombreProducto,strInterrumpido,avatarUri,strCantidadUnidad;
    public int intUnidadStock,intUnidadPedido,intNivelReaprovisionamiento,intInterrumpido;
    public double precioUnidad;
    private Context context;
    public DataBaseHelper myDBHelper;
    public Cursor myCursor;
    public ListView lv;
    public Cursor myCursorNombreProveedor;
    public Cursor myCursorNombreCategoria;
    public static int intProveedorId;
    public static int intCategoriaId;

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.productos_itemsfragment, container, false);
        lv = (ListView)rootView.findViewById(R.id.lista_Productos);
        imagenProducto =(ImageView) rootView.findViewById(R.id.imagenProducto);
        tvNombreProducto = (TextView)rootView.findViewById(R.id.tv_nombreProducto);
        tvProveedorId = (TextView)rootView.findViewById(R.id.tv_proveedorId);
        tvNombreProveedor = (TextView)rootView.findViewById(R.id.tv_nombreProveedor);
        tvCategoriaId = (TextView)rootView.findViewById(R.id.tv_categoriaID);
        tvNombreCategoria = (TextView)rootView.findViewById(R.id.tv_nombreCategoria);
        tvCantidadUnidad = (TextView)rootView.findViewById(R.id.tv_CantidadPorUnidad);
        tvPrecioUnidad = (TextView)rootView.findViewById(R.id.tv_PrecioPorUnidad);
        tvUnidadStock = (TextView)rootView.findViewById(R.id.tv_UnidadesEnStock);
        tvUnidadPedido = (TextView)rootView.findViewById(R.id.tv_UnidadesEnPedido);
        tvNivelReaprovisionamiento = (TextView)rootView.findViewById(R.id.tv_NivelDeReaprovisionamiento);
        tvInterrumpido = (TextView)rootView.findViewById(R.id.tv_Interrumpido);


        context=getActivity().getApplicationContext();

        Bundle bundle = getArguments();

        if(bundle != null) {

            boolean restart = bundle.getBoolean("restart");

            if (restart) {
                lv.setAdapter(null);
            } else {

                productoId = (String) bundle.get("productoId");
                myDBHelper = new DataBaseHelper(getActivity());


                try {
                    myDBHelper.createDataBase();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myDBHelper.openDatabase();
                myCursor = myDBHelper.fetchProductoById(productoId);

                if (myCursor != null) {
                    strNombreProducto = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(1))));
                    intProveedorId = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(2))));
                    intCategoriaId = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(3))));
                    strCantidadUnidad = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(4))));
                    precioUnidad= (myCursor.getDouble(myCursor.getColumnIndex(myCursor.getColumnName(5))));
                    intUnidadStock = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(6))));
                    intUnidadPedido = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(7))));
                    intNivelReaprovisionamiento = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(8))));
                    intInterrumpido = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(9))));
                    strInterrumpido=(intInterrumpido==1) ? "Si": "No";
                    avatarUri = myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(10)));

                    myCursorNombreProveedor = myDBHelper.fetchNombreProveedor(String.valueOf(intProveedorId));
                    myCursorNombreCategoria = myDBHelper.fetchNombreCategoria(String.valueOf(intCategoriaId));
                    myDBHelper.close();

                    tvNombreProducto.setText(strNombreProducto);
                    tvProveedorId.setText(String.valueOf(intProveedorId));
                    tvNombreProveedor.setText(myCursorNombreProveedor.getString(myCursorNombreProveedor.getColumnIndex(myCursorNombreProveedor.getColumnName(0))));
                    tvCategoriaId.setText(String.valueOf(intCategoriaId));
                    tvNombreCategoria.setText(myCursorNombreCategoria.getString(myCursorNombreCategoria.getColumnIndex(myCursorNombreCategoria.getColumnName(0))));
                    tvCantidadUnidad.setText(strCantidadUnidad);
                    tvPrecioUnidad.setText(String.valueOf(precioUnidad));
                    tvUnidadStock.setText(String.valueOf(intUnidadStock));
                    tvUnidadPedido.setText(String.valueOf(intUnidadPedido));
                    tvNivelReaprovisionamiento.setText(String.valueOf(intNivelReaprovisionamiento));
                    tvInterrumpido.setText(strInterrumpido);



                    Glide
                            .with(context)
                            .load(Uri.parse("file:///sdcard/Android/data/alfonso8969.com.proyecto2/cache/temp/" + avatarUri))
                            .asBitmap()
                            .error(R.drawable.emptyproduct)
                            .centerCrop()
                            .into(new BitmapImageViewTarget(imagenProducto) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable drawable
                                            = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    drawable.setCircular(true);
                                    imagenProducto.setImageDrawable(drawable);
                                }
                            });
                }
            }
        }

        return rootView;
    }

}


