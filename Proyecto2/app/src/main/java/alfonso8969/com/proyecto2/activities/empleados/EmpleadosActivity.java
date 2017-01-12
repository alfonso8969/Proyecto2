package alfonso8969.com.proyecto2.activities.empleados;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.fragments.empleados.Empleado_ItemsFragment;
import alfonso8969.com.proyecto2.fragments.empleados.EmpleadosListaFragment;

public class EmpleadosActivity extends AppCompatActivity implements EmpleadosListaFragment.CallBacks , EmpleadosListaFragment.Refresh{
    public static boolean mTwoPane;
    private static  final String ELEMENTS_TAG = "ELEMENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados);


        if(findViewById(R.id.empleados_list_item) != null ){
            mTwoPane =true;

            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.empleados_list_item,new Empleado_ItemsFragment(), ELEMENTS_TAG)
                        .commit();
            }else {
                mTwoPane = false;
            }
        }
    }
    public boolean getmTwoPane(){
        return mTwoPane;
    }

    @Override
    public void onItemSelected(String empleadoId) {

        if(getmTwoPane()){
            Bundle bundle = new Bundle();
            bundle.putString("empleadoId",empleadoId);
            Empleado_ItemsFragment itemsFragment = new Empleado_ItemsFragment();
            itemsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.empleados_list_item,itemsFragment).
                    commit();
        }
        else{
            Intent intent = new Intent(this,Empleados_itemsList.class);
            intent.putExtra("empleadoId",empleadoId);
            startActivity(intent);
        }
    }

    @Override
    public void refreshList() {
        Bundle bundle = new Bundle();

        Empleado_ItemsFragment itemsFragment = new Empleado_ItemsFragment();

        bundle.putBoolean("restart",true);
        itemsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.empleados_list_item,itemsFragment).
                commit();
    }

}
