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
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import gachon.mp.livre_bottom_navigation.R;

public class MoreFragment extends Fragment {

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    ListView listView;

    @Override
    public void onStart() {
        super.onStart();

        listView=(ListView)this.getView().findViewById(R.id.listView);

        ArrayList<String> items=new ArrayList<>();
        items.add("알림");
        items.add("환경설정");

        CustomAdapter adapter=new CustomAdapter(getActivity(), 0, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if(position==0){
                    intent=new Intent(getActivity(), AlarmFragment.class);
                    startActivity(intent);
                    //finish();
                }
                else if(position==1){
                    intent=new Intent(getActivity(), SettingFragment.class);
                    startActivity(intent);
                    //finish();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more, container, false);
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

            ImageView imageView = (ImageView)view.findViewById(R.id.imageView);

            if("알림".equals(items.get(position)))
                imageView.setImageResource(R.drawable.baseline_notifications_24);
            else if("환경설정".equals(items.get(position)))
                imageView.setImageResource(R.drawable.baseline_settings_24);

            TextView textView = (TextView)view.findViewById(R.id.textView);
            textView.setText(items.get(position));

            return view;
        }
    }

}