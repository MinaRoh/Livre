package gachon.mp.livre_bottom_navigation.ui.more;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gachon.mp.livre_bottom_navigation.CustomAdapter;
import gachon.mp.livre_bottom_navigation.NotiInfo;
import gachon.mp.livre_bottom_navigation.Protocol;
import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.ui.mypage.CommentInfo;
import gachon.mp.livre_bottom_navigation.ui.mypage.PostActivity;
import gachon.mp.livre_bottom_navigation.ui.writing.WriteInfo;

public class AlarmActivity extends AppCompatActivity {
    private FirebaseUser user;
    List<NotiInfo> writeNotiList = new ArrayList<>();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_more_alarm);
        RecyclerView mRecyclerView;
        //layout manager for recyclerview
        RecyclerView.LayoutManager layoutManager;

        //firestore instance
        FirebaseFirestore db;
        CustomAdapter adapter;

        // 2. 파베 Alarm -> 내 uid -> 알람들 싹 다 가져오기(시간순)


//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        /*사용자가 WritingActivity에서 쓴 포스트 내용 가져오기*/
//        db.collection("Alarm")
//                .whereEqualTo("receiver", user.getUid())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                //Log.d(TAG, document.getId() + " => " + document.getData());
//
//                                this.uploadTime = uploadTime;
//                                this.category = category;
//                                this.msgTitle = msgTitle;
//                                this.msgContent = msgContent;
//                                this.sender = sender;
//                                this.receiver = receiver;
//
//
//                                Timestamp time = (Timestamp) document.getData().get("uploadTime");
//                                String title = document.getData().get("msgTitle").toString();
//                                String txt_book = document.getData().get("category").toString();
//                                String contents = document.getData().get("msgContent").toString();
//                                String sender = document.getData().get("sender").toString();
//                                String receiver = document.getData().get("receiver").toString();
//
//                                adapter.addItem(new CommentInfo(user.getUid(), post_id, nickname, upload_time, imagePath, title, txt_book, contents, num_heart, num_comment));
//                                recyclerView.setAdapter(adapter);
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
    }


//    private void showData() {
////        final DocumentReference documentReference = db.collection("Alarm").document();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("Alarm")
//                .whereEqualTo("receiver", user.getUid())
//                .orderBy("uploadTime", Query.Direction.DESCENDING) // show from the recent posts
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        //show data
//                        for (DocumentSnapshot doc : task.getResult()) {
//
//
//                            NotiInfo notiInfo = new NotiInfo(
//
//                                    doc.getTimestamp("uploadTime"),
//                                    doc.getString("category"),
//                                    doc.getString("msgTitle"),
//                                    doc.getString("msgContent"),
//                                    doc.getString("sender"),
//                                    doc.getString("receiver")
//                            );
//
//                        }
//
//
//                        //adapter
//                        adapter = new NoticeAdapter(gachon.mp.livre_bottom_navigation.ui.more.AlarmActivity.this, writeNotiList);
//                        //set adapter to recyclerview
//                        mRecyclerView.setAdapter(adapter);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        //called when there is any error while retrieving
//                        startToast(e.getMessage());
//                    }
//                });
//    }





}
