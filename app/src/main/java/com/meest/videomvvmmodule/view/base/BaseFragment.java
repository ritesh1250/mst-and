package com.meest.videomvvmmodule.view.base;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.meest.videomvvmmodule.utils.SessionManager;

import org.jetbrains.annotations.NotNull;


public class BaseFragment extends Fragment {


    public SessionManager sessionManager;
    private Context context;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();

        if (context != null) {
            sessionManager = new SessionManager(context);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "onCreate: ");
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        Log.d("TAG", "onCreate: ");
    }
}
