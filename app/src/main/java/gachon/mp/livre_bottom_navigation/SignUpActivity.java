package gachon.mp.livre_bottom_navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1000;
    private boolean nicknameCheck = false;
    private CallbackManager mCallbackManager;
    SharedPreferences sh_Pref;
    SharedPreferences.Editor toEdit;
    private String token;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ImageButton sign_up_btn_back = (ImageButton) findViewById(R.id.sign_up_btn_back);
        ImageButton btn_continue = (ImageButton) findViewById(R.id.btn_continue);
        ImageButton btn_login = (ImageButton) findViewById(R.id.btn_login);
        ImageButton btn_duplication_check = (ImageButton) findViewById(R.id.btn_duplication_check);
        ImageButton btn_google_signup = (ImageButton) findViewById(R.id.btn_google_signup);
        ImageButton btn_facebook_signup = (ImageButton) findViewById(R.id.btn_facebook_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        getHashKey();
        //?????? ????????? ???????????? ????????? ????????? ???
        sign_up_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Continue ??????(????????? ???????????? ??????)??? ????????? ???
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
        //????????? ????????? ????????? ???
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivityForResult(intent, Protocol.SIGN_IN_CLICKED);
                finish();
            }
        });
        //????????? ???????????? ????????? ????????? ???
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
                                    int num = 0;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        num = num + 1;
                                    }
                                    if (num == 0) {
                                        Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "????????????",
                                                Toast.LENGTH_SHORT).show();
                                        nicknameCheck = true;
                                    } else {
                                        Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "????????????",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
        //???????????? ????????? ????????? ???
        btn_facebook_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbackManager = CallbackManager.Factory.create(); //????????? ????????? ????????? ?????? ?????????
                SplashActivity SA = (SplashActivity) SplashActivity.Splash_Activity;//???????????? ????????????
                LoginManager.getInstance().logInWithReadPermissions(SignUpActivity.this,
                        Arrays.asList("public_profile", "user_friends"));//?????????, ???????????? ???????????? ?????? ??????(?????????)
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
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
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });
    }

    @Override// ?????? ????????? ?????? ?????? ?????? ??? ??? ?????? + ???????????? ????????????
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);//?????? ??????
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount(); //?????? ????????? ?????? ?????????
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                String email = account.getEmail();
                db.collection("Users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int num = 0;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        num = num + 1;
                                    }
                                    if (num == 0) {
                                        resultLogin(account);
                                    } else {
                                        Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "????????? ???????????? ???????????????",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);//???????????? ??????
        }
    }

    private void resultLogin(GoogleSignInAccount account) {//????????? ?????? ?????????
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        EditText nicknameEditText = findViewById(R.id.nicknameEditText);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {//????????? ??????
                            Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "???????????? ??????",
                                    Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            HashMap<Object, String> hashMap = new HashMap<>();
                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if (!task.isSuccessful()) {
                                                Log.w("Token", "Fetching FCM registration token failed", task.getException());
                                                return;
                                            }
                                            token = task.getResult();
                                            System.out.println("????????? ?????? ?????? ??????: " + token);


                                        }
                                    });

                            String email = user.getEmail();
                            String uid = user.getUid();
                            String nickname = user.getDisplayName();

                            //????????? ???????????? ?????????????????? ????????????????????? ??????

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    hashMap.put("uid", uid);
                                    hashMap.put("email", email);
                                    hashMap.put("nickname", nickname);
                                    hashMap.put("token", token);
                                    System.out.println("?????? ?????? ??????: " + token);
                                    hashMap.put("commentAlarm", "on");
                                    hashMap.put("heartAlarm", "on");
                                    hashMap.put("nightTimeAlarm", "on");
                                    hashMap.put("profileImage", "profile_img/profile.png");
                                    hashMap.put("method", "2");

                                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                                    database.collection("Users").document(user.getUid()).set(hashMap);


                                    // ?????? ??????
                                    HashMap<Object, String> hashMap2 = new HashMap<>();
                                    hashMap2.put("uid", uid);
                                    hashMap2.put("level", "1");
                                    hashMap2.put("color_leaf", "");
                                    hashMap2.put("color_trunk", "1");
                                    hashMap2.put("background", "1");
                                    database.collection("Tree_current").document(user.getUid()).set(hashMap2);

                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "???????????????",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                    startActivityForResult(intent, Protocol.SIGN_IN_CLICKED);
                                    finish();

                                }
                            }, 2000); // 1sec


                        } else {
                            Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "???????????? ??????",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /*????????? ???????????? ??? ???????????? ?????? ??????????????? ????????? ??????*/
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }

    }

    /*?????? ????????? ????????? ?????? ?????????*/
    private void createAccount() {
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText nicknameEditText = findViewById(R.id.nicknameEditText);
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String Nname = nicknameEditText.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "???????????? ????????? ?????????",
                    Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "??????????????? ????????? ?????????",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (nicknameCheck) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {


                                    FirebaseUser user = mAuth.getCurrentUser();
                                    HashMap<Object, String> hashMap = new HashMap<>();
                                    FirebaseMessaging.getInstance().getToken()
                                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                                @Override
                                                public void onComplete(@NonNull Task<String> task) {
                                                    if (!task.isSuccessful()) {
                                                        Log.w("Token", "Fetching FCM registration token failed", task.getException());
                                                        return;
                                                    }
                                                    token = task.getResult();
                                                    System.out.println("????????? ?????? ?????? ??????: " + token);


                                                }
                                            });


                                    String email = user.getEmail();
                                    String uid = user.getUid();
                                    String nickname = Nname;
                                    sharedPreference("Nickname", Nname);
                                    //????????? ???????????? ?????????????????? ????????????????????? ??????

                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            hashMap.put("uid", uid);
                                            hashMap.put("email", email);
                                            hashMap.put("nickname", nickname);
                                            hashMap.put("token", token);
                                            System.out.println("?????? ?????? ??????: " + token);
                                            hashMap.put("commentAlarm", "on");
                                            hashMap.put("heartAlarm", "on");
                                            hashMap.put("nightTimeAlarm", "on");
                                            hashMap.put("profileImage", "profile_img/profile.png");
                                            hashMap.put("method", "1");

                                            FirebaseFirestore database = FirebaseFirestore.getInstance();
                                            database.collection("Users").document(user.getUid()).set(hashMap);


                                            // ?????? ??????
                                            HashMap<Object, String> hashMap2 = new HashMap<>();
                                            hashMap2.put("uid", uid);
                                            hashMap2.put("level", "1");
                                            hashMap2.put("color_leaf", "");
                                            hashMap2.put("color_trunk", "1");
                                            hashMap2.put("background", "1");
                                            database.collection("Tree_current").document(user.getUid()).set(hashMap2);

                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "???????????????",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                            startActivityForResult(intent, Protocol.SIGN_IN_CLICKED);
                                            finish();

                                        }
                                    }, 2000); // 1sec


                                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "????????? ???????????? ???????????????",
                                            Toast.LENGTH_SHORT).show();
                                } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "??????????????? 6?????? ??????????????? ?????????",
                                            Toast.LENGTH_SHORT).show();
                                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "???????????? ?????? ????????? ???????????????",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "?????? ???????????????",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            } else {
                Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "????????? ??????????????? ????????????",
                        Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /*???????????? ???????????? ?????????*/
    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.d(TAG, "handleFacebookAccessToken:" + accessToken);

        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText nicknameEditText = findViewById(R.id.nicknameEditText);


        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "???????????? ??????",
                                    Toast.LENGTH_SHORT).show();


                            FirebaseUser user = mAuth.getCurrentUser();
                            HashMap<Object, String> hashMap = new HashMap<>();
                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if (!task.isSuccessful()) {
                                                Log.w("Token", "Fetching FCM registration token failed", task.getException());
                                                return;
                                            }
                                            token = task.getResult();
                                            System.out.println("????????? ?????? ?????? ??????: " + token);


                                        }
                                    });



                            String email = user.getEmail();
                            String uid = user.getUid();
                            String nickname = user.getDisplayName();
                            //????????? ???????????? ?????????????????? ????????????????????? ??????

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    hashMap.put("uid", uid);
                                    hashMap.put("email", email);
                                    hashMap.put("nickname", nickname);
                                    hashMap.put("token", token);
                                    hashMap.put("commentAlarm", "on");
                                    hashMap.put("heartAlarm", "on");
                                    hashMap.put("nightTimeAlarm", "on");
                                    hashMap.put("profileImage", "profile_img/profile.png");
                                    hashMap.put("method", "3");


                                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                                    database.collection("Users").document(user.getUid()).set(hashMap);


                                    // ?????? ??????
                                    HashMap<Object, String> hashMap2 = new HashMap<>();
                                    hashMap2.put("uid", uid);
                                    hashMap2.put("level", "1");
                                    hashMap2.put("color_leaf", "");
                                    hashMap2.put("color_trunk", "1");
                                    hashMap2.put("background", "1");
                                    database.collection("Tree_current").document(user.getUid()).set(hashMap2);

                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(gachon.mp.livre_bottom_navigation.SignUpActivity.this, "??????????????? ?????????????????????!",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                    startActivityForResult(intent, Protocol.SIGN_IN_CLICKED);
                                    finish();

                                }
                            }, 2000); // 1sec


                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignUpActivity.this, "????????? ???????????? ???????????????",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void getHashKey() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    public void sharedPreference(String key, String value) {
        sh_Pref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_Pref.edit();
        toEdit.putString(key, value);//??????
        toEdit.commit();
    }
}
