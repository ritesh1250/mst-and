package com.meest.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Variables {
    public static final SimpleDateFormat df =
            new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZZ", Locale.ENGLISH);

    public final static int permission_camera_code = 786;
    public final static int permission_write_data = 788;
    public final static int permission_Read_data = 789;
    public final static int permission_Recording_audio = 790;
    public final static int Pick_video_from_gallery = 791;
    public static String gif_api_key1 = "ELizJkxIAW3IZn96C31xIHTVrOOQqbPb";


    public static int max_recording_duration = 18000;
    public static int recording_duration = 18000;
    public static int min_time_recording = 5000;

//    public static String local(String latitudeFinal,String longitudeFinal){
//        return "https://maps.googleapis.com/maps/api/staticmap?center="+latitudeFinal+","+longitudeFinal+"&zoom=18&size=280x280&markers=color:red|"+latitudeFinal+","+longitudeFinal+"&key=AIzaSyAGcXccMnuDHeEQDWmZ4Wl1fT2wGHiTOWE";
//    }


    public static String local(String latitudeFinal, String longitudeFinal) {
        return "https://maps.googleapis.com/maps/api/staticmap?center=" + latitudeFinal + "," + longitudeFinal + "&zoom=18&size=600x300&maptype=normal&markers=color:red|" + latitudeFinal + "," + longitudeFinal + "&key=AIzaSyAGcXccMnuDHeEQDWmZ4Wl1fT2wGHiTOWE";
    }

}
