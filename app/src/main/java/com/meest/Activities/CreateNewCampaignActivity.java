package com.meest.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.CampaignInterestAdapter;
import com.meest.Adapters.CampaignTargetAudienceAdaper;
import com.meest.Adapters.LocationAdapter;
import com.meest.Interfaces.CampaignInterestCallback;
import com.meest.Interfaces.CampaignInterestListCallback;
import com.meest.Interfaces.CompressMedia;
import com.meest.Interfaces.LocationCallback;
import com.meest.model.AdMediaData;
import com.meest.model.CheckboxModel;
import com.meest.R;
import com.meest.responses.CampaignPreviewResponse;
import com.meest.responses.CampaignUpdateResponse;
import com.meest.responses.CreateNewCampaignResponse;
import com.meest.meestbhart.register.fragment.choosetopic.model.TopicsResponse;
import com.meest.meestbhart.register.fragment.profile.model.UploadimageResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.utils.CircularProgressBar;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.MapView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.tom.range.slider.RangeSliderView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;


public class CreateNewCampaignActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, CompressMedia, CampaignInterestCallback, LocationCallback, CampaignInterestListCallback {

    TextView txt_rs, txtlinkBtType, txtlinkBtTypeTwo, txtlinkBtTypeThree, txtlinkBtTypeFour, txtlinkBtTypeFive;

    Button btn_pay;
    TopicsResponse topicsResponse;

    RelativeLayout relUrl, relUrlTwo, relUrlThree, relUrlFour, relUrlFive, relHeading;

    RecyclerView recyclerViewTargetAudience;
    LinearLayout addOtherMedia;
    RelativeLayout relMediaManageOne, relMediaManageTwo,
            relMediaManageThree, relMediaManageFour, relMediaManageFive, layout_select_days, txt_totalBudget, relDailyBudget;
    public List<AdMediaData> mediaPathList = new ArrayList<>();
    int count = 0;
    private EditText edHead, edSubHead, edUrl, ed_txt, edHeadTwo, edSubHeadTwo,
            edUrlTwo, edHeadThree, edSubHeadThree, edUrlThree, edHeadFour,
            edSubHeadFour, edUrlFour, edHeadFive, edSubHeadFive, edUrlFive,
            edTotalBudget;
    List<String> stringList = new ArrayList<>();
    List<String> stringListGender = new ArrayList<>();
    List<CheckboxModel> genderList = new ArrayList<>();
    TextView tvInterest, tvTargetAudience, txt_Select_Interest, txt_Select_days, txt_Daily_budget, txtTotalBudget, txt_per_day;
    RecyclerView recyclerViewInterest, recyclerViewLocation;
    LocationAdapter locationAdapter;
    CampaignTargetAudienceAdaper campaignTargetAudienceAdaper;

    List<CheckboxModel> interestList;
    public static final int CAMERA_PERMISSION_CODE = 124;

    List<String> locationList = new ArrayList<>();
    List<String> interestListItem = new ArrayList<>();

    private TextView tvSelectImgVid;
    private TextView edt_dailyBudget, tvTextSelectInterest;
    String campaignType;
    RelativeLayout layout_select_interest, layout_select_location;
    VideoView vidViewOne, vidViewTwo, vidViewThree, vidViewFour, vidViewFive;
    RelativeLayout rel_uploadImgSec, rel_uploadImgThird;
    AutoCompleteTextView autoCompleteTextView;
    private RelativeLayout layout_upload_img, layout_upload_video, layout_upload_imgTwo, layout_upload_imgThree, layout_upload_imgFour, layout_upload_imgFive;
    //    private TextView DailyBudget_seekNum;
    private static final int PICK_IMAGE = 100;
    private static final int PICK_IMAGE_TWO = 101;
    private static final int PICK_IMAGE_THREE = 102;
    private static final int PICK_IMAGE_FOUR = 103;
    private static final int PICK_IMAGE_FIVE = 104;
    LottieAnimationView loadingProgress;
    private ImageView imv_uploadImg, imv_uploadImgTwo, imv_uploadImgThree, imv_uploadImgFour, imv_uploadImgFive;
    private Uri mediaUriOne, mediaUriTwo, mediaUriThree, mediaUriFour, mediaUriFive;
    private Spinner spinGender, spinCalltoAction, spinInterest;
    private TextView txt_date_select, main_title;
    private RangeSliderView rangeSeekBar;
    private String startDateData = "";
    private String endDateData = "";
    private Date startDate, endDate;
    private String minAge = "13", maxAge = "65";
    int dailyBudget;
    ScrollView sv;
    final Calendar dateSelected = Calendar.getInstance();
    private CircularProgressBar cp;
    private static final String LOG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyDfcsomOyYAs3BXBUD58CpE0WulwtSz8Ms";
    private Button privew;
    private ImageView back;
    String vidUrl = "";
    Switch switch_map;
    MapView map_view;
    CreateNewCampaignResponse campaignResponse;
    String firstButton, secondButton, thirdButton, fourthButton, fifthButton;
    boolean firstSelected = false, secondSelected = false, thirdSelected = false,
            fourthSelected = false, fifthSelected = false, isLocationRequired = false;
    Spinner spinFirst, spinSecond, spinThird, spinFourth, spinFifth;
    TextView txtButtonType, txtButtonTypeTwo, txtButtonTypeThree, txtButtonTypeFour, txtButtonTypeFive;
    List<String> buttonList = new ArrayList<>();

