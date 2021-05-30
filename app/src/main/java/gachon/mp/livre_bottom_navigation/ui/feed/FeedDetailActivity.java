package gachon.mp.livre_bottom_navigation.ui.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import gachon.mp.livre_bottom_navigation.CustomAdapter;
import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.ui.mypage.CommentInfo;
import gachon.mp.livre_bottom_navigation.ui.mypage.MyPageAdapter;
import gachon.mp.livre_bottom_navigation.ui.writing.WritingActivity;

public class FeedDetailActivity extends AppCompatActivity {
    private static final String TAG = "FeedDetailActivity";
    private FirebaseUser user;

    String titleFromMain;
    String descriptionFromMain;
    String authorFromMain;
    String imageFromMain;
    String isbnFromMain;

    TextView title;
    TextView author;
    TextView description;
    ImageView image;

    ImageButton back_btn;
    Button writing_report;
    Button reading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        // activity_book_detail 에 put 할 것들
        title = findViewById(R.id.txttitle);
        description = findViewById(R.id.txtdescription);
        author = findViewById(R.id.txtauthor);
        image = findViewById(R.id.image_detail);

        Intent intent = getIntent();
        titleFromMain = intent.getStringExtra("title");
        descriptionFromMain = intent.getStringExtra("description");
        authorFromMain = intent.getStringExtra("author");
        imageFromMain = intent.getStringExtra("image");
        // isbn
        isbnFromMain = intent.getStringExtra("isbn");
        // test
        System.out.println("***********************************************************************");
        System.out.println(isbnFromMain);

        title.setText(titleFromMain);
        description.setText(descriptionFromMain);
        author.setText(authorFromMain);

        // load image
        Picasso.get().load(imageFromMain).resize(200, 270).into(image);

        //ISBN과 책 제목을 WritingActivity로 전달
        writing_report = findViewById(R.id.btn_write_report);
        writing_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isbn_toWriting=isbnFromMain;
                String title_toWriting=titleFromMain;

                Intent writing_intent=new Intent(getApplicationContext(), WritingActivity.class);
                writing_intent.putExtra("isbn", isbn_toWriting);
                writing_intent.putExtra("book_title", title_toWriting);

                startActivity(writing_intent);
            }
        });

        // 읽고있는 책으로 추가하기 버튼 클릭
        reading = findViewById(R.id.btn_reading);
        reading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookUpdate();
            }
        });

        // 뒤로가기 버튼 클릭
        back_btn = findViewById(R.id.feed_detail_btn_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        FeedDetailAdapter adapter = new FeedDetailAdapter();

        /*사용자가 WritingActivity에서 쓴 포스트 내용 가져오기*/
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Posts")
                .whereEqualTo("bookTitle", titleFromMain)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String publisher = document.getData().get("publisher").toString();
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

                                user = FirebaseAuth.getInstance().getCurrentUser();
                                adapter.addItem(new CommentInfo(publisher, post_id, nickname, upload_time, imagePath, title, txt_book, contents, num_heart, num_comment));
                                recyclerView.setAdapter(adapter);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    static String getTime(Timestamp time) {
        Date date_createdAt = time.toDate();//Date형식으로 변경
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
        String txt_createdAt = formatter.format(date_createdAt).toString();
        return txt_createdAt;
    }

    private void bookUpdate() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        Book book = new Book(titleFromMain, authorFromMain, imageFromMain, user.getUid(), isbnFromMain);
        bookUploader(book);
    }

    /* firestore Books 에 업로드 하는 메서드 */
    private void bookUploader(Book book) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Books").add(book)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "등록 완료하였습니다.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getApplicationContext(), "등록에 실패하였습니다.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void mOnClick(View view) {
        finish();
    }
}
