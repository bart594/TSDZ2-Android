package spider65.ebike.tsdz2_sw102;


import androidx.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import spider65.ebike.tsdz2_sw102.data.TSDZ_Debug;
import spider65.ebike.tsdz2_sw102.data.TSDZ_Status;
import spider65.ebike.tsdz2_sw102.data.TSDZ_Trip;
import spider65.ebike.tsdz2_sw102.fragments.FragmentDebug;
import spider65.ebike.tsdz2_sw102.fragments.FragmentStatus;
import spider65.ebike.tsdz2_sw102.fragments.FragmentTrip;
import spider65.ebike.tsdz2_sw102.fragments.MyFragmentListener;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    private static final Fragment[] TAB_FRAGMENTS = new Fragment[3];
    private final Context mContext;

    MainPagerAdapter(Context context, FragmentManager fm, TSDZ_Trip trip, TSDZ_Status status, TSDZ_Debug debug) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        TAB_FRAGMENTS[0] = FragmentTrip.newInstance(trip);
        TAB_FRAGMENTS[1] = FragmentStatus.newInstance(status);
        TAB_FRAGMENTS[2] = FragmentDebug.newInstance(debug);
        mContext = context;

    }


    @NotNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        return TAB_FRAGMENTS[position];
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
       // TSDZBTService service = TSDZBTService.getBluetoothService();
        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.trip_data);
            case 1:
                return mContext.getResources().getString(R.string.status_data);
            case 2:
                return mContext.getResources().getString(R.string.debug_data);

        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_FRAGMENTS.length;
    }

    MyFragmentListener getMyFragment(int position) {
        return (MyFragmentListener)TAB_FRAGMENTS[position];
    }
}