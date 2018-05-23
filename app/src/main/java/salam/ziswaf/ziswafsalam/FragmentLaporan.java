package salam.ziswaf.ziswafsalam;

import android.app.DatePickerDialog;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.app.Dialog;
//import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class FragmentLaporan extends Fragment {
        BarChart chart;
    ArrayList<BarEntry> barEntries;
    ArrayList<String> barLabels;
    BarDataSet barDataSet;
    BarData barData;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    Button btnTanggal;
    String tanggal;
    WebView mWebView;

    public static FragmentLaporan newInstance() {
        FragmentLaporan fragment = new FragmentLaporan();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_laporan, container, false);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MM/yyyy/dd ");
        String strDate = mdformat.format(calendar.getTime());
        String url="http://keuangan.sekolahalambogor.id/json/json_penerimaan/all/"+strDate+"/1";
        mWebView = (WebView) view.findViewById(R.id.webview);

        WebSettings webSettings = mWebView.getSettings();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
            webSettings.setDomStorageEnabled(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mWebView.loadUrl(url);
        }

        btnTanggal = (Button) view.findViewById(R.id.btnTanggal);
        btnTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });



//        chart= (BarChart) view.findViewById(R.id.chart);
//        barEntries = new ArrayList<BarEntry>();
//        barLabels = new ArrayList<String>();
//
//        barLabels.add("");// index 0 kosongkan saja
//        barEntries.add(new BarEntry(1, 70f));
//        barLabels.add("Zakat Maal");
//        barEntries.add(new BarEntry(2, 60f));
//        barLabels.add("Zakat Fitrah");
//        barEntries.add(new BarEntry(3, 30f));
//        barLabels.add("Project C");
//        barEntries.add(new BarEntry(4, 60f));
//        barLabels.add("Project D");
//        barEntries.add(new BarEntry(5, 30f));
//        barLabels.add("Project E");
//
//        barDataSet = new BarDataSet(barEntries, "Jenis Penerimaan");
//
//        barData = new BarData(barDataSet);
//        chart.getXAxis().setValueFormatter(
//                new IndexAxisValueFormatter(barLabels));
//
//        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        chart.setData(barData);
//
//        chart.animateY(3000);
        return view;
    }

    public void lihatlaporan(String tanggal)
    {

    }

    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        WebView mWebView;
        @Override

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
            String tgl=yy+"-"+mm+"-"+dd;
            //lihatlaporan(tgl);
        }
        public void populateSetDate(int year, int month, int day) {
            String tgl=day+"-"+month+"-"+year;
            String url="http://keuangan.sekolahalambogor.id/json/json_penerimaan/all/"+month+"/"+year+"/"+day+"/1";
            mWebView = (WebView) getActivity().findViewById(R.id.webview);

            WebSettings webSettings = mWebView.getSettings();
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
                webSettings.setDomStorageEnabled(true);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                mWebView.loadUrl(url);
            }
            // Enable Javascript
//            WebSettings webSettings = mWebView.getSettings();
//            webSettings.setJavaScriptEnabled(true);
//
//            // Force links and redirects to open in the WebView instead of in a browser
//            mWebView.setWebViewClient(new WebViewClient());
//            mWebView.loadUrl(url);
            //return tgl;
            //Toast.makeText(getActivity().getApplicationContext(), "Tanggal : " + tgl, Toast.LENGTH_LONG).show();
        }
    }
}

