package gachon.mp.livre_bottom_navigation.ui.more;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import gachon.mp.livre_bottom_navigation.MainActivity;
import gachon.mp.livre_bottom_navigation.PasswordChangeActivity;
import gachon.mp.livre_bottom_navigation.Protocol;
import gachon.mp.livre_bottom_navigation.R;

public class ChangePersonalInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_more_1personal_info);

        ImageView user_profile = (ImageView)findViewById(R.id.user_profile);
        ImageButton btn_gallery = (ImageButton)findViewById(R.id.btn_gallery);
        EditText editText_nickname = (EditText)findViewById(R.id.editText_nickname);
        ImageButton btn_change_pw = (ImageButton)findViewById(R.id.btn_change_pw);
        ImageButton btn_save = (ImageButton)findViewById(R.id.btn_save);
        //닉네임 edittext를 유저 닉네임으로 바꿔놔야 함.

        //프로필 동그랗게 하기
        user_profile.setBackground(new ShapeDrawable(new OvalShape()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//롤리팝 이상만 지원하나봄?
            user_profile.setClipToOutline(true);
        }
        //갤러리 불러와서 프로필 바꾸기
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //닉네임 바꾸기
        String nickname = editText_nickname.getText().toString();

        //비밀번호 변경 액티비티로 던지기
        btn_change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PasswordChangeActivity.class);
                startActivity(intent);
            }
        });
        //저장하기(파이어스토어 업데이트)
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
