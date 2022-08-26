package com.example.mandarinlearning.ui.favorite;

import com.example.mandarinlearning.data.remote.model.WordLookup;

import java.util.ArrayList;

/**
 * Created by macos on 23,August,2022
 */
public interface IFavoriteFragmentPresenter {
    ArrayList<WordLookup> getListWordSaved();
}
