package gachon.mp.livre_bottom_navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ListDetailActivity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<String> participants;
    String posts_id;
    int int_numOfRecruits, int_curRecruits;
    TextView peopleNum;
    TextView status;
    int int_num_heart;
    int int_num_comment;
    Boolean heart_clicked = false;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Intent intent = getIntent();


        TextView title = (TextView) findViewById(R.id.title);
        TextView nickname = (TextView) findViewById(R.id.nickname);
        TextView uploadTime = (TextView) findViewById(R.id.upload_time);
        ImageButton user_menu = (ImageButton) findViewById(R.id.user_menu);
        TextView contents = (TextView) findViewById(R.id.contents);
        ImageButton comment = (ImageButton) findViewById(R.id.comment);
        ImageButton heart = (ImageButton)findViewById(R.id.heart);
        TextView num_heart = (TextView)findViewById(R.id.num_heart);
        //        TextView num_recruit = (TextView)findViewById(R.id.num_recruit);




        // getIntent??? customAdapter?????? ???????????? ??? ????????????


        String txt_title = intent.getExtras().getString("title");
        String txt_nickname = intent.getExtras().getString("nickname");
        String txt_contents = intent.getExtras().getString("contents");
        String txt_publisher = intent.getExtras().getString("publisher");
        String txt_selectedCategory = intent.getExtras().getString("selectedCategory");
        String txt_createdAt = intent.getExtras().getString("created_at");
        String txt_status = intent.getExtras().getString("status");
        posts_id = intent.getExtras().getString("posts_id");


        //????????? ????????? ?????? ????????? textview??? setText
        title.setText(txt_title);
        nickname.setText(txt_nickname);
        uploadTime.setText(txt_createdAt);
        contents.setText(txt_contents);


        /*?????? ????????? ???*/
        heart.setOnClickListener(new View.OnClickListener() {
            //?????????: ?????? ??????????????? ?????? ????????? ???????????? ????????? ???????????? ??????
            //????????? ????????? ??? ????????? ??? ????????? ?????? ?????? ????????? ????????? ?????? ?????? ????????? ????????????.
            @Override
            public void onClick(View view) {
                heart_clicked = !(heart_clicked);
                if (heart_clicked) {
                    int_num_heart++;//?????? ??? ?????????
                    heart.setImageResource(R.drawable.baseline_favorite_24);
                } else {
                    int_num_heart--;//?????? ??? ?????????
                    heart.setImageResource(R.drawable.baseline_favorite_border_24);
                }
                num_heart.setText(String.valueOf(int_num_heart));//???????????? ??????
                DocumentReference docRef = db.collection("Posts").document(posts_id);
                docRef
                        .update("num_heart", int_num_heart)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }
        });



    }






    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
