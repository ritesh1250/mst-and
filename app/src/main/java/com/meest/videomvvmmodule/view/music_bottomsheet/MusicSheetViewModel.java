package com.meest.videomvvmmodule.view.music_bottomsheet;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

public class MusicSheetViewModel extends ViewModel {
    public ObservableInt selectPosition = new ObservableInt(0);
    public ObservableBoolean isSearch = new ObservableBoolean(false);
    public ObservableBoolean isMore = new ObservableBoolean(false);
    public ObservableBoolean isSearchEmpty = new ObservableBoolean(false);
}
