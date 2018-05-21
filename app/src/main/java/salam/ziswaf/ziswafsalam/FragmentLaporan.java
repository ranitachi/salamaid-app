package salam.ziswaf.ziswafsalam;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class FragmentLaporan extends Fragment {

    BarChart chart;
    ArrayList<BarEntry> barEntries;
    ArrayList<String> barLabels;
    BarDataSet barDataSet;
    BarData barData;


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

        chart= (BarChart) view.findViewById(R.id.chart);
        barEntries = new ArrayList<BarEntry>();
        barLabels = new ArrayList<String>();

        barLabels.add("");// index 0 kosongkan saja
        barEntries.add(new BarEntry(1, 70f));
        barLabels.add("Zakat Maal");
        barEntries.add(new BarEntry(2, 60f));
        barLabels.add("Zakat Fitrah");
        barEntries.add(new BarEntry(3, 30f));
        barLabels.add("Project C");
        barEntries.add(new BarEntry(4, 60f));
        barLabels.add("Project D");
        barEntries.add(new BarEntry(5, 30f));
        barLabels.add("Project E");

        barDataSet = new BarDataSet(barEntries, "Jenis Penerimaan");

        barData = new BarData(barDataSet);
        chart.getXAxis().setValueFormatter(
                new IndexAxisValueFormatter(barLabels));

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        chart.setData(barData);

        chart.animateY(3000);
        return view;
    }


}
