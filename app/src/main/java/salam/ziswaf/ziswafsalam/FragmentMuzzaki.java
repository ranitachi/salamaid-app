package salam.ziswaf.ziswafsalam;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import salam.ziswaf.ziswafsalam.kelas.DialogHandler;

public class FragmentMuzzaki extends Fragment {

    Button btnSimpan;
    EditText txt_nama,txt_telp,txt_alamat,txt_email;
    String nama,telp,alamat,email;
    TextView text;
    View layout;
    String uri3 = "http://keuangan.sekolahalambogor.id/json/json_muzzaki";
    FileCacher<String> stringCacher;
    public static FragmentMuzzaki newInstance() {
        FragmentMuzzaki fragment = new FragmentMuzzaki();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_muzzaki, container, false);
        layout= inflater.inflate(R.layout.custom_toast, (ViewGroup) getActivity().findViewById(R.id.custom_toast_container));

        text = (TextView) layout.findViewById(R.id.text);

        txt_nama = (EditText) view.findViewById(R.id.txtNama);
        txt_telp = (EditText) view.findViewById(R.id.txtTelp);
        txt_alamat = (EditText) view.findViewById(R.id.txtAlamat);
        txt_email = (EditText) view.findViewById(R.id.txtEmail);


        btnSimpan = (Button) view.findViewById(R.id.ButtonSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    doClick();

                }

            }
        });

        return view;
    }

    public void doClick()
    {
        DialogHandler dialog = new DialogHandler();
        dialog.Confirm(getActivity(), "Konfirmasi", "Apakah Data Muzzaki yang Di Input Sudah Benar ?",
                "Cancel", "OK", aproc(), bproc());
    }

    public Runnable aproc(){
        return new Runnable() {
            public void run() {
                //Log.d("Test", "This from A proc");

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://keuangan.sekolahalambogor.id/json/from_android");
                try{



                    nama = txt_nama.getText().toString();
                    telp = txt_telp.getText().toString();
                    alamat = txt_alamat.getText().toString();
                    email = txt_email.getText().toString();

                    JSONObject json=new JSONObject();
                    json.put("apikey","mykey123");
                    json.put("command","tambah");
                    json.put("nama",nama);
                    json.put("alamat",alamat);
                    json.put("telp",telp);
                    json.put("email",email);

                    Log.d("JSON",json.toString());
                    List nameValuePairs = new ArrayList(1);
                    nameValuePairs.add(new BasicNameValuePair("json", json.toString()));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    String responseBody = EntityUtils.toString(response.getEntity());

                    txt_nama.setText("");
                    txt_alamat.setText("");
                    txt_telp.setText("");
                    txt_nama.requestFocus();
                    JSONObject resp = new JSONObject(responseBody);
                    String pesan = resp.optString("response")+", Silahkan Lanjut Ke Transaksi";

                    Log.d("JSON","RESPONSE : " + responseBody);


//                    text.setText(pesan);
//
//                    Toast toast = new Toast(getActivity().getApplicationContext());
//                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                    toast.setDuration(Toast.LENGTH_LONG);
//                    toast.setView(layout);
//                    toast.show();
                    Toast toast = Toast.makeText(
                            getActivity().getApplicationContext(), pesan, Toast.LENGTH_LONG
                    );
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    getmuzzaki(uri3);
                }
                catch (ClientProtocolException e) {
                    Log.d("JSON Client : ",e.getMessage());
                } catch (IOException e) {
                    Log.d("JSON IO : ",e.getMessage());
                } catch (JSONException e) {
                    Log.d("JSON Exception : ",e.getMessage());
                }

            }
        };
    }

    public Runnable bproc(){
        return new Runnable() {
            public void run() {
                Log.d("Test", "This from B proc");
            }
        };
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

                    stringCacher= new FileCacher<>(getActivity(), "datamuzzaki.txt");
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
