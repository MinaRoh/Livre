package gachon.mp.livre_bottom_navigation.ui.feed;

import android.content.ClipData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeedViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private final MutableLiveData<ClipData.Item> selected = new MutableLiveData<ClipData.Item>();

    public void select(ClipData.Item item) {
        selected.setValue(item);
    }

    public MutableLiveData<ClipData.Item> getSelected() {
        return selected;
    }

    public FeedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is feed fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}