package gachon.mp.livre_bottom_navigation.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.listener.DismissListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.DateTime;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import gachon.mp.livre_bottom_navigation.ListActivity;
import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.pushNoti.SendMessage;
import gachon.mp.livre_bottom_navigation.ui.feed.Book;
import gachon.mp.livre_bottom_navigation.ui.mypage.CommentInfo;
import gachon.mp.livre_bottom_navigation.ui.writing.WritingActivity;


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
    SimpleDateFormat formatter;
    ConstraintLayout bg_area;
    LinearLayout tree_setting;
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

    ImageView reading_img;
    TextView reading_book_title;
    TextView reading_author;
    ViewPager2 reading_pager;
    Button btn_toWriting;
    String reading_book_title_txt;
    String reading_author_txt;
    String reading_img_url_txt;
    String isbn_txt;
    ImageButton magic_want;

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
        bg_area = (ConstraintLayout) getActivity().findViewById(R.id.bg_area);
        tree_setting = getActivity().findViewById(R.id.tree_setting);
        magic_want = getActivity().findViewById(R.id.magic_wand);
        butterfly.setVisibility(View.GONE);//처음엔 나비 안보이게


        // 파베에서 저장한 정보 가져오기
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

                                if (leaf_color.isEmpty()) {
                                    changeLeavesStatus(Integer.parseInt(level));
                                    changeTrunk(Integer.parseInt(trunk_color));
                                    changeBGStatus(Integer.parseInt(background));
                                } else {
                                    changeLeavesStatus(Integer.parseInt(level));
                                    changeTrunk(Integer.parseInt(trunk_color));
                                    changeBGStatus(Integer.parseInt(background));
                                    leaves.setColorFilter(parseColor(leaf_color));
                                }
                            }
                        }
                    }
                });

//         개발자 테스트용(나무성장)
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int data = (Integer.parseInt(level) + 1) % 11;
                db.collection("Tree_current").document(user.getUid()).update("level", Integer.toString(data));
                changeLeavesStatus(data);
                level = Integer.toString(data);
            }
        });


        // 나무 설정 버튼 클릭
        tree_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TreeSetting.class);
                startActivity(intent);
            }
        });
        // 나무 설정 버튼(마법봉) 클릭
        magic_want.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TreeSetting.class);
                startActivity(intent);
            }
        });


        reading_pager = getActivity().findViewById(R.id.reading_pager);
        reading_pager.setOrientation(reading_pager.ORIENTATION_HORIZONTAL);
        ReadingBookAdapter adapter = new ReadingBookAdapter();

        // 읽고 있는 책 정보 db 에서 가져오기
        // user id 가 같은 경우에~
        assert user != null;
        String user_id = user.getUid();//사용자 uid

        reading_book_title = getActivity().findViewById(R.id.reading_book_title);
        reading_author = getActivity().findViewById(R.id.reading_author);
        reading_img = getActivity().findViewById(R.id.reading_img);
        db.collection("Books")
                .whereEqualTo("uid", user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                reading_book_title_txt = document.getData().get("book_title").toString();
                                reading_author_txt = document.getData().get("author").toString();
                                reading_img_url_txt = document.getData().get("imageUrl").toString();
                                isbn_txt = document.getData().get("isbn").toString();

                                adapter.addItem(new Book(reading_book_title_txt, reading_author_txt, reading_img_url_txt, user.getUid(), isbn_txt));
                                reading_pager.setAdapter(adapter);

                            }
                        }
                    }
                });
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
            case 0: // 나무 완성
                butterfly.setVisibility(View.VISIBLE);
                bg_area.setDrawingCacheEnabled(true);
                Bitmap bm = bg_area.getDrawingCache();
                try {
                    // 나무 사진 저장
                    onCap(bm);
                } catch (Exception e) {
                }

                break;
            default:
                break;

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

            default:
                break;

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

            default:
                break;

        }
    }


    private void toastMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void onCap(Bitmap bm) throws Exception {
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            //Unique한 파일명을 만들자.
            formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date now = new Date();
            String filename = "tree_complete/" + user.getUid() + "/" + formatter.format(now) + ".jpg";
            // 저장소에 사진 저장
            StorageReference riverRef = storageRef.child(filename);
            UploadTask uploadTask = riverRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { // db 업데이트
                    int data = (Integer.parseInt(level) + 1) % 11;
                    db.collection("Tree_current").document(user.getUid()).update("level", Integer.toString(data));
                    changeLeavesStatus(data);
                    level = Integer.toString(data);

                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("uid", user.getUid());
                    hashMap.put("img", filename);

                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    database.collection("Tree_complete").add(hashMap);

                }
            });


        } catch (Exception e) {

        } finally {

        }

    }
}
