package gachon.mp.livre_bottom_navigation.ui.more;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import gachon.mp.livre_bottom_navigation.MainActivity;
import gachon.mp.livre_bottom_navigation.Protocol;
import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.SplashActivity;
import gachon.mp.livre_bottom_navigation.ui.writing.WritingActivity;

public class SettingActivity extends AppCompatActivity {
    ListView listView;
    MainActivity MA = (MainActivity)MainActivity.Main_Activity;//메인 액티비티
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_more_setting);

        listView=(ListView)this.findViewById(R.id.listView);

        ArrayList<String> items=new ArrayList<>();
        items.add("개인정보 변경");
        items.add("알림 설정");
        items.add("로그아웃");
        items.add("제작자");
        items.add("버전 정보");
        items.add("오류 신고");
        items.add("회원 탈퇴");
        items.add("글쓰기(베타)");

        CustomAdapter adapter=new CustomAdapter(this, 0, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if(position==0){
                    intent=new Intent(getApplicationContext(), ChangePersonalInfoActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(position==1){
                    intent=new Intent(getApplicationContext(), AlarmSettingActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(position==2){//로그아웃!
                    AlertDialog.Builder alBuilder = new AlertDialog.Builder(SettingActivity.this);
                    alBuilder.setMessage("로그아웃하시겠습니까?");

                    // "예" 버튼을 누르면 실행되는 리스너
                    alBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FirebaseAuth.getInstance().signOut();//로그아웃
                            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                            startActivityForResult(intent, Protocol.SIGN_OUT_OK);
                            MA.finish();//MainActivity 종료
                            finish(); //SettingActivity 종료
                        }
                    });
                    // "아니오" 버튼을 누르면 실행되는 리스너
                    alBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return; // 아무런 작업도 하지 않고 돌아간다
                        }
                    });
                    alBuilder.setTitle("로그아웃");
                    alBuilder.show(); // AlertDialog.Bulider로 만든 AlertDialog를 보여준다.

//                    intent=new Intent(getApplicationContext(), LogoutActivity.class);
//                    startActivity(intent);
//                    finish();
                }
                else if(position==3){
                    intent=new Intent(getApplicationContext(), DeveloperActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(position==4){
                    intent=new Intent(getApplicationContext(), VersionActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(position==5){
                    intent=new Intent(getApplicationContext(), BugReportActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(position==6){
                    intent=new Intent(getApplicationContext(), DeleteActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(position==7){
                    intent=new Intent(getApplicationContext(), WritingActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.fragment_more_listview_item, null);
            }

            ImageView imageView = (ImageView)view.findViewById(R.id.iv_photo);

            if("개인정보 변경".equals(items.get(position)))
                imageView.setImageResource(R.drawable.baseline_person_24);
            else if("알림 설정".equals(items.get(position)))
                imageView.setImageResource(R.drawable.baseline_notifications_24);
            else if("로그아웃".equals(items.get(position)))
                imageView.setImageResource(R.drawable.baseline_logout_24);
            else if("버전 정보".equals(items.get(position)))
                imageView.setImageResource(R.drawable.baseline_description_24);
            else if("오류 신고".equals(items.get(position)))
                imageView.setImageResource(R.drawable.baseline_report_problem_24);
            else if("제작자".equals(items.get(position)))
                imageView.setImageResource(R.drawable.baseline_badge_24);
            else if("회원 탈퇴".equals(items.get(position)))
                imageView.setImageResource(R.drawable.baseline_delete_24);

            TextView textView = (TextView)view.findViewById(R.id.textView);
            textView.setText(items.get(position));

            return view;
        }
    }

}
