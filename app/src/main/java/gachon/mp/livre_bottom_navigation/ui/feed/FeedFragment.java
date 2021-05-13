package gachon.mp.livre_bottom_navigation.ui.feed;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gachon.mp.livre_bottom_navigation.MainActivity;
import gachon.mp.livre_bottom_navigation.R;

public class FeedFragment extends Fragment {
    //
    private FragmentActivity myContext;
    //

    //데이터를 생성하기 위해서
    ArrayList<BookVO> array;
    String apiURL = "https://openapi.naver.com/v1/search/book.json?";
    // 나중에 이부분은 좋아요 순으로 바꿀 것
    String query = "안드로이드";
    int start = 1;

    RecyclerView list;
    BookAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    private GestureDetector gestureDetector;

    // 자동완성
    // 데이터를 넣은 리스트 변수
    private List<String> autotextviewlist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        feedViewModel =
//                new ViewModelProvider(this).get(FeedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feed, container, false);
        // final TextView textView = root.findViewById(R.id.text_dashboard);
//        feedViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//
//            }
//        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        list = getView().findViewById(R.id.list);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        list.setLayoutManager(manager);

        //생성
        array=new ArrayList<BookVO>();

        //생성
        new BookThread().execute();

        final EditText edtsearch = getView().findViewById(R.id.autoCompleteTextView);
        edtsearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                start = 1;
                array.clear();
                query = edtsearch.getText().toString();
                new BookThread().execute();
                return false;
            }
        });

        // 자동완성
        // 리스트를 생성한다
        autotextviewlist = new ArrayList<String>();

        // 리스트에 검색될 데이터(단어)를 추가한다
        settingList();

        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) getView().findViewById(R.id.autoCompleteTextView);

        // edtsearch 에 adapter 를 연결한다
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, autotextviewlist));
        //

    }

    //
    // 검색에 사용될 데이터를 리스트에 추가한다.
    private void settingList() {
        autotextviewlist.add("coffee");
        autotextviewlist.add("best");
    }
    //

    // html tag 없이 출력하기 ex) <b>주식</b> 이런거
    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }

    //BackThread 생성
    class BookThread extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            return NaverAPI.main(apiURL,query,start);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("result.........."+s);
            // api 에서 받아온 정보를 setting 하는 부분 (title, image, author...)
            try {
                JSONArray jArray=new JSONObject(s).getJSONArray("items");
                for(int i=0; i<jArray.length(); i++){
                    JSONObject obj=jArray.getJSONObject(i);
                    BookVO vo=new BookVO();
                    vo.setTitle(stripHtml(obj.getString("title")));
                    vo.setImage(obj.getString("image"));
                    vo.setAuthor(stripHtml(obj.getString("author")));
                    vo.setDescription(stripHtml(obj.getString("description")));

                    array.add(vo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 정렬방식 설정: 가로 3개씩 정렬
            mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            list.setLayoutManager(mLayoutManager);
            adapter = new BookAdapter(array, getActivity());
            list.setAdapter(adapter);
            list.scrollToPosition(start);

            gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                // 누르고 뗄 때 한번만 인식하도록 하기 위해서
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            // 상세 페이지로 넘어가기 위해
            RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                    // 손으로 터치한 곳의 좌표를 토대로 해당 Item 의 View 를 가져온다
                    View childView = rv.findChildViewUnder(e.getX(), e.getY());

                    // 터치한 곳의 View 가 RecyclerView 안의 아이템이고 그 아이템의 view 가 null 이 아니라
                    // 정확한 item 의 view 를 가져왔고, gestureDetector 에서 한번만 누르면 true 를 넘기게 구현했으니
                    // 한번만 눌려서 그 값이 true 가 넘어 왔다면
                    if (childView != null && gestureDetector.onTouchEvent(e)) {

                        // 현재 터치된 곳의 position 을 가져오고
                        int currentPosition = rv.getChildAdapterPosition(childView);

                        // 해당 위치의 data 를 가져옴
                        BookVO vo = array.get(currentPosition);

                        //
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        FeedDetailFragment feedDetailFragment = new FeedDetailFragment();
                        // FeedFragment 에서 FeedDetailFragment 로 데이터 전달 (book title, author, description...)
                        Bundle bundle = new Bundle();
                        bundle.putString("title", vo.getTitle());
//                        bundle.putString("description", vo.getDescription());
//                        bundle.putString("author", vo.getAuthor());
//                        bundle.putString("image", vo.getImage());
                        // Bundle 변수 값 전달
                        // setArguements() method 사용 안하면 받는 쪽에서 null 로 받음
                        feedDetailFragment.setArguments(bundle);
                        // transaction.replace(R.id.txttitle, feedDetailFragment);
                        transaction.commit();

                        // Activity 간 통신만 intent 가 사용 가능하다 ㅠㅠ
//                        Intent intent = new Intent(getActivity(), FeedDetailFragment.class);
//                        // Toast.makeText(MainActivity.this, "You touched" + vo.getTitle(), Toast.LENGTH_LONG).show();
//                        // 해당 책의 제목을 BookDetail_Clicked 로 넘겨줌
//                        intent.putExtra("title", vo.getTitle());
//                        intent.putExtra("description", vo.getDescription());
//                        intent.putExtra("author", vo.getAuthor());
//                        intent.putExtra("image", vo.getImage());
//                        startActivity(intent);

                        // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                        // 새로 불러올 Fragment의 Instance를 Main으로 전달
                         ((MainActivity)getActivity()).replaceFragment(FeedDetailFragment.newInstance());


                        return true;
                    }
                    return false;
                }

                @Override
                public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            };

            list.addOnItemTouchListener(onItemTouchListener);
        }
    }
}