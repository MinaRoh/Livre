package gachon.mp.livre_bottom_navigation.ui.feed;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import gachon.mp.livre_bottom_navigation.R;

public class FeedDetailFragment extends Fragment {
    String titleFromMain;
    String descriptionFromMain;
    String authorFromMain;
    String imageFromMain;

    TextView title;
    TextView author;
    TextView description;
    ImageView image;

    public static Fragment newInstance() {
        FeedDetailFragment fragment = new FeedDetailFragment();
        Bundle args =  new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_feed_detail, container, false);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        // 책 정보를 MainActivity 에서 받는다 - title, author, description...
        // activity_book_detail 에 put 할 것들
        title = getView().findViewById(R.id.txttitle);
        description = getView().findViewById(R.id.txtdescription);
        author = getView().findViewById(R.id.txtauthor);
        image = getView().findViewById(R.id.image_detail);

//        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
//                // We use a String here, but any type that can be put in a Bundle is supported
//                String result = bundle.getString("bundleKey");
//                // Do something with the result...
//            }
//        });


        Bundle bundle = getArguments();

        if (bundle != null) {
            titleFromMain = bundle.getString("title");
        }

//        Intent intent = getActivity().getIntent();
//        titleFromMain = intent.getStringExtra("title");
//        descriptionFromMain = intent.getStringExtra("description");
//        authorFromMain = intent.getStringExtra("author");
//        imageFromMain = intent.getStringExtra("image");

        title.setText(titleFromMain);
//        description.setText(descriptionFromMain);
        author.setText(authorFromMain);



        // load image
//        Picasso.get().load(imageFromMain).into(image);
    }

    public void mOnClick(View view) {
        getActivity().finish();
    }

    public FeedDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}