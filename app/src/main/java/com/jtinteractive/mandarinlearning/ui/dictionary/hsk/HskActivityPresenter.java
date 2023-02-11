package com.jtinteractive.mandarinlearning.ui.dictionary.hsk;

import com.jtinteractive.mandarinlearning.utils.Const;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by macos on 26,August,2022
 */
public class HskActivityPresenter implements IHskActivityPresenter {
    private int hskLevel = 0;

    public HskActivityPresenter(int hskLevel) {
        this.hskLevel = hskLevel;
    }

    @Override
    public ArrayList<String> getHskList() {
        ArrayList<String> hsk = new ArrayList<>();
        String hskString = getHskCharacter();
        String[] hskArray = hskString.split(" ");
        hsk = new ArrayList<>(Arrays.asList(hskArray));
        return hsk;
    }

    private String getHskCharacter() {
        switch (hskLevel) {
            case 1:
                return Const.HskData.HSK_1;
            case 2:
                return Const.HskData.HSK_2;
            case 3:
                return Const.HskData.HSK_3;
            case 4:
                return Const.HskData.HSK_4;
            case 5:
                return Const.HskData.HSK_5;
            case 6:
                return Const.HskData.HSK_6;
        }
        return "";
    }
}
