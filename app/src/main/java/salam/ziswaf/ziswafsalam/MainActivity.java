package salam.ziswaf.ziswafsalam;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentTransaction;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.json.JSONObject;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //private BottomBar bottomBar;
    String uri = "http://keuangan.sekolahalambogor.id/json/json_siswa_nis";
    String uri2 = "http://keuangan.sekolahalambogor.id/json/json_jenis_setoran";
    String uri3 = "http://keuangan.sekolahalambogor.id/json/json_muzzaki";
    FragmentManager fragmentManager;
    Bundle bundle;
    FileCacher<String> stringCacher;
    ProgressBar progressBar;
    String tentang="";
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
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

        getsiswa(uri);
        getjenissetoran(uri2);
        getmuzzaki(uri3);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Klik Sekali Lagi untuk Keluar", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
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

            if (id == R.id.beranda) {
                frameSelection(1);
                // Handle the camera action
            } else if (id == R.id.data_muzzaki) {
                frameSelection(2);
            } else if (id == R.id.zakat) {
                frameSelection(3);
            } else if (id == R.id.laporan) {
                frameSelection(4);
            }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void frameSelection(int i){
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
            case 6:
                FragmentTransaksiZakat transaksiFragZ = new FragmentTransaksiZakat();
                fragmentTransaction.replace(R.id.frame_layout, transaksiFragZ);
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
    public void gotransaksizakat(View view) {
        frameSelection(6);
    }
    public void golaporan(View view) {
        frameSelection(4);
    }
    public void goconfig(View view) {
        frameSelection(5);
    }
    private void getsiswa(String uri){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(uri, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("GETSISWA", "onSuccess: " + '['+response.toString()+']');
                try {
//                    JSONArray ob = response.getJSONArray("results");
//                    //JSONObject obj = ob.getJSONObject(0);
////                    tentang = obj.getString("konten");
////                    JSONObject trm = response.getJSONObject("results");
//                    String datasiswa = ob.toString();
//                    stringCacher= new FileCacher<>(MainActivity.this, "datasiswa.txt");
//                    stringCacher.writeCache(datasiswa);

                    stringCacher= new FileCacher<>(MainActivity.this, "datasiswajson.txt");
                    stringCacher.writeCache('['+response.toString()+']');

//                    Log.d("GETSISWA", "onSuccess: " + datasiswa);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("GETSISWA", "onFailure: "+responseString);
            }
        });
    }
    private void getjenissetoran(String uri){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(uri, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {

                    stringCacher= new FileCacher<>(MainActivity.this, "datajenis.txt");
                    stringCacher.writeCache('['+response.toString()+']');
                    Log.d("GETJENIS", "onSuccess: " + '['+response.toString()+']');

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("GETJENIS", "onFailure: "+responseString);
            }
        });
    }
    private void getmuzzaki(String uri){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(uri, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {

                    stringCacher= new FileCacher<>(MainActivity.this, "datamuzzaki.txt");
                    stringCacher.writeCache('['+response.toString()+']');
                    Log.d("GETJENIS", "onSuccess: " + '['+response.toString()+']');

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("GETJENIS", "onFailure: "+responseString);
            }
        });
    }


}