    String SaveType, id;
    CampaignPreviewResponse.Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_new_campaign);
        if (getIntent().getExtras() == null) {
            Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            finish();
        }

        loadingProgress = findViewById(R.id.loading);
        campaignType = getIntent().getExtras().getString("type", "");
        SaveType = getIntent().getExtras().getString("SaveType", "");
        SubType = getIntent().getExtras().getString("SubType", "");
        tvSelectImgVid = findViewById(R.id.tvSelectImgVid);
        relUrl = findViewById(R.id.relUrl);
        relUrlTwo = findViewById(R.id.relUrlTwo);
        relUrlThree = findViewById(R.id.relUrlThree);
        relUrlFour = findViewById(R.id.relUrlFour);
        relUrlFive = findViewById(R.id.relUrlFive);


        txtlinkBtType = findViewById(R.id.txtlinkBtType);
        txtlinkBtTypeTwo = findViewById(R.id.txtlinkBtTypeTwo);
        txtlinkBtTypeThree = findViewById(R.id.txtlinkBtTypeThree);
        txtlinkBtTypeFour = findViewById(R.id.txtlinkBtTypeFour);
        txtlinkBtTypeFive = findViewById(R.id.txtlinkBtTypeFive);
        txt_rs = findViewById(R.id.txt_rs);
        layout_select_interest = findViewById(R.id.layout_select_interest);
        tvTextSelectInterest = findViewById(R.id.tvTextSelectInterest);
        main_title = findViewById(R.id.main_title);
        edHead = findViewById(R.id.edHead);
        edSubHead = findViewById(R.id.edSubHead);
        edUrl = findViewById(R.id.edUrl);
        ed_txt = findViewById(R.id.ed_txt);
        edHeadTwo = findViewById(R.id.edHeadTwo);
        edSubHeadTwo = findViewById(R.id.edSubHeadTwo);
        edUrlTwo = findViewById(R.id.edUrlTwo);
        edHeadThree = findViewById(R.id.edHeadThree);
        edSubHeadThree = findViewById(R.id.edSubHeadThree);
        edUrlThree = findViewById(R.id.edUrlThree);
        edHeadFour = findViewById(R.id.edHeadFour);
        edSubHeadFour = findViewById(R.id.edSubHeadFour);
        edUrlFour = findViewById(R.id.edUrlFour);
        edHeadFive = findViewById(R.id.edHeadFive);
        edSubHeadFive = findViewById(R.id.edSubHeadFive);
        edUrlFive = findViewById(R.id.edUrlFive);
        edTotalBudget = findViewById(R.id.edTotalBudget);
        vidViewOne = findViewById(R.id.vidViewOne);
        vidViewTwo = findViewById(R.id.vidViewTwo);
        vidViewThree = findViewById(R.id.vidViewThree);
        vidViewFour = findViewById(R.id.vidViewFour);
        vidViewFive = findViewById(R.id.vidViewFive);
        relMediaManageOne = findViewById(R.id.relMediaManageOne);
        relMediaManageTwo = findViewById(R.id.relMediaManageTwo);
        relMediaManageThree = findViewById(R.id.relMediaManageThree);
        relMediaManageFour = findViewById(R.id.relMediaManageFour);
        relMediaManageFive = findViewById(R.id.relMediaManageFive);
        sv = findViewById(R.id.campaighnscroll);
        txt_Select_Interest = findViewById(R.id.txt_Select_Interest);
        relHeading = findViewById(R.id.relHeading);

        addOtherMedia = findViewById(R.id.addOtherMedia);

        loadingProgress.setAnimation("loading.json");
        loadingProgress.playAnimation();
        loadingProgress.loop(true);
       /* buttonList.add("download");
        buttonList.add("explore");
        buttonList.add("know more");*/
        spinFirst = findViewById(R.id.spinFirst);

        CheckboxModel objmodel1 = new CheckboxModel();
        objmodel1.set_name("All");
        objmodel1.set_ischecked(false);
        genderList.add(objmodel1);

        CheckboxModel objmodel2 = new CheckboxModel();
        objmodel2.set_name(getString(R.string.male));
        objmodel2.set_ischecked(false);
        genderList.add(objmodel2);

        CheckboxModel objmodel3 = new CheckboxModel();
        objmodel3.set_name(getString(R.string.female));
        objmodel3.set_ischecked(false);
        genderList.add(objmodel3);

        CheckboxModel objmodel4 = new CheckboxModel();
        objmodel4.set_name("Other");
        objmodel4.set_ischecked(false);
        genderList.add(objmodel4);

        if (campaignType.equals("CPC")) {

            buttonList.add("Know More");
            buttonList.add("Explore");
            buttonList.add("Download");


        } else if (campaignType.equals("CPL")) {

            buttonList.add("Contact us");
            buttonList.add("Enquire us");
            buttonList.add("SignUp");


            relUrl.setVisibility(View.GONE);
            relUrlTwo.setVisibility(View.GONE);
            relUrlThree.setVisibility(View.GONE);
            relUrlFour.setVisibility(View.GONE);
            relUrlFive.setVisibility(View.GONE);

            txtlinkBtType.setVisibility(View.GONE);
            txtlinkBtTypeTwo.setVisibility(View.GONE);
            txtlinkBtTypeThree.setVisibility(View.GONE);
            txtlinkBtTypeFour.setVisibility(View.GONE);
            txtlinkBtTypeFive.setVisibility(View.GONE);

//            txtButtonType = findViewById(R.id.txtButtonType);
//            txtButtonTypeTwo = findViewById(R.id.txtButtonTypeTwo);
//            txtButtonTypeThree = findViewById(R.id.txtButtonTypeThree);
//            txtButtonTypeFour = findViewById(R.id.txtButtonTypeFour);
//            txtButtonTypeFive = findViewById(R.id.txtButtonTypeFive);
//            txtButtonType.setVisibility(View.GONE);
//            txtButtonTypeTwo.setVisibility(View.GONE);
//            txtButtonTypeThree.setVisibility(View.GONE);
//            txtButtonTypeFour.setVisibility(View.GONE);
//            txtButtonTypeFive.setVisibility(View.GONE);


//                spinFirst.setVisibility(View.GONE);
//                spinSecond.setVisibility(View.GONE);
//                spinThird.setVisibility(View.GONE);
//                spinFourth.setVisibility(View.GONE);
//                spinFifth.setVisibility(View.GONE);

        } else {

            relUrl.setVisibility(View.GONE);
            relUrlTwo.setVisibility(View.GONE);
            relUrlThree.setVisibility(View.GONE);
            relUrlFour.setVisibility(View.GONE);
            relUrlFive.setVisibility(View.GONE);


            txtlinkBtType.setVisibility(View.GONE);
            txtlinkBtTypeTwo.setVisibility(View.GONE);
            txtlinkBtTypeThree.setVisibility(View.GONE);
            txtlinkBtTypeFour.setVisibility(View.GONE);
            txtlinkBtTypeFive.setVisibility(View.GONE);

            buttonList.add("");
            spinFirst = findViewById(R.id.spinFirst);
            spinSecond = findViewById(R.id.spinTwo);
            spinThird = findViewById(R.id.spinThree);
            spinFourth = findViewById(R.id.spinFour);
            spinFifth = findViewById(R.id.spinFive);
            txtButtonType = findViewById(R.id.txtButtonType);
            txtButtonTypeTwo = findViewById(R.id.txtButtonTypeTwo);
            txtButtonTypeThree = findViewById(R.id.txtButtonTypeThree);
            txtButtonTypeFour = findViewById(R.id.txtButtonTypeFour);
            txtButtonTypeFive = findViewById(R.id.txtButtonTypeFive);

            spinFirst.setVisibility(View.GONE);
            spinSecond.setVisibility(View.GONE);
            spinThird.setVisibility(View.GONE);
            spinFourth.setVisibility(View.GONE);
            spinFifth.setVisibility(View.GONE);
            txtButtonType.setVisibility(View.GONE);
            txtButtonTypeTwo.setVisibility(View.GONE);
            txtButtonTypeThree.setVisibility(View.GONE);
            txtButtonTypeFour.setVisibility(View.GONE);
            txtButtonTypeFive.setVisibility(View.GONE);


        }

        ArrayAdapter<String> a = new ArrayAdapter<String>(CreateNewCampaignActivity.this, android.R.layout.simple_spinner_item, buttonList);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinFirst.setAdapter(a);
        if (buttonList.size() != 0) {
            firstButton = buttonList.get(0);
        }
        spinFirst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                firstButton = buttonList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        spinSecond = findViewById(R.id.spinTwo);
        ArrayAdapter<String> aTwo = new ArrayAdapter<String>(CreateNewCampaignActivity.this, android.R.layout.simple_spinner_item, buttonList);
        aTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSecond.setAdapter(aTwo);
        if (buttonList.size() != 0) {
            secondButton = buttonList.get(0);
        }
        spinSecond.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secondButton = buttonList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        spinThird = findViewById(R.id.spinThree);
        ArrayAdapter<String> aThree = new ArrayAdapter<String>(CreateNewCampaignActivity.this, android.R.layout.simple_spinner_item, buttonList);
        aThree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinThird.setAdapter(aThree);
        if (buttonList.size() != 0) {
            thirdButton = buttonList.get(0);
        }
        spinThird.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                thirdButton = buttonList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        spinFourth = findViewById(R.id.spinFour);
        ArrayAdapter<String> aFour = new ArrayAdapter<String>(CreateNewCampaignActivity.this, android.R.layout.simple_spinner_item, buttonList);
        aFour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinFourth.setAdapter(aFour);
        if (buttonList.size() != 0) {
            fourthButton = buttonList.get(0);
        }
        spinFourth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fourthButton = buttonList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        spinFifth = findViewById(R.id.spinFive);
        ArrayAdapter<String> aFive = new ArrayAdapter<String>(CreateNewCampaignActivity.this, android.R.layout.simple_spinner_item, buttonList);
        aFive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinFifth.setAdapter(aFive);
        if (buttonList.size() != 0) {
            fifthButton = buttonList.get(0);
        }
        spinFifth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fifthButton = buttonList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

//        overlay1 = findViewById(R.id.overlay_view1);
//        overlay2 = findViewById(R.id.overlay_view2);
//        overlay3 = findViewById(R.id.overlay_view3);
//        overlay4 = findViewById(R.id.overlay_view4);
//        overlay5 = findViewById(R.id.overlay_view5);


        addOtherMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    String headOne = edHead.getText().toString();
                    String subheadOne = edSubHead.getText().toString();
                    String urlOne = edUrl.getText().toString();
                    if (TextUtils.isEmpty(headOne)) {
                        edHead.setError("Enter Heading!");
                    } else if (TextUtils.isEmpty(subheadOne)) {
                        edSubHead.setError("Enter Sub Heading!");
                    } else if (campaignType.equalsIgnoreCase("CPC") && TextUtils.isEmpty(urlOne)) {
                        edUrl.setError("Enter a URL");
                    } else if (mediaUriOne == null) {
                        Toast.makeText(CreateNewCampaignActivity.this, "Please select a media!", Toast.LENGTH_SHORT).show();
                    } else {
                        mediaPathList.get(0).setHeading(headOne);
                        mediaPathList.get(0).setSubHeading(subheadOne);
                        mediaPathList.get(0).setWebsiteUrl(urlOne);
                        mediaPathList.get(0).setButtonType("");
                        relMediaManageTwo.setVisibility(View.VISIBLE);
                        count++;
                    }
                } else if (count == 1) {

                    String headTwo = edHeadTwo.getText().toString();
                    String subheadTwo = edSubHeadTwo.getText().toString();
                    String urlTwo = edUrlTwo.getText().toString();

                    if (TextUtils.isEmpty(headTwo)) {
                        edHeadTwo.setError(getString(R.string.Enter_Heading));
                    } else if (TextUtils.isEmpty(subheadTwo)) {
                        edSubHeadTwo.setError(getString(R.string.Enter_Sub_Heading));
                    } else if (campaignType.equalsIgnoreCase("CPC") && TextUtils.isEmpty(urlTwo)) {
                        edUrlTwo.setError("Enter a URL");
                    } else if (mediaUriTwo == null) {
                        Toast.makeText(CreateNewCampaignActivity.this, getString(R.string.Please_select_a_media), Toast.LENGTH_SHORT).show();
                    } else {
                        mediaPathList.get(1).setHeading(headTwo);
                        mediaPathList.get(1).setSubHeading(subheadTwo);
                        mediaPathList.get(1).setWebsiteUrl(urlTwo);
                        mediaPathList.get(1).setButtonType("");
                        relMediaManageThree.setVisibility(View.VISIBLE);

                        count++;
                    }

                } else if (count == 2) {
                    String headThree = edHeadThree.getText().toString();
                    String subheadThree = edSubHeadThree.getText().toString();
                    String urlThree = edUrlThree.getText().toString();

                    if (TextUtils.isEmpty(headThree)) {
                        edHeadThree.setError(getString(R.string.Enter_Heading));
                    } else if (TextUtils.isEmpty(subheadThree)) {
                        edSubHeadThree.setError(getString(R.string.Enter_Sub_Heading));
                    } else if (campaignType.equalsIgnoreCase("CPC") && TextUtils.isEmpty(urlThree)) {
                        edUrlThree.setError("Enter a URL");
                    } else if (mediaUriThree == null) {
                        Toast.makeText(CreateNewCampaignActivity.this, getString(R.string.Please_select_a_media), Toast.LENGTH_SHORT).show();
                    } else {
                        mediaPathList.get(2).setHeading(headThree);
                        mediaPathList.get(2).setSubHeading(subheadThree);
                        mediaPathList.get(2).setWebsiteUrl(urlThree);
                        mediaPathList.get(2).setButtonType("");
                        relMediaManageFour.setVisibility(View.VISIBLE);

                        count++;
                    }

                } else if (count == 3) {

                    String headFour = edHeadFour.getText().toString();
                    String subheadFour = edSubHeadFour.getText().toString();
                    String urlFour = edUrlFour.getText().toString();

                    if (TextUtils.isEmpty(headFour)) {
                        edHeadFour.setError(getString(R.string.Enter_Heading));
                    } else if (TextUtils.isEmpty(subheadFour)) {
                        edSubHeadFour.setError(getString(R.string.Enter_Sub_Heading));
                    } else if (campaignType.equalsIgnoreCase("CPC") && TextUtils.isEmpty(urlFour)) {
                        edUrlFour.setError(getString(R.string.Enter_a_URL));
                    } else if (mediaUriFour == null) {
                        Toast.makeText(CreateNewCampaignActivity.this, getString(R.string.Please_select_a_media), Toast.LENGTH_SHORT).show();
                    } else {
                        mediaPathList.get(3).setHeading(headFour);
                        mediaPathList.get(3).setSubHeading(subheadFour);
                        mediaPathList.get(3).setWebsiteUrl(urlFour);
                        mediaPathList.get(3).setButtonType("");
                        relMediaManageFive.setVisibility(View.VISIBLE);

                        addOtherMedia.setVisibility(View.GONE);
                        count++;
                    }
                }
            }
        });

        recyclerViewLocation = findViewById(R.id.recyclerLocation);
        layout_select_location = findViewById(R.id.layout_select_location);
        recyclerViewLocation.setHasFixedSize(true);
        recyclerViewLocation.setLayoutManager(new LinearLayoutManager(this));

        layout_select_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recyclerViewLocation.getVisibility() == View.VISIBLE) {

                    recyclerViewLocation.setVisibility(View.GONE);
                } else {
                    recyclerViewLocation.setVisibility(View.VISIBLE);
                }

