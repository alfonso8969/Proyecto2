package alfonso8969.com.proyecto2.activities.empleados;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.fragments.empleados.Empleado_ItemsFragment;
import alfonso8969.com.proyecto2.fragments.empleados.EmpleadosListaFragment;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

/**
 * Creado por alfonso en fecha 18/10/2016.
 */

public class Empleados_itemsList extends AppCompatActivity {

    public ViewGroup containerEmp;
    public DataBaseHelper myDBHelper;
    public String empleadoID;
    public Cursor myCursor;
    Context context;
    public static TextView tvFechaNacimiento,tvFechaContratacion;
    public String nombreCompleto,nombre,apellido,puesto,tratoCortesia, direccion
            ,ciudad,region,codPos,pais, telefono, extension,notas,reportarA,avatarUri;
    public static  String strFechaNacimiento, strFechaContratacion;
    EmpleadosListaFragment empleadosListaFragment;
    SQLiteDatabase db;
    Button btnFechaNacimiento,btnFechaContratacion;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados_itemslist);

        Bundle intent = getIntent().getExtras();
        Bundle extras = getIntent().getExtras();
        empleadoID = extras.getString("empleadoId");
        if(savedInstanceState == null){

           Empleado_ItemsFragment fragment = new Empleado_ItemsFragment();
            fragment.setArguments(intent);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.empleados_list_item,fragment)
                    .commit();
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detaileditdel, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId()==R.id.action_edit){
          context = this;

            myDBHelper=new DataBaseHelper(getApplication().getApplicationContext());
            myDBHelper.openDatabase();
            myCursor= myDBHelper.fetchEmpleadoById(empleadoID);
            myDBHelper.close();

            if (myCursor != null) {
                 nombre = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(1))));
                 apellido = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(2))));
                 puesto = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(3))));
                 tratoCortesia = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(4))));
                 strFechaNacimiento = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(5))));
                 strFechaContratacion = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(6))));
                 direccion = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(7))));
                 ciudad = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(8))));
                 region = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(9))));
                 codPos = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(10))));
                 pais = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(11))));
                 telefono = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(12))));
                 extension = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(13))));
                 notas = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(14))));
                 reportarA = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(15))));
                 avatarUri = myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(16)));
                 nombreCompleto=apellido+", "+nombre;
            }

            Toast.makeText(getApplicationContext(),"Editando empleado: "+nombreCompleto, Toast.LENGTH_SHORT).show();


            empleadosListaFragment = new EmpleadosListaFragment();
            containerEmp= empleadosListaFragment.containerEmp;
            final View viewEditarEmpleado= EmpleadosListaFragment.inflaterEmp.inflate(R.layout.insertar_empleado,containerEmp,false);

            final EditText edtImagen        =(EditText)viewEditarEmpleado.findViewById(R.id.edt_imagen) ;
            final EditText edtNombre        =(EditText)viewEditarEmpleado.findViewById(R.id.edt_nombre);
            final EditText edtApellido      =(EditText)viewEditarEmpleado.findViewById(R.id.edt_apellido);
            final EditText edtTratoCortesia =(EditText)viewEditarEmpleado.findViewById(R.id.edt_traCort);
            final EditText edtPuesto        =(EditText)viewEditarEmpleado.findViewById(R.id.edt_puesto);
            final EditText edtDireccion     =(EditText)viewEditarEmpleado.findViewById(R.id.edt_direccion);
            final EditText edtCiudad        =(EditText)viewEditarEmpleado.findViewById(R.id.edt_ciudad);
            final EditText edtRegion        =(EditText)viewEditarEmpleado.findViewById(R.id.edt_region);
            final EditText edtCodPost       =(EditText)viewEditarEmpleado.findViewById(R.id.edt_codPostal);
            final EditText edtPais          =(EditText)viewEditarEmpleado.findViewById(R.id.edt_pais);
            final EditText edtTelefono      =(EditText)viewEditarEmpleado.findViewById(R.id.edt_telefono);
            final EditText edtExtension     =(EditText)viewEditarEmpleado.findViewById(R.id.edt_extension);
            final EditText edtNotas         =(EditText)viewEditarEmpleado.findViewById(R.id.edt_notas);
            final EditText edtReportarA     =(EditText)viewEditarEmpleado.findViewById(R.id.edt_reportar);
            tvFechaNacimiento = (TextView)viewEditarEmpleado.findViewById(R.id.tv_fechaNacimiento);
            tvFechaContratacion = (TextView)viewEditarEmpleado.findViewById(R.id.tv_fechaContratacion);
            edtImagen            .setText(avatarUri);
            edtNombre            .setText(nombre);
            edtApellido          .setText(apellido);
            edtTratoCortesia     .setText(tratoCortesia);
            edtPuesto            .setText(puesto);
            tvFechaNacimiento    .setText(strFechaNacimiento);
            tvFechaContratacion  .setText(strFechaContratacion);
            edtDireccion         .setText(direccion);
            edtCiudad            .setText(ciudad);
            edtRegion            .setText(region);
            edtCodPost           .setText(codPos);
            edtPais              .setText(pais);
            edtTelefono          .setText(telefono);
            edtExtension         .setText(extension);
            edtNotas             .setText(notas);
            edtReportarA         .setText(reportarA);

            btnFechaNacimiento = (Button) viewEditarEmpleado.findViewById(R.id.btn_fechaNacimiento);
            btnFechaNacimiento.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DialogFragment newFragment = new SelectDateFragmentFechaNacimiento();
                    newFragment.show(getSupportFragmentManager(), "DatePicker");

                }
            });

            btnFechaContratacion = (Button)viewEditarEmpleado.findViewById(R.id.btn_fechaContratacion);
            btnFechaContratacion.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DialogFragment newFragment = new SelectDateFragmentFechaContratacion();
                    newFragment.show(getSupportFragmentManager(), "DatePicker");
                }
            });
            final Builder builderEditarEmpleado = new Builder(this);
            builderEditarEmpleado.setTitle("Editar empleado")
            .setView(viewEditarEmpleado)
            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myDBHelper=new DataBaseHelper(getApplication().getApplicationContext());
                    myDBHelper.openDatabase();
                    db = myDBHelper.getWritableDatabase();
                    ContentValues valores = new ContentValues();

                    nombre = edtNombre.getText().toString();
                    apellido = edtApellido.getText().toString();
                    if (nombre.isEmpty() || apellido.isEmpty()) {
                        String message;
                        if (nombre.isEmpty())
                            message="Debe asignar Nombre";
                        else if (apellido.isEmpty())
                            message="Debe asignar Apellido";
                        else
                        message="Debe asignar Nombre y Apellido";
                        Builder builderEditClienteErr = new Builder(context);
                        builderEditClienteErr.setTitle("Error al actualizar Empleado")
                        .setMessage(message)
                        .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((ViewGroup)viewEditarEmpleado.getParent()).removeView(viewEditarEmpleado);
                                builderEditarEmpleado.show();
                                if (nombre.isEmpty())
                                edtNombre.requestFocus();
                                if (apellido.isEmpty())
                                edtApellido.requestFocus();
                                else
                                edtNombre.requestFocus();
                            }
                        })
                        .show();
                    }else{
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
                    String where = "_id" + "=?";
                    String[] whereArgs = new String[]{empleadoID};

                    try {

                        if (db.update("Empleados", valores,where, whereArgs)!=0) {
                            Builder builderInsertEmpSuccess = new Builder(context);
                            builderInsertEmpSuccess.setTitle("Editar empleado")
                            .setMessage("Se actualizó el empleado: " + nombreCompleto)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(context,EmpleadosActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                }
                            })
                            .show();
                        }
                        db.close();
                        myDBHelper.close();
                    } catch (SQLException e) {
                        Builder builderInsertEmpErr = new Builder(context);
                        builderInsertEmpErr.setTitle("Error al editar empleado")
                        .setMessage(e.getMessage())
                        .setNegativeButton("Aceptar",null)
                        .show();
                         }
                    }
                }
            })
            .setNegativeButton("Cancelar",null )
            .show();
        }

            if (item.getItemId() == R.id.action_delete) {
                context=this;
                myDBHelper=new DataBaseHelper(getApplication().getApplicationContext());
                myDBHelper.openDatabase();
                db=myDBHelper.getWritableDatabase();
                myCursor= myDBHelper.fetchEmpleadoById(empleadoID);
                if (myCursor != null) {
                    nombre = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(1))));
                    apellido = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(2))));

                    nombreCompleto=apellido+", "+nombre;
                }

                Builder dialog = new Builder(this);
                dialog.setTitle("Eliminar Empleado")
                .setMessage("¿Seguro que deseas eliminar al Empleado: " + nombreCompleto + "?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String where = "_id" + "=?";
                        String[] whereArgs = new String[]{empleadoID};

                        db.delete("Empleados", where, whereArgs);

                        db.close();
                        myDBHelper.close();
                        Toast.makeText(getApplicationContext(), "Se borro el empleado: " + nombreCompleto, Toast.LENGTH_SHORT).show();

                        try {
                            myDBHelper.openDatabase();
                            Cursor nCursor = myDBHelper.fetchAllEmpleados();
                            myDBHelper.close();
                            if (nCursor != null) {
                                Intent i = new Intent(context,EmpleadosActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }


                            if (EmpleadosActivity.mTwoPane) {
                                ((EmpleadosListaFragment.Refresh) getApplication()).refreshList();
                            }

                        } catch (SQLException e) {
                            Builder builderInsertEmpErr = new Builder(context);
                            builderInsertEmpErr.setTitle("Error al borrar empleado")
                            .setMessage(e.getMessage())
                            .setNegativeButton("Aceptar", null)

                                    .show();
                        }

                    }
                })
                .show();

            }

        return super.onOptionsItemSelected(item);
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
