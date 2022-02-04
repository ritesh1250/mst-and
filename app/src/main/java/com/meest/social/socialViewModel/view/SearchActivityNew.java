package com.meest.social.socialViewModel.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.meest.R;
import com.meest.databinding.SearchFragmentNewBinding;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.networkcheck.OnNoInternetRetry;
import com.meest.social.socialViewModel.adapter.search.SocialSearchFragPagerAdapter;
import com.meest.social.socialViewModel.view.search.VideoHashtagFrag;
import com.meest.social.socialViewModel.view.search.VideoImageFrag;
import com.meest.social.socialViewModel.view.search.VideoPeopleFrag;
import com.meest.social.socialViewModel.view.search.VideoVideoFrag;
import com.meest.social.socialViewModel.viewModel.searchViewModel.SearchActivityViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class
SearchActivityNew extends AppCompatActivity implements OnNoInternetRetry {

    private int selectedPos = 0;

    SearchFragmentNewBinding binding;
    SearchActivityViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.search_fragment_new);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new SearchActivityViewModel()).createFor()).get(SearchActivityViewModel.class);
        binding.setViewModel(viewModel);

        initViews();
    }

    private void initViews() {

        binding.backArrow.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.loading.setAnimation(getString(R.string.loading_json));
        binding.loading.playAnimation();
        binding.loading.loop(true);
/*
        binding.editDataFind.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (selectedPos == 0) {
                    VideoPeopleFrag vpf = (VideoPeopleFrag) binding.output.getAdapter().instantiateItem(binding.output, binding.output.getCurrentItem());
                    vpf.updateSearch(binding.editDataFind.getText().toString());
                } else if (selectedPos == 1) {
                    VideoVideoFrag vvf = (VideoVideoFrag) binding.output.getAdapter().instantiateItem(binding.output, binding.output.getCurrentItem());
                    vvf.updateSearch(binding.editDataFind.getText().toString());
                } else if (selectedPos == 2) {
                    VideoHashtagFrag vhf = (VideoHashtagFrag) binding.output.getAdapter().instantiateItem(binding.output, binding.output.getCurrentItem());
                    vhf.updateSearch(binding.editDataFind.getText().toString());
                } else if (selectedPos == 3) {
                    VideoImageFrag vhf = (VideoImageFrag) binding.output.getAdapter().instantiateItem(binding.output, binding.output.getCurrentItem());
                    vhf.updateSearch(binding.editDataFind.getText().toString());
                }
                return false;
            }
            return false;
        });*/

        binding.output.setOffscreenPageLimit(4);
        binding.output.setAdapter(new SocialSearchFragPagerAdapter(getSupportFragmentManager(), this, false, binding.editDataFind.getText().toString()));

        if (ConnectionUtils.isConnected(this)) {
            selectPeople();
        } else {
            ConnectionUtils.showNoConnectionDialog(this, this);
        }

        binding.output.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        selectPeople();
                        break;
                    case 1:
                        selectVideo();
                        break;
                    case 2:
                        selectHashtag();
                        break;

                    case 3:
                        selectPhotos();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.editDataFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    binding.ivClear.setVisibility(View.GONE);

                } else {
                    binding.ivClear.setVisibility(View.VISIBLE);
                }
                if (selectedPos == 0) {
                    VideoPeopleFrag vpf = (VideoPeopleFrag) binding.output.getAdapter().instantiateItem(binding.output, binding.output.getCurrentItem());
                    vpf.updateSearch(s.toString());
                } else if (selectedPos == 1) {
                    VideoVideoFrag vvf = (VideoVideoFrag) binding.output.getAdapter().instantiateItem(binding.output, binding.output.getCurrentItem());
                    vvf.updateSearch(s.toString());
                } else if (selectedPos == 2) {
                    VideoHashtagFrag vhf = (VideoHashtagFrag) binding.output.getAdapter().instantiateItem(binding.output, binding.output.getCurrentItem());
                    vhf.updateSearch(s.toString());
                } else if (selectedPos == 3) {
                    VideoImageFrag vhf = (VideoImageFrag) binding.output.getAdapter().instantiateItem(binding.output, binding.output.getCurrentItem());
                    vhf.updateSearch(s.toString());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.ivClear.setOnClickListener(v -> binding.editDataFind.getText().clear());

        binding.layoutPeoples.setOnClickListener(view -> {
            selectPeople();
        });
        binding.layoutVideos.setOnClickListener(view -> {
            selectVideo();
        });
        binding.layoutHastegs.setOnClickListener(view -> {
            selectHashtag();
        });
        binding.layoutPhoto.setOnClickListener(view -> {
            selectPhotos();
        });

        binding.layoutPhoto.setOnClickListener(v -> selectPhotos());

    }

    private void selectPeople() {

        binding.txtPeddoples.setTextColor(ContextCompat.getColor(this, R.color.social_background_blue));
        binding.txtHasteg.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected));
        binding.txtVideos.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected));
        binding.txtPhoto.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected));

        binding.imgPeoples.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.s_people_select));
        binding.imgVideo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_video_tab));
        binding.imgHashtag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_tag_tab));
        binding.imgPhoto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_photo_tab));

        binding.layoutPeoplesLine.setBackgroundColor(ContextCompat.getColor(this, R.color.social_background_blue));
        binding.layoutVideosLine.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        binding.layoutTagsLine.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        binding.layoutPhotosLine.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));

        binding.output.setCurrentItem(0);
        selectedPos = 0;
    }

    private void selectVideo() {

        binding.txtPeddoples.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected));
        binding.txtHasteg.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected));
        binding.txtVideos.setTextColor(ContextCompat.getColor(this, R.color.social_background_blue));
        binding.txtPhoto.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected));

        binding.imgPeoples.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_people_tab));
        binding.imgVideo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selected_video_tab));
        binding.imgHashtag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_tag_tab));
        binding.imgPhoto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_photo_tab));

        binding.layoutPeoplesLine.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        binding.layoutVideosLine.setBackgroundColor(ContextCompat.getColor(this, R.color.new_action_color));
        binding.layoutTagsLine.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        binding.layoutPhotosLine.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));

        binding.output.setCurrentItem(1);
        selectedPos = 1;
    }

    private void selectHashtag() {

        binding.txtPeddoples.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected));
        binding.txtHasteg.setTextColor(ContextCompat.getColor(this, R.color.social_background_blue));
        binding.txtVideos.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected));
        binding.txtPhoto.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected));

        binding.imgPeoples.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_people_tab));
        binding.imgVideo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_video_tab));
        binding.imgHashtag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selected_tag_tab));
        binding.imgPhoto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_photo_tab));

        binding.layoutPeoplesLine.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        binding.layoutVideosLine.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        binding.layoutTagsLine.setBackgroundColor(ContextCompat.getColor(this, R.color.new_action_color));
        binding.layoutPhotosLine.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));

        binding.output.setCurrentItem(2);
        selectedPos = 2;
    }

    private void selectPhotos() {

        binding.txtPeddoples.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected));
        binding.txtHasteg.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected));
        binding.txtVideos.setTextColor(ContextCompat.getColor(this, R.color.tab_unselected));
        binding.txtPhoto.setTextColor(ContextCompat.getColor(this, R.color.social_background_blue));

        binding.imgPeoples.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_people_tab));
        binding.imgVideo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_video_tab));
        binding.imgHashtag.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unselected_tag_tab));
        binding.imgPhoto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.selected_photo_tab));

        binding.layoutPeoplesLine.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        binding.layoutVideosLine.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        binding.layoutTagsLine.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        binding.layoutPhotosLine.setBackgroundColor(ContextCompat.getColor(this, R.color.new_action_color));

        binding.output.setCurrentItem(3);
        selectedPos = 3;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRetry() {
        if (ConnectionUtils.isConnected(this)) {
            selectPeople();
        } else {
            ConnectionUtils.showNoConnectionDialog(this, this);
        }
    }
}
