package spider65.ebike.tsdz2_sw102.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import spider65.ebike.tsdz2_sw102.R;
import spider65.ebike.tsdz2_sw102.data.TSDZ_Trip;
import spider65.ebike.tsdz2_sw102.databinding.FragmentTripStatsBinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


import org.jetbrains.annotations.NotNull;


public class FragmentTrip extends Fragment implements View.OnLongClickListener, MyFragmentListener {

    private static final String TAG = "FragmentTrip";

    //private IntentFilter mIntentFilter = new IntentFilter();

    private TSDZ_Trip tsdz_trip;
    private TextView speed, speedV, maxspeed, maxspeedV;
    private FragmentTripStatsBinding binding;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentStatus.
     * @param trip
     */
    public static FragmentTrip newInstance(TSDZ_Trip trip) {
        return new FragmentTrip(trip);
    }

    private FragmentTrip(TSDZ_Trip tsdz_trip) {
        this.tsdz_trip = tsdz_trip;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip_stats, container, false);
        View view = binding.getRoot();
        binding.setTsdzTrip(tsdz_trip);
        FrameLayout frame = view.findViewById(R.id.fl21);
        frame.setOnLongClickListener(this);
        speed =  (TextView) view.findViewById(R.id.speedTV);
        speedV = (TextView)  view.findViewById(R.id.speedValueTV);
        maxspeed =  (TextView) view.findViewById(R.id.maxspeedTV);
        maxspeedV = (TextView)  view.findViewById(R.id.maxspeedValueTV);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Data could be changed when fragment was not visible. Refresh the view
        binding.invalidateAll();
    }

    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()) {
            case R.id.fl21:
                if (speed.getVisibility() == View.VISIBLE) {
                    speed.setVisibility(View.GONE);
                    speedV.setVisibility(View.GONE);
                    maxspeed.setVisibility(View.VISIBLE);
                    maxspeedV.setVisibility(View.VISIBLE);
                }else {
                    maxspeed.setVisibility(View.GONE);
                    maxspeedV.setVisibility(View.GONE);
                    speed.setVisibility(View.VISIBLE);
                    speedV.setVisibility(View.VISIBLE);
                }
                break;
        }
        return false;
    }


    @Override
    public void refreshView() {
        binding.invalidateAll();
    }
}
