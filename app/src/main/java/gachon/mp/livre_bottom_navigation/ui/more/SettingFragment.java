package gachon.mp.livre_bottom_navigation.ui.more;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import gachon.mp.livre_bottom_navigation.R;

public class SettingFragment extends Fragment {

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    ListView listView;

    @Override
    public void onStart() {
        super.onStart();

        //setContentView(R.layout.fragment_more);

        listView=(ListView)this.getView().findViewById(R.id.listView);

        ArrayList<String> items=new ArrayList<>();
        items.add("개인정보 변경");
        items.add("알림 설정");
        items.add("로그아웃");
        items.add("제작자");
        items.add("버전 정보");
        items.add("오류 신고");
        items.add("회원 탈퇴");

        CustomAdapter adapter=new CustomAdapter(getActivity(), 0, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if(position==0){
                    intent=new Intent(getActivity(), PersonalInfoFragment.class);
                    startActivity(intent);
                    //finish();
                }
                else if(position==1){
                    intent=new Intent(getActivity(), AlarmSettingFragment.class);
                    startActivity(intent);
                    //finish();
                }
                else if(position==2){
                    intent=new Intent(getActivity(), LogoutFragment.class);
                    startActivity(intent);
                    //finish();
                }
                else if(position==3){
                    intent=new Intent(getActivity(), DeveloperFragment.class);
                    startActivity(intent);
                    //finish();
                }
                else if(position==4){
                    intent=new Intent(getActivity(), VersionFragment.class);
                    startActivity(intent);
                    //finish();
                }
                else if(position==5){
                    intent=new Intent(getActivity(), BugReportFragment.class);
                    startActivity(intent);
                    //finish();
                }
                else if(position==6){
                    intent=new Intent(getActivity(), DeleteFragment.class);
                    startActivity(intent);
                    //finish();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more_setting, container, false);
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
                LayoutInflater vi = LayoutInflater.from(parent.getContext());
                view = vi.inflate(R.layout.fragment_more_listview_item, null);
            }
            /*if (view == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.fragment_more_listview_item, null);
            }*/

            ImageView imageView = (ImageView)view.findViewById(R.id.imageView);

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
