package com.example.mandarinlearning.ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mandarinlearning.R;

public class BaseFragment extends Fragment {

    protected void replaceFragment(Fragment fragment, String tag, boolean removeLast){
        if (removeLast){
            removeLastFragment();
        }
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainView, fragment, tag)
                .addToBackStack(tag)
                .commit();
        // getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    protected void replaceChildFragment(Fragment fragment, String tag,boolean removeLast){
        if (removeLast){
            removeLastFragment();
        }
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.childMainView, fragment, tag)
                .addToBackStack(tag)
                .commit();
        // getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    protected void replaceParentFragment(Fragment fragment, String tag,boolean removeLast){
        if (removeLast){
            removeLastFragment();
        }
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.childMainView, fragment, tag)
                .addToBackStack(tag)
                .commit();
        // getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    protected void addFragment(Fragment fragment, String tag){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainView, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }
    protected void removeLastFragment(){
        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    protected void backToFirstFragment(String fmTag){
        //back ve truoc fragment co tag do?
        //  getActivity().getSupportFragmentManager().popBackStack(fmTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().getSupportFragmentManager().popBackStack(fmTag, 0);
    }
    protected void finishChildFragment(){
        getParentFragmentManager().popBackStack();
    }

}
