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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import gachon.mp.livre_bottom_navigation.MainActivity;
import gachon.mp.livre_bottom_navigation.R;

public class DeveloperActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_more_4developer);

        listView=(ListView)this.findViewById(R.id.listView);

        ArrayList<String> items=new ArrayList<>();
        items.add("201935003 고유미");
        items.add("201935038 노민하");
        items.add("201935072 안현영");
        items.add("201935077 오예진");
        items.add("201935078 오정민");

        CustomAdapter adapter=new CustomAdapter(this, 0, items);
        listView.setAdapter(adapter);
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

            if("201935003 고유미".equals(items.get(position)))
                imageView.setImageResource(R.drawable.round_star_24);
            else if("201935038 노민하".equals(items.get(position)))
                imageView.setImageResource(R.drawable.round_star_24);
            else if("201935072 안현영".equals(items.get(position)))
                imageView.setImageResource(R.drawable.round_star_24);
            else if("201935077 오예진".equals(items.get(position)))
                imageView.setImageResource(R.drawable.round_star_24);
            else if("201935078 오정민".equals(items.get(position)))
                imageView.setImageResource(R.drawable.round_star_24);

            TextView textView = (TextView)view.findViewById(R.id.textView);
            textView.setText(items.get(position));

            return view;
        }
    }

}
