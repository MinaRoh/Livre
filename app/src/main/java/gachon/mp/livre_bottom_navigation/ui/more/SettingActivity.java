package gachon.mp.livre_bottom_navigation.ui.more;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gachon.mp.livre_bottom_navigation.MainActivity;
import gachon.mp.livre_bottom_navigation.Protocol;
import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.SplashActivity;
import gachon.mp.livre_bottom_navigation.ui.writing.WritingActivity;

public class SettingActivity extends AppCompatActivity {
    ListView listView;
    MainActivity MA = (MainActivity)MainActivity.Main_Activity;//메인 액티비티
    String TAG = "SettingActivity";
    String token;
    String method;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AuthCredential credential;
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
                    alBuilder.setMessage("로그아웃 하시겠습니까?");

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
                    /*회원탈퇴
                    * 참고 깃허브 주소 : https://gist.github.com/stack07142/cd5eaa22f5ff0971f9b76118d825a09b
                    * 구글, 페이스북 가입은 그냥 token만 있으면 delete가 되는거 같은데
                    * 이메일의 경우에는 token이랑 비밀번호가 있어야 함.
                    * 그래서 구글, 페북 사용자는 바로 알림창으로 '탈퇴하시겠습니까? - 예' 누르면 탈퇴하게 했고
                    * 이메일 가입자는 따로 DeleteActivity가 나와서 비밀번호 입력 받고 확인 버튼을 누르면 탈퇴되게 함
                    * 근데 구글/페북/이메일 사용자 나누는 것부터 막혀서 탈퇴가 잘 되는지는 확인 못해봤음. 오류 있을 수 있음!*/
                    //토큰 가져오기
                    user.getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                token = task.getResult().getToken();
                            }
                            else {
                                Toast.makeText(SettingActivity.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    // 사용자의 로그인 방식 가져옴
                    String user_id = user.getUid();//사용자 uid
                    db.collection("Users")
                            .whereEqualTo("uid", user_id)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Intent intent;
                                            method = document.getData().get("method").toString();
                                            if (method.equals("2")) {
                                                credential = GoogleAuthProvider.getCredential(token, null);
                                                System.out.println("구글");
                                                alertFunction();//밑에 함수 있음

                                            } else if (method.equals("3")) {
                                                credential = FacebookAuthProvider.getCredential(token);
                                                System.out.println("페북");
                                                alertFunction();
                                            } else {
                                                System.out.println("이메일");
                                                intent=new Intent(getApplicationContext(), DeleteActivity.class);
                                                intent.putExtra("token", token);
                                                intent.putExtra("method", method);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }

                                }
                            });

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
    public void alertFunction(){
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(SettingActivity.this);
        alBuilder.setMessage("회원탈퇴 하시겠습니까?");
        // "예" 버튼을 누르면 실행되는 리스너
        alBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
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
                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                    DocumentReference userRef = db.collection("Users").document("uid");
                                                    userRef.update("uid", Uid);
                                                    Log.d(TAG, "User account deleted.");
                                                } else
                                                    Log.d(TAG, "외않되냐", task.getException());
                                            }
                                        });
                            }
                        });
//                db.collection("Users").document(user.getUid()).delete();
                Toast.makeText(SettingActivity.this, "탈퇴 되었습니다", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(intent);
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
        alBuilder.setTitle("회원탈퇴");
        alBuilder.show(); // AlertDialog.Bulider로 만든 AlertDialog를 보여준다.
    }
}
