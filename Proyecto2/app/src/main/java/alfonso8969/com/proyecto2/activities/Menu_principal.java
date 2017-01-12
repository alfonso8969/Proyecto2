package alfonso8969.com.proyecto2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.activities.barCode.BarcodeActivity;
import alfonso8969.com.proyecto2.activities.clientes.ClientesActivity;
import alfonso8969.com.proyecto2.activities.empleados.EmpleadosActivity;
import alfonso8969.com.proyecto2.activities.envios.EnviosActivity;
import alfonso8969.com.proyecto2.activities.pedidos.PedidosActivity;
import alfonso8969.com.proyecto2.activities.productos.ProductosActivity;
import alfonso8969.com.proyecto2.activities.proveedores.ProveedoresActivity;
import alfonso8969.com.proyecto2.adapters.TablasAdapter;
import alfonso8969.com.proyecto2.clases.Tablas;

public class Menu_principal extends AppCompatActivity {
    ListView lv;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);


        final Tablas tablas_datos[] = new Tablas[]{
                new Tablas(R.drawable.empleados,"Empleados"),
                new Tablas(R.drawable.clientes,"Clientes"),
                new Tablas(R.drawable.productos,"Productos"),
                new Tablas(R.drawable.proveedores,"Proveedores"),
                new Tablas(R.drawable.envios,"Envios"),
                new Tablas(R.drawable.pedido,"Pedidos"),
                new Tablas(R.drawable.codigobarras,"Barcodes")

        };

        TablasAdapter adapter =new TablasAdapter(this,R.layout.listview_item_row,tablas_datos);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        lv= (ListView)findViewById(R.id.lvItemsTablas);
        View header =  getLayoutInflater().inflate(R.layout.listview_header_row,null);
        lv.addHeaderView(header);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mostrarTablas;
                String ID = String.valueOf(lv.getItemIdAtPosition(position));
                int Id = Integer.valueOf(ID);
                String nombre= tablas_datos[Id].nomTabla;
                Toast.makeText(getApplicationContext(),"Tabla seleccionada: "+ nombre,Toast.LENGTH_SHORT).show();
                switch (nombre){
                    case "Empleados":
                        mostrarTablas= new Intent(Menu_principal.this,EmpleadosActivity.class);
                        startActivity(mostrarTablas);
                        break;
                    case "Clientes":
                        mostrarTablas= new Intent(Menu_principal.this, ClientesActivity.class);
                        startActivity(mostrarTablas);
                        break;
                    case "Proveedores":
                        mostrarTablas= new Intent(Menu_principal.this, ProveedoresActivity.class);
                        startActivity(mostrarTablas);
                        break;
                    case "Productos":
                        mostrarTablas= new Intent(Menu_principal.this, ProductosActivity.class);
                        startActivity(mostrarTablas);
                        break;
                    case "Envios":
                        mostrarTablas= new Intent(Menu_principal.this, EnviosActivity.class);
                        startActivity(mostrarTablas);
                        break;
                    case "Pedidos":
                        mostrarTablas= new Intent(Menu_principal.this, PedidosActivity.class);
                        startActivity(mostrarTablas);
                        break;
                    case "Barcodes":
                        mostrarTablas= new Intent(Menu_principal.this, BarcodeActivity.class);
                        startActivity(mostrarTablas);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
