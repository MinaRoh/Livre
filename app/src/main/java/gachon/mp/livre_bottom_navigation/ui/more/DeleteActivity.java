package gachon.mp.livre_bottom_navigation.ui.more;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import gachon.mp.livre_bottom_navigation.MainActivity;
import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.SplashActivity;

public class DeleteActivity extends AppCompatActivity {
    MainActivity MA = (MainActivity)MainActivity.Main_Activity;//메인 액티비티
    String TAG = "DeleteActivity";
    EditText passwordEditText;
    ImageButton btn_continue;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    AuthCredential credential;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String token;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        btn_continue = (ImageButton)findViewById(R.id.btn_continue);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();
                credential = EmailAuthProvider.getCredential(token, password);
                //탈퇴 전에 재인증 해야함

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                final String Uid = user.getUid();
                                Log.d(TAG, "User re-authenticated.");
                                //사용자 삭제
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Database에서 user 정보 삭제
                                                    db.collection("Users").document(user.getUid()).delete();
                                                    Log.d(TAG, "User account deleted.");
                                                } else
                                                    Log.d(TAG, "User account isn't deleted.", task.getException());
                                            }
                                        });
                            }
                        });
                Toast.makeText(DeleteActivity.this, "탈퇴 되었습니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                MA.finish();//MainActivity 종료
                finish(); //DeleteActivity 종료
            }
        });
    }
}
