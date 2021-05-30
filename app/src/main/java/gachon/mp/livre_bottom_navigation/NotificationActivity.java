package gachon.mp.livre_bottom_navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import gachon.mp.livre_bottom_navigation.ui.feed.Feed;
import gachon.mp.livre_bottom_navigation.ui.feed.FeedAdapter;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


//        /*피드-하트 순으로 포스트 나열*/
//        RecyclerView notiRecyclerView = findViewById(R.id.noti_recycler_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        notiRecyclerView.setLayoutManager(layoutManager);
//        NotiAdapter adapter = new NotiAdapter();
//
//
//
//
//        /*사용자가 WritingActivity에서 쓴 포스트 내용 가져오기*/
//        db.collection("Alarm").orderBy("num_heart", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        String publisher = document.getData().get("publisher").toString();
//                        String post_id = document.getId();
//                        String nickname = document.getData().get("nickname").toString();
//                        Timestamp time = (Timestamp) document.getData().get("uploadTime");
//                        String upload_time = getTime(time);
//                        String title = document.getData().get("title").toString();
//                        String contents = document.getData().get("contents").toString();
//                        String imagePath = document.getData().get("imagePath").toString();
//                        Integer num_heart = Integer.parseInt(String.valueOf(document.getData().get("num_heart")));
//                        Integer num_comment = Integer.parseInt(String.valueOf(document.getData().get("num_comment")));
//
//                        user = FirebaseAuth.getInstance().getCurrentUser();
//                        adapter.addItem(new Feed(publisher, post_id, nickname, upload_time, imagePath, title, contents, num_heart, num_comment));
//                        recyclerView.setAdapter(adapter);
//                    }
//                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });
//
    }
//
//    public void setNoti(){ //보내는 값을 받아서 파베에 저장
//        Intent intent = getIntent();
//
//        intent.putExtra("uploadTime", new Timestamp(new Date()));
//        intent.putExtra("detail", "comment");
//        intent.putExtra("user_sent", user.getUid());
//        intent.putExtra("msgTitle", msgTitle);
//        intent.putExtra("msgContent", msgContent);
//
//        Timestamp uploadTime = intent.getExtra("uploadTime");
//        List user_sent = Collections.singletonList(intent.getStringExtra("user_sent"));
//        String msgTitle = intent.getStringExtra("msgTitle");
//        String msgContent = intent.getStringExtra("msgContent");
//
//        //리스트에 add로 추가? 해서 리스트를 리사이클러뷰로 보여주기
//    }
//
//
//    //FeedFragment에서 가져온것
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_feed, container, false);
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        /*피드-하트 순으로 포스트 나열*/
//        recyclerView = (RecyclerView)layout.findViewById(R.id.recycler_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        FeedAdapter adapter = new FeedAdapter();
//
//        /*사용자가 WritingActivity에서 쓴 포스트 내용 가져오기*/
//        db.collection("Posts").orderBy("num_heart", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        String publisher = document.getData().get("publisher").toString();
//                        String post_id = document.getId();
//                        String nickname = document.getData().get("nickname").toString();
//                        Timestamp time = (Timestamp) document.getData().get("uploadTime");
//                        String upload_time = getTime(time);
//                        String title = document.getData().get("title").toString();
//                        String contents = document.getData().get("contents").toString();
//                        String imagePath = document.getData().get("imagePath").toString();
//                        Integer num_heart = Integer.parseInt(String.valueOf(document.getData().get("num_heart")));
//                        Integer num_comment = Integer.parseInt(String.valueOf(document.getData().get("num_comment")));
//
//                        user = FirebaseAuth.getInstance().getCurrentUser();
//                        adapter.addItem(new Feed(publisher, post_id, nickname, upload_time, imagePath, title, contents, num_heart, num_comment));
//                        recyclerView.setAdapter(adapter);
//                    }
//                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });
//        return layout;
//    }
//
//    static String getTime(Timestamp time) {
//        Date date_createdAt = time.toDate();//Date형식으로 변경
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
//        String txt_createdAt = formatter.format(date_createdAt).toString();
//        return txt_createdAt;
//    }


}
