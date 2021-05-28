package gachon.mp.livre_bottom_navigation.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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


import com.bumptech.glide.Glide;
import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.listener.DismissListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import org.jetbrains.annotations.NotNull;

import gachon.mp.livre_bottom_navigation.ListActivity;
import gachon.mp.livre_bottom_navigation.R;


import static android.graphics.Color.parseColor;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    public static Context context_home;
    private String level;
    private String trunk_color;
    private String leaf_color;
    private String background;
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


        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Tree_current")
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                level = document.get("level").toString();
                                trunk_color = document.get("color_trunk").toString();
                                leaf_color = document.get("color_leaf").toString();
                                background = document.get("background").toString();

                                if(leaf_color.isEmpty()) {
                                    changeLeavesStatus(Integer.parseInt(level));
                                    changeTrunk(Integer.parseInt(trunk_color));
                                    changeBGStatus(Integer.parseInt(background));
                                    leaves.setColorFilter(Color.parseColor(leaf_color));
                                }
                                else{
                                    changeLeavesStatus(Integer.parseInt(level));
                                    changeTrunk(Integer.parseInt(trunk_color));
                                    changeBGStatus(Integer.parseInt(background));
                                }
                            }
                        }
                    }
                });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int data = (Integer.parseInt(level)+1)%11;
                db.collection("Tree_current").document(user.getUid()).update("level", Integer.toString(data));
                changeLeavesStatus(data);
                level = Integer.toString(data);
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
                int data = (Integer.parseInt(background)+1)%6;
                db.collection("Tree_current").document(user.getUid()).update("background", Integer.toString(data));
                changeBGStatus(data);
                background = Integer.toString(data);
            }
        });

        //임시 trunk 전환 버튼
        btn_change_trunk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int data = (Integer.parseInt(trunk_color)+1)%2;
                db.collection("Tree_current").document(user.getUid()).update("color_trunk", Integer.toString(data));
                changeTrunk(data);
                trunk_color = Integer.toString(data);
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
                        db.collection("Tree_current").document(user.getUid()).update("color_leaf", colorHex);
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
            case 0:
                butterfly.setVisibility(View.VISIBLE);
                break;
            default: break;

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
            case 0:
                bg_color = getResources().getColor(R.color.bg_pink);
                bg.setImageResource(R.drawable.bg_pink_orange);
                break;

            default: break;

        }

        bg_area.setBackgroundColor(bg_color);
    }

    private void changeTrunk(int num) {

        switch (num) {
            case 1:
                trunk.setImageResource(R.drawable.trunk_white);
                break;
            case 0:
                trunk.setImageResource(R.drawable.trunk_brown);
                break;

            default:  break;

        }
    }




    private void toastMsg(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}