package com.meest.Insights;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Paramaters.InsightGraphParameter;
import com.meest.R;
import com.meest.responses.UserInsightGraphResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsightGraphFragment extends Fragment {

    PieChartView pieChartView;

    // line chart code for Engagement
    LineChartView lineChartView;
    int[] yAxisData = {0, 20, 40, 60, 80, 100};

    // line chart code for likes and views

    LineChartView lineChartViewLikes;
    String[] axisDataLikes = {"30 july", "1 aug", "3 aug", "5 aug"};
    int[] yAxisDataLikes = {0, 20, 40, 60, 80, 100};


    // line chart for followers

    LineChartView lineChartViewFollowers;
    String[] axisDatafollowers = {"30 july", "1 aug", "3 aug", "5 aug"};
    int[] yAxisDatafollowers = {0, 20, 40, 60, 80, 100};


    List<SliceValue> pieData = new ArrayList<>();

    public InsightGraphFragment() {
        // Required empty public constructor
    }

    private View view;
    LottieAnimationView image;

    private void initViews() {
        image = view.findViewById(R.id.loading);
        pieChartView = view.findViewById(R.id.Piechart);
        lineChartView = view.findViewById(R.id.LinechartEngagement);
        createSpinnerForEngagement();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_activity, container, false);
        initViews();
        getChartData();
        return view;
    }

    private void showHideLoader(boolean showLoader) {
        if (showLoader) {
            if (image.getVisibility() != View.VISIBLE) {
                image.setVisibility(View.VISIBLE);
            }
        } else {
            image.setVisibility(View.GONE);
        }
    }

    private void createSpinnerForEngagement() {
        Spinner spinner = view.findViewById(R.id.spinnerEnaggement);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Last 7 Days");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private void createPieChart(UserInsightGraphResponse serverResponse) {
        try {
            pieData.add(new SliceValue(serverResponse.getData().getFemaleCount(), ContextCompat.getColor(getActivity(), (R.color.grey))).setLabel(getResources().getString(R.string.female)));
            pieData.add(new SliceValue(serverResponse.getData().getMaleCount(), ContextCompat.getColor(getActivity(), (R.color.blue_dark))).setLabel(getResources().getString(R.string.male)));
            PieChartData pieChartData = new PieChartData(pieData);
            pieChartData.setHasLabels(true).setValueLabelTextSize(16);
            pieChartData.setHasLabels(true).setValueLabelsTextColor(Color.WHITE);
            pieChartView.setPieChartData(pieChartData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLineChartForEngagement(UserInsightGraphResponse serverResponse) {
        try {
            List yAxisValues = new ArrayList();
            List axisValues = new ArrayList();

            Line line = new Line(yAxisValues).setColor(ContextCompat.getColor(getActivity(), (R.color.blue_dark)));

            for (int i = 0; i < serverResponse.getData().getEngagementGraph().size(); i++) {
                axisValues.add(i, new AxisValue(i).setLabel(serverResponse.getData().getEngagementGraph().get(i).getDate()));
            }

            for (int i = 0; i < serverResponse.getData().getEngagementGraph().size(); i++) {

                yAxisValues.add(new PointValue(i, serverResponse.getData().getEngagementGraph().get(i).getCount()));
            }

            List lines = new ArrayList();
            lines.add(line);

            LineChartData data = new LineChartData();
            data.setLines(lines);

            Axis axis = new Axis();
            axis.setValues(axisValues);
            axis.setTextSize(13);

            // axis.setTextColor(Color.parseColor("#03A9F4"));
            data.setAxisXBottom(axis);

            Axis yAxis = new Axis();
            // yAxis.setName("Sales in millions");
            //  yAxis.setTextColor(Color.parseColor("#03A9F4"));
            yAxis.setTextSize(13);
            data.setAxisYLeft(yAxis);
            lineChartView.setLineChartData(data);

            Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
            viewport.top = 100;
            lineChartView.setMaximumViewport(viewport);
            lineChartView.setCurrentViewport(viewport);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String numberDays = "7";

    private void getChartData() {
        try {
            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
            Map<String, String> header = new HashMap<>();
            header.put("x-token", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));
            InsightGraphParameter parameter = new InsightGraphParameter("70e41ece-b935-4deb-b115-b8298a113701",
                    numberDays, "days");
            Call<UserInsightGraphResponse> call = webApi.getInsightsGraph(header, parameter);
            showHideLoader(true);
            call.enqueue(new Callback<UserInsightGraphResponse>() {
                @Override
                public void onResponse(Call<UserInsightGraphResponse> call, Response<UserInsightGraphResponse> response) {
                    showHideLoader(false);
                    UserInsightGraphResponse serverResponse = response.body();
                    if (serverResponse.getSuccess()) {
                        try {
                            createPieChart(serverResponse);
                            setLineChartForEngagement(serverResponse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserInsightGraphResponse> call, Throwable t) {
                    showHideLoader(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void getLineChartForLikes() {
        try {

            Spinner spinnerLikeView = view.findViewById(R.id.spinnerLikes);
            ArrayList<String> arrayListLikes = new ArrayList<>();
            arrayListLikes.add("Last 7 Days");
            ArrayAdapter<String> arrayAdapterlikes = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayListLikes);
            arrayAdapterlikes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerLikeView.setAdapter(arrayAdapterlikes);


            lineChartViewLikes = view.findViewById(R.id.LinechartLikes);
            List yAxisValuesLikes = new ArrayList();
            List axisValuesLikes = new ArrayList();
            Line lineLikes = new Line(yAxisValuesLikes).setColor(Color.parseColor("#3B5998"));

            for (int i = 0; i < axisDataLikes.length; i++) {
                axisValuesLikes.add(i, new AxisValue(i).setLabel(axisDataLikes[i]));
            }

            for (int i = 0; i < yAxisDataLikes.length; i++) {
                yAxisValuesLikes.add(new PointValue(i, yAxisDataLikes[i]));
            }

            List linesLikes = new ArrayList();
            linesLikes.add(lineLikes);

            LineChartData dataLikes = new LineChartData();
            dataLikes.setLines(linesLikes);

            Axis axisLikes = new Axis();
            axisLikes.setValues(axisValuesLikes);

            axisLikes.setTextSize(13);

            // axis.setTextColor(Color.parseColor("#03A9F4"));
            dataLikes.setAxisXBottom(axisLikes);
            Axis yAxisLikes = new Axis();

            // yAxis.setName("Sales in millions");
            //  yAxis.setTextColor(Color.parseColor("#03A9F4"));
            yAxisLikes.setTextSize(13);
            dataLikes.setAxisYLeft(yAxisLikes);
            lineChartViewLikes.setLineChartData(dataLikes);

            Viewport viewportLikes = new Viewport(lineChartViewLikes.getMaximumViewport());
            viewportLikes.top = 100;
            lineChartViewLikes.setMaximumViewport(viewportLikes);
            lineChartViewLikes.setCurrentViewport(viewportLikes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLineChartForFollowers() {
        try {
            Spinner spinnerFollow = view.findViewById(R.id.spinnerFollowers);
            ArrayList<String> arrayListFollow = new ArrayList<>();
            arrayListFollow.add("Last 7 Days");

            ArrayAdapter<String> arrayAdapterfollow = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayListFollow);
            arrayAdapterfollow.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFollow.setAdapter(arrayAdapterfollow);

            lineChartViewFollowers = view.findViewById(R.id.LinechartFollowers);

            List yAxisValuesfollowers = new ArrayList();
            List axisValuesfollowers = new ArrayList();

            Line linefollowers = new Line(yAxisValuesfollowers).setColor(Color.parseColor("#3B5998"));

            for (int i = 0; i < axisDatafollowers.length; i++) {
                axisValuesfollowers.add(i, new AxisValue(i).setLabel(axisDatafollowers[i]));
            }

            for (int i = 0; i < yAxisDatafollowers.length; i++) {
                yAxisValuesfollowers.add(new PointValue(i, yAxisDatafollowers[i]));
            }

            List linesfollowers = new ArrayList();
            linesfollowers.add(linefollowers);

            LineChartData datafollowers = new LineChartData();
            datafollowers.setLines(linesfollowers);

            Axis axisfollowers = new Axis();
            axisfollowers.setValues(axisValuesfollowers);
            axisfollowers.setTextSize(13);

            // axis.setTextColor(Color.parseColor("#03A9F4"));
            datafollowers.setAxisXBottom(axisfollowers);

            Axis yAxisfollowers = new Axis();

            // yAxis.setName("Sales in millions");
            //  yAxis.setTextColor(Color.parseColor("#03A9F4"));

            yAxisfollowers.setTextSize(13);
            datafollowers.setAxisYLeft(yAxisfollowers);
            lineChartViewFollowers.setLineChartData(datafollowers);

            Viewport viewportfollowers = new Viewport(lineChartViewFollowers.getMaximumViewport());
            viewportfollowers.top = 100;
            lineChartViewFollowers.setMaximumViewport(viewportfollowers);
            lineChartViewFollowers.setCurrentViewport(viewportfollowers);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createFollowOneSpinner(){
        Spinner spinnerFollowOne = view.findViewById(R.id.spinnerFollowersOne);
        ArrayList<String> arrayListfollowone = new ArrayList<>();
        arrayListfollowone.add("All");
        ArrayAdapter<String> arrayAdapterfollowone = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayListfollowone);
        arrayAdapterfollowone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFollowOne.setAdapter(arrayAdapterfollowone);
    }*/
}
