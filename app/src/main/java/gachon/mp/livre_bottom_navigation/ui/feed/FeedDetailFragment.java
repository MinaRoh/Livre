package gachon.mp.livre_bottom_navigation.ui.feed;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import gachon.mp.livre_bottom_navigation.MainActivity;
import gachon.mp.livre_bottom_navigation.R;

public class FeedDetailFragment extends Fragment {
    String titleFromMain;
    String descriptionFromMain;
    String authorFromMain;
    String imageFromMain;

    MainActivity activity;

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
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_feed_detail, container, false);
        // 책 정보를 MainActivity 에서 받는다 - title, author, description...
        // activity_book_detail 에 put 할 것들
        title = root.findViewById(R.id.txttitle);
        description = root.findViewById(R.id.txtdescription);
        author = root.findViewById(R.id.txtauthor);
        image = root.findViewById(R.id.image_detail);

        activity = (MainActivity) getActivity();

        if(activity.mBundle != null) {
            Bundle bundle = activity.mBundle;
            titleFromMain = bundle.getString("title");
            title.setText(titleFromMain);
            System.out.println("**************************************************************************************");
            System.out.println(titleFromMain);
            System.out.println(title);
        }

//        if(getArguments() != null) {
////            // Bundle bundle = activity.mBundle;
//            titleFromMain = getArguments().getString("title");
//        }


//        Intent intent = getActivity().getIntent();
//        titleFromMain = intent.getStringExtra("title");
//        descriptionFromMain = intent.getStringExtra("description");
//        authorFromMain = intent.getStringExtra("author");
//        imageFromMain = intent.getStringExtra("image");

        // title.setText(titleFromMain);

//        description.setText(descriptionFromMain);
        // author.setText(authorFromMain);



        // load image
//        Picasso.get().load(imageFromMain).into(image);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

//        // 책 정보를 MainActivity 에서 받는다 - title, author, description...
//        // activity_book_detail 에 put 할 것들
//        title = getView().findViewById(R.id.txttitle);
//        description = getView().findViewById(R.id.txtdescription);
//        author = getView().findViewById(R.id.txtauthor);
//        image = getView().findViewById(R.id.image_detail);
//
////        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
////            @Override
////            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
////                // We use a String here, but any type that can be put in a Bundle is supported
////                String result = bundle.getString("bundleKey");
////                // Do something with the result...
////            }
////        });
//        if(activity.mBundle != null) {
//            activity = (MainActivity) getActivity();
//            // Bundle bundle = activity.mBundle;
//            titleFromMain = bundle.getString("title");
//        }
//
//
////        Intent intent = getActivity().getIntent();
////        titleFromMain = intent.getStringExtra("title");
////        descriptionFromMain = intent.getStringExtra("description");
////        authorFromMain = intent.getStringExtra("author");
////        imageFromMain = intent.getStringExtra("image");
//
//        title.setText(titleFromMain);
//        System.out.println("**************************************************************************************");
//        System.out.println(titleFromMain);
//        System.out.println(title);
////        description.setText(descriptionFromMain);
//        // author.setText(authorFromMain);
//
//
//
//        // load image
////        Picasso.get().load(imageFromMain).into(image);
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