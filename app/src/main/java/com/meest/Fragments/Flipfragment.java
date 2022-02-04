
package com.meest.Fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Interfaces.FragmentCommunication;
import com.meest.Interfaces.SwitchViewCallback;
import com.meest.MainActivity;
import com.meest.R;
import com.meest.meestbhart.utilss.Constant;

public class Flipfragment extends Fragment implements FragmentCommunication {

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;
    public View view;
    public Animation animZoomin, animZoomout;
    private Context context;
    private String selectedFragment;
    private SwitchViewCallback switchViewCallback;
    private Fragment fragmentHome, fragmentVideo;
    RecyclerView postRecyclerView;
    RelativeLayout frame;

    private Intent videoIntent;

    public Flipfragment(Context context, String selectedFragment,
                        SwitchViewCallback switchViewCallback) {
        this.context = context;
        this.selectedFragment = selectedFragment;
        this.switchViewCallback = switchViewCallback;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_flip, container, false);

        // for animation

        findViews();
        fragmentHome = new HomeFragments();

      //  fragmentVideo = new VideoPostFragment(context, this);
       // videoIntent = new Intent(context, MainVideoActivity.class);

      //  pushFragmentVideo();
        pushFragment();

       // loadAnimations();
       // changeCameraDistance();

        if (selectedFragment.equalsIgnoreCase("video")) {
            mIsBackVisible = true;
            mCardFrontLayout.setVisibility(View.GONE);
            mCardBackLayout.setVisibility(View.VISIBLE);
        } else {
            mIsBackVisible = false;
            mCardFrontLayout.setVisibility(View.VISIBLE);
            mCardBackLayout.setVisibility(View.GONE);
        }

        return view;
    }

    public void animateNow() {
        Constant.isFlipping = true;

        switchViewCallback.startAnimation(true);

        mCardBackLayout.clearAnimation();
        mCardFrontLayout.clearAnimation();

        frame.setAnimation(animZoomin);
        frame.startAnimation(animZoomin);

        mCardFrontLayout.setAnimation(animZoomin);
        mCardFrontLayout.startAnimation(animZoomin);

        mCardBackLayout.setAnimation(animZoomin);
        mCardBackLayout.startAnimation(animZoomin);
    }

    private void changeCameraDistance() {
        int distance = 12000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    @SuppressLint("ResourceType")
    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.anim.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.anim.in_animation);
        animZoomin = AnimationUtils.loadAnimation(getActivity(),
                R.anim.zoom_out);
        animZoomout = AnimationUtils.loadAnimation(getContext(),
                R.anim.zoom_in);

        animZoomin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                flipCard();
                changeCameraDistance();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animZoomout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCardBackLayout.clearAnimation();
                mCardFrontLayout.clearAnimation();

                if (mIsBackVisible) {

                    mCardFrontLayout.setVisibility(View.GONE);
                } else {
                    mCardBackLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mSetLeftIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCardFrontLayout.clearAnimation();
                mCardFrontLayout.setAnimation(animZoomout);
                mCardFrontLayout.startAnimation(animZoomout);

                mCardBackLayout.clearAnimation();
                mCardBackLayout.setAnimation(animZoomout);
                mCardBackLayout.startAnimation(animZoomout);

                frame.setAnimation(animZoomout);
                frame.startAnimation(animZoomout);
            }
        });

    }

    public void findViews() {
        mCardBackLayout = view.findViewById(R.id.card_back);
        mCardFrontLayout = view.findViewById(R.id.card_front);
        frame = view.findViewById(R.id.frame);
    }


    public void flipCard() {

        if (!mIsBackVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
            done(1);

        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
            done(0);
        }
    }


    public void done(int a) {

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mCardFrontLayout.clearAnimation();
//                mCardFrontLayout.setAnimation(animZoomout);
//                mCardFrontLayout.startAnimation(animZoomout);
//
//                mCardBackLayout.clearAnimation();
//                mCardBackLayout.setAnimation(animZoomout);
//                mCardBackLayout.startAnimation(animZoomout);
//
//                frame.setAnimation(animZoomout);
//                frame.startAnimation(animZoomout);
//
//            }
//        }, 1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switchViewCallback.startAnimation(false);
//                switchViewCallback.changeMainView();
            }
        }, 2900);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (a == 1) {
                    mCardBackLayout.setVisibility(View.VISIBLE);

                    mIsBackVisible = true;

                    MainActivity.isMainOpened = false;
                } else {
                    mCardFrontLayout.setVisibility(View.VISIBLE);

                    mIsBackVisible = false;

                    MainActivity.isMainOpened = true;
                }

                // changing MainActivity's view

            }
        }, 750);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (a == 1) {
//
//                    Log.e("front","gone");
//                    mCardFrontLayout.setVisibility(View.GONE);
//                } else {
//                    Log.e("back","gone");
//                    mCardBackLayout.setVisibility(View.GONE);
//                }
//            }
//        }, 1300);
    }

    public void pushFragment() {

//        if (context instanceof MainActivity) {
//            MainActivity mainActivity = (MainActivity) context;
//            mainActivity.showSocialView();
//        }

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.card_front, fragmentHome, "home")
                .commit();
    }

    public void pushFragmentVideo() {
//        if (context instanceof MainActivity) {
//            MainActivity mainActivity = (MainActivity) context;
//            mainActivity.showVideoView();
//        }

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.card_back, fragmentVideo, "videofeed")
                .commit();
//        Intent intent = new Intent(context, MainVideoActivity.class);
//        startActivity(intent);

    }

    @Override
    public void initAnimation() {
        if (!Constant.isFlipping) {
            animateNow();
        } else {
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!MainActivity.isMainOpened && fragmentVideo != null) {
            Fragment oldFragment = getActivity().getSupportFragmentManager().findFragmentByTag("videofeed");
            if (oldFragment != null) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragmentVideo).commit();
                fragmentVideo = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!MainActivity.isMainOpened && fragmentVideo == null) {
            fragmentVideo = new VideoPostFragment(context, Flipfragment.this);
            pushFragmentVideo();
        }
    }
}
