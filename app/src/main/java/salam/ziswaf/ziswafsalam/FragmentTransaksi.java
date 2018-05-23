package salam.ziswaf.ziswafsalam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

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
import salam.ziswaf.ziswafsalam.kelas.PrinterCommands;
import salam.ziswaf.ziswafsalam.kelas.Utils;
import salam.ziswaf.ziswafsalam.kelas.Terbilang;
public class FragmentTransaksi extends Fragment implements Runnable{

    String[] dataSiswa,dataKelas;
    String strJson;
    Button btnSimpan;
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    private static OutputStream outputStream;
    AutoCompleteTextView textView;
    EditText nJumlah;
    Spinner cJenis,cKelas,cSiswa;
    FileCacher<String> stringCacher;
    String c_siswa,n_jumlah,c_jenis,cKwitansi,cTanggal,c_user,c_kelas,data_siswa,level_kelas,nama_kelas="";
    String c_amilin="";
    public static FragmentTransaksi newInstance() {
        FragmentTransaksi fragment = new FragmentTransaksi();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {

//            FileCacher<String> term = new FileCacher<String>(getActivity(),"datasiswajson.txt");
//            if(term.hasCache())
//            {
//                    strJson=term.readCache();
//                    Log.d("Data Siswa : ",strJson);
//                    JSONArray jr = new JSONArray(strJson);
//                    JSONObject jb = jr.getJSONObject(0);
//                    JSONArray st = jb.getJSONArray("results");
//                    dataSiswa=new String[st.length()];
//                    for(int i=0;i<st.length();i++)
//                    {
//                        String nama = st.getString(i);
//                        JSONObject subjb=new JSONObject(nama);
//                        String nm=subjb.optString("nama");
//                        String kls=subjb.optString("kelas");
//                        Log.i("Tambah :",""+nm);
//                        dataSiswa[i]=nm+" :: "+kls;
//                    }
//
//            }
            FileCacher<String> user = new FileCacher<String>(getActivity(), "datauser.txt");
            if (user.hasCache()) {
                c_user = user.readCache();
                JSONObject objUser=new JSONObject(c_user);
                JSONObject d_user = objUser.getJSONObject("data");
                c_amilin = d_user.getString("nama");
            }
            FileCacher<String> kelas = new FileCacher<String>(getActivity(), "datakelas.txt");
            if (user.hasCache()) {
                c_kelas = kelas.readCache();
                Log.d("Data Kelas : ",c_kelas);
                JSONArray jr = new JSONArray(c_kelas);
                JSONObject jb = jr.getJSONObject(0);
                JSONArray d_user = jb.getJSONArray("kelas");
                JSONObject d_siswa = jb.getJSONObject("siswa");
                data_siswa=d_siswa.toString();
                Log.d("DKELAS :",""+d_user.toString());
                Log.d("DSISWA :",""+data_siswa);
                dataKelas=new String[(d_user.length()+1)];
                dataKelas[0]="-Pilih Kelas-";
                for(int i=0;i<d_user.length();i++)
                {
                    String nama = d_user.getString(i);
                    JSONObject subjb=new JSONObject(nama);
                    String lvl=subjb.optString("kategori").toUpperCase();
                    String kls=subjb.optString("nama_batch");

                    dataKelas[(i+1)]=lvl+"-"+kls;
                }
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        View view= inflater.inflate(R.layout.fragment_transaksi, container, false);
        btnSimpan = (Button) view.findViewById(R.id.btnSimpanTransaksi);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        textView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteMuzzaki);
        nJumlah = (EditText) view.findViewById(R.id.EditTextNominal);
        cJenis = (Spinner) view.findViewById(R.id.SpinnerJenis);
        cSiswa = (Spinner) view.findViewById(R.id.spinSiswa);

        cKelas = (Spinner) view.findViewById(R.id.spinKelas);

        ArrayAdapter<String> adapterr = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, dataKelas);
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cKelas.setAdapter(adapterr);

        cKelas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                String getSiswa = (String) parent.getItemAtPosition(position);
                Log.d("Load Siswa : ", data_siswa);
                if(!getSiswa.equalsIgnoreCase("-pilih kelas-")) {
                    try {
                        JSONObject ds = new JSONObject(data_siswa);
                        JSONArray arr_sis = ds.getJSONArray(getSiswa);
                        dataSiswa=new String[arr_sis.length()+1];
                        dataSiswa[0]="-Pilih Siswa-";
                        for(int i=0;i<arr_sis.length();i++)
                        {
                            String nama = arr_sis.getString(i);
                            JSONObject subjb=new JSONObject(nama);
                            String nm=subjb.optString("nama");
                            String nis=subjb.optString("nisn");
                            Log.i("Tambah :",""+nm);
                            dataSiswa[i]=nm+" :: "+nis;
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, dataSiswa);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cSiswa.setAdapter(adapter);

//                        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dataSiswa);
//                        textView.setAdapter(adapter);
                        Log.d("Item Dipilih : ", (String) parent.getItemAtPosition(position));
                        //Log.d("Jlh Siswa : ", String.valueOf(arr_sis.length()));
                    } catch (JSONException e) {
                        Log.d("Error Load Siswa : ", e.toString());
                        Toast toast = Toast.makeText(
                                getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG
                        );
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });



        return view;
    }
    public void doClick()
    {
        DialogHandler dialog = new DialogHandler();
        dialog.Confirm(getActivity(), "Konfirmasi", "Apakah Data Transaksi ini Sudah Benar ?",
                "Cancel", "OK", aproc(), bproc());
    }
    public Runnable aproc() {
        return new Runnable() {
            public void run() {
                //doWebViewPrint();
                //c_siswa = textView.getText().toString();
                c_siswa=cSiswa.getSelectedItem().toString();
                n_jumlah = nJumlah.getText().toString();
                c_jenis = cJenis.getSelectedItem().toString();
                if(c_siswa.equals(""))
                {
                    Toast toast = Toast.makeText(
                            getActivity().getApplicationContext(), "Nama Siswa Belum Di Pilih", Toast.LENGTH_LONG
                    );
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else if(n_jumlah.equals(""))
                {
                    Toast toast = Toast.makeText(
                            getActivity().getApplicationContext(), "Nominal Setoran Belum Diisi", Toast.LENGTH_LONG
                    );
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else if(c_jenis.equals(""))
                {
                    Toast toast = Toast.makeText(
                            getActivity().getApplicationContext(), "Jenis Penerimaan Belum Dipilih", Toast.LENGTH_LONG
                    );
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                else
                {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat getTanggal = new SimpleDateFormat("dd/MM/yyyy");
                    Random rand = new Random();
                    int n = rand.nextInt(1000);

                    String strDate = mdformat.format(calendar.getTime());
                    cTanggal= getTanggal.format(calendar.getTime());
                    cKwitansi=strDate+""+String.valueOf(n);
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://keuangan.sekolahalambogor.id/json/json_simpan_transaksi");
                    try{

                        JSONObject json=new JSONObject();
                        json.put("apikey","mykey123");
                        json.put("command","tambah");
                        json.put("nama",c_siswa);
                        json.put("jumlah",n_jumlah);
                        json.put("jenis",c_jenis);
                        json.put("kwitansi",cKwitansi);
                        json.put("amilin",c_amilin);
                        json.put("flag",1);

                        Log.d("JSON",json.toString());
                        List nameValuePairs = new ArrayList(1);
                        nameValuePairs.add(new BasicNameValuePair("json", json.toString()));

                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        // Execute HTTP Post Request
                        HttpResponse response = httpclient.execute(httppost);
                        String responseBody = EntityUtils.toString(response.getEntity());

                        //textView.setText("");
                        nJumlah.setText("");
                        cSiswa.setAdapter(null);
                        ArrayAdapter<String> adapterr = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, dataKelas);
                        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cKelas.setAdapter(adapterr);
                        JSONObject resp = new JSONObject(responseBody);
                        String pesan = resp.optString("response");

                        Log.d("JSON","RESPONSE : " + responseBody);

                        Toast toast = Toast.makeText(
                                getActivity().getApplicationContext(), pesan, Toast.LENGTH_LONG
                        );
                        toast.setGravity(Gravity.BOTTOM,0,0);
                        toast.show();
                    }
                    catch (ClientProtocolException e) {
                        Log.d("JSON Client : ",e.getMessage());
                    } catch (IOException e) {
                        Log.d("JSON IO : ",e.getMessage());
                    } catch (JSONException e) {
                        Log.d("JSON Exception : ",e.toString());
                    }

                    connectBT();
                }
            }
        };
    }

    public Runnable bproc() {
        return new Runnable() {
            public void run() {
            }
        };
    }

    public void cetakStruk()
    {
        if(mBluetoothSocket == null){

            Toast.makeText(getActivity().getApplicationContext(), "Error BT SOcket ", Toast.LENGTH_LONG).show();
        }
        else {
            OutputStream opstream = null;
            try {
                opstream = mBluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error 1 : " + e.toString(), Toast.LENGTH_LONG).show();
            }
            outputStream = opstream;
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Error 2 : " + e.toString(), Toast.LENGTH_LONG).show();
                }
                outputStream = mBluetoothSocket.getOutputStream();
                byte[] printformat = new byte[]{0x1B,0x21,0x03};
                outputStream.write(printformat);

                NumberFormat formatter = new DecimalFormat("#,###");
                double jlh_setoran = Double.parseDouble(n_jumlah);
                String jlh_setor = formatter.format(jlh_setoran);

                printPhoto(R.drawable.logo_struk,1);
                //printPhoto(R.drawable.logo_baznas_black,2);
                printNewLine();
                printNewLine();
                printNewLine();
                printCustom("BUKTI PENERIMAAN\n" +
                        "ZAKAT, INFAK, SEDEKAH & WAKAF",1,1);
                printCustom(new String(new char[32]).replace("\0", "."),0,1);
                String dateTime[] = getDateTime();

                String[] separated = c_siswa.split("::");

                printCustom("Tanggal   : "+ cTanggal+" "+dateTime[1],0,0);
                printCustom("Nama Amil : "+c_amilin,0,0);
                printCustom("No. Bukti : "+cKwitansi,0,0);
                printCustom(new String(new char[32]).replace("\0", "."),0,1);

                printCustom("Nama Muzzaki  : "+separated[0],0,0);
                printCustom("Kelas         : "+separated[1].trim(),0,0);
                printCustom("Jenis Setoran : "+c_jenis,0,0);
                printCustom("Jumlah        : Rp. "+jlh_setor,0,0);

                printCustom(new String(new char[32]).replace("\0", "."),0,1);

                String terbilang=new Terbilang().bilangx(jlh_setoran);

                printCustom("Terbilang : "+terbilang+" rupiah",0,2);
                printNewLine();
                printCustom("\"Ajarakallahu Fiima A'thaita Wa Baaraka Fima Abqoita Wa Ja'alallahu Laka Thahuuran\"",0,1);
                printCustom(new String(new char[32]).replace("\0", "."),0,1);
                printCustom(new String(new char[22]).replace("\0", "."),0,1);

                printNewLine();
                printNewLine();
                printCustom("Terima Kasih Telah Menyalurkan Zakat/Infak/Sedekah pada Salam Aid",0,1);
                printCustom(new String(new char[32]).replace("\0", "."),0,1);

                printNewLine();
                printNewLine();
                outputStream.flush();
                gettransaksi();
                //reloadfragment();

                mBluetoothSocket.close();
                outputStream.close();
            }
            catch (IOException e) {
                //e.printStackTrace();
                // Log.d("Error 3 : ", e.toString());
                Toast.makeText(getActivity().getApplicationContext(), "Error 3 : " + e.toString(), Toast.LENGTH_LONG).show();
            }
            //reloadfragment();
        }
    }

