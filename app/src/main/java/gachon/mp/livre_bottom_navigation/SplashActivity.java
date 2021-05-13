package gachon.mp.livre_bottom_navigation;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/*스플래시 화면은 애니매이션이 있는 버전(splash_activity_anim), 없는 버전(splash_activity) 2가지로 구성되어 있다.
 * 로그인을 하지 않은 사용자에게는 애니매이션 버전이 나오고 회원가입 또는 로그인 화면과 연결된다.
 * 자동 로그인이 된 사용자는 1초간 정지된 스플래시 화면이 나오고 메인 액티비티(나무화면)로 이동한다.*/
public class SplashActivity extends AppCompatActivity {
    Handler handler = new Handler();
    public static Activity Splash_Activity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Splash_Activity = SplashActivity.this;

        //1. if (로그인이 안 된 상태일 때) -> 애니매이션 효과 + 버튼 2개 등장
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            setContentView(R.layout.activity_splash_anim);

            TextView livre1 = findViewById(R.id.livre1);//제목
            ImageButton signUp = (ImageButton)findViewById(R.id.btn_signUp);//회원가입 버튼
            ImageButton signIn = (ImageButton)findViewById(R.id.btn_signIn);//로그인 버튼

            Animation translate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_trans);//이동 객체
            Animation alpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_alpha);//나타나기 효과 객체

            livre1.startAnimation(translate);//제목 이동
            signUp.startAnimation(alpha);//회원가입 버튼 나타나기
            signIn.startAnimation(alpha);//로그인 버튼 나타나기

            signUp.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivityForResult(intent, Protocol.SIGN_UP_CLICKED);
                }
            });

            signIn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivityForResult(intent, Protocol.SIGN_IN_CLICKED);
                }
            });
        }
        //2. else (로그인이 된 상태일 때) -> 정지 스플래시 1초 + 나무 화면(mainActivity)으로 간다!
        else{
            setContentView(R.layout.activity_splash);
            TextView livre2 = findViewById(R.id.livre2);//제목
            handler.postDelayed(new Runnable(){
                public void run(){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000); // 1sec
        }



    }
    // 뒤로가기 키를 눌렀을 때에 대한 상황을 오버라이딩한다.
    @Override
    public void onBackPressed() {
        // AlertDialog 빌더를 이용해 종료시 발생시킬 창을 띄운다
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setMessage("종료하시겠습니까?");

        // "예" 버튼을 누르면 실행되는 리스너
        alBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); // 현재 액티비티를 종료한다.
            }
        });
        // "아니오" 버튼을 누르면 실행되는 리스너
        alBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                return; // 아무런 작업도 하지 않고 돌아간다
            }
        });
        alBuilder.setTitle("프로그램 종료");
        alBuilder.show(); // AlertDialog.Bulider로 만든 AlertDialog를 보여준다.
    }
}
