package gachon.mp.livre_bottom_navigation.ui.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import gachon.mp.livre_bottom_navigation.R;

public class MypageFragment extends Fragment {

    private MypageViewModel mypageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mypageViewModel =
                new ViewModelProvider(this).get(MypageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mypage, container, false);
        final TextView textView = root.findViewById(R.id.text_mypages);
        mypageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}