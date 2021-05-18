package gachon.mp.livre_bottom_navigation.ui.writing;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import gachon.mp.livre_bottom_navigation.R;

/*글쓰기 완료한 포스트를 보여주는 액티비티
* 하트수, 댓글, 메뉴(수정,삭제) 기능이 연결 돼 있음*/
public class PostActivity extends AppCompatActivity {
    private static final String TAG = "PostActivity";
    private String posts_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        TextView title = (TextView)findViewById(R.id.title);
        ImageView user_profile = (ImageView)findViewById(R.id.user_profile);
        TextView nickname = (TextView)findViewById(R.id.nickname);
        TextView upload_time = (TextView)findViewById(R.id.upload_time);
        ImageButton user_menu = (ImageButton)findViewById(R.id.user_menu);
        ImageView post_image = (ImageView)findViewById(R.id.post_image);
        TextView contents = (TextView)findViewById(R.id.contents);
        ImageButton heart = (ImageButton)findViewById(R.id.heart);
        TextView num_heart = (TextView)findViewById(R.id.num_heart);
        ImageButton comment = (ImageButton)findViewById(R.id.comment);
        TextView num_comment = (TextView)findViewById(R.id.num_comment);
        //문서의 uid를 전달 받아서 해당 문서를 보여준다.
        Intent intent = getIntent();
        posts_id = intent.getStringExtra("posts_id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Posts").document(posts_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String txt_title = document.getData().get("title").toString();
                        String txt_nickname = document.getData().get("nickname").toString();
                        String txt_contents = document.getData().get("contents").toString();
                        String txt_uploadTime = document.getData().get("uploadTime").toString();
                        int int_num_heart = Integer.parseInt(String.valueOf(document.getData().get("num_heart")));
                        int int_num_comment = Integer.parseInt(String.valueOf(document.getData().get("num_comment")));
                        title.setText(txt_title);
                        nickname.setText(txt_nickname);
                        upload_time.setText(txt_uploadTime);
                        contents.setText(txt_contents);
                        num_heart.setText(String.valueOf(int_num_heart));
                        num_comment.setText(String.valueOf(int_num_comment));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }
}
