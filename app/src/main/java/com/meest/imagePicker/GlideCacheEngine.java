package com.meest.imagePicker;


public class GlideCacheEngine  {
    /**
     * glide版本号,请根据用户集成为准 这里只是模拟
     */
    private final static int GLIDE_VERSION = 4;



    private GlideCacheEngine() {
    }

    private static GlideCacheEngine instance;

    public static GlideCacheEngine createCacheEngine() {
        if (null == instance) {
            synchronized (GlideCacheEngine.class) {
                if (null == instance) {
                    instance = new GlideCacheEngine();
                }
            }
        }
        return instance;
    }
}
