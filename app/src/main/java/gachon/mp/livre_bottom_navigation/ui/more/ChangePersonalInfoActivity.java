package gachon.mp.livre_bottom_navigation.ui.more;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import gachon.mp.livre_bottom_navigation.PasswordResetActivity;
import gachon.mp.livre_bottom_navigation.R;

public class ChangePersonalInfoActivity extends AppCompatActivity {
    private static final String TAG = "ChangePersonalInfo";
    private FirebaseAuth mAuth;
    private String nickname;
    private boolean nicknameCheck = false;
    private String document_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_more_1personal_info);
        ImageView user_profile = (ImageView)findViewById(R.id.user_profile);
        ImageButton btn_gallery = (ImageButton)findViewById(R.id.btn_gallery);
        EditText editText_nickname = (EditText)findViewById(R.id.editText_nickname);
        ImageButton btn_change_pw = (ImageButton)findViewById(R.id.btn_change_pw);
        ImageButton btn_save = (ImageButton)findViewById(R.id.btn_save);

        /*유저 닉네임 파이어스토어에서 가져오기*/
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();//사용자
        assert user != null;
        String user_id = user.getUid();//사용자 uid
        //현재 사용자 uid와 동일한 Users document에 접근, 닉네임 값 얻어냄
        db.collection("Users")
                .whereEqualTo("uid", user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                document_id = document.getId();
                                nickname = document.getData().get("nickname").toString();
                                editText_nickname.setHint(nickname);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        //프로필 동그랗게 하기
        user_profile.setBackground(new ShapeDrawable(new OvalShape()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//롤리팝 이상만 지원하나봄?
            user_profile.setClipToOutline(true);
        }
        //갤러리 불러와서 프로필 이미지뷰 바꾸기
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //비밀번호 재설정 액티비티로 던지기
        btn_change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PasswordResetActivity.class);
                startActivity(intent);
            }
        });
        //저장하기(파이어스토어 업데이트) - 프로필 사진, 닉네임
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_nickname = editText_nickname.getText().toString();
                //닉네임 안바꾼 경우
                if(new_nickname == nickname){
                    //프로필 사진만 업데이트
                    Toast.makeText(ChangePersonalInfoActivity.this, "저장되었습니다",
                            Toast.LENGTH_SHORT).show();
                }
                //닉네임 바꾼 경우 - 중복검사 필요함
                else{
                    db.collection("Users")
                            .whereEqualTo("nickname", new_nickname)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int num=0;
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            num = num+1;
                                        }
                                        if(num==0) {
                                            nicknameCheck = true;
                                        }
                                        else{
                                            Toast.makeText(ChangePersonalInfoActivity.this, "중복된 닉네임입니다",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                    if(nicknameCheck){//중복검사 통과
                        //닉네임 업데이트
                        final DocumentReference sfDocRef = db.collection("Users").document(document_id);

                        db.runTransaction(new Transaction.Function<Void>() {
                            @Override
                            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                DocumentSnapshot snapshot = transaction.get(sfDocRef);

                                // Note: this could be done without a transaction
                                //       by updating the population using FieldValue.increment()
                                transaction.update(sfDocRef, "nickname", new_nickname);
                                // Success
                                return null;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Transaction success!");
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Transaction failure.", e);
                                    }
                                });

                        //프로필 사진 업데이트

                        Toast.makeText(ChangePersonalInfoActivity.this, "저장되었습니다",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