//setting adapter to recyclerview
            }
        });


        edt_dailyBudget = findViewById(R.id.edt_dailyBudget);

        tvInterest = findViewById(R.id.tvInterest);
        tvTargetAudience = findViewById(R.id.tvTargetAudience);
        recyclerViewTargetAudience = findViewById(R.id.recyclerViewTargetAudience);
        recyclerViewTargetAudience.setHasFixedSize(true);
        recyclerViewTargetAudience.setLayoutManager(new LinearLayoutManager(this));

        tvTargetAudience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewTargetAudience.getVisibility() == View.VISIBLE) {
                    recyclerViewTargetAudience.setVisibility(View.GONE);
                } else {
                    recyclerViewTargetAudience.setVisibility(View.VISIBLE);
                }
                CampaignInterestAdapter interestAdapter = new CampaignInterestAdapter(CreateNewCampaignActivity.this, genderList, CreateNewCampaignActivity.this, true);
                //setting adapter to recyclerview
                recyclerViewTargetAudience.setAdapter(interestAdapter);
            }
        });

        recyclerViewInterest = findViewById(R.id.recyclerViewInterest);
        recyclerViewInterest.setHasFixedSize(true);
        recyclerViewInterest.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist

        interestList = new ArrayList<>();

        InterestList();
