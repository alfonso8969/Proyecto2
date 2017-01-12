package alfonso8969.com.proyecto2.fragments.empleados;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.activities.empleados.EmpleadosActivity;
import alfonso8969.com.proyecto2.activities.fotos.AddNewPhoto;
import alfonso8969.com.proyecto2.adapters.EmpleadosListaAdapter;
import alfonso8969.com.proyecto2.clases.RecyclerItemClickListener;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

/**
 * Creado por alfonso en fecha 13/10/2016.
 */

public class EmpleadosListaFragment extends Fragment {
    RecyclerView recyclerViewEmpleados;
    EmpleadosListaAdapter empleadosListaAdapter;
    DataBaseHelper myDBHelper;
    public static final String TAG="RecyclerViewEmpleadosFragment";
    TextView delete;
    TextView tvNombreEmpleado;
    public static TextView tvFechaNacimiento,tvFechaContratacion;
    String empleadoID;
    String strNombreEmpleado;
    public static String strFechaNacimiento;
    public static String strFechaContratacion;
    FloatingActionButton fb;
    Button btnFechaNacimiento,btnFechaContratacion;
    public  ViewGroup containerEmp;
    public static LayoutInflater inflaterEmp;

    public interface CallBacks{
          void onItemSelected(String empleadoId);
    }
    public  interface Refresh{
          void refreshList();
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle saveInstanceState){
    View viewRoot= inflater.inflate(R.layout.lista_empleados_fragments,container,false);
    viewRoot.setTag(TAG);

        containerEmp=container;
        inflaterEmp=inflater;
        recyclerViewEmpleados=(RecyclerView)viewRoot.findViewById(R.id.lista_Empleados);
        recyclerViewEmpleados.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        fb=(FloatingActionButton)viewRoot.findViewById(R.id.fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onCreateView(inflater,container,saveInstanceState);
                final View viewAgregarNuevoEmpleado=inflater.inflate(R.layout.insertar_empleado,container,false);

                final EditText edtImagen=(EditText)viewAgregarNuevoEmpleado.findViewById(R.id.edt_imagen) ;
                edtImagen.setText(AddNewPhoto.nombreFotoBuscar);
                final EditText edtNombre=(EditText)viewAgregarNuevoEmpleado.findViewById(R.id.edt_nombre);
                final EditText edtApellido=(EditText)viewAgregarNuevoEmpleado.findViewById(R.id.edt_apellido);
                final EditText edtTratoCortesia=(EditText)viewAgregarNuevoEmpleado.findViewById(R.id.edt_traCort);
                final EditText edtPuesto=(EditText)viewAgregarNuevoEmpleado.findViewById(R.id.edt_puesto);
                final EditText edtDireccion=(EditText)viewAgregarNuevoEmpleado.findViewById(R.id.edt_direccion);
                final EditText edtCiudad=(EditText)viewAgregarNuevoEmpleado.findViewById(R.id.edt_ciudad);
                final EditText edtRegion=(EditText)viewAgregarNuevoEmpleado.findViewById(R.id.edt_region);
                final EditText edtCodPost=(EditText)viewAgregarNuevoEmpleado.findViewById(R.id.edt_codPostal);
                final EditText edtPais=(EditText)viewAgregarNuevoEmpleado.findViewById(R.id.edt_pais);
                final EditText edtTelefono=(EditText)viewAgregarNuevoEmpleado.findViewById(R.id.edt_telefono);
                final EditText edtExtension=(EditText)viewAgregarNuevoEmpleado.findViewById(R.id.edt_extension);
                final EditText edtNotas=(EditText)viewAgregarNuevoEmpleado.findViewById(R.id.edt_notas);
                final EditText edtReportarA=(EditText)viewAgregarNuevoEmpleado.findViewById(R.id.edt_reportar);

                tvFechaNacimiento = (TextView)viewAgregarNuevoEmpleado.findViewById(R.id.tv_fechaNacimiento);
                btnFechaNacimiento = (Button) viewAgregarNuevoEmpleado.findViewById(R.id.btn_fechaNacimiento);
                btnFechaNacimiento.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        DialogFragment newFragment = new SelectDateFragmentFechaNacimiento();
                        newFragment.show(getFragmentManager(), "DatePicker");

                    }
                });

                tvFechaContratacion = (TextView)viewAgregarNuevoEmpleado.findViewById(R.id.tv_fechaContratacion);
                btnFechaContratacion = (Button)viewAgregarNuevoEmpleado.findViewById(R.id.btn_fechaContratacion);
                btnFechaContratacion.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        DialogFragment newFragment = new SelectDateFragmentFechaContratacion();
                        newFragment.show(getFragmentManager(), "DatePicker");
                    }
                });

                final Builder builderAgregarNuevoEmpleado = new Builder(getActivity());
                builderAgregarNuevoEmpleado.setView(viewAgregarNuevoEmpleado)
                .setTitle("Agregar un empleado")
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBaseHelper myDbHelper = new DataBaseHelper(getActivity().getApplicationContext());
                        SQLiteDatabase db = myDbHelper.getWritableDatabase();
                        ContentValues valores = new ContentValues();

                        if (edtNombre.getText().toString().isEmpty() || edtApellido.getText().toString().isEmpty()){
                            String message=null;
                            if (edtNombre.getText().toString().isEmpty() && edtApellido.getText().toString().isEmpty())
                                message="Debe asignar Nombre y Apellido";
                            else if (edtApellido.getText().toString().isEmpty())
                                message="Debe asignar Apellido";
                            else if (edtNombre.getText().toString().isEmpty())
                                message="Debe asignar Nombre";
                            Builder builderEditClienteErr = new Builder(getActivity());
                            builderEditClienteErr.setTitle("Error al insertar Empleado")
                            .setMessage(message)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((ViewGroup)viewAgregarNuevoEmpleado.getParent()).removeView(viewAgregarNuevoEmpleado);
                                    builderAgregarNuevoEmpleado.show();
                                    if (edtNombre.getText().toString().isEmpty() && edtApellido.getText().toString().isEmpty())
                                        edtNombre.requestFocus();
                                    else if (edtApellido.getText().toString().isEmpty())
                                        edtApellido.requestFocus();
                                    else if(edtNombre.getText().toString().isEmpty())
                                        edtNombre.requestFocus();
                                }
                            })
                            .show();

                        }
                        else{
                            valores.put("Nombre", edtNombre.getText().toString());
                            valores.put("Apellido", edtApellido.getText().toString());
                            valores.put("Puesto", edtPuesto.getText().toString());
                            valores.put("TituloDeCortesia", edtTratoCortesia.getText().toString());
                            valores.put("FechaNacimiento", strFechaNacimiento);
                            valores.put("FechaContratacion", strFechaContratacion);
                            valores.put("Direccion", edtDireccion.getText().toString());
                            valores.put("Ciudad", edtCiudad.getText().toString());
                            valores.put("Region", edtRegion.getText().toString());
                            valores.put("CodigoPostal", edtCodPost.getText().toString());
                            valores.put("Pais", edtPais.getText().toString());
                            valores.put("Telefono", edtTelefono.getText().toString());
                            valores.put("Extension", edtExtension.getText().toString());
                            valores.put("Notas", edtNotas.getText().toString());
                            valores.put("ReportarA", edtReportarA.getText().toString());
                            valores.put("Foto", edtImagen.getText().toString());

                            try {

                                if ( db.insert("Empleados", null, valores) != 0) {
                                    Builder builderInsertEmpSuccess = new Builder(getActivity());
                                    builderInsertEmpSuccess.setTitle("Nuevo empleado")
                                    .setMessage("Se agregó el empleado: " + edtNombre.getText().toString()
                                            +" "+edtApellido.getText().toString())
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            onDestroyInsert();
                                        }
                                    })
                                    .show();
                                }
                                db.close();
                            } catch (SQLException e) {
                                Builder builderInsertEmpErr = new Builder(getActivity());
                                builderInsertEmpErr.setTitle("Error  al insertar empleado")
                                .setMessage(e.getMessage())
                                .setPositiveButton("Aceptar", null)
                                .show();
                            }
                        }
                    }
                })
                .setNeutralButton("Foto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (edtImagen.getText().toString().isEmpty()) {
                            Builder builderInsertFoto = new Builder(getActivity());
                            builderInsertFoto.setTitle("Error  al insertar Foto")
                            .setMessage("Debe asignar un nombre de archivo imagen")
                            .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((ViewGroup)viewAgregarNuevoEmpleado.getParent()).removeView(viewAgregarNuevoEmpleado);
                                    builderAgregarNuevoEmpleado.show();
                                    edtImagen.requestFocus();
                                }
                            })
                            .show();

                        }
                        else{
                        Intent intent = new Intent(getActivity(), AddNewPhoto.class);
                        String nombreFoto = edtImagen.getText().toString();
                        intent.putExtra("nombreFoto", nombreFoto);
                        startActivity(intent);
                         }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .setIcon(R.drawable.empleados)
                .show();
            }
        });

        myDBHelper = new DataBaseHelper(getActivity().getApplicationContext());

        try {
            myDBHelper.createDataBase();
        } catch (IOException e) {
            throw new Error("No se puede crear BD");
        }

        try {
            myDBHelper.openDatabase();
            Cursor cursor = myDBHelper.fetchAllEmpleados();
            if(cursor != null){
                empleadosListaAdapter = new EmpleadosListaAdapter(getActivity().getApplicationContext(),cursor);
                recyclerViewEmpleados.setAdapter(empleadosListaAdapter);
                recyclerViewEmpleados.addOnItemTouchListener(new RecyclerItemClickListener(getActivity()
                        .getApplicationContext(), new OnItemClickListener()));

            myDBHelper.close();
            }
        } catch (SQLException e) {
            throw new Error("Fallo en BD: "+e.getMessage()+"\n"+e.getCause());
        }

        return  viewRoot;
    }
    private class OnItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener{

        public void onItemClick(View childView, int position){
            TextView textViewId = (TextView) childView.findViewById(R.id.id);
            empleadoID=textViewId.getText().toString();
            tvNombreEmpleado = (TextView) childView.findViewById(R.id.textempleadoNombreCompleto);
            strNombreEmpleado = tvNombreEmpleado.getText().toString();
            ((CallBacks) getActivity()).onItemSelected(empleadoID);
            Toast.makeText(getActivity(), "Ha seleccionado el empleado: " + strNombreEmpleado
                    +"\nCon Id: "+empleadoID, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onItemLongPress(View childView, int position){

            delete = (TextView) childView.findViewById(R.id.id);
            tvNombreEmpleado = (TextView) childView.findViewById(R.id.textempleadoNombreCompleto);
            empleadoID = delete.getText().toString();
            strNombreEmpleado =tvNombreEmpleado.getText().toString();
            Builder dialog = new Builder(getActivity());
            dialog.setTitle("Eliminar Empleado")
            .setMessage("¿Seguro que deseas eliminar al Empleado: "+ strNombreEmpleado +"?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
            myDBHelper = new DataBaseHelper(getActivity().getApplication());
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            String where = "_id" + "=?";
            String[] whereArgs = new String[]{empleadoID};

            db.delete("Empleados", where, whereArgs);

            db.close();

            Toast.makeText(getActivity(), "Se borro el empleado: " + strNombreEmpleado, Toast.LENGTH_SHORT).show();

            try {
                myDBHelper.openDatabase();
                Cursor nCursor = myDBHelper.fetchAllEmpleados();
                myDBHelper.close();
                if (nCursor != null) {
                    empleadosListaAdapter = new EmpleadosListaAdapter(getActivity().getApplicationContext(), nCursor);
                    recyclerViewEmpleados.setAdapter(empleadosListaAdapter);
                    recyclerViewEmpleados.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new OnItemClickListener()));
                }


                if (EmpleadosActivity.mTwoPane) {
                    ((Refresh)getActivity()).refreshList();
                }

            } catch (SQLException e) {
                Builder builderInsertEmpErr = new Builder(getActivity());
                builderInsertEmpErr.setTitle("Error al borrar el empleado")
                .setMessage(e.getMessage())
                .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
            }

                }
            })
            .show();
        }

    }
    public void onDestroyInsert(){
        Intent intent = new Intent(getActivity(), EmpleadosActivity.class);
        getActivity().finish();
        startActivity(intent);
    }
    public static class SelectDateFragmentFechaNacimiento extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            SetDateFechaNacimiento(yy, mm+1, dd);
        }

        private void SetDateFechaNacimiento(int mYear, int mMonth, int mDay) {
            tvFechaNacimiento.setText(new StringBuilder()
                    .append(mDay).append("/")
                    .append(mMonth).append("/")
                    .append(mYear).append(" "));
            strFechaNacimiento = tvFechaNacimiento.getText().toString();
        }

    }
    public static class SelectDateFragmentFechaContratacion extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            SetDateFechaContratacion(yy, mm+1, dd);
        }

        private void SetDateFechaContratacion(int mYear, int mMonth, int mDay) {
            tvFechaContratacion.setText(new StringBuilder()
                    .append(mDay).append("/")
                    .append(mMonth).append("/")
                    .append(mYear).append(" "));
            strFechaContratacion = tvFechaContratacion.getText().toString();
        }
    }
}
