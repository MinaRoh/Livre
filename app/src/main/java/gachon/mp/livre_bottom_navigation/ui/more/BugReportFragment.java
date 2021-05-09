package gachon.mp.livre_bottom_navigation.ui.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import gachon.mp.livre_bottom_navigation.R;

public class BugReportFragment extends Fragment {

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more_5bug_report, container, false);
    }
}
