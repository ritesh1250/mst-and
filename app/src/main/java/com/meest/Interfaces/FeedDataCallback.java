package com.meest.Interfaces;

import com.meest.model.AdMediaData;

import java.util.List;

public interface FeedDataCallback {

    void commentClicked(int position, String id, String caption, boolean isCommentAllowed,Boolean isAD,int comments);

    void optionsClicked(int position);

    void storyClicked(int position);
    void myStoryClicked(int position);
    void storyAddClicked(int position);

    void onSignUpClicked(List<AdMediaData> mediaList,int position, String id);

}
