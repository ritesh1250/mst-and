package com.meest.metme.camera2.mp4compose.composer;



interface IAudioComposer {

    void setup();

    boolean stepPipeline();

    long getWrittenPresentationTimeUs();

    boolean isFinished();

    void release();
}
