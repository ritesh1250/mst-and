package com.meest.medley_camera2.camera2.utills;



import android.util.ArrayMap;

import com.meest.medley_camera2.camera2.cameraUtils.AspectRatio;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * A collection class that automatically groups {@link Size}s by their {@link AspectRatio}s.
 */
public class SizeMap {
    private static final String TAG = "SizeMap";
    public final ArrayMap<AspectRatio, SortedSet<Size>> mRatios = new ArrayMap<>();

    /**
     * Add a new {@link Size} to this collection.
     *
     * @param size The size to add.
     * @return {@code true} if it is added, {@code false} if it already exists and is not added.
     */
    public boolean add(Size size) {
        //Log.e(TAG, +size.getWidth() +" "+ size.getHeight());
        for (AspectRatio ratio : mRatios.keySet()) {
            if (ratio.matches(size)) {
                final SortedSet<Size> sizes = mRatios.get(ratio);
                if (sizes.contains(size)) {
                    return false;
                } else {
                    sizes.add(size);
                    return true;
                }
            }

        }
        // None of the existing ratio matches the provided size; add a new key
        SortedSet<Size> sizes = new TreeSet<>();
        sizes.add(size);
       // Log.e(TAG, "Ratio: "+AspectRatio.of(size.getWidth(), size.getHeight()));
        //Log.e(TAG, "sizes: "+sizes.toString());
        mRatios.put(AspectRatio.of(size.getWidth(), size.getHeight()), sizes);

//        SortedSet<Size> sizes = new TreeSet<>();
//        sizes.add(size);
//
        mRatios.put(AspectRatio.of(720, 720), sizes);
//        mRatios.put(AspectRatio.of(1440,1080 ), sizes);
//        mRatios.put(AspectRatio.of(1920,1080), sizes);


        return true;
    }


    public void remove(AspectRatio ratio) {
        mRatios.remove(ratio);
    }

    public Set<AspectRatio> ratios() {
        return mRatios.keySet();
    }

    public  SortedSet<Size> sizes(AspectRatio ratio) {
        return mRatios.get(ratio);
    }

    public void clear() {
        mRatios.clear();
    }

    public boolean isEmpty() {
        return mRatios.isEmpty();
    }

}
