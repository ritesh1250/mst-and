package com.meest.model;

import com.meest.responses.FeedResponse;
import com.meest.responses.ShowFeedAdOne;

public class HomeModel {
    FeedResponse.Row feedResponse;
    ShowFeedAdOne showAdvtResponse;
    Boolean isAD;
    Boolean isVideoMute=true;

    public ShowFeedAdOne getShowAdvtResponse() {
        return showAdvtResponse;
    }

    public void setShowAdvtResponse(ShowFeedAdOne showAdvtResponse) {
        this.showAdvtResponse = showAdvtResponse;
    }

    public Boolean getAD() {
        return isAD;
    }

    public void setAD(Boolean AD) {
        isAD = AD;
    }

    public FeedResponse.Row getFeedResponse() {
        return feedResponse;
    }

    public Boolean getVideoMute() {
        return isVideoMute;
    }

    public void setVideoMute(Boolean videoMute) {
        isVideoMute = videoMute;
    }

    public void setFeedResponse(FeedResponse.Row feedResponse) {
        this.feedResponse = feedResponse;
    }
}