    public void printUnicode(){
        try {
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void printText2(byte[] msg,int align) {
        try {
            // Print normal text
            switch (align){
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
            }

            switch (align){
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void printPhoto(int img,int align) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                switch (align){
                    case 0:
                        //left align
                        outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                        break;
                    case 1:
                        //center align
                        outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                        break;
                    case 2:
                        //right align
                        outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                        break;
                }
                //outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText2(command,align);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }
    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime [] = new String[2];
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM / yyyy ");
        String strDate = mdformat.format(calendar.getTime());
        //dateTime[0] = c.get(Calendar.DAY_OF_MONTH) +"/"+ c.get(Calendar.MONTH) +"/"+ c.get(Calendar.YEAR);
        dateTime[0] = strDate;
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) +":"+ c.get(Calendar.MINUTE);
        return dateTime;
    }
    private String leftRightAlign(String str1, String str2) {
        String ans = str1 +str2;
        if(ans.length() <31){
            int n = (31 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }

    public void connectBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "Message1", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                try {
                    Intent enableBtIntent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent,
                            REQUEST_ENABLE_BT);
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            } else {
                try{

                    ListPairedDevices();
                    Intent connectIntent = new Intent(getActivity(),
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent,
                            REQUEST_CONNECT_DEVICE);}
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }
    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(getActivity(),
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(getActivity(),
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(getActivity(), "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(getActivity(), "DeviceConnected", Toast.LENGTH_SHORT).show();
            cetakStruk();
            //mBluetoothAdapter=null;
            //closeSocket(mBluetoothSocket);
            //reloadfragment();

        }
    };

    private void gettransaksi(){
        String user_login,c_user="";
        try
        {
            FileCacher<String> user = new FileCacher<String>(getActivity(), "datauser.txt");
            if (user.hasCache()) {
                user_login = user.readCache();
                JSONObject objUser=new JSONObject(user_login);
                JSONObject d_user = objUser.getJSONObject("data");
                c_user = d_user.getString("nama");

            }
        }
        catch (JSONException e)
        {
            Toast toast = Toast.makeText(
                    getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG
            );
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
        }
        catch (IOException e)
        {
            Toast toast = Toast.makeText(
                    getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG
            );
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
        }
        String uri ="http://keuangan.sekolahalambogor.id/json/json_penerimaan/";
        String url=uri+c_user;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {

                    stringCacher= new FileCacher<>(getActivity(), "datatransaksi.txt");
                    stringCacher.writeCache(response.toString());
                    Log.d("TRANSAKSI", "onSuccess: " +response.toString());

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
    public void reloadfragment()
    {
//        Fragment frg = null;
//        //FragmentTransaction
//        frg = getFragmentManager().findFragmentByTag("fragment_transaksi");
//        final FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(frg);
//        ft.attach(frg);
//        ft.commit();
        //getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        FragmentTransaction ftr = getFragmentManager().beginTransaction();
        ftr.detach(FragmentTransaksi.this).attach(FragmentTransaksi.this).commit();
    }

//    private void resetConnection() {
//        if (mBTInputStream != null) {
//            try {mBTInputStream.close();} catch (Exception e) {}
//            mBTInputStream = null;
//        }
//
//        if (mBTOutputStream != null) {
//            try {mBTOutputStream.close();} catch (Exception e) {}
//            mBTOutputStream = null;
//        }
//
//        if (mBTSocket != null) {
//            try {mBTSocket.close();} catch (Exception e) {}
//            mBTSocket = null;
//        }
//
//    }
}
