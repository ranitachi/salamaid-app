package salam.ziswaf.ziswafsalam;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.BottomNavigationView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //private BottomBar bottomBar;
    FragmentManager fragmentManager;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();



//        BottomNavigationView bottomNavigationView = (BottomNavigationView)
//                findViewById(R.id.navigation);
        //bottomNavigationView.setItemIconTintList(null);

//        bottomNavigationView.setOnNavigationItemSelectedListener
//                (new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        Fragment selectedFragment = null;
//                        switch (item.getItemId()) {
//                            case R.id.beranda:
//                                selectedFragment = ContentMain.newInstance();
//                                break;
//                            case R.id.action_item1:
//                                selectedFragment = FragmentOne.newInstance();
//                                break;
//                            case R.id.action_item2:
//                                selectedFragment = FragmentTwo.newInstance();
//                                break;
//                            case R.id.action_item3:
//                                selectedFragment = FragmentThree.newInstance();
//                                break;
//                            case R.id.action_pengaturan:
//                                selectedFragment = FragmentPengaturan.newInstance();
//                                break;
//                        }
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.frame_layout, selectedFragment);
//                        transaction.commit();
//                        return true;
//                    }
//                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ContentMain.newInstance());
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

    //        if (id == R.id.nav_camera) {
    //            // Handle the camera action
    //        } else if (id == R.id.nav_gallery) {
    //
    //        } else if (id == R.id.nav_slideshow) {
    //
    //        } else if (id == R.id.nav_manage) {
    //
    //        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void frameSelection(int i){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Log.d("MAINMENU: ", i+" ");
        //make button active

        switch (i){

            case 1:
                ContentMain berandaFrag = new ContentMain();
                fragmentTransaction.replace(R.id.frame_layout, berandaFrag);
                break;
            case 2:
                FragmentMuzzaki muzzakiFrag = new FragmentMuzzaki();
                fragmentTransaction.replace(R.id.frame_layout, muzzakiFrag);
                break;
            case 3:
                FragmentTransaksi transaksiFrag = new FragmentTransaksi();
                fragmentTransaction.replace(R.id.frame_layout, transaksiFrag);
                break;
            case 4:
                FragmentLaporan laporanFrag = new FragmentLaporan();
                fragmentTransaction.replace(R.id.frame_layout, laporanFrag);
                break;
            case 5:
                FragmentPengaturan pengaturanFrag = new FragmentPengaturan();
                fragmentTransaction.replace(R.id.frame_layout, pengaturanFrag);
                break;
            default:
                FragmentBeranda berandFrag = new FragmentBeranda();
                fragmentTransaction.replace(R.id.frame_layout, berandFrag);
                break;
        }
        //fragmentTransaction.addToBackStack(null);
        //fragmentManager.popBackStack("minicctv", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.commit();
    }
    public void goberanda(View view) {
        frameSelection(1);
    }
    public void gomuzzaki(View view) {
        frameSelection(2);
    }
    public void gotransaksi(View view) {
        frameSelection(3);
    }
    public void golaporan(View view) {
        frameSelection(4);
    }
    public void goconfig(View view) {
        frameSelection(5);
    }
}
