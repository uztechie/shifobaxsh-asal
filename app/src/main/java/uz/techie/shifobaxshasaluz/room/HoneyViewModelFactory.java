package uz.techie.shifobaxshasaluz.room;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

public class HoneyViewModelFactory implements ViewModelProvider.Factory {
    Context mContext;
    public HoneyViewModelFactory(Context context){
        mContext = context;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HoneyViewModel(mContext);
    }
}