//        //creating recyclerview adapter

        tvInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewInterest.getVisibility() == View.VISIBLE) {
                    recyclerViewInterest.setVisibility(View.GONE);
                    tvTextSelectInterest.setVisibility(View.GONE);
                } else {
                    recyclerViewInterest.setVisibility(View.VISIBLE);
                    tvTextSelectInterest.setVisibility(View.VISIBLE);
                }

                CampaignInterestAdapter interestAdapter = new CampaignInterestAdapter(CreateNewCampaignActivity.this, interestList, CreateNewCampaignActivity.this, false);
                //setting adapter to recyclerview
                recyclerViewInterest.setAdapter(interestAdapter);
            }
        });

        layout_select_days = findViewById(R.id.layout_select_days);
        rel_uploadImgSec = findViewById(R.id.rel_uploadImgSec);
        rel_uploadImgThird = findViewById(R.id.rel_uploadImgThird);


        spinInterest = findViewById(R.id.spinInterest);
        ArrayAdapter<String> adapterInterest = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.InterestValue));//setting the country_array to spinner
        adapterInterest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinInterest.setAdapter(adapterInterest);

        switch_map = findViewById(R.id.switch_map);
        map_view = findViewById(R.id.map_view);
        switch_map.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isLocationRequired = isChecked;
            }
        });

        ArrayList<Integer> sliderrangeList = new ArrayList<>();
        for (int rangevalue = 13; rangevalue <= 65; rangevalue++) {
            sliderrangeList.add(rangevalue);
        }

        rangeSeekBar = (RangeSliderView) findViewById(R.id.range_seekbar); // initiate the Seekbar

        rangeSeekBar.setRangeValues(sliderrangeList);
        rangeSeekBar.setMinAndMaxValue(16, 60);

        rangeSeekBar.setOnValueChangedListener(new RangeSliderView.OnValueChangedListener() {
            @Override
            public void onValueChanged(int minValue, int maxValue) {
                minAge = String.valueOf(minValue);
                maxAge = String.valueOf(maxValue);
            }

            @Override
            public String parseMinValueDisplayText(int minValue) {
                return super.parseMinValueDisplayText(minValue);
            }

            @Override
            public String parseMaxValueDisplayText(int maxValue) {
                return super.parseMinValueDisplayText(maxValue);
            }
        });


        txt_date_select = findViewById(R.id.txt_date_select);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                dateSelected.set(Calendar.YEAR, year);
                dateSelected.set(Calendar.MONTH, monthOfYear);
                dateSelected.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                startDate = dateSelected.getTime();

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                startDateData = simpleDateFormat.format(startDate);


                DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        dateSelected.set(Calendar.YEAR, year);
                        dateSelected.set(Calendar.MONTH, monthOfYear);
                        dateSelected.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        endDate = dateSelected.getTime();
                        endDateData = simpleDateFormat.format(endDate);
                        txt_date_select.setText(startDateData + " - " + endDateData);

                        long difference = endDate.getTime() - startDate.getTime();
                        int daysDifference = (int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
                        int budget = Integer.parseInt(edTotalBudget.getText().toString());
                        dailyBudget = (int) budget / daysDifference;
                        edt_dailyBudget.setText(" Rs." + " " + dailyBudget + " " + "approx");
                        txt_rs.setText(edTotalBudget.getText().toString() + "/-");

                    }
                };

                // creating a min date
                Calendar minCalendar = Calendar.getInstance();
                minCalendar.setTime(startDate);
                minCalendar.add(Calendar.DATE, 1);

                DatePickerDialog endDialog = new DatePickerDialog(CreateNewCampaignActivity.this, date2,
                        dateSelected.get(Calendar.YEAR),
                        dateSelected.get(Calendar.MONTH),
                        dateSelected.get(Calendar.DAY_OF_MONTH));
                endDialog.getDatePicker().setMinDate(minCalendar.getTimeInMillis());
                endDialog.show();
            }
        };

        layout_select_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateNewCampaignActivity.this, date,
                        dateSelected.get(Calendar.YEAR),
                        dateSelected.get(Calendar.MONTH),
                        dateSelected.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        autoCompleteTextView.setAdapter(new GooglePlacesAutocompleteAdapter(CreateNewCampaignActivity.this, R.layout.google_places_custom_child));
        autoCompleteTextView.setOnItemClickListener(this);

        layout_upload_img = findViewById(R.id.layout_upload_img);
        layout_upload_imgTwo = findViewById(R.id.layout_upload_imgTwo);
        layout_upload_imgThree = findViewById(R.id.layout_upload_imgThree);
        layout_upload_imgFour = findViewById(R.id.layout_upload_imgFour);
        layout_upload_imgFive = findViewById(R.id.layout_upload_imgFive);

        imv_uploadImg = findViewById(R.id.imv_uploadImg);
        imv_uploadImgTwo = findViewById(R.id.imv_uploadImgTwo);
        imv_uploadImgThree = findViewById(R.id.imv_uploadImgThree);
        imv_uploadImgFour = findViewById(R.id.imv_uploadImgFour);
        imv_uploadImgFive = findViewById(R.id.imv_uploadImgFive);

        privew = (Button) findViewById(R.id.btn_privew_campaign);


        back = (ImageView) findViewById(R.id.img_back_CreateNewCampaign);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        layout_upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreateNewCampaignActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(CreateNewCampaignActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(CreateNewCampaignActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateNewCampaignActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);

                } else {
                    ActivityCompat.requestPermissions(CreateNewCampaignActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
                    //Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        layout_upload_imgTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryTwo = new Intent(Intent.ACTION_PICK);
                galleryTwo.setType("image/* video/*");
                startActivityForResult(galleryTwo, PICK_IMAGE_TWO);

            }
        });

        layout_upload_imgThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryThree = new Intent(Intent.ACTION_PICK);
                galleryThree.setType("image/* video/*");
                startActivityForResult(galleryThree, PICK_IMAGE_THREE);

            }
        });

        layout_upload_imgFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryFour = new Intent(Intent.ACTION_PICK);
                galleryFour.setType("image/* video/*");
                startActivityForResult(galleryFour, PICK_IMAGE_FOUR);

            }
        });

        layout_upload_imgFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryFive = new Intent(Intent.ACTION_PICK);
                galleryFive.setType("image/* video/*");
                startActivityForResult(galleryFive, PICK_IMAGE_FIVE);

            }
        });

        if (SaveType.equalsIgnoreCase("edit")) {
            edit();
        } else {
            create();
        }

    }

    private void startPayment() {


    }

    void edit() {
        id = getIntent().getStringExtra("id");
        fetchData();
    }

    String SubType;

    void setData() {
        try {
            stringListGender = data.getGender();

            for (int i = 0; i < genderList.size(); i++) {
                if (stringListGender.contains(genderList.get(i).get_name().toLowerCase())) {
                    this.interestClick(i, true, true);
                    stringListGender.remove(genderList.get(i).get_name().toLowerCase());
                }
            }
            if (!SubType.equalsIgnoreCase("Completed")) {
                startDateData = data.getStartDate();
                endDateData = data.getEndDate();
                txt_date_select.setText(startDateData.substring(0, 10) + " - " + endDateData.substring(0, 10));
            }

            dailyBudget = Integer.parseInt(data.getDailyBudget());
            edTotalBudget.setText(data.getTotalAmount().toString());
            edt_dailyBudget.setText(dailyBudget + " Rs approx");

            minAge = data.getStartAge();
            maxAge = data.getEndAge();
            rangeSeekBar.setMinAndMaxValue(Integer.parseInt(minAge), Integer.parseInt(maxAge));

            main_title.setText("Edit Ad");
            ed_txt.setText(data.getName());

            edSubHead.setText(data.getOtherImageVideos().get(0).getSubHeading());
            edHead.setText(data.getOtherImageVideos().get(0).getHeading());
            edUrl.setText(data.getOtherImageVideos().get(0).getWebsiteUrl());
            Glide.with(this).load(data.getOtherImageVideos().get(0).getFileUrl()).into(imv_uploadImg);
            AdMediaData adMediaData1 = new AdMediaData();
            adMediaData1.setFileUrl(data.getOtherImageVideos().get(0).getFileUrl());
            adMediaData1.setFileType(data.getOtherImageVideos().get(0).getFileType());
            mediaPathList.add(adMediaData1);
            firstSelected = true;
            mediaUriOne = Uri.parse("");

            if (data.getOtherImageVideos().size() >= 2) {
                edSubHeadTwo.setText(data.getOtherImageVideos().get(1).getSubHeading());
                edHeadTwo.setText(data.getOtherImageVideos().get(1).getHeading());
                edUrlTwo.setText(data.getOtherImageVideos().get(1).getWebsiteUrl());
                Glide.with(this).load(data.getOtherImageVideos().get(1).getFileUrl()).into(imv_uploadImgTwo);
                AdMediaData adMediaData = new AdMediaData();
                adMediaData.setFileUrl(data.getOtherImageVideos().get(1).getFileUrl());
                adMediaData.setFileType(data.getOtherImageVideos().get(1).getFileType());
                mediaPathList.add(adMediaData);
                secondSelected = true;
                relMediaManageTwo.setVisibility(View.VISIBLE);
                count++;
                mediaUriTwo = Uri.parse("");
            }
            if (data.getOtherImageVideos().size() >= 3) {
                edSubHeadThree.setText(data.getOtherImageVideos().get(2).getSubHeading());
                edHeadThree.setText(data.getOtherImageVideos().get(2).getHeading());
                edUrlThree.setText(data.getOtherImageVideos().get(2).getWebsiteUrl());
                Glide.with(this).load(data.getOtherImageVideos().get(2).getFileUrl()).into(imv_uploadImgThree);
                AdMediaData adMediaData = new AdMediaData();
                adMediaData.setFileUrl(data.getOtherImageVideos().get(2).getFileUrl());
                adMediaData.setFileType(data.getOtherImageVideos().get(2).getFileType());
                mediaPathList.add(adMediaData);
                thirdSelected = true;
                relMediaManageThree.setVisibility(View.VISIBLE);
                count++;
                mediaUriThree = Uri.parse("");
            }
            if (data.getOtherImageVideos().size() >= 4) {
                edSubHeadFour.setText(data.getOtherImageVideos().get(3).getSubHeading());
                edHeadFour.setText(data.getOtherImageVideos().get(3).getHeading());
                edUrlFour.setText(data.getOtherImageVideos().get(3).getWebsiteUrl());
                Glide.with(this).load(data.getOtherImageVideos().get(3).getFileUrl()).into(imv_uploadImgFour);
                AdMediaData adMediaData = new AdMediaData();
                adMediaData.setFileUrl(data.getOtherImageVideos().get(3).getFileUrl());
                adMediaData.setFileType(data.getOtherImageVideos().get(3).getFileType());
                mediaPathList.add(adMediaData);
                fourthSelected = true;
                relMediaManageFour.setVisibility(View.VISIBLE);
                count++;
                mediaUriFour = Uri.parse("");
            }
            if (data.getOtherImageVideos().size() >= 5) {
                edSubHeadFive.setText(data.getOtherImageVideos().get(4).getSubHeading());
                edHeadFive.setText(data.getOtherImageVideos().get(4).getHeading());
                edUrlFive.setText(data.getOtherImageVideos().get(4).getWebsiteUrl());
                Glide.with(this).load(data.getOtherImageVideos().get(4).getFileUrl()).into(imv_uploadImgFive);
                AdMediaData adMediaData = new AdMediaData();
                adMediaData.setFileUrl(data.getOtherImageVideos().get(4).getFileUrl());
                adMediaData.setFileType(data.getOtherImageVideos().get(4).getFileType());
                mediaPathList.add(adMediaData);
                fifthSelected = true;
                relMediaManageFive.setVisibility(View.VISIBLE);
                count++;
                mediaUriFive = Uri.parse("");
            }

            bindLocation();
            txt_totalBudget = findViewById(R.id.txt_totalBudget);
            relDailyBudget = findViewById(R.id.relDailyBudget);
            txt_Daily_budget = findViewById(R.id.txt_Daily_budget);
            txt_Select_days = findViewById(R.id.txt_Select_days);
            txtTotalBudget = findViewById(R.id.txtTotalBudget);
            txt_per_day = findViewById(R.id.txt_per_day);

            locationList.addAll(data.getLocation());
            locationAdapter.notifyDataSetChanged();

            txt_Select_Interest.setVisibility(View.GONE);
            layout_select_interest.setVisibility(View.GONE);
            tvTextSelectInterest.setVisibility(View.GONE);


            privew.setText("Update");
            privew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPathList.size() < 1) {
                        Utilss.showToast(CreateNewCampaignActivity.this, "Please select atleast one media", R.color.msg_fail);
                        return;
                    } else if (TextUtils.isEmpty(ed_txt.getText().toString())) {
                        sv.scrollTo(100, sv.getTop());
                        ed_txt.setError("Enter Your Text Here!");
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Your Text", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (TextUtils.isEmpty(edHead.getText().toString())) {
                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Your heading!", Toast.LENGTH_SHORT).show();
                        edHead.setError("Enter Your heading!");
                        return;
                    } else if (TextUtils.isEmpty(edSubHead.getText().toString())) {
                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Sub Heading!", Toast.LENGTH_SHORT).show();
                        edSubHead.setError("Enter Sub Heading!");
                        return;
                    } else if (campaignType.equalsIgnoreCase("CPC") && URLUtil.isValidUrl(edUrl.getText().toString())) {
                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Your URL", Toast.LENGTH_SHORT).show();
                        edUrl.setError("Enter Your URL");
                        return;
                    } else if (TextUtils.isEmpty(edTotalBudget.getText().toString())) {
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Your Total Budget!", Toast.LENGTH_SHORT).show();
                        edTotalBudget.setError("Enter Your Total Budget!");
                        return;
                    } else if (startDateData.length() == 0 || endDateData.length() == 0) {
                        Toast.makeText(CreateNewCampaignActivity.this, "Please select start and end date", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (mediaPathList.size() == 5) {
                        String headFive = edHeadFive.getText().toString();
                        String subheadFive = edSubHeadFive.getText().toString();
                        String urlFive = edUrlFive.getText().toString();
                        if (TextUtils.isEmpty(headFive)) {
                            edHeadFive.setError("Enter Heading!");
                            return;
                        } else if (TextUtils.isEmpty(subheadFive)) {
                            edSubHeadFive.setError("Enter Sub Heading!");
                            return;
                        } else if (campaignType.equalsIgnoreCase("CPC") && URLUtil.isValidUrl(edUrlFive.getText().toString())) {
                            edUrlFive.setError("Enter a URL");
                            return;
                        } else if (mediaUriFive == null) {
                            Toast.makeText(CreateNewCampaignActivity.this, "Please select a media!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            mediaPathList.get(4).setHeading(headFive);
                            mediaPathList.get(4).setSubHeading(subheadFive);
                            mediaPathList.get(4).setFileUrl(urlFive);
                            mediaPathList.get(4).setButtonType("");
                        }
                    }
                    loadingProgress.setVisibility(View.VISIBLE);

                    // uploading bulk media
                    loadingProgress.setVisibility(View.VISIBLE);
                    updateMedia(0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void create() {
        privew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPathList.size() < 1) {
                    Utilss.showToast(CreateNewCampaignActivity.this, "Please select atleast one media", R.color.msg_fail);
                    return;
                } else if (TextUtils.isEmpty(ed_txt.getText().toString())) {
                    sv.scrollTo(100, sv.getTop());
                    ed_txt.setError("Enter Your Text Here!");
                    Toast.makeText(CreateNewCampaignActivity.this, "Enter Your Text", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(edHead.getText().toString())) {

                    sv.scrollTo(100, sv.getTop());
                    Toast.makeText(CreateNewCampaignActivity.this, "Enter Your heading!", Toast.LENGTH_SHORT).show();
                    edHead.setError("Enter Your heading!");

                    return;
                } else if (TextUtils.isEmpty(edSubHead.getText().toString())) {
                    sv.scrollTo(100, sv.getTop());
                    Toast.makeText(CreateNewCampaignActivity.this, "Enter Sub Heading!", Toast.LENGTH_SHORT).show();
                    edSubHead.setError("Enter Sub Heading!");
                    return;
                } else if (campaignType.equalsIgnoreCase("CPC") && URLUtil.isValidUrl(edUrl.getText().toString())) {
                    sv.scrollTo(100, sv.getTop());
                    Toast.makeText(CreateNewCampaignActivity.this, "Enter Your URL", Toast.LENGTH_SHORT).show();
                    edUrl.setError("Enter Your URL");

                    return;
                } else if (TextUtils.isEmpty(edTotalBudget.getText().toString())) {
                    Toast.makeText(CreateNewCampaignActivity.this, "Enter Your Total Budget!", Toast.LENGTH_SHORT).show();
                    edTotalBudget.setError("Enter Your Total Budget!");
                    return;
                } else if (startDateData.length() == 0 || endDateData.length() == 0) {
                    Toast.makeText(CreateNewCampaignActivity.this, "Please select start and end date", Toast.LENGTH_SHORT).show();
                    return;
                } else if (campaignType.equalsIgnoreCase("CPC") && URLUtil.isValidUrl(edUrl.getText().toString())) {

                    Toast.makeText(CreateNewCampaignActivity.this, "Url should start with http", Toast.LENGTH_SHORT).show();
                    edUrl.setError("Please fill correct URL");

                    return;
                } else if (secondSelected) {
                    if (TextUtils.isEmpty(edHeadTwo.getText().toString())) {
                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Your heading!", Toast.LENGTH_SHORT).show();
                        edHeadTwo.setError("Enter Your heading!");
                        return;
                    } else if (TextUtils.isEmpty(edSubHeadTwo.getText().toString())) {
                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Sub Heading!", Toast.LENGTH_SHORT).show();
                        edSubHeadTwo.setError("Enter Sub Heading!");
                        return;
                    } else if (campaignType.equalsIgnoreCase("CPC") && URLUtil.isValidUrl(edUrlTwo.getText().toString())) {

                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Your URL", Toast.LENGTH_SHORT).show();
                        edUrlTwo.setError("Enter Your URL");

                        return;
                    } else if (campaignType.equalsIgnoreCase("CPC") && URLUtil.isValidUrl(edUrlTwo.getText().toString())) {

                        Toast.makeText(CreateNewCampaignActivity.this, "Url should start with http", Toast.LENGTH_SHORT).show();
                        edUrlTwo.setError("Please fill correct URL");

                        return;
                    }
                } else if (thirdSelected) {
                    if (TextUtils.isEmpty(edHeadThree.getText().toString())) {
                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Your heading!", Toast.LENGTH_SHORT).show();
                        edHeadThree.setError("Enter Your heading!");
                        return;
                    } else if (TextUtils.isEmpty(edSubHeadThree.getText().toString())) {
                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Sub Heading!", Toast.LENGTH_SHORT).show();
                        edSubHeadThree.setError("Enter Sub Heading!");
                        return;
                    } else if (campaignType.equalsIgnoreCase("CPC") && URLUtil.isValidUrl(edUrlThree.getText().toString())) {

                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Your URL", Toast.LENGTH_SHORT).show();
                        edUrlThree.setError("Enter Your URL");

                        return;
                    } else if (campaignType.equalsIgnoreCase("CPC") && URLUtil.isValidUrl(edUrlThree.getText().toString())) {
                        Toast.makeText(CreateNewCampaignActivity.this, "Url should start with http", Toast.LENGTH_SHORT).show();
                        edUrlThree.setError("Please fill correct URL");
                        return;
                    }
                } else if (fourthSelected) {
                    if (TextUtils.isEmpty(edHeadFour.getText().toString())) {
                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Your heading!", Toast.LENGTH_SHORT).show();
                        edHeadFour.setError("Enter Your heading!");
                        return;
                    } else if (TextUtils.isEmpty(edSubHeadFour.getText().toString())) {
                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Sub Heading!", Toast.LENGTH_SHORT).show();
                        edSubHeadFour.setError("Enter Sub Heading!");
                        return;
                    } else if (campaignType.equalsIgnoreCase("CPC") && URLUtil.isValidUrl(edUrlFour.getText().toString())) {
                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Your URL", Toast.LENGTH_SHORT).show();
                        edUrlFour.setError("Enter Your URL");
                        return;
                    } else if (campaignType.equalsIgnoreCase("CPC") && URLUtil.isValidUrl(edUrlFour.getText().toString())) {

                        Toast.makeText(CreateNewCampaignActivity.this, "Url should start with httpL", Toast.LENGTH_SHORT).show();
                        edUrlFour.setError("Please fill correct URL");

                        return;
                    }
                } else if (fifthSelected) {
                    if (TextUtils.isEmpty(edHeadFive.getText().toString())) {
                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Your heading!", Toast.LENGTH_SHORT).show();
                        edHeadFive.setError("Enter Your heading!");
                        return;
                    } else if (TextUtils.isEmpty(edSubHeadFive.getText().toString())) {
                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Sub Heading!", Toast.LENGTH_SHORT).show();
                        edSubHeadFive.setError("Enter Sub Heading!");
                        return;
                    } else if (campaignType.equalsIgnoreCase("CPC") && URLUtil.isValidUrl(edUrlFive.getText().toString())) {

                        sv.scrollTo(100, sv.getTop());
                        Toast.makeText(CreateNewCampaignActivity.this, "Enter Your URL", Toast.LENGTH_SHORT).show();
                        edUrlFive.setError("Enter Your URL");

                        return;
                    } else if (campaignType.equalsIgnoreCase("CPC") && URLUtil.isValidUrl(edUrlFive.getText().toString())) {
                        Toast.makeText(CreateNewCampaignActivity.this, "Url should start with http", Toast.LENGTH_SHORT).show();
                        edUrlFive.setError("Please fill correct URL");
                        return;
                    }
                } else if (mediaPathList.size() == 5) {
                    String headFive = edHeadFive.getText().toString();
                    String subheadFive = edSubHeadFive.getText().toString();
                    String urlFive = edUrlFive.getText().toString();
                    if (TextUtils.isEmpty(headFive)) {
                        edHeadFive.setError("Enter Heading!");
                        return;
                    } else if (TextUtils.isEmpty(subheadFive)) {
                        edSubHeadFive.setError("Enter Sub Heading!");
                        return;
                    } else if (campaignType.equalsIgnoreCase("CPC") && URLUtil.isValidUrl(edUrlFive.getText().toString())) {
                        edUrlFive.setError("Enter a URL");
                        return;
                    } else if (mediaUriFive == null) {
                        Toast.makeText(CreateNewCampaignActivity.this, "Please select a media!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        mediaPathList.get(4).setHeading(headFive);
                        mediaPathList.get(4).setSubHeading(subheadFive);
                        mediaPathList.get(4).setFileUrl(urlFive);
                        mediaPathList.get(4).setButtonType(fifthButton);
                    }
                }

                // uploading bulk media
                loadingProgress.setVisibility(View.VISIBLE);
                uploadMedia(0);
            }
        });
    }

    private void uploadMedia(final int uploadCount) {
        if (uploadCount >= mediaPathList.size()) {
            createNewAd();
            return;
        }
        File file = new File(mediaPathList.get(uploadCount).getFileUrl());
        RequestBody requestFile;
        MultipartBody.Part body;
        if (mediaPathList.get(uploadCount).getFileType().equalsIgnoreCase("image")) {
            requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            requestFile = RequestBody.create(MediaType.parse("video/*"), file);
            body = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
        }

        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<UploadimageResponse> call = webApi.uploadImageRequest(map, body);

        call.enqueue(new Callback<UploadimageResponse>() {
            @Override
            public void onResponse(Call<UploadimageResponse> call, Response<UploadimageResponse> response) {
                try {
                    if (response.code() == 200 && response.body().getSuccess()) {
                        mediaPathList.get(uploadCount).setFileUrl(response.body().getData().getUrl());

                        if (uploadCount < count) {
                            uploadMedia(uploadCount + 1);
                        } else {
                            createNewAd();
                        }
                    } else {
                        loadingProgress.setVisibility(View.VISIBLE);
                        Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loadingProgress.setVisibility(View.VISIBLE);
                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);

                }
            }

            @Override
            public void onFailure(Call<UploadimageResponse> call, Throwable t) {
                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void updateMedia(final int uploadCount) {
        if (uploadCount >= mediaPathList.size()) {
            updateAd();
            return;
        }

        if (mediaPathList.get(uploadCount).getFileUrl().startsWith("http")) {
            if (uploadCount < count) {
                updateMedia(uploadCount + 1);
                return;
            } else {
                updateAd();
            }
        }

        File file = new File(mediaPathList.get(uploadCount).getFileUrl());
        RequestBody requestFile;
        MultipartBody.Part body;
        if (mediaPathList.get(uploadCount).getFileType().equalsIgnoreCase("image")) {
            requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            requestFile = RequestBody.create(MediaType.parse("video/*"), file);
            body = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
        }

        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<UploadimageResponse> call = webApi.uploadImageRequest(map, body);

        call.enqueue(new Callback<UploadimageResponse>() {
            @Override
            public void onResponse(Call<UploadimageResponse> call, Response<UploadimageResponse> response) {
                try {
                    if (response.code() == 200 && response.body().getSuccess()) {

                        mediaPathList.get(uploadCount).setFileUrl(response.body().getData().getUrl());

                        if (uploadCount < count) {
                            updateMedia(uploadCount + 1);
                        } else {
                            updateAd();
                        }
                    } else {
                        loadingProgress.setVisibility(View.VISIBLE);
                        Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loadingProgress.setVisibility(View.VISIBLE);
                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);

                }
            }

            @Override
            public void onFailure(Call<UploadimageResponse> call, Throwable t) {
//                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void fetchData() {

        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        // body
        HashMap<String, String> body = new HashMap<>();
        body.put("id", id);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<CampaignPreviewResponse> call = webApi.getCampaignDetail(header, body);
        call.enqueue(new Callback<CampaignPreviewResponse>() {
            @Override
            public void onResponse(Call<CampaignPreviewResponse> call, Response<CampaignPreviewResponse> response) {
                if (response.code() == 200 && response.body().getSuccess()) {
                    data = response.body().getData();
                    setData();
                } else {
                    Utilss.showToast(CreateNewCampaignActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<CampaignPreviewResponse> call, Throwable t) {
                Utilss.showToast(CreateNewCampaignActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void createNewAd() {
        Map<String, String> header = new HashMap<>();
        HashMap<String, Object> requestData = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(CreateNewCampaignActivity.this, SharedPrefreances.AUTH_TOKEN));

        // body
        List<String> targetGender = new ArrayList<>();
        for (CheckboxModel model : genderList) {
            if (model.is_ischecked())
                targetGender.add(model.get_name());
        }

        if (targetGender.size() == 0) {
            targetGender.add("All");
        }

        // AdMediaData firstData = mediaPathList.remove(0);
        for (int i = 0; i < mediaPathList.size(); i++) {
            if (i == 0) {
                mediaPathList.get(i).setHeading(edHead.getText().toString());
                mediaPathList.get(i).setSubHeading(edSubHead.getText().toString());
                mediaPathList.get(i).setWebsiteUrl(edUrl.getText().toString());
                mediaPathList.get(i).setButtonType(firstButton);
            }
            if (i == 1) {
                mediaPathList.get(i).setHeading(edHeadTwo.getText().toString());
                mediaPathList.get(i).setSubHeading(edSubHeadTwo.getText().toString());
                mediaPathList.get(i).setWebsiteUrl(edUrlTwo.getText().toString());
                mediaPathList.get(i).setButtonType(secondButton);
            }
            if (i == 2) {
                mediaPathList.get(i).setHeading(edHeadThree.getText().toString());
                mediaPathList.get(i).setSubHeading(edSubHeadThree.getText().toString());
                mediaPathList.get(i).setWebsiteUrl(edUrlThree.getText().toString());
                mediaPathList.get(i).setButtonType(thirdButton);
            }
            if (i == 3) {
                mediaPathList.get(i).setHeading(edHeadFour.getText().toString());
                mediaPathList.get(i).setSubHeading(edSubHeadFour.getText().toString());
                mediaPathList.get(i).setWebsiteUrl(edUrlFour.getText().toString());
                mediaPathList.get(i).setButtonType(fourthButton);
            }
            if (i == 4) {
                mediaPathList.get(i).setHeading(edHeadFive.getText().toString());
                mediaPathList.get(i).setSubHeading(edSubHeadFive.getText().toString());
                mediaPathList.get(i).setWebsiteUrl(edUrlFive.getText().toString());
                mediaPathList.get(i).setButtonType(fifthButton);
            }
        }

        requestData.put("fileURL", mediaPathList.get(0).getFileUrl());
        requestData.put("campaignTitle", edHead.getText().toString());
        requestData.put("campaignText", edSubHead.getText().toString());
        requestData.put("websiteUrl", edUrl.getText().toString());
        requestData.put("callToAction", firstButton);
        requestData.put("mediaType", mediaPathList.get(0).getFileType());
        requestData.put("gender", targetGender);
        requestData.put("startAge", minAge);
        requestData.put("endAge", maxAge);
        requestData.put("location", locationList);
        requestData.put("campaignType", campaignType);
        requestData.put("otherImageVideos", mediaPathList);
        requestData.put("reachPeopleWithAddress", isLocationRequired);
        requestData.put("startDate", startDateData);
        requestData.put("endDate", endDateData);
        requestData.put("dailyBudget", dailyBudget + "");
        requestData.put("totalAmmount", edTotalBudget.getText().toString());
        requestData.put("addedBy", "user");
        requestData.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
        requestData.put("campaignStatus", "Drafted");
        requestData.put("paymentStatus", "Pending");
        requestData.put("isPreview", true);
        requestData.put("isAdvertise", true);
        requestData.put("duplicate", "0");
        requestData.put("name", ed_txt.getText().toString());
        requestData.put("email", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.EMAil));
        requestData.put("mobileNumber", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.MOBILE));

        String json = new Gson().toJson(requestData);
        System.out.println("data ===>  " + json);

        WebApi webApi2 = ApiUtils.getClient().create(WebApi.class);
        Call<CreateNewCampaignResponse> call2 = webApi2.createNewCampaign(header, requestData);

        call2.enqueue(new Callback<CreateNewCampaignResponse>() {
            @Override
            public void onResponse(Call<CreateNewCampaignResponse> call2, Response<CreateNewCampaignResponse> response) {
                try {
                    loadingProgress.setVisibility(View.GONE);
                    campaignResponse = response.body();
                    if (response.code() == 200 && response.body().getSuccess()) {
                        campaignResponse = response.body();

                        Intent intent = new Intent(getApplicationContext(), CampaignPreviewActivity.class);
                        intent.putExtra("id", campaignResponse.getData().getId());
                        intent.putExtra("amount", Integer.parseInt(edTotalBudget.getText().toString()));
                        intent.putExtra("campaignStatus", campaignResponse.getData().getCampaignStatus());

                        startActivity(intent);
                        finish();
                    } else {
                        Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    }

                } catch (Exception e) {
                    loadingProgress.setVisibility(View.GONE);
                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CreateNewCampaignResponse> call2, Throwable t) {
                loadingProgress.setVisibility(View.GONE);
                Utilss.showToast(CreateNewCampaignActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void updateAd() {
        Map<String, String> header = new HashMap<>();
        HashMap<String, Object> requestData = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(CreateNewCampaignActivity.this, SharedPrefreances.AUTH_TOKEN));

        // body
        List<String> targetGender = new ArrayList<>();
        for (CheckboxModel model : genderList) {
            if (model.is_ischecked())
                targetGender.add(model.get_name());
        }

        if (targetGender.size() == 0) {
            targetGender.add("All");
        }

        // AdMediaData firstData = mediaPathList.remove(0);
        for (int i = 0; i < mediaPathList.size(); i++) {
            if (i == 0) {
                mediaPathList.get(i).setHeading(edHead.getText().toString());
                mediaPathList.get(i).setSubHeading(edSubHead.getText().toString());
                mediaPathList.get(i).setWebsiteUrl(edUrl.getText().toString());
                mediaPathList.get(i).setButtonType(firstButton);
            }
            if (i == 1) {
                mediaPathList.get(i).setHeading(edHeadTwo.getText().toString());
                mediaPathList.get(i).setSubHeading(edSubHeadTwo.getText().toString());
                mediaPathList.get(i).setWebsiteUrl(edUrlTwo.getText().toString());
                mediaPathList.get(i).setButtonType(secondButton);
            }
            if (i == 2) {
                mediaPathList.get(i).setHeading(edHeadThree.getText().toString());
                mediaPathList.get(i).setSubHeading(edSubHeadThree.getText().toString());
                mediaPathList.get(i).setWebsiteUrl(edUrlThree.getText().toString());
                mediaPathList.get(i).setButtonType(thirdButton);
            }
            if (i == 3) {
                mediaPathList.get(i).setHeading(edHeadFour.getText().toString());
                mediaPathList.get(i).setSubHeading(edSubHeadFour.getText().toString());
                mediaPathList.get(i).setWebsiteUrl(edUrlFour.getText().toString());
                mediaPathList.get(i).setButtonType(fourthButton);
            }
            if (i == 4) {
                mediaPathList.get(i).setHeading(edHeadFive.getText().toString());
                mediaPathList.get(i).setSubHeading(edSubHeadFive.getText().toString());
                mediaPathList.get(i).setWebsiteUrl(edUrlFive.getText().toString());
                mediaPathList.get(i).setButtonType(fifthButton);
            }
        }

        requestData.put("fileURL", mediaPathList.get(0).getFileUrl());
        requestData.put("campaignTitle", edHead.getText().toString());
        requestData.put("campaignText", edSubHead.getText().toString());
        requestData.put("websiteUrl", edUrl.getText().toString());
        requestData.put("callToAction", firstButton);
        requestData.put("mediaType", mediaPathList.get(0).getFileType());
        requestData.put("gender", targetGender);
        requestData.put("startAge", minAge);
        requestData.put("endAge", maxAge);
        requestData.put("location", locationList);
        requestData.put("campaignType", campaignType);
        requestData.put("otherImageVideos", mediaPathList);
        requestData.put("startDate", startDateData);
        requestData.put("endDate", endDateData);
        requestData.put("dailyBudget", dailyBudget + "");
        requestData.put("totalAmmount", edTotalBudget.getText().toString());
        requestData.put("id", id);
        requestData.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
        requestData.put("duplicate", "0");
        requestData.put("name", ed_txt.getText().toString());

        if (SubType.equalsIgnoreCase("Completed")) {
            requestData.put("campaignStatus", "Drafted");
            requestData.put("paymentStatus", "Pending");
            requestData.put("isPaused", false);
        }

        String json = new Gson().toJson(requestData);
        System.out.println("data ===>  " + json);

        WebApi webApi2 = ApiUtils.getClient().create(WebApi.class);
        Call<CampaignUpdateResponse> call2 = webApi2.getupdatecampaign(header, requestData);

        call2.enqueue(new Callback<CampaignUpdateResponse>() {
            @Override
            public void onResponse(Call<CampaignUpdateResponse> call2, Response<CampaignUpdateResponse> response) {
                try {
                    loadingProgress.setVisibility(View.GONE);
                    if (response.code() == 200 && response.body().getSuccess()) {
                        if (SubType.equalsIgnoreCase("Completed")) {
                            Intent intent = new Intent(CreateNewCampaignActivity.this, CampaignPreviewActivity.class);
                            intent.putExtra("id", data.getId());
                            intent.putExtra("campaignStatus", "Drafted");
                            intent.putExtra("amount", Integer.parseInt(edTotalBudget.getText().toString()));
                            startActivity(intent);
                        } else {
                            finish();
                        }
                    } else {
                        Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    }

                } catch (Exception e) {
                    loadingProgress.setVisibility(View.GONE);
                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CampaignUpdateResponse> call2, Throwable t) {
                loadingProgress.setVisibility(View.GONE);
                Utilss.showToast(CreateNewCampaignActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void pauseAllVideo() {
        if (vidViewOne != null && vidViewOne.isPlaying()) {
            vidViewOne.stopPlayback();
            vidViewOne.seekTo(2);
        }

        if (vidViewTwo != null && vidViewTwo.isPlaying()) {
            vidViewTwo.stopPlayback();
            vidViewTwo.seekTo(2);
        }

        if (vidViewThree != null && vidViewThree.isPlaying()) {
            vidViewThree.stopPlayback();
            vidViewThree.seekTo(2);
        }

        if (vidViewFour != null && vidViewFour.isPlaying()) {
            vidViewFour.stopPlayback();
            vidViewFour.seekTo(2);
        }

        if (vidViewFive != null && vidViewFive.isPlaying()) {
            vidViewFive.stopPlayback();
            vidViewFive.seekTo(2);
        }
    }

    String output;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null) {
            Uri selectedMediaUri = data.getData();
            String selectedMedia = Constant.getPath(CreateNewCampaignActivity.this, selectedMediaUri);


            // pausing any playing video
            pauseAllVideo();
            output = Constant.createOutputPath(this, ".png");
            // checking for media type
            AdMediaData adMediaData = new AdMediaData();

            /// for selected image size validation

            if (isImageFile(selectedMedia)) {

                File file = new File(selectedMedia);
                long length = file.length() / 1024;

                if (length >= 100 && length <= 1024 * 5) {//length
                    imv_uploadImg.setImageURI(selectedMediaUri);
                } else {
                    Toast.makeText(this, "Standard image size should be minimum limit 100KB to 5MB ", Toast.LENGTH_SHORT).show();
                    return;
                }

//                imv_uploadImg.setImageURI(selectedMediaUri);
                vidViewOne.setVisibility(View.GONE);
                // imv_uploadImg.setImageURI(selectedMediaUri);

                new Thread() {
                    public void run() {
                        ArrayList<String> cmdList = new ArrayList<>();

                        cmdList.add("-noautorotate");
                        cmdList.add("-i");
                        cmdList.add(selectedMedia);
                        cmdList.add("-vf");
                        cmdList.add("scale=300:-1");
                        cmdList.add(output);

                        StringBuilder sb = new java.lang.StringBuilder();

                        for (String str : cmdList) {
                            sb.append(str).append(" ");
                        }

                        String[] cmd = cmdList.toArray(new String[cmdList.size()]);

                        int rc = FFmpeg.execute(cmd);
                        if (rc == RETURN_CODE_SUCCESS) {
                            adMediaData.setFileType("image");
                            adMediaData.setFileUrl(output);
                            mediaUriOne = selectedMediaUri;
                            if (mediaPathList.size() > 0) {
                                mediaPathList.set(0, adMediaData);
                            } else {
                                mediaPathList.add(0, adMediaData);
                            }
                            firstSelected = true;
                        } else {
                            adMediaData.setFileType("image");
                            adMediaData.setFileUrl(selectedMedia);
                            mediaUriOne = selectedMediaUri;
                            if (mediaPathList.size() > 0) {
                                mediaPathList.set(0, adMediaData);
                            } else {
                                mediaPathList.add(0, adMediaData);
                            }
                            firstSelected = true;
                            Log.i("alhaj", String.format("Command execution failed with rc=%d and the output below.", rc));
                        }
                    }

                }.start();

            } else if (isVideoFile(selectedMedia)) {

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(CreateNewCampaignActivity.this, Uri.parse(selectedMedia));
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                //for checking selected video limit
                if (time != null) {
                    long timeInMillisec = Long.parseLong(time);

                    if (timeInMillisec < 10000 || timeInMillisec > 60000) {
                        Toast.makeText(this, "Video limit should be minimum 10 sec maximum 1 minute", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        vidViewOne.setVideoURI(selectedMediaUri);
                    }
                } else {
                    Utilss.showToast(this, "Something went wrong, please try again later",
                            R.color.msg_fail);
                    return;
                }


                vidViewOne.setVisibility(View.VISIBLE);
                imv_uploadImg.setVisibility(View.GONE);

                adMediaData.setFileUrl(selectedMedia);
                adMediaData.setFileType("video");
                vidViewOne.setVideoURI(selectedMediaUri);
                vidViewOne.start();
                mediaUriOne = selectedMediaUri;
                if (mediaPathList.size() > 0) {
                    mediaPathList.set(0, adMediaData);
                } else {
                    mediaPathList.add(0, adMediaData);
                }
                firstSelected = true;
            }

        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_TWO && data != null) {
            Uri selectedMediaUri = data.getData();
            String selectedMedia = Constant.getPath(CreateNewCampaignActivity.this, selectedMediaUri);

            // pausing any playing video
            output = Constant.createOutputPath(this, ".png");
            pauseAllVideo();

            // checking for media type
            AdMediaData adMediaData = new AdMediaData();
            if (isImageFile(selectedMedia)) {

                File file = new File(selectedMedia);
                long length = file.length() / 1024;

                if (length >= 100 && length <= 1024 * 5) {
                    imv_uploadImgTwo.setImageURI(selectedMediaUri);
                } else {
                    Toast.makeText(this, "Standard image size should be minimum limit 100KB to 5MB ", Toast.LENGTH_SHORT).show();
                    return;
                }


                vidViewTwo.setVisibility(View.GONE);
                //  imv_uploadImgTwo.setImageURI(selectedMediaUri);


                new Thread() {
                    public void run() {
                        ArrayList<String> cmdList = new ArrayList<>();

                        cmdList.add("-i");
                        cmdList.add(selectedMedia);
                        cmdList.add("-vf");
                        cmdList.add("scale=300:-1");
                        cmdList.add(output);

                        StringBuilder sb = new java.lang.StringBuilder();

                        for (String str : cmdList) {
                            sb.append(str).append(" ");
                        }

                        String[] cmd = cmdList.toArray(new String[cmdList.size()]);

                        int rc = FFmpeg.execute(cmd);
                        if (rc == RETURN_CODE_SUCCESS) {
                            adMediaData.setFileType("image");
                            adMediaData.setFileUrl(output);
                            mediaUriTwo = selectedMediaUri;
                            if (mediaPathList.size() > 1) {
                                mediaPathList.set(1, adMediaData);
                            } else {
                                mediaPathList.add(1, adMediaData);
                            }
                            secondSelected = true;
                        } else {
                            adMediaData.setFileType("image");
                            adMediaData.setFileUrl(selectedMedia);
                            mediaUriTwo = selectedMediaUri;
                            if (mediaPathList.size() > 1) {
                                mediaPathList.set(1, adMediaData);
                            } else {
                                mediaPathList.add(1, adMediaData);
                            }
                            secondSelected = true;
                            Log.i("alhaj", String.format("Command execution failed with rc=%d and the output below.", rc));
                        }

                    }

                }.start();

            } else if (isVideoFile(selectedMedia)) {

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(CreateNewCampaignActivity.this, Uri.parse(selectedMedia));
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                //for checking selected video limit
                if (time != null) {
                    long timeInMillisec = Long.parseLong(time);

                    if (timeInMillisec < 10000 || timeInMillisec > 60000) {
                        Toast.makeText(this, "Video limit should be minimum 10 sec maximum 1 minute", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        vidViewTwo.setVideoURI(selectedMediaUri);
                    }
                } else {
                    Utilss.showToast(this, "Something went wrong, please try again later",
                            R.color.msg_fail);
                    return;
                }


                vidViewTwo.setVisibility(View.VISIBLE);
                imv_uploadImgTwo.setVisibility(View.GONE);
                // vidViewTwo.setVideoURI(selectedMediaUri);

                adMediaData.setFileType("video");
                adMediaData.setFileUrl(selectedMedia);

                vidViewTwo.setVideoURI(selectedMediaUri);
                vidViewTwo.start();
                mediaUriTwo = selectedMediaUri;
                if (mediaPathList.size() > 1) {
                    mediaPathList.set(1, adMediaData);
                } else {
                    mediaPathList.add(1, adMediaData);
                }
                secondSelected = true;
            }
        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_THREE && data != null) {
            Uri selectedMediaUri = data.getData();
            String selectedMedia = Constant.getPath(CreateNewCampaignActivity.this, selectedMediaUri);

            // pausing any playing video
            pauseAllVideo();
            output = Constant.createOutputPath(this, ".png");

            // checking for media type
            AdMediaData adMediaData = new AdMediaData();
            if (isImageFile(selectedMedia)) {


                File file = new File(selectedMedia);
                long length = file.length() / 1024;

                if (length >= 100 && length <= 1024 * 5) {//length
                    imv_uploadImgThree.setImageURI(selectedMediaUri);

                } else {
                    Toast.makeText(this, "Standard image size should be minimum limit 100KB to 5MB ", Toast.LENGTH_SHORT).show();
                    return;
                }

                vidViewThree.setVisibility(View.GONE);
                imv_uploadImgThree.setImageURI(selectedMediaUri);

                new Thread() {
                    public void run() {
                        ArrayList<String> cmdList = new ArrayList<>();

                        cmdList.add("-i");
                        cmdList.add(selectedMedia);
                        cmdList.add("-vf");
                        cmdList.add("scale=300:-1");
                        cmdList.add(output);

                        StringBuilder sb = new java.lang.StringBuilder();

                        for (String str : cmdList) {
                            sb.append(str).append(" ");
                        }

                        String[] cmd = cmdList.toArray(new String[cmdList.size()]);

                        int rc = FFmpeg.execute(cmd);
                        if (rc == RETURN_CODE_SUCCESS) {
                            adMediaData.setFileType("image");
                            adMediaData.setFileUrl(output);
                            mediaUriThree = selectedMediaUri;
                            if (mediaPathList.size() > 2) {
                                mediaPathList.set(2, adMediaData);
                            } else {
                                mediaPathList.add(2, adMediaData);
                            }
                            thirdSelected = true;
                        } else {
                            adMediaData.setFileType("image");
                            adMediaData.setFileUrl(selectedMedia);
                            mediaUriThree = selectedMediaUri;
                            if (mediaPathList.size() > 2) {
                                mediaPathList.set(2, adMediaData);
                            } else {
                                mediaPathList.add(2, adMediaData);
                            }
                            thirdSelected = true;
                            Log.i("alhaj", String.format("Command execution failed with rc=%d and the output below.", rc));
                        }

                    }

                }.start();
            } else if (isVideoFile(selectedMedia)) {

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(CreateNewCampaignActivity.this, Uri.parse(selectedMedia));
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                //for checking selected video limit
                if (time != null) {
                    long timeInMillisec = Long.parseLong(time);

                    if (timeInMillisec < 10000 || timeInMillisec > 60000) {
                        Toast.makeText(this, "Video limit should be minimum 10 sec maximum 1 minute", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        vidViewThree.setVideoURI(selectedMediaUri);
                    }
                } else {
                    Utilss.showToast(this, "Something went wrong, please try again later",
                            R.color.msg_fail);
                    return;
                }


                vidViewThree.setVisibility(View.VISIBLE);
                imv_uploadImgThree.setVisibility(View.GONE);
                //  vidViewThree.setVideoURI(selectedMediaUri);

                adMediaData.setFileType("video");
                adMediaData.setFileUrl(selectedMedia);

                vidViewThree.setVideoURI(selectedMediaUri);
                vidViewThree.start();
                mediaUriThree = selectedMediaUri;
                if (mediaPathList.size() > 2) {
                    mediaPathList.set(2, adMediaData);
                } else {
                    mediaPathList.add(2, adMediaData);
                }
                thirdSelected = true;
            }
        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_FOUR && data != null) {
            Uri selectedMediaUri = data.getData();
            String selectedMedia = Constant.getPath(CreateNewCampaignActivity.this, selectedMediaUri);

            // pausing any playing video
            pauseAllVideo();
            output = Constant.createOutputPath(this, ".png");

            // checking for media type
            AdMediaData adMediaData = new AdMediaData();
            if (isImageFile(selectedMedia)) {

                File file = new File(selectedMedia);
                long length = file.length() / 1024;

                if (length >= 100 && length <= 1024 * 5) {//length
                    imv_uploadImgFour.setImageURI(selectedMediaUri);

                } else {
                    Toast.makeText(this, "Standard image size should be minimum limit 100KB to 5MB ", Toast.LENGTH_SHORT).show();
                    return;
                }


                vidViewFour.setVisibility(View.GONE);
                //  imv_uploadImgFour.setImageURI(selectedMediaUri);

                new Thread() {
                    public void run() {
                        ArrayList<String> cmdList = new ArrayList<>();

                        cmdList.add("-i");
                        cmdList.add(selectedMedia);
                        cmdList.add("-vf");
                        cmdList.add("scale=300:-1");
                        cmdList.add(output);

                        StringBuilder sb = new java.lang.StringBuilder();

                        for (String str : cmdList) {
                            sb.append(str).append(" ");
                        }

                        String[] cmd = cmdList.toArray(new String[cmdList.size()]);

                        int rc = FFmpeg.execute(cmd);
                        if (rc == RETURN_CODE_SUCCESS) {

                            adMediaData.setFileType("image");
                            adMediaData.setFileUrl(output);
                            mediaUriFour = selectedMediaUri;
                            if (mediaPathList.size() > 3) {
                                mediaPathList.set(3, adMediaData);
                            } else {
                                mediaPathList.add(3, adMediaData);
                            }
                            fourthSelected = true;
                        } else {
                            adMediaData.setFileType("image");
                            adMediaData.setFileUrl(selectedMedia);
                            mediaUriFour = selectedMediaUri;
                            if (mediaPathList.size() > 3) {
                                mediaPathList.set(3, adMediaData);
                            } else {
                                mediaPathList.add(3, adMediaData);
                            }
                            fourthSelected = true;
                            Log.i("alhaj", String.format("Command execution failed with rc=%d and the output below.", rc));
                        }

                    }

                }.start();
            } else if (isVideoFile(selectedMedia)) {

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(CreateNewCampaignActivity.this, Uri.parse(selectedMedia));
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                //for checking selected video limit
                if (time != null) {
                    long timeInMillisec = Long.parseLong(time);

                    if (timeInMillisec < 10000 || timeInMillisec > 60000) {
                        Toast.makeText(this, "Video limit should be minimum 10 sec maximum 1 minute", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        vidViewFour.setVideoURI(selectedMediaUri);
                    }
                } else {
                    Utilss.showToast(this, "Something went wrong, please try again later",
                            R.color.msg_fail);
                    return;
                }

                vidViewFour.setVisibility(View.VISIBLE);
                imv_uploadImgFour.setVisibility(View.GONE);
                //   vidViewFour.setVideoURI(selectedMediaUri);

                adMediaData.setFileType("video");
                adMediaData.setFileUrl(selectedMedia);

                vidViewFour.setVideoURI(selectedMediaUri);
                vidViewFour.start();
                mediaUriFour = selectedMediaUri;
                if (mediaPathList.size() > 3) {
                    mediaPathList.set(3, adMediaData);
                } else {
                    mediaPathList.add(3, adMediaData);
                }
                fourthSelected = true;
            }
        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_FIVE && data != null) {
            Uri selectedMediaUri = data.getData();
            String selectedMedia = Constant.getPath(CreateNewCampaignActivity.this, selectedMediaUri);

            // pausing any playing video
            pauseAllVideo();
            output = Constant.createOutputPath(this, ".png");

            // checking for media type
            AdMediaData adMediaData = new AdMediaData();
            if (isImageFile(selectedMedia)) {


                File file = new File(selectedMedia);
                long length = file.length() / 1024;

                if (length >= 100 && length <= 1024 * 5) {//length
                    imv_uploadImgFive.setImageURI(selectedMediaUri);

                } else {
                    Toast.makeText(this, "Standard image size should be minimum limit 100KB to 5MB ", Toast.LENGTH_SHORT).show();
                    return;
                }
                vidViewFive.setVisibility(View.GONE);
                // imv_uploadImgFive.setImageURI(selectedMediaUri);
                new Thread() {
                    public void run() {
                        ArrayList<String> cmdList = new ArrayList<>();

                        cmdList.add("-i");
                        cmdList.add(selectedMedia);
                        cmdList.add("-vf");
                        cmdList.add("scale=300:-1");
                        cmdList.add(output);

                        StringBuilder sb = new java.lang.StringBuilder();

                        for (String str : cmdList) {
                            sb.append(str).append(" ");
                        }

                        String[] cmd = cmdList.toArray(new String[cmdList.size()]);

                        int rc = FFmpeg.execute(cmd);
                        if (rc == RETURN_CODE_SUCCESS) {
                            adMediaData.setFileType("image");
                            adMediaData.setFileUrl(output);
                            mediaUriFive = selectedMediaUri;
                            if (mediaPathList.size() > 4) {
                                mediaPathList.set(4, adMediaData);
                            } else {
                                mediaPathList.add(4, adMediaData);
                            }
                            fifthSelected = true;
                        } else {
                            adMediaData.setFileType("image");
                            adMediaData.setFileUrl(selectedMedia);
                            mediaUriFive = selectedMediaUri;
                            if (mediaPathList.size() > 4) {
                                mediaPathList.set(4, adMediaData);
                            } else {
                                mediaPathList.add(4, adMediaData);
                            }
                            fifthSelected = true;
                            Log.i("alhaj", String.format("Command execution failed with rc=%d and the output below.", rc));
                        }

                    }

                }.start();
            } else if (isVideoFile(selectedMedia)) {

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(CreateNewCampaignActivity.this, Uri.parse(selectedMedia));
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                //for checking selected video limit
                if (time != null) {
                    long timeInMillisec = Long.parseLong(time);

                    if (timeInMillisec < 10000 || timeInMillisec > 60000) {
                        Toast.makeText(this, "Video limit should be minimum 10 sec maximum 1 minute", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        vidViewFive.setVideoURI(selectedMediaUri);
                    }
                } else {
                    Utilss.showToast(this, "Something went wrong, please try again later",
                            R.color.msg_fail);
                    return;
                }


                vidViewFive.setVisibility(View.VISIBLE);
                imv_uploadImgFive.setVisibility(View.GONE);
                //  vidViewFive.setVideoURI(selectedMediaUri);

                adMediaData.setFileType("video");
                adMediaData.setFileUrl(selectedMedia);

                vidViewFive.setVideoURI(selectedMediaUri);
                vidViewFive.start();
                mediaUriFive = selectedMediaUri;
                if (mediaPathList.size() > 4) {
                    mediaPathList.set(4, adMediaData);
                } else {
                    mediaPathList.add(4, adMediaData);
                }
                fifthSelected = true;
            }
        }
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        locationList.add(str);
        autoCompleteTextView.setText("");
        bindLocation();
    }

    private void bindLocation() {
        locationAdapter = new LocationAdapter(CreateNewCampaignActivity.this, locationList, CreateNewCampaignActivity.this);
        recyclerViewLocation.setAdapter(locationAdapter);
    }

    @Override
    public void compressDone(boolean isDone, int requestCode) {
        if (isDone) {
            if (requestCode == 111) {
//                mediaPathList.add(output);
            }
        }
    }

    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("harsh", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("harsh", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            System.out.println("result=============================>" + predsJsonArray);
            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e("harsh", "Cannot process JSON results", e);
        }

        return resultList;
    }

    @Override
    public void removeLocation(int position) {
        locationList.remove(position);
        bindLocation();
    }

    @Override
    public void interestItemClick(int position) {


    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {

        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    @Override
    public void interestClick(int position, boolean isSelected, boolean isGender) {

        if (isGender) {
            genderList.get(position).set_ischecked(isSelected);
            if (isSelected) {
                if (!stringListGender.contains(genderList.get(position).get_name())) {
                    stringListGender.add(genderList.get(position).get_name());
                }
            } else {
                stringListGender.remove(genderList.get(position).get_name());
            }

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < stringListGender.size(); i++) {
                String data = stringListGender.get(i);
                builder.append(data);
                if (i < stringListGender.size() - 1) {
                    builder.append(", ");
                }
            }
            String data = builder.toString();
            if (data.length() > 0) {
                tvTargetAudience.setText(data);
            } else {
                tvTargetAudience.setText("Select Target Audience");
            }
        } else {
            interestList.get(position).set_ischecked(isSelected);
            if (isSelected) {
                if (!stringList.contains(interestList.get(position).get_name())) {
                    stringList.add(interestList.get(position).get_name());
                }
            } else {
                stringList.remove(interestList.get(position).get_name());
            }

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < stringList.size(); i++) {
                String data = stringList.get(i);
                builder.append(data);
                if (i < stringList.size() - 1) {
                    builder.append(", ");
                }
            }
            String data = builder.toString();
            if (data.length() > 0) {
                tvInterest.setText(data);
            } else {
                tvInterest.setText("Select Interest");
            }
        }

        // recyclerViewInterest.setVisibility(View.GONE);
    }


    // permission section........................
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(CreateNewCampaignActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(CreateNewCampaignActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(CreateNewCampaignActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent galleryOne = new Intent(Intent.ACTION_PICK);
                    galleryOne.setType("image/* video/*");
                    startActivityForResult(galleryOne, PICK_IMAGE);

                }
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void InterestList() {

        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));


        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<TopicsResponse> call = webApi.getAdInterest(header);
        call.enqueue(new Callback<TopicsResponse>() {
            @Override
            public void onResponse(Call<TopicsResponse> call, Response<TopicsResponse> response) {
                if (response.code() == 200 && response.body().getSuccess()) {
                    for (int i = 0; i < response.body().getData().getRows().size(); i++) {
                        CheckboxModel objmodelInterest3 = new CheckboxModel();
                        objmodelInterest3.set_name(response.body().getData().getRows().get(i).getTopic());
                        objmodelInterest3.set_ischecked(false);
                        interestList.add(objmodelInterest3);
                    }
                } else {
                    Utilss.showToast(CreateNewCampaignActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<TopicsResponse> call, Throwable t) {
                Utilss.showToast(CreateNewCampaignActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

}

