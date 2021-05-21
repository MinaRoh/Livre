package gachon.mp.livre_bottom_navigation.ui.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import gachon.mp.livre_bottom_navigation.R;

import static android.content.ContentValues.TAG;

public class FeedPostFragment extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<String> participants;
    String posts_id;
    int int_numOfRecruits, int_curRecruits;
    TextView peopleNum;
    TextView status;


    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_post, container, false);
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = getIntent();

        TextView title = (TextView) findViewById(R.id.title);
        TextView nickname = (TextView) findViewById(R.id.nickname);

        TextView upload_time = (TextView) findViewById(R.id.upload_time);
        ImageButton user_menu = (ImageButton) findViewById(R.id.user_menu);
        TextView contents = (TextView) findViewById(R.id.contents);
        ImageButton comment = (ImageButton) findViewById(R.id.comment);
        //        TextView num_recruit = (TextView)findViewById(R.id.num_recruit);

//        Button btn_join = findViewById(R.id.btn_join);


        // getIntent로 customAdapter에서 전달받은 값 가져오기


        String txt_title = intent.getExtras().getString("title");
        String txt_nickname = intent.getExtras().getString("nickname");
        String txt_contents = intent.getExtras().getString("contents");
        String txt_publisher = intent.getExtras().getString("uid(publisher)");
        String txt_selectedCategory = intent.getExtras().getString("selectedCategory");
        String txt_createdAt = intent.getExtras().getString("created_at");
        String txt_status = intent.getExtras().getString("status");
        int_numOfRecruits = intent.getExtras().getInt("numOfRecruits");
        int_curRecruits = intent.getExtras().getInt("curRecruits");
        participants = intent.getExtras().getStringArrayList("participants");
        posts_id = intent.getExtras().getString("posts_id");



        //위에서 받아온 내용 각각의 textview에 setText
        title.setText(txt_title);
        nickname.setText(txt_nickname);
        upload_time.setText(txt_createdAt);
        contents.setText(txt_contents);
        status.setText(txt_status);
        peopleNum.setText(int_curRecruits + "/" + int_numOfRecruits);


//        //참여하기 버튼 클릭
//        btn_join = findViewById(R.id.btn_join);
//        btn_join.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                joinIn(user.getUid()); //참여자의 uid
//
//            }
//        });


    }


    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
