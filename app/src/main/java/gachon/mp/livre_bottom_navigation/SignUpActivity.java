package gachon.mp.livre_bottom_navigation;

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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1000;
    private boolean nicknameCheck = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ImageButton sign_up_btn_back = (ImageButton)findViewById(R.id.sign_up_btn_back);
        ImageButton btn_continue = (ImageButton)findViewById(R.id.btn_continue);
        ImageButton btn_login = (ImageButton)findViewById(R.id.btn_login);
        ImageButton btn_duplication_check = (ImageButton)findViewById(R.id.btn_duplication_check);
        ImageButton btn_google_signup = (ImageButton)findViewById(R.id.btn_google_signup);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //왼쪽 상단의 뒤로가기 버튼을 눌렀을 때
        sign_up_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Continue 버튼(이메일 회원가입 버튼)을 눌렀을 때
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivityForResult(intent, Protocol.SIGN_IN_CLICKED);
                finish();
            }
        });
        btn_duplication_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                EditText nicknameEditText = findViewById(R.id.nicknameEditText);
                String nickname = nicknameEditText.getText().toString();
//                Toast.makeText(SignUpActivity.this, db.collection("Users").whereEqualTo("nickname", nickname),
//                        Toast.LENGTH_SHORT).show();
                db.collection("Users")
                        .whereEqualTo("nickname", nickname)
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
                                        Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "사용가능",
                                                Toast.LENGTH_SHORT).show();
                                        nicknameCheck = true;
                                    }
                                    else{
                                        Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "사용불가",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btn_google_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });
    }

    @Override// 구글 로그인 인증 요청 했을 때 값 받음
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount(); //구글 로그인 정보 담는다
                resultLogin(account);
            }
            else
                Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, result.getStatus().toString(),
                        Toast.LENGTH_SHORT).show();
        }
    }

    private void resultLogin(GoogleSignInAccount account) {//결과값 출력 메소드
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {//로그인 성공\
                            Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "회원가입 성공",
                                    Toast.LENGTH_SHORT).show();
                            //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("uid", account.getId());
                            hashMap.put("email", account.getEmail());
                            hashMap.put("nickname", account.getDisplayName());

                            FirebaseFirestore database = FirebaseFirestore.getInstance();
                            database.collection("Users").add(hashMap);

                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            startActivityForResult(intent, Protocol.SIGN_IN_CLICKED);
                            finish();
                        }
                        else{
                            Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "회원가입 실패",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /*활동을 초기화할 때 사용자가 현재 로그인되어 있는지 확인*/
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }

    }

    /*신규 사용자 이메일 가입 메소드*/
    private void createAccount(){
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText nicknameEditText = findViewById(R.id.nicknameEditText);
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String Nname = nicknameEditText.getText().toString();
        if(email.isEmpty()){
            Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "이메일을 입력해 주세요",
                    Toast.LENGTH_SHORT).show();
        }
        else if(password.isEmpty()){
            Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "패스워드를 입력해 주세요",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            if (nicknameCheck) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String email = user.getEmail();
                                    String uid = user.getUid();
                                    String nickname = Nname;

                                    //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                                    HashMap<Object, String> hashMap = new HashMap<>();
                                    hashMap.put("uid", uid);
                                    hashMap.put("email", email);
                                    hashMap.put("nickname", nickname);

                                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                                    database.collection("Users").add(hashMap);

                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "환영합니다",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                    startActivityForResult(intent, Protocol.SIGN_IN_CLICKED);
                                    finish();

                                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "이미 존재하는 계정입니다",
                                            Toast.LENGTH_SHORT).show();
                                } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "비밀번호는 6자리 이상이어야 합니다",
                                            Toast.LENGTH_SHORT).show();
                                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "유효하지 않은 이메일 형식입니다",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "인증 실패입니다",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
            else{
                Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "닉네임 중복체크를 해주세요",
                        Toast.LENGTH_SHORT).show();
            }
        }
        

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    /*기존 사용자 로그인 메소드*/
}
