package alfonso8969.com.proyecto2.activities.envios;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.fragments.envios.EnviosListaFragment;
import alfonso8969.com.proyecto2.fragments.envios.Envios_ItemsFragment;

public class EnviosActivity extends AppCompatActivity implements EnviosListaFragment.CallBacksEnvios,EnviosListaFragment.Refresh{

    public static boolean mTwoPane;
    private static  final String ELEMENTS_TAG = "ELEMENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envios);

        if(findViewById(R.id.envios_list_item) != null ){
            mTwoPane =true;

            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.envios_list_item,new Envios_ItemsFragment(), ELEMENTS_TAG)
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
    public void onItemSelected(String envioId) {

        if(getmTwoPane()){
            Bundle bundle = new Bundle();
            bundle.putString("envioId",envioId);
            Envios_ItemsFragment itemsFragment = new Envios_ItemsFragment();
            itemsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.envios_list_item,itemsFragment).
                    commit();
        }
        else{
            Intent intent = new Intent(this,Envios_itemsList.class);
            intent.putExtra("envioId",envioId);
            startActivity(intent);
        }

    }

    @Override
    public void refreshList() {
        Bundle bundle = new Bundle();
        Envios_ItemsFragment itemsFragment = new Envios_ItemsFragment();

        bundle.putBoolean("restart",true);
        itemsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.envios_list_item,itemsFragment).
                commit();
    }
}
