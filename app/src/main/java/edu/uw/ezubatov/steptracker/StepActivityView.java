package edu.uw.ezubatov.steptracker;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepActivityView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepActivityView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepActivityView extends android.support.v4.app.Fragment
        implements StepsDetector.OnStepsListener,
        SensorDataManager.OnAndroidStepsListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StepActivityView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StepActivityView.
     */
    // TODO: Rename and change types and number of parameters
    public static StepActivityView newInstance(String param1, String param2) {
        StepActivityView fragment = new StepActivityView();
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

        StepsDetector.getInstance().subscribeForSteps(this);
        SensorDataManager.getInstance().subscribeForSteps(this);

        /*
        MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        User user = new User("Test", "User");
        binding.setUser(user);
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_activity_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressBar bar = (ProgressBar) getView().findViewById(R.id.progressBar2);
        if (bar != null) {
            bar.setMax(100);
            bar.setProgressTintMode(PorterDuff.Mode.DARKEN);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(R.layout.fragment_step_activity_view);
        }
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
        View view = this.getView();
        if (view == null) return;

        TextView counter = (TextView) view.findViewById(R.id.stepCounterView);
        if (counter != null) {
            Log.i("","Setting steps");
            counter.setText(Integer.toString(steps));
        }

        ProgressBar bar = (ProgressBar) view.findViewById(R.id.progressBar2);
        if (bar != null) {
            Log.i("","Setting bar");
            bar.setProgress(steps);
        }
    }

    @Override
    public void onAndroidSteps(int steps) {
        View view = this.getView();
        if (view == null) return;

        TextView androidSensorStepsCounter = (TextView) view.findViewById(R.id.androidSensorStepsCounter);
        if (androidSensorStepsCounter != null) {
            Log.i("","Setting android steps");
            androidSensorStepsCounter.setText(Integer.toString(steps));
        }
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
}
