package com.meest.metme.camera2.camera;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.meest.metme.camera2.utills.CameraUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;



/**
 * A simple dialog that allows user to pick an aspect ratio.
 */
public class AspectRatioFragment extends DialogFragment {

    private static final String TAG = "AspectRatioFragment";
    
    private static final String ARG_ASPECT_RATIOS = "aspect_ratios";
    private static final String ARG_CURRENT_ASPECT_RATIO = "current_aspect_ratio";

    private AspectRatioListener mListener;

    public static AspectRatioFragment newInstance(Set<AspectRatio> ratios,
                                                  AspectRatio currentRatio) {
        final AspectRatioFragment fragment = new AspectRatioFragment();
        final Bundle args = new Bundle();
        args.putParcelableArray(ARG_ASPECT_RATIOS,
            ratios.toArray(new AspectRatio[ratios.size()]));
        args.putParcelable(ARG_CURRENT_ASPECT_RATIO, currentRatio);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (AspectRatioListener) context;
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final AspectRatio[] ratios = (AspectRatio[]) args.getParcelableArray(ARG_ASPECT_RATIOS);
        ArrayList<AspectRatio> ratios2 = new ArrayList<>();
        ArrayList<AspectRatio> stringList = new ArrayList<>(Arrays.asList(ratios));
        if (ratios == null) {
            throw new RuntimeException("No ratios");
        }
        Arrays.sort(ratios);

        for (AspectRatio ratio : ratios) {

            if (ratio.toString().equalsIgnoreCase("1:1") || ratio.toString().equalsIgnoreCase("4:3") || ratio.toString().equalsIgnoreCase("16:9")) {
                ratios2.add(ratio);
            }
        }

        ratios2.add(CameraUtil.fsAspectRatio);

//        AspectRatio maxAspectRatio = Collections.max(stringList);
//        ratios2.add(maxAspectRatio);
        Collections.sort(ratios2);


        final AspectRatio current = args.getParcelable(ARG_CURRENT_ASPECT_RATIO);
        final AspectRatioAdapter adapter = new AspectRatioAdapter(ratios2, current);
        return new AlertDialog.Builder(getActivity())
            .setAdapter(adapter, (dialog, position) -> mListener.onAspectRatioSelected(ratios2.get(position)))
            .create();
    }

    private static class AspectRatioAdapter extends BaseAdapter {

        private final ArrayList<AspectRatio> mRatios;
        private final AspectRatio mCurrentRatio;

        AspectRatioAdapter(ArrayList<AspectRatio> ratios, AspectRatio current) {
            mRatios = ratios;
            mCurrentRatio = current;
        }

        @Override
        public int getCount() {
            return mRatios.size();
        }

        @Override
        public AspectRatio getItem(int position) {
            return mRatios.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
                holder = new ViewHolder();
                holder.text = view.findViewById(android.R.id.text1);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            AspectRatio ratio = getItem(position);


//            if (aspectRatio.toString().equalsIgnoreCase(ratio.toString()))
//            {
//
//            }
            StringBuilder sb = new StringBuilder(ratio.toString());
//            if (ratio.equals(mCurrentRatio)) {
//                sb.append(" *");
//            }

            if (ratio == CameraUtil.fsAspectRatio)
                holder.text.setText("Full-screen");
            else
                holder.text.setText(sb);


            return view;
        }

        private static class ViewHolder {
            TextView text;
        }

    }

    public interface AspectRatioListener {
        void onAspectRatioSelected(@NonNull AspectRatio ratio);
    }

}
