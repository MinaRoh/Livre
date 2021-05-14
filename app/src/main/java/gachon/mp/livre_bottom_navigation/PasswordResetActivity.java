package gachon.mp.livre_bottom_navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class PasswordResetActivity extends AppCompatActivity{
    private static final String TAG = "PasswordResetActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);
        ImageButton btn_send = (ImageButton)findViewById(R.id.btn_send);
        EditText emailEditText = findViewById(R.id.emailEditText);
        ImageButton btn_find_pw = (ImageButton)findViewById(R.id.btn_find_pw);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //왼쪽 상단의 뒤로가기 버튼을 눌렀을 때
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //이메일 보내기 버튼을 눌렀을 때
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = emailEditText.getText().toString();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "인증 이메일을 전송했습니다",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                                    Toast.makeText(PasswordResetActivity.this, "해당하는 계정 정보가 없습니다",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "PasswordResetEmail:failure", task.getException());
                                    Toast.makeText(PasswordResetActivity.this, "메일 전송에 실패했습니다",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
    }
    }
