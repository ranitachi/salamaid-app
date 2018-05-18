package salam.ziswaf.ziswafsalam;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;


public class FragmentMain extends FragmentActivity{
    FragmentManager fragmentManager;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        fragmentManager = getSupportFragmentManager();
    }

    private void frameSelection(int i){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Log.d("MAINMENU: ", i+" ");
        //make button active

        switch (i){

            case 1:
                FragmentBeranda berandaFrag = new FragmentBeranda();
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
