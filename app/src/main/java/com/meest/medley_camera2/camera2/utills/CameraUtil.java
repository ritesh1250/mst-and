package com.meest.medley_camera2.camera2.utills;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.meest.medley_camera2.camera2.cameraUtils.AspectRatio;


public class CameraUtil {

    /**
     * Clamps x to between min and max (inclusive on both ends, x = min --> min,
     * x = max --> max).
     */
    public static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    /**
     * Clamps x to between min and max (inclusive on both ends, x = min --> min,
     * x = max --> max).
     */
    public static float clamp(float x, float min, float max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    public static void inlineRectToRectF(RectF rectF, Rect rect) {
        rect.left = Math.round(rectF.left);
        rect.top = Math.round(rectF.top);
        rect.right = Math.round(rectF.right);
        rect.bottom = Math.round(rectF.bottom);
    }

    public static Rect rectFToRect(RectF rectF) {
        Rect rect = new Rect();
        inlineRectToRectF(rectF, rect);
        return rect;
    }

    public static RectF rectToRectF(Rect r) {
        return new RectF(r.left, r.top, r.right, r.bottom);
    }

    /**
     * Linear interpolation between a and b by the fraction t. t = 0 --> a, t =
     * 1 --> b.
     */
    public static float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    /**
     * Given (nx, ny) \in [0, 1]^2, in the display's portrait coordinate system,
     * returns normalized sensor coordinates \in [0, 1]^2 depending on how the
     * sensor's orientation \in {0, 90, 180, 270}.
     * <p>
     * Returns null if sensorOrientation is not one of the above.
     * </p>
     */
    public static PointF normalizedSensorCoordsForNormalizedDisplayCoords(
        float nx, float ny, int sensorOrientation) {
        switch (sensorOrientation) {
            case 0:
                return new PointF(nx, ny);
            case 90:
                return new PointF(ny, 1.0f - nx);
            case 180:
                return new PointF(1.0f - nx, 1.0f - ny);
            case 270:
                return new PointF(1.0f - ny, nx);
            default:
                return null;
        }
    }


    public static Bitmap bitmap;
    public static int twoGridValue;
    public static int threeGridValue;
    public static int fourGridValue;
    public static int sixGridValue;
    public static int twoGridValueHorizontal;

    public static boolean previewComplete = false;
    public static boolean gridLayoutActive = false;

    public static boolean gridLayout2Active = false;
    public static boolean gridLayout3Active = false;
    public static boolean gridLayout4Active = false;
    public static boolean gridLayout6Active = false;
    public static boolean gridLayout2ActiveHorizontal = false;

    public static boolean gridLayoutHVideoActive = false;


    public static boolean gridCamera = false;
    public static boolean superZoomEnable = false;
    public static boolean handsFreeEnable = false;
    public static boolean multiSnapEnable = false;
    public static boolean timerEnable = false;
    public static boolean isRunningTimer = false;
    public static boolean speedEnable = false;
    public static boolean aspectRatioEnable = false;

    public static boolean focusEnable = false;

    public static String currentCamera = "Video";

    public static boolean videoProcessing = false;

    public static String speed = "Normal";

    public static Uri imageUri;



    public static String stickerURL = "";

    public static AspectRatio fsAspectRatio;

    public static boolean fsEnable = true;

    public static boolean progressTimer = true;

    public static boolean music = false;

    public static boolean oneTime ;

    public static String comeFrom = "";

    public static long timer =  0;

    public static void setVideoThumbnail(ImageView imageView,String path) {
        Bitmap bmThumbnail;
        bmThumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
        bmThumbnail =  getResizedBitmap(bmThumbnail,300,300);
        imageView.setImageBitmap(bmThumbnail);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }



}
