package gachon.mp.livre_bottom_navigation.ui.more;

import android.net.Uri;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import gachon.mp.livre_bottom_navigation.R;

public class AlarmSettingActivity extends AppCompatActivity {
    private FirebaseUser user;
    private String current_comment;
    private String current_heart;
    Switch comment;
    Switch heart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_more_2alarm_setting);
        comment = findViewById(R.id.commentSwich);
        heart = findViewById(R.id.heartSwich);
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
                                current_comment = document.get("commentAlarm").toString();
                                if(current_comment.equals("on")) comment.setChecked(true);
                                else comment.setChecked(false);

                                current_heart = document.get("heartAlarm").toString();
                                if(current_heart.equals("on")) heart.setChecked(true);
                                else heart.setChecked(false);
                            }

                        }
                    }
                });

        comment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    db.collection("Users").document(user.getUid()).update("commentAlarm", "off");
                    Toast.makeText(getApplication(),"댓글 알림이 해제되었습니다.",Toast.LENGTH_SHORT).show();
                } else{
                    db.collection("Users").document(user.getUid()).update("commentAlarm", "on");
                    Toast.makeText(getApplication(),"댓글 알림이 활성화되었습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        heart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    db.collection("Users").document(user.getUid()).update("heartAlarm", "off");
                    Toast.makeText(getApplication(),"좋아요 알림이 해제되었습니다.",Toast.LENGTH_SHORT).show();
                } else{
                    db.collection("Users").document(user.getUid()).update("heartAlarm", "on");
                    Toast.makeText(getApplication(),"좋아요 알림이 활성화되었습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
