package spider65.ebike.tsdz2_esp32.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import spider65.ebike.tsdz2_esp32.R;
import spider65.ebike.tsdz2_esp32.TSDZBTService;
import spider65.ebike.tsdz2_esp32.data.TSDZ_Status;
import spider65.ebike.tsdz2_esp32.databinding.FragmentStatusBinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;


public class FragmentStatus extends Fragment implements MainFragment, View.OnLongClickListener {

    private static final String TAG = "FragmentStatus";

    private OnFragmentInteractionListener mListener;

    private IntentFilter mIntentFilter = new IntentFilter();

    private TSDZ_Status status = new TSDZ_Status();


    private TextView modeLevelTV;
    private TextView statusTV;
    private ImageView brakeIV;
    private ImageView streetModeIV;


    private FragmentStatusBinding binding;

    public FragmentStatus() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentStatus.
     */
    public static FragmentStatus newInstance() {
        return new FragmentStatus();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        mIntentFilter.addAction(TSDZBTService.TSDZ_STATUS_BROADCAST);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver, mIntentFilter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_status, container, false);
        binding.setStatus(status);

        View view = binding.getRoot();
        modeLevelTV = view.findViewById(R.id.modeLevelTV);
        statusTV = view.findViewById(R.id.statusTV);
        brakeIV = view.findViewById(R.id.brakeIV);
        streetModeIV = view.findViewById(R.id.streetModeIV);

        return view;
    }

    private void refreshView() {
        if (status.brake)
            brakeIV.setVisibility(View.VISIBLE);
        else
            brakeIV.setVisibility(View.INVISIBLE);

        if (status.status != 0) {
            statusTV.setVisibility(View.VISIBLE);
            statusTV.setText(String.valueOf(status.status));
        } else
            statusTV.setVisibility(View.INVISIBLE);

        if (status.streetMode)
            brakeIV.setVisibility(View.VISIBLE);
        else
            brakeIV.setVisibility(View.INVISIBLE);

        switch (status.ridingMode) {
            case OFF_MODE:
                modeLevelTV.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.off_mode_icon, 0, 0, 0);
                modeLevelTV.setText("0");
                break;
            case eMTB_ASSIST_MODE:
                modeLevelTV.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.emtb_mode_icon, 0, 0, 0);
                modeLevelTV.setText(status.assistLevel);
                break;
            case WALK_ASSIST_MODE:
                modeLevelTV.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.walk_mode_icon, 0, 0, 0);
                modeLevelTV.setText(status.assistLevel);
                break;
            case POWER_ASSIST_MODE:
                modeLevelTV.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.power_mode_icon, 0, 0, 0);
                modeLevelTV.setText(status.assistLevel);
                break;
            case TORQUE_ASSIST_MODE:
                modeLevelTV.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.torque_mode_icon, 0, 0, 0);
                modeLevelTV.setText(status.assistLevel);
                break;
            case CADENCE_ASSIST_MODE:
                modeLevelTV.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.cadence_mode_icon, 0, 0, 0);
                modeLevelTV.setText(status.assistLevel);
                break;
            case CRUISE_MODE:
                modeLevelTV.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.cruise_mode_icon, 0, 0, 0);
                modeLevelTV.setText(status.assistLevel);
                break;
            case CADENCE_SENSOR_CALIBRATION_MODE:
                modeLevelTV.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.off_mode_icon, 0, 0, 0);
                modeLevelTV.setText(R.string.calibration);
                break;
        }
        binding.invalidateAll();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mListener.onFragmentInteraction(1);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.d(TAG, "onReceive " + intent.getAction());
            if (TSDZBTService.TSDZ_STATUS_BROADCAST.equals(intent.getAction())) {
                byte[] statusVal = intent.getByteArrayExtra(TSDZBTService.VALUE_EXTRA);
                //Log.d(TAG, "value = " + Utils.bytesToHex(statusVal));
                if (status.setData(statusVal))
                    refreshView();
            }
        }
    };



    @Override
    public void selected(boolean visibile) {
        // Log.d(TAG, "selected = " + visibile);
        if (visibile)
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver, mIntentFilter);
        else
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.speedValueTV:
            case R.id.cadenceValueTV:
                break;
        }
        return false;
    }
}