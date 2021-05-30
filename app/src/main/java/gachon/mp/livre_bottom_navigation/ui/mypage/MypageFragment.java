package gachon.mp.livre_bottom_navigation.ui.mypage;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

import gachon.mp.livre_bottom_navigation.R;

public class MypageFragment extends Fragment {
    private static final String TAG = "MyPageFragment";
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String profileImg = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mypage, container, false);
        final TextView textView = root.findViewById(R.id.text_mypages);

        ImageView imageView = root.findViewById(R.id.iv_profile);
        //프로필 동그랗게 하기
        imageView.setBackground(new ShapeDrawable(new OvalShape()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setClipToOutline(true);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Users")
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                textView.setText(document.get("nickname").toString());
                                profileImg = document.get("profileImage").toString();
                            }
                            //FirebaseStorage 인스턴스를 생성
                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                            // 위의 저장소를 참조하는 파일명으로 지정
                            StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://mp-livre.appspot.com/"+profileImg);
                            //StorageReference에서 파일 다운로드 URL 가져옴
                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        // Glide 이용하여 이미지뷰에 로딩
                                        if(getActivity() != null){
                                            Glide.with(getActivity())//네비게이션 왔다갔다 이동하다보면 여기서 맨날 에러남.
                                                    .load(task.getResult())
                                                    .override(1024, 980)
                                                    .into(imageView);
                                        }

                                    } else {
                                        // URL을 가져오지 못하면 토스트 메세지
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
        /*사용자 트리 이미지 파트*/
        RecyclerView recyclerView2 = (RecyclerView)root.findViewById(R.id.recyclerView2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        MyTreeAdapter adapter2 = new MyTreeAdapter();
        adapter2.addItem(new MyTree("tree url1"));
        adapter2.addItem(new MyTree("tree url2"));
        adapter2.addItem(new MyTree("tree url3"));
        recyclerView2.setAdapter(adapter2);

        /*마이페이지 - 글 목록 파트*/
        RecyclerView recyclerView = (RecyclerView)root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        MyPageAdapter adapter = new MyPageAdapter();

        /*사용자가 WritingActivity에서 쓴 포스트 내용 가져오기*/
        db.collection("Posts")
                .whereEqualTo("publisher", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                String post_id = document.getId();
                                String nickname = document.getData().get("nickname").toString();
                                Timestamp time = (Timestamp) document.getData().get("uploadTime");
                                String upload_time = getTime(time);
                                String title = document.getData().get("title").toString();
                                String txt_book = document.getData().get("bookTitle").toString();
                                String contents = document.getData().get("contents").toString();
                                String imagePath = document.getData().get("imagePath").toString();
                                Integer num_heart = Integer.parseInt(String.valueOf(document.getData().get("num_heart")));
                                Integer num_comment = Integer.parseInt(String.valueOf(document.getData().get("num_comment")));
                                adapter.addItem(new CommentInfo(user.getUid(), post_id, nickname, upload_time, imagePath, title, txt_book, contents, num_heart, num_comment));
                                recyclerView.setAdapter(adapter);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        return root;
    }

    static String getTime(Timestamp time) {
        Date date_createdAt = time.toDate();//Date형식으로 변경
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
        String txt_createdAt = formatter.format(date_createdAt).toString();
        return txt_createdAt;
    }
}