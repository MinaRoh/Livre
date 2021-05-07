package gachon.mp.livre_bottom_navigation.ui.writing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WritingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WritingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is writing fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}