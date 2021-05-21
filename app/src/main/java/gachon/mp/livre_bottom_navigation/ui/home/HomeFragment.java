package gachon.mp.livre_bottom_navigation.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.skydoves.colorpickerpreference.ColorPickerDialog;

import gachon.mp.livre_bottom_navigation.ListActivity;
import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.ui.more.AlarmActivity;

public class HomeFragment extends Fragment {

    Button button;
    Button button2;
    ImageView bg;
    ImageView trunk;
    ImageView leaves;
    int number = 0;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // final TextView textView = root.findViewById(R.id.text_dashboard);


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        button = getActivity().findViewById(R.id.button);
        leaves = getActivity().findViewById(R.id.leaves);

        Button btn_change_color = getActivity().findViewById(R.id.btn_change_color);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(number==11) number=0;
                else number++;

                switch (number) {
                    case 1:
                        leaves.setImageResource(R.drawable.level1);
                        break;
                    case 2:
                        leaves.setImageResource(R.drawable.level2);
                        break;
                    case 3:
                        leaves.setImageResource(R.drawable.level3);
                        break;
                    case 4:
                        leaves.setImageResource(R.drawable.level4);
                        break;
                    case 5:
                        leaves.setImageResource(R.drawable.level5);
                        break;
                    case 6:
                        leaves.setImageResource(R.drawable.level6);
                        break;
                    case 7:
                        leaves.setImageResource(R.drawable.level7);
                        break;
                    case 8:
                        leaves.setImageResource(R.drawable.level8);
                        break;
                    case 9:
                        leaves.setImageResource(R.drawable.level9);
                        break;
                    case 10:
                        leaves.setImageResource(R.drawable.level10);
                        break;
                    case 11:
                        leaves.setImageResource(R.drawable.level11);
                        break;
                    default: number=0; break;

                }
            }
        });

//        btn_change_color.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getActivity().HomeFragment.this);
//                builder.setTitle("MyColorPickerDialog");
//                builder.setPreferenceName("MyColorPickerDialog");
////                builder.setFlagView(new CustomFlag(HomeFragment.this, R.layout.custom_flag));
//
//                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialogInterface.dismiss();
//                    }
//                });
//            }
//        });


                //임시로 list acvitiy로 가는 버튼
        Button button_temp = getActivity().findViewById(R.id.button_temp);
        button_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startListActivity();
            }
        });


    }

    private void changeColor(String color){

    }

    private void startListActivity() {
        Intent intent = new Intent(getActivity(), ListActivity.class);
        startActivity(intent);
    }
}