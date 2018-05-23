package salam.ziswaf.ziswafsalam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ContentMain extends Fragment {
    TextView txtUser,txtzakatfitrah,txtzakatmaal,txtinfak,txtprogramramadhan,txtprogram,txtambulance,txtwakaf,txtTotal;
    String c_transaksi,c_user,c_amilin,c_zakat_fitrah,c_zakat_maal,c_infak_sedekah,c_infak_program,c_infak_ambulance,c_infak_ramadhan,c_wakaf,c_jumlah="";
    public static ContentMain newInstance() {
        ContentMain fragment = new ContentMain();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.content_main, container, false);
        txtUser = (TextView) view.findViewById(R.id.txtUser);
        txtzakatfitrah = (TextView) view.findViewById(R.id.txtzakatfitrah);
        txtzakatmaal = (TextView) view.findViewById(R.id.txtzakatmaal);
        txtinfak = (TextView) view.findViewById(R.id.txtinfak);
        txtambulance = (TextView) view.findViewById(R.id.txtambulance);
        txtprogram = (TextView) view.findViewById(R.id.txtprogram);
        txtprogramramadhan = (TextView) view.findViewById(R.id.txtprogramramadhan);
        txtwakaf = (TextView) view.findViewById(R.id.txtwakaf);
        txtTotal = (TextView) view.findViewById(R.id.jlhTotal);
        lihatlaporan();

        return view;
    }

    public void lihatlaporan()
    {
        try
        {
            FileCacher<String> user = new FileCacher<String>(getActivity(), "datauser.txt");
            if (user.hasCache()) {
                c_user = user.readCache();
                JSONObject objUser=new JSONObject(c_user);
                JSONObject d_user = objUser.getJSONObject("data");
                c_amilin = d_user.getString("nama");
                txtUser.setText(c_amilin);
            }

            FileCacher<String> transaksi = new FileCacher<String>(getActivity(), "datatransaksi.txt");
            if (user.hasCache()) {
                c_transaksi = transaksi.readCache();
                JSONObject objTrans=new JSONObject(c_transaksi);
                JSONObject data = objTrans.getJSONObject("data");
                c_zakat_fitrah= data.getString("Zakat Fitrah");
                c_zakat_maal= data.getString("Zakat Maal");
                c_infak_sedekah= data.getString("Infak/Sedekah");
                c_infak_program= data.getString("Infak Program");
                c_infak_ambulance= data.getString("Infak Ambulance");
                c_infak_ramadhan= data.getString("Infak Program Ramadhan");
                c_wakaf= data.getString("Wakaf Rumah Sehat");
                c_jumlah= data.getString("jumlah");

                txtzakatmaal.setText(c_zakat_maal);
                txtzakatfitrah.setText(c_zakat_fitrah);
                txtinfak.setText(c_infak_sedekah);
                txtambulance.setText(c_infak_ambulance);
                txtprogram.setText(c_infak_program);
                txtprogramramadhan.setText(c_infak_ramadhan);
                txtwakaf.setText(c_wakaf);
                txtTotal.setText(c_jumlah);
                Log.d("Zakat Fitrah",c_zakat_fitrah);
                Log.d("Zakat Mall",c_zakat_maal);
                Log.d("Infak Ambulance",c_infak_ambulance);
                Log.d("Infak Sedekah",c_infak_sedekah);
                Log.d("Infak Ramadhan",c_infak_ramadhan);
//                Log.d("Data Error : ",data.toString());
                //txtUser.setText(c_amilin);
            }
        }
        catch (JSONException e)
        {
            Toast toast = Toast.makeText(
                    getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG
            );
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
            Log.d("Error Jumlah :",e.toString());
        }
        catch (IOException e)
        {
            Toast toast = Toast.makeText(
                    getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG
            );
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
        }
    }
}
