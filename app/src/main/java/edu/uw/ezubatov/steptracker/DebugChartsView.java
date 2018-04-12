package edu.uw.ezubatov.steptracker;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DebugChartsView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DebugChartsView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebugChartsView extends android.support.v4.app.Fragment
implements StepsDetector.OnStepsListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // We use timers to intermittently generate random data for the two graphs
    private final Handler _handler = new Handler();
    private Runnable _timer1;
    private Runnable _timer2;

    private LineGraphSeries<DataPoint> _series1;
    private LineGraphSeries<DataPoint> _series2;
    private double _graph2LastXValue = 30d;

    private LineGraphSeries<DataPoint> _seriesX;
    private LineGraphSeries<DataPoint> _seriesY;
    private LineGraphSeries<DataPoint> _seriesZ;
    private LineGraphSeries<DataPoint> _seriesMag;

    private LineGraphSeries<DataPoint> _seriesRaw;
    private LineGraphSeries<DataPoint> _seriesSmooth;

    private int counter = 0;
    private int rawCounter = 0;

    private OnFragmentInteractionListener mListener;

    public DebugChartsView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DebugChartsView.
     */
    // TODO: Rename and change types and number of parameters
    public static DebugChartsView newInstance(String param1, String param2) {
        DebugChartsView fragment = new DebugChartsView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_debug_charts_view, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        GraphView graphMag = (GraphView)  getView().findViewById(R.id.graphMag);
        _seriesMag = new LineGraphSeries<>();
        _seriesMag.setColor(Color.GREEN);
        _seriesMag.setDrawDataPoints(true);
        _seriesMag.setDataPointsRadius(10);
        _seriesMag.setThickness(8);

        graphMag.addSeries(_seriesMag);
        graphMag.setTitle("Real-Time Magnitude");
        graphMag.getGridLabelRenderer().setVerticalAxisTitle("Accelerometer Magnitude");

        GraphView graphX = (GraphView)  getView().findViewById(R.id.graphX);
        _seriesX = new LineGraphSeries<>();
        _seriesX.setColor(Color.GREEN);
        _seriesX.setTitle("X");
        _seriesY = new LineGraphSeries<>();
        _seriesY.setColor(Color.RED);
        _seriesY.setTitle("Y");
        _seriesZ = new LineGraphSeries<>();
        _seriesZ.setColor(Color.BLUE);
        _seriesZ.setTitle("Z");
        graphX.addSeries(_seriesX);
        graphX.addSeries(_seriesY);
        graphX.addSeries(_seriesZ);
        graphX.setTitle("Real-Time X,Y,Z");
        graphX.getGridLabelRenderer().setVerticalAxisTitle("Accelerometer X,Y,Z");

        GraphView graphSmooth = (GraphView)  getView().findViewById(R.id.graphSmooth);
        _seriesSmooth = new LineGraphSeries<>();
        _seriesRaw = new LineGraphSeries<>();

        _seriesRaw.setColor(Color.BLACK);
        _seriesRaw.setDrawDataPoints(true);
        _seriesRaw.setDataPointsRadius(10);
        _seriesRaw.setThickness(8);


        _seriesSmooth.setColor(Color.RED);
        _seriesSmooth.setDrawDataPoints(true);
        _seriesSmooth.setDataPointsRadius(10);
        _seriesSmooth.setThickness(12);

        graphSmooth.addSeries(_seriesRaw);
        graphSmooth.addSeries(_seriesSmooth);
        graphSmooth.setTitle("Smoothed and Raw data");
        graphSmooth.getGridLabelRenderer().setVerticalAxisTitle("Smoothed Magnitude");

        StepsDetector.getInstance().subscribeForSteps(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSteps(int steps) {

    }

    @Override
    public void onNextBatch(double[] raw, double[] smooted) {
        int i=0;
        for (double r: raw) {
            _seriesRaw.appendData(new DataPoint(rawCounter + (i++), r), false, 40);
        }
        i=0;
        for (double r: smooted) {
            _seriesSmooth.appendData(new DataPoint(rawCounter + (i++), r), false, 40);
        }
        rawCounter += raw.length;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int resource);
    }


    @Override
    public void onResume() {
        super.onResume();
        final int delayMs = 80;
        _timer1 = new Runnable() {
            @Override
            public void run() {
                //_series1.resetData(generateData());
                updateData(counter++);
                _handler.postDelayed(this, delayMs);
            }
        };
        _handler.postDelayed(_timer1, delayMs);
    }

    private void updateData(int c) {
        float[] data = AccelerometerDataManager.getInstance().getCurrentAverage();
        int maxDataPoints = 20;
        _seriesX.appendData(new DataPoint(c, data[0]), false, maxDataPoints);
        _seriesY.appendData(new DataPoint(c, data[1]), false, maxDataPoints);
        _seriesZ.appendData(new DataPoint(c, data[2]), false, maxDataPoints);

        _seriesMag.appendData(new DataPoint(c, MathUtils.vectorLength(data)), false, maxDataPoints);
    }

    @Override
    public void onPause() {
        _handler.removeCallbacks(_timer1);
        //_handler.removeCallbacks(_timer2);
        super.onPause();
    }
}
