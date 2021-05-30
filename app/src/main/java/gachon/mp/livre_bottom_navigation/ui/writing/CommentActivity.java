package gachon.mp.livre_bottom_navigation.ui.writing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gachon.mp.livre_bottom_navigation.R;

public class CommentActivity extends AppCompatActivity {
    private static final String TAG = "CommentActivity";
    private String post_id;
    String comment;
    String nickname;
    String profile_image;
    String time;
    Timestamp timestamp;
    long num_comment;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        user = FirebaseAuth.getInstance().getCurrentUser();
        EditText edit_comment = (EditText) findViewById(R.id.edit_comment);
        Button btn_button = (Button) findViewById(R.id.btn_submit);

        /*문서의 uid를 전달 받아서 해당 문서의 댓글 내용을 보여준다.*/
        Intent intent = getIntent();
        post_id = intent.getStringExtra("posts_id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CommentActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        CommentAdapter adapter = new CommentAdapter();

        db.collection("Comment")
                .whereEqualTo("postID", post_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                profile_image = document.get("profile_image").toString();
                                nickname = document.get("nickname").toString();
                                comment = document.get("content").toString();
                                timestamp = (Timestamp) document.get("time");
                                time = getTime(timestamp);
                                //어댑터 add
                                adapter.addItem(new CommentInfo(comment, nickname, post_id, profile_image, time));
                                recyclerView.setAdapter(adapter);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        /*포스트 댓글 수 가져오기*/
        db.collection("Posts")
                .whereEqualTo("posts_id", post_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                num_comment = (long) document.get("num_comment");
                                num_comment++;
                                //Comment DB에 데이터 업데이트
                                Map<String, Object> data = new HashMap<>();
                                data.put("num_comment", num_comment);
                                db.collection("Comment")
                                        .add(data)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        /*사용자가 댓글을 입력했을 때*/
        btn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment = edit_comment.getText().toString();
                String uid = user.getUid(); // 댓글 쓴 유저의 uid
                timestamp = new Timestamp(new Date()); // 댓글 등록한 시간
                time = getTime(timestamp);
                // 유저 닉네임 가져오기
                db.collection("Users")
                        .whereEqualTo("uid", uid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        nickname = document.get("nickname").toString();
                                        profile_image = document.get("profileImage").toString();
                                        //Comment DB에 데이터 추가
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("content", comment);
                                        data.put("nickname", nickname);
                                        data.put("postID", post_id);
                                        data.put("profile_image", profile_image);
                                        data.put("time", timestamp);

                                        db.collection("Comment")
                                                .add(data)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error adding document", e);
                                                    }
                                                });
                                        //어댑터에 값 전달
                                        adapter.addItem(new CommentInfo(comment, nickname, post_id, profile_image, time));
                                        recyclerView.setAdapter(adapter);
                                        edit_comment.setText("");
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });


            }
        });


    }

    static String getTime(Timestamp time) {
        Date date_createdAt = time.toDate();//Date형식으로 변경
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
        String txt_createdAt = formatter.format(date_createdAt).toString();
        return txt_createdAt;
    }
}
