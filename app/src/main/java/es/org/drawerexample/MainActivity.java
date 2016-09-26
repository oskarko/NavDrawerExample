package es.org.drawerexample;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import es.org.drawerexample.fragments.FragmentFourth;
import es.org.drawerexample.fragments.FragmentOne;
import es.org.drawerexample.fragments.FragmentThird;
import es.org.drawerexample.fragments.FragmentTwo;
import es.org.drawerexample.fragments.SXFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private FragmentManager manager;

    private List<String> currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = getSupportFragmentManager();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            public void onDrawerClosed(View view) {
                syncActionBarArrowState();
            }

            public void onDrawerOpened(View drawerView) {
                mDrawerToggle.setDrawerIndicatorEnabled(true);
            }
        };

        // Navigation back icon listener
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // "listener" para cuando abre/cierra el Nav Drawer
        mDrawer.setDrawerListener(mDrawerToggle);

        // Navigation Drawer
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // "listener" del Navigation Drawer
        setupDrawerContent(nvDrawer);

        // Escuchador de vuelta atras
        manager.addOnBackStackChangedListener(mOnBackStackChangedListener);

        // por defecto, colocamos el primer fragment en el frameLayout pero sin añadirlo a la pila
        SXFragment fragment = new FragmentOne();
        manager.beginTransaction().replace(R.id.flContent, fragment, fragment.getTagName()).commit();
        currentFragment = new ArrayList<>();
        currentFragment.add(fragment.getTagName());
    }


    // Listener del Navigation Drawer
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        // Creamos un nuevo fragment en función de la opción seleccionada en el Nav Drawer
                        SXFragment fragment;

                        switch(menuItem.getItemId()) {
                            case R.id.nav_first_fragment:
                                fragment = new FragmentOne();
                                break;
                            case R.id.nav_second_fragment:
                                fragment = new FragmentTwo();
                                break;
                            case R.id.nav_third_fragment:
                                fragment = new FragmentThird();
                                break;
                            case R.id.nav_fourth_fragment:
                                fragment = new FragmentFourth();
                                break;
                            default:
                                fragment = new FragmentOne();
                        }

                        // Insertamos el fragment reemplazando el anterior ya existente
                        replaceFragment(fragment);
                        mDrawer.closeDrawers();

                        return true;
                    }
                });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * Escuchador para la vuelta atrás
     */
    private FragmentManager.OnBackStackChangedListener
            mOnBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            syncActionBarArrowState();
        }
    };


    /**
     * método que comprueba que no estemos atrás del to-do (first fragment), y si no es así, va vaciando la pila y
     * actualizando currentFragment en función del fragment a mostrar.
     * @return
     */
    private boolean canGoBack() {

        SXFragment oldFragment = (SXFragment) manager.findFragmentByTag(currentFragment.get(currentFragment.size() - 1));

        if (oldFragment.onBackStack()) {
            if (currentFragment.size() > 1) {
                currentFragment.remove(currentFragment.size() - 1);
            }
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {

        // Si estamos atrás del to-do, preguntamos si queremos salir
        if (currentFragment.size() == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Drawer Example")
                    .setMessage("¿Realmente desea salir?")
                    .setCancelable(false)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (canGoBack()) {
            super.onBackPressed();
        }

    }


    @Override
    protected void onDestroy() {
        manager.removeOnBackStackChangedListener(mOnBackStackChangedListener);
        super.onDestroy();
    }


    private void syncActionBarArrowState() {
        int backStackEntryCount = manager.getBackStackEntryCount();
        mDrawerToggle.setDrawerIndicatorEnabled(backStackEntryCount == 0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.isDrawerIndicatorEnabled() &&
                mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == android.R.id.home &&
                manager.popBackStackImmediate()) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    public void replaceFragment(SXFragment fragment) {

        try {
            // sólo cambiaremos el fragment cuando sea diferente el que queremos mostrar de aquel que estamos mostrando ahora mismo
            if (!currentFragment.get(currentFragment.size() - 1).substring(0, 9).equals(fragment.getTagName().substring(0, 9))) {

                manager.beginTransaction().replace(R.id.flContent, fragment, fragment.getTagName()).addToBackStack(fragment.getTagName()).commit();
                currentFragment.add(fragment.getTagName());
            }
        }catch (IllegalStateException ies){
            Log.e("ERROR", "IllegalStateException capturada en replaceFragment()");
        }

    }
}
