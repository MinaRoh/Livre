package gachon.mp.livre_bottom_navigation.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.listener.DismissListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;


import org.jetbrains.annotations.NotNull;

import gachon.mp.livre_bottom_navigation.ListActivity;
import gachon.mp.livre_bottom_navigation.R;


import static android.graphics.Color.parseColor;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    public static Context context_home;
    ConstraintLayout bg_area;
    Button button;
    ImageView colorView;
    ImageView bg;
    ImageView trunk;
    ImageView leaves;
    ImageView butterfly;
    int number = 0;
    int bgnum = 1;
    int trunknum = 1;
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

        bg_area = (ConstraintLayout) getActivity().findViewById(R.id.bg_area);
        bg = getActivity().findViewById(R.id.bg);


        button = getActivity().findViewById(R.id.button);
        leaves = getActivity().findViewById(R.id.leaves);
        trunk = getActivity().findViewById(R.id.trunk);
        colorView = getActivity().findViewById(R.id.colorView);
        butterfly = getActivity().findViewById(R.id.butterfly);

        butterfly.setVisibility(View.GONE);//처음엔 나비 안보이게


        Button btn_change_color = getActivity().findViewById(R.id.btn_change_color);
        Button btn_change_bg = getActivity().findViewById(R.id.btn_change_bg);
        Button btn_change_trunk = getActivity().findViewById(R.id.btn_change_trunk);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(number==11) number=0;
                else number++;
                changeLeavesStatus(number);


            }
        });

        //임시 잎 색 변경 버튼
        btn_change_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorPicker();
            }
        });

        //임시 배경 전환 버튼
        btn_change_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bgnum==6) bgnum=1;
                else bgnum++;
                changeBGStatus(bgnum);
            }
        });

        //임시 trunk 전환 버튼
        btn_change_trunk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(trunknum==2) trunknum=1;
                else trunknum++;
                changeTrunk(trunknum);
            }
        });


        //임시 list activity로 가는 버튼
        Button button_temp = getActivity().findViewById(R.id.button_temp);
        button_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startListActivity();
            }
        });


    }


    
    private void colorPicker(){
        new ColorPickerDialog
                .Builder(getActivity())
                .setTitle("나뭇잎 색상을 선택하세요")
                .setColorShape(ColorShape.SQAURE)
                .setDefaultColor(parseColor("#ffffff"))
                .setColorListener(new ColorListener() {
                    @Override
                    public void onColorSelected(int color, @NotNull String colorHex) {
                        System.out.println("색상 int값: " + color + "/ String colorHex: "+colorHex);
                        // Handle Color Selection
                        leaves.setColorFilter(Color.parseColor(colorHex));//나뭇잎 색 변경

                    }
                })
                .setDismissListener(new DismissListener() {
                    @Override
                    public void onDismiss() {
                        // Handle Dismiss Event
                    }
                })
                .show();

    }

    private void startListActivity() {
        Intent intent = new Intent(getActivity(), ListActivity.class);
        startActivity(intent);
    }

    private void changeLeavesStatus(int num) {
        butterfly.setVisibility(View.GONE);
        switch (num) {
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
                butterfly.setVisibility(View.VISIBLE);
                break;
            default: number=0; break;

        }
    }

    private void changeBGStatus(int num) {

        int bg_color = getResources().getColor(R.color.bg_night);

        switch (num) {
            case 1:
                bg_color = getResources().getColor(R.color.bg_night);
                bg.setImageResource(R.drawable.bg_night);
                break;
            case 2:
                bg_color = getResources().getColor(R.color.bg_sky1);
                bg.setImageResource(R.drawable.bg_sky_orange);
                break;
            case 3:
                bg_color = getResources().getColor(R.color.bg_blue1);
                bg.setImageResource(R.drawable.bg_blue_sky);
                break;
            case 4:
                bg_color = getResources().getColor(R.color.bg_blue2);
                bg.setImageResource(R.drawable.bg_blue_lime);
                break;
            case 5:
                bg_color = getResources().getColor(R.color.bg_sky2);
                bg.setImageResource(R.drawable.bg_sky_white);
                break;
            case 6:
                bg_color = getResources().getColor(R.color.bg_pink);
                bg.setImageResource(R.drawable.bg_pink_orange);
                break;

            default: bgnum=0; break;

        }

        bg_area.setBackgroundColor(bg_color);
    }

    private void changeTrunk(int num) {

        switch (num) {
            case 1:
                trunk.setImageResource(R.drawable.trunk_white);
                break;
            case 2:
                trunk.setImageResource(R.drawable.trunk_brown);
                break;

            default: bgnum=0; break;

        }
    }




    private void toastMsg(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}