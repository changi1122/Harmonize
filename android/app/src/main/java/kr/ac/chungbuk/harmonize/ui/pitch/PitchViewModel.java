package kr.ac.chungbuk.harmonize.ui.pitch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PitchViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PitchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is pitch fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}