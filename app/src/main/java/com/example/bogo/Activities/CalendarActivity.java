package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bogo.Adapters.HomeCollection;
import com.example.bogo.Adapters.HwAdapter;
import com.example.bogo.R;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class CalendarActivity extends Fragment {

    public GregorianCalendar cal_month, cal_month_copy;
    private HwAdapter hwAdapter;
    private TextView tv_month;
    private LinearLayout llLayoutEventos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_calendar2, container, false);

        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();
        HomeCollection.date_collection_arr.add( new HomeCollection("2017-07-08" ,"Diwali","Holiday","this is holiday"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2017-07-08" ,"Holi","Holiday","this is holiday"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2017-07-08" ,"Statehood Day","Holiday","this is holiday"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2017-08-08" ,"Republic Unian","Holiday","this is holiday"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2017-07-09" ,"ABC","Holiday","this is holiday"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2017-06-15" ,"demo","Holiday","this is holiday"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2017-09-26" ,"weekly off","Holiday","this is holiday"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2018-01-08" ,"Events","Holiday","this is holiday"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2018-01-16" ,"Dasahara","Holiday","this is holiday"));
        HomeCollection.date_collection_arr.add( new HomeCollection("2018-02-09" ,"Christmas","Holiday","this is holiday"));



        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        hwAdapter = new HwAdapter(getActivity(), cal_month,HomeCollection.date_collection_arr);

        tv_month = (TextView) view.findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));

        llLayoutEventos = view.findViewById(R.id.llLayoutEventos);


        ImageButton previous = view.findViewById(R.id.ib_prev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 4&&cal_month.get(GregorianCalendar.YEAR)==2017) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(view.getContext(), "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setPreviousMonth();
                    refreshCalendar();
                }


            }
        });
        ImageButton next = (ImageButton) view.findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 5&&cal_month.get(GregorianCalendar.YEAR)==2018) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(view.getContext(), "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setNextMonth();
                    refreshCalendar();
                }
            }
        });
        GridView gridview = (GridView) view.findViewById(R.id.gv_calendar);
        gridview.setAdapter(hwAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedGridDate = HwAdapter.day_string.get(position);

                //QUEMADO
                if(selectedGridDate.equals("2020-09-04"))
                {
                    for(int i = 0; i < 3; i++)
                    {
                        View child = getLayoutInflater().inflate(R.layout.layout_events_list, null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        child.setLayoutParams(params);
                        llLayoutEventos.addView(child);
                    }
                }
                if(selectedGridDate.equals("2020-09-07"))
                {
                    for(int i = 0; i < 3; i++)
                    {
                        View child = getLayoutInflater().inflate(R.layout.layout_events_list, null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        child.setLayoutParams(params);
                        llLayoutEventos.addView(child);
                    }
                }
                if(selectedGridDate.equals("2020-09-13"))
                {
                    for(int i = 0; i < 3; i++)
                    {
                        View child = getLayoutInflater().inflate(R.layout.layout_events_list, null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        child.setLayoutParams(params);
                        llLayoutEventos.addView(child);
                    }
                }
                if(selectedGridDate.equals("2020-09-17"))
                {
                    for(int i = 0; i < 3; i++)
                    {
                        View child = getLayoutInflater().inflate(R.layout.layout_events_list, null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        child.setLayoutParams(params);
                        llLayoutEventos.addView(child);
                    }
                }
                if(selectedGridDate.equals("2020-09-19"))
                {
                    for(int i = 0; i < 3; i++)
                    {
                        View child = getLayoutInflater().inflate(R.layout.layout_events_list, null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        child.setLayoutParams(params);
                        llLayoutEventos.addView(child);
                    }
                }
                if(selectedGridDate.equals("2020-09-23"))
                {
                    for(int i = 0; i < 3; i++)
                    {
                        View child = getLayoutInflater().inflate(R.layout.layout_events_list, null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        child.setLayoutParams(params);
                        llLayoutEventos.addView(child);
                    }
                }
                if(selectedGridDate.equals("2020-09-26"))
                {
                    for(int i = 0; i < 3; i++)
                    {
                        View child = getLayoutInflater().inflate(R.layout.layout_events_list, null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        child.setLayoutParams(params);
                        llLayoutEventos.addView(child);
                    }
                }
                if(selectedGridDate.equals("2020-09-29"))
                {
                    for(int i = 0; i < 3; i++)
                    {
                        View child = getLayoutInflater().inflate(R.layout.layout_events_list, null);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        child.setLayoutParams(params);
                        llLayoutEventos.addView(child);
                    }
                }
                //FIN QUEMADO
                //((HwAdapter) parent.getAdapter()).getPositionList(selectedGridDate, MainActivity.this);
            }

        });


        return view;
    }


    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    public void refreshCalendar() {
        hwAdapter.refreshDays();
        hwAdapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }

}