package taes.accessstreet;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.os.Bundle;

public class InterestPointsFragment extends Fragment {

    public static InterestPointsFragment newInstance() {
        InterestPointsFragment fragment = new InterestPointsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interest_points, container, false);
    }
}
