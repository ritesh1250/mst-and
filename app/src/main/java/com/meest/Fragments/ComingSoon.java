package com.meest.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.meest.R;
import com.meest.meestbhart.utilss.SharedPrefreances;

public class ComingSoon extends Fragment {

    TextView countdown;
    ImageView back_arrow;
    private Activity activity;
    private int selectedPos = 0;
    private static final String FORMAT = "%02d:%02d:%02d";

    int seconds , minutes;

    public ComingSoon(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_coming_soon, null);
        SharedPrefreances.setSharedPreferenceString(getContext(), "back", "2");
        back_arrow = view.findViewById(R.id.back_arrow);
        countdown = view.findViewById(R.id.countdown);

/*

        new CountDownTimer(300000, 10000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                countdown.setText("seconds remaining: " + millisUntilFinished / 10000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                countdown.setText("done!");
            }

        }.start();
*/

//        new CountDownTimer(16069000, 1000) { // adjust the milli seconds here
//
//            @SuppressLint({"DefaultLocale", "SetTextI18n"})
//            public void onTick(long millisUntilFinished) {
//
//                countdown.setText(""+String.format(FORMAT,
//                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
//                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
//                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
//                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
//                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
//            }
//
//            public void onFinish() {
//                countdown.setText("done!");
//            }
//        }.start();

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });

        return view;
    }
    @Override
    public void onResume() {
        //fetchSearchData();
        super.onResume();
    }
}
