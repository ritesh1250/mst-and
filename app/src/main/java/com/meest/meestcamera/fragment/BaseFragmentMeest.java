package com.meest.meestcamera.fragment;


import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;

@SuppressLint("NewApi")
public abstract class BaseFragmentMeest extends Fragment {
    //-----------------Sticker data start------------
//    private ARGFrame.Ratio mScreenRatio = ARGFrame.Ratio.RATIO_FULL;
//    private int mDeviceWidth = 0;
//    private int mDeviceHeight = 0;
//    private int mGLViewWidth = 0;
//    private int mGLViewHeight = 0;
//    private static final int DISCARD_UPLOAD_CODE = 5;
//    LinearLayout viewSticker, stickerLayout, viewFilters,selectMusic,addMusicLayout;
//    ImageView img_file_upload;
//    List<String> speedList;
//    int SELECTED_SPEED = 1;
//    public static MediaPlayer audioPlayer = null;
//    boolean isAudioSelected, isSpeedOn, isTimerSet;
//    CameraSpeedAdapter cameraSpeedAdapter;
//    TextView tvTime, tvSpeed, txt_song_name;
//    private RecyclerView rvFilterList, rvSpeed, rvStickerList;
//    ARGearDataModel arGearDataModel;
//    List<ARGearDataModel.ARGearItem> filterList = new ArrayList<>();
//    List<ARGearDataModel.ARGearItem> stickerList = new ArrayList<>();
//    private RelativeLayout viewCapture;
//    private String parentFolderPath;
//    ARGSession argsession;
//    CircularProgressBar cpb;
//    RelativeLayout viewProgressbar;
//    LinearLayout filterLayout,viewSpeed;
//    private ARGearDataModel.ARGearItem mCurrentStickeritem = null;
//    private boolean mHasTrigger = false;
//    private static final int SELECT_VIDEO = 12;
//    //-----------------Sticker data finish------------
//    public SessionManager sessionManager = null;
//    private NetWorkChangeReceiver netWorkChangeReceiver = null;
//    private BottomSheetDialog dialog;
//    private CompositeDisposable disposable = new CompositeDisposable();
//
//    Context baseContext;
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        baseContext=context;
//    }
//
//    /*   private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                GraphRequest request = GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(), (JSONObject object, GraphResponse response) -> {
//                            try {
//                                HashMap<String, Object> hashMap = new HashMap<>();
//                                hashMap.put("device_token", Global.firebaseDeviceToken);
//                                hashMap.put("user_email", object.getString("email"));
//                                hashMap.put("full_name", object.getString("name"));
//                                hashMap.put("login_type", Const.FACEBOOK_LOGIN);
//                                hashMap.put("user_name", object.getString("id"));
//                                hashMap.put("platform", "android");
//                                hashMap.put("identity", object.getString("id"));
//                                registerUser(hashMap);
//                            } catch (JSONException | NullPointerException e) {
//                                e.printStackTrace();
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,email,gender,birthday");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        };*/
//    private ARGContents.Type type;
//    private String path;
//    private ARGearDataModel.ARGearItem itemModel;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        sessionManager = new SessionManager(baseContext);
//
//    }
//
//
//
//    View myView;
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        myView= inflater.inflate(R.layout.fragment_videos, container, false);
//
//
//
//        return myView;
//
//    }
//
//
//
//
//
//    protected void startReceiver() {
//        netWorkChangeReceiver = new NetWorkChangeReceiver(this::showHideInternet);
//        registerNetworkBroadcastForNougat();
//    }
//
//    private void showHideInternet(Boolean isOnline) {
//        final TextView tvInternetStatus =myView.findViewById(R.id.tv_internet_status);
//
//        if (isOnline) {
//            if (tvInternetStatus != null && tvInternetStatus.getVisibility() == View.VISIBLE && tvInternetStatus.getText().toString().equalsIgnoreCase(getString(R.string.no_internet_connection))) {
//                tvInternetStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.kellygreen));
//                tvInternetStatus.setText(R.string.back_online);
//                new Handler().postDelayed(() -> slideToBottom(tvInternetStatus), 200);
//            }
//        } else {
//            if (tvInternetStatus != null) {
//                tvInternetStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.india_red));
//                tvInternetStatus.setText(R.string.no_internet_connection);
//                if (tvInternetStatus.getVisibility() == View.GONE) {
//                    slideToTop(tvInternetStatus);
//                }
//            }
//        }
//    }
//
//    private void slideToTop(View view) {
//        TranslateAnimation animation = new TranslateAnimation(0f, 0f, view.getHeight(), 0f);
//        animation.setDuration(300);
//        view.startAnimation(animation);
//        view.setVisibility(View.VISIBLE);
//    }
//
//    private void slideToBottom(final View view) {
//        TranslateAnimation animation = new TranslateAnimation(0f, 0f, 0f, view.getHeight());
//        animation.setDuration(300);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                view.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        view.startAnimation(animation);
//    }
//
//    private void registerNetworkBroadcastForNougat() {
//        baseContext.registerReceiver(netWorkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//    }
//
//    protected void unregisterNetworkChanges() {
//        try {
//            baseContext. unregisterReceiver(netWorkChangeReceiver);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//
//
//
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK) {
//            Uri contentURI = data.getData();
//            String selectedVideoPath = FileUtils.getPath(baseContext, contentURI);
//            Log.d("harsh", "selectedVideoPath == " + selectedVideoPath);
//            try {
//                if (selectedVideoPath != null) {
//                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                    retriever.setDataSource(baseContext, contentURI);
//                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                    long timeInMillisec = Long.parseLong(time);
//                    retriever.release();
//
//                    if (timeInMillisec > 60000) {
//                       /* VIDEO_TIME_SECONDS = 60;
//                        trimToOneMinute(selectedVideoPath, contentURI, true);*/
//                    } else {
//                       /* Intent intent = new Intent(BaseActivity.this, PostVideoActivity.class);
//                        intent.putExtra("video_path", selectedVideoPath);
//                        startActivity(intent);
//                        finish();*/
//                    }
////                    Intent intent = new Intent(BaseCameraActivity.this, PostVideoActivity.class);
////                    intent.putExtra("video_path", selectedVideoPath);
////                    startActivityForResult(intent, DISCARD_UPLOAD_CODE);
////                    finish();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(baseContext, "Something went wrong, please try again later", Toast.LENGTH_LONG).show();
//            }
//        }else if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK) {
//            Uri contentURI = data.getData();
//            String selectedVideoPath = FileUtils.getPath(baseContext, contentURI);
//            Log.e("harsh", "selectedVideoPath == " + selectedVideoPath);
//            try {
//                if (selectedVideoPath != null) {
//                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                    retriever.setDataSource(baseContext, contentURI);
//                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                    long timeInMillisec = Long.parseLong(time);
//                    retriever.release();
//
//                   /* Intent intent = new Intent(BaseActivity.this, PostVideoActivity.class);
//                    intent.putExtra("video_path", selectedVideoPath);
//                    startActivity(intent);
//                    finish();*/
////                    Intent intent = new Intent(BaseCameraActivity.this, PostVideoActivity.class);
////                    intent.putExtra("video_path", selectedVideoPath);
////                    startActivityForResult(intent, DISCARD_UPLOAD_CODE);
////                    finish();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(baseContext, "Something went wrong, please try again later", Toast.LENGTH_LONG).show();
//            }
//        }else if (requestCode == DISCARD_UPLOAD_CODE && data != null) {
//            if (data.getExtras().getBoolean("discard", false)) {
//                // resetting video recording
//                //resetShoot();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//
//
//
//
//    protected void onCreateActivity() {
//        initUI();
//        fetchARGearData();
//    }
//
//    private void initUI() {
//        // for flash
//        findIds();
//        parentFolderPath = getFilesDir().getAbsolutePath();
//        viewSpeed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (rvSpeed.getVisibility() == View.VISIBLE) {
//                    rvSpeed.setVisibility(View.GONE);
//                    tvSpeed.setText("Speed\nOff");
//                } else {
//                    rvSpeed.setVisibility(View.VISIBLE);
//                    tvSpeed.setText("Speed\nOn");
//                    rvFilterList.setVisibility(View.GONE);
//                    filterLayout.setVisibility(View.GONE);
//                    viewCapture.setVisibility(View.VISIBLE);
//                    stickerLayout.setVisibility(View.GONE);
//                    rvStickerList.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        img_file_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("video/*");
//                intent.setAction(Intent.ACTION_PICK);
//                startActivityForResult(Intent.createChooser(intent, "Select Video"), SELECT_VIDEO);
//            }
//        });
//
//        viewSticker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("Sticker=========", "Done in Base Activity");
//                if (stickerLayout.getVisibility() == View.VISIBLE) {
//                    stickerLayout.setVisibility(View.GONE);
//                    rvStickerList.setVisibility(View.GONE);
////                    viewCapture.setVisibility(View.VISIBLE);
//                    return;
//                }
//
//                rvStickerList.setVisibility(View.VISIBLE);
//                stickerLayout.setVisibility(View.VISIBLE);
////                viewCapture.setVisibility(View.GONE);
//            }
//        });
//
//        viewFilters.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (filterLayout.getVisibility() == View.VISIBLE) {
//                    rvFilterList.setVisibility(View.GONE);
//                    filterLayout.setVisibility(View.GONE);
////                    viewCapture.setVisibility(View.VISIBLE);
//                    return;
//                }
//                rvFilterList.setVisibility(View.VISIBLE);
//                filterLayout.setVisibility(View.VISIBLE);
//
////                viewCapture.setVisibility(View.GONE);
//            }
//        });
//    }
//
//    private void findIds() {
//        viewSticker = findViewById(R.id.viewSticker);
//        stickerLayout = findViewById(R.id.stickerLayout);
//        rvStickerList = findViewById(R.id.rvStickerList);
//        selectMusic = findViewById(R.id.selectMusic);
//        cpb = findViewById(R.id.cpb);
//        tvSpeed = findViewById(R.id.tvSpeed);
//        viewProgressbar = findViewById(R.id.viewProgressbar);
//        viewCapture = findViewById(R.id.viewCapture);
//        viewFilters = findViewById(R.id.viewFilters);
//        filterLayout = findViewById(R.id.filterLayout);
//        rvFilterList = findViewById(R.id.rvFilterList);
//        img_file_upload = findViewById(R.id.postVideoFromGallery);
//        addMusicLayout=findViewById(R.id.addMusic);
//        viewSpeed=findViewById(R.id.viewSpeed);
//        rvSpeed = findViewById(R.id.rvSpeed);
//        rvSpeed.setLayoutManager(new LinearLayoutManager(BaseFragmentMeest.this, RecyclerView.HORIZONTAL, false));
//        speedList = new ArrayList<>();
//        speedList.add("0.5x");
//        speedList.add("1x");
//        speedList.add("1.5x");
//        speedList.add("2x");
//        cameraSpeedAdapter = new CameraSpeedAdapter(BaseFragmentMeest.this, speedList);
//        rvSpeed.setAdapter(cameraSpeedAdapter);
//        addMusicLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(BaseFragmentMeest.this, "Coming soon", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        cameraSpeedAdapter.setOnItemClickListener(new CameraSpeedAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                SELECTED_SPEED = position;
//                isSpeedOn = position != 2;
//                Toast.makeText(BaseFragmentMeest.this, "Coming soon", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void fetchARGearData() {
//        ARGearApi arGearApi = ARGearWebService.createContentsService();
//
//        arGearApi.getContents(Constant.ARGEAR_API_KEY).enqueue(new Callback<ARGearDataModel>() {
//            @Override
//            public void onResponse(Call<ARGearDataModel> call, Response<ARGearDataModel> response) {
//                if (response.code() == 200 && response.isSuccessful()) {
//                    Log.v("harsh", "body == " + response.body());
//                    arGearDataModel = response.body();
//
//                    extractData();
//                } else {
//                    Utilss.showToast(BaseFragmentMeest.this, "Something went wrong, please try again later-1",
//                            R.color.msg_fail);
////                    finish();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ARGearDataModel> call, Throwable t) {
//                Utilss.showToast(BaseFragmentMeest.this, "Something went wrong, please try again later-2",
//                        R.color.msg_fail);
////                finish();
//            }
//        });
//    }
//
//    private void extractData() {
//        // clearing old items
//        filterList.clear();
//        stickerList.clear();
//
//        for (ARGearDataModel.ARGearCategory arGearCategory : arGearDataModel.getCategories()) {
//            for (ARGearDataModel.ARGearItem arGearItem : arGearCategory.getARGearItems()) {
//                if (arGearItem.getType().equalsIgnoreCase("filter")) {
//                    filterList.add(arGearItem);
//                } else {
//                    stickerList.add(arGearItem);
//                }
//            }
//        }
//        // binding data
//        bindData();
//    }
//
//    private void bindData() {
//// binding filter data
//        ARGearFilterAdapter filterAdapter = new ARGearFilterAdapter(this, filterList);
//        rvFilterList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        rvFilterList.setAdapter(filterAdapter);
//        filterAdapter.setOnItemClickListener(new CameraFilterAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Log.e("====SetFilterClick","Done");
//                Toast.makeText(BaseFragmentMeest.this, "Coming soon", Toast.LENGTH_SHORT).show();
//               // setFilter(filterList.get(position));
//               // viewCapture.setVisibility(View.VISIBLE);
//            }
//        });
//        // binding sticker data
//        ARGearStickerAdapter stickerAdapter = new ARGearStickerAdapter(this, stickerList);
//        rvStickerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        rvStickerList.setAdapter(stickerAdapter);
//        stickerAdapter.setOnItemClickListener(new CameraFilterAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Toast.makeText(BaseFragmentMeest.this, "Coming soon", Toast.LENGTH_SHORT).show();
//                Log.e("====SetStickerClick","Done");
//               // setSticker(stickerList.get(position));
//                //viewCapture.setVisibility(View.VISIBLE);
//            }
//        });
//    }
//
//    public void setSticker(ARGearDataModel.ARGearItem item) {
//        String filePath = parentFolderPath + "/" + item.getUuid();
//        showPrd();
//
//        if (getLastUpdateAt(BaseFragmentMeest.this) > getStickerUpdateAt(BaseFragmentMeest.this, item.getUuid())) {
//            new FileDeleteAsyncTask(new File(filePath), new FileDeleteAsyncTask.OnAsyncFileDeleteListener() {
//                @Override
//                public void processFinish(Object result) {
//                    Log.d("harsh", "file delete success!");
//
//                    setStickerUpdateAt(BaseFragmentMeest.this, item.getUuid(), getLastUpdateAt(BaseFragmentMeest.this));
//                    requestSignedUrl(item, filePath, true);
//                }
//            }).execute();
//        } else {
//            if (new File(filePath).exists()) {
//                setItem(ARGContents.Type.ARGItem, filePath, item);
//            } else {
//                requestSignedUrl(item, filePath, true);
//            }
//        }
//    }
//
//    private void showPrd() {
//        if (cpb != null && viewProgressbar != null) {
//            viewProgressbar.setVisibility(View.VISIBLE);
//            ((CircularProgressDrawable) cpb.getIndeterminateDrawable()).start();
//        }
//    }
//
//    private long getLastUpdateAt(Context context) {
//        return PreferenceUtil.getLongValue(context, Constant.USER_PREF_NAME, "ContentLastUpdateAt");
//    }
//
//    private void setStickerUpdateAt(Context context, String itemId, long updateAt) {
//        PreferenceUtil.putLongValue(context, Constant.USER_PREF_NAME_STICKER, itemId, updateAt);
//    }
//
//    private long getStickerUpdateAt(Context context, String itemId) {
//        return PreferenceUtil.getLongValue(context, Constant.USER_PREF_NAME_STICKER, itemId);
//    }
//
//    // region - network
//    private void requestSignedUrl(ARGearDataModel.ARGearItem item, String path, final boolean isArItem) {
//        showPrd();
//        argsession.auth().requestSignedUrl(item.getZipFile(), item.getTitle(), item.getType(), new ARGAuth.Callback() {
//            @Override
//            public void onSuccess(String url) {
//                requestDownload(path, url, item, isArItem);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                if (e instanceof SignedUrlGenerationException) {
//                    Log.e("harsh", "SignedUrlGenerationException !! ");
//                } else if (e instanceof NetworkException) {
//                    Log.e("harsh", "NetworkException !!");
//                }
//
//                hidePrd();
//            }
//        });
//    }
//
//    private void hidePrd() {
//        if (cpb != null && viewProgressbar != null) {
//            viewProgressbar.setVisibility(View.GONE);
//            ((CircularProgressDrawable) cpb.getIndeterminateDrawable()).progressiveStop();
//        }
//    }
//
//    public void setItem(ARGContents.Type type, String path, ARGearDataModel.ARGearItem itemModel) {
//        this.type = type;
//        this.path = path;
//        this.itemModel = itemModel;
//
//        mCurrentStickeritem = null;
//        mHasTrigger = false;
//        hidePrd();
//
//        argsession.contents().setItem(type, path, itemModel.getUuid(), new ARGContents.Callback() {
//            @Override
//            public void onSuccess() {
//                if (type == ARGContents.Type.ARGItem) {
//                    mCurrentStickeritem = itemModel;
//                    mHasTrigger = itemModel.getHasTrigger();
//
//                    Utilss.showToast(BaseFragmentMeest.this, "Sticker applied",
//                            R.color.green);
//                } else {
//                    Utilss.showToast(BaseFragmentMeest.this, "Filter applied",
//                            R.color.green);
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                mCurrentStickeritem = null;
//                mHasTrigger = false;
//                if (e instanceof InvalidContentsException) {
//                    Log.e("harsh", "InvalidContentsException");
//                }
//                Utilss.showToast(BaseFragmentMeest.this, "Something went wrong while processing your request, please try again later",
//                        R.color.msg_fail);
//            }
//        });
//    }
//
//    private void requestDownload(String targetPath, String url, ARGearDataModel.ARGearItem item, boolean isSticker) {
//        new DownloadAsyncTask(targetPath, url, new DownloadAsyncResponse() {
//            @Override
//            public void processFinish(boolean result) {
//                // hiding progress dialog
//                hidePrd();
//                if (result) {
//                    if (isSticker) {
//                        setItem(ARGContents.Type.ARGItem, targetPath, item);
//                    } else {
//                        setItem(ARGContents.Type.FilterItem, targetPath, item);
//                    }
//                } else {
//                    Utilss.showToast(BaseFragmentMeest.this, "Something went wrong while processing your request, please try again later",
//                            R.color.msg_fail);
//                }
//            }
//        }).execute();
//    }
//    public void setFilter(ARGearDataModel.ARGearItem item) {
//        String filePath = parentFolderPath + "/" + item.getUuid();
//        showPrd();
//        if (getLastUpdateAt(BaseFragmentMeest.this) > getFilterUpdateAt(BaseFragmentMeest.this, item.getUuid())) {
//            new FileDeleteAsyncTask(new File(filePath), new FileDeleteAsyncTask.OnAsyncFileDeleteListener() {
//                @Override
//                public void processFinish(Object result) {
//                    Log.d("harsh", "file delete success!");
//
//                    setFilterUpdateAt(BaseFragmentMeest.this, item.getUuid(), getLastUpdateAt(BaseFragmentMeest.this));
//                    requestSignedUrl(item, filePath, false);
//                }
//            }).execute();
//        } else {
//            if (new File(filePath).exists()) {
//                setItem(ARGContents.Type.FilterItem, filePath, item);
//            } else {
//                requestSignedUrl(item, filePath, false);
//            }
//        }
//    }
//
//    private void setFilterUpdateAt(Context context, String itemId, long updateAt) {
//        PreferenceUtil.putLongValue(context, Constant.USER_PREF_NAME_FILTER, itemId, updateAt);
//    }
//
//    private long getFilterUpdateAt(Context context, String itemId) {
//        return PreferenceUtil.getLongValue(context, Constant.USER_PREF_NAME_FILTER, itemId);
//    }
//    public int getGLViewWidth() {
//        return mGLViewWidth;
//    }
//
//    public int getGLViewHeight() {
//        return mGLViewHeight;
//    }
//    public void setMeasureSurfaceView(View view) {
//        if (view.getParent() instanceof FrameLayout) {
//            view.setLayoutParams(new FrameLayout.LayoutParams(mGLViewWidth, mGLViewHeight));
//        } else if (view.getParent() instanceof RelativeLayout) {
//            view.setLayoutParams(new RelativeLayout.LayoutParams(mGLViewWidth, mGLViewHeight));
//        }
//
//        /* to align center */
//        if ((mScreenRatio == ARGFrame.Ratio.RATIO_FULL) && (mGLViewWidth > mDeviceWidth)) {
//            view.setX((mDeviceWidth - mGLViewWidth) / 2);
//        } else {
//            view.setX(0);
//        }
//    }
//    /*public void showCollectionsDialog() {
//        // stopping audio player if it is playing
//        if (audioPlayer != null && audioPlayer.isPlaying()) {
//            audioPlayer.stop();
//        }
//
//        View view = getLayoutInflater().inflate(R.layout.fragment_music_category_list, null);
//        RecyclerView recyclerView = view.findViewById(R.id.recycler_music_track);
//        LottieAnimationView loading = view.findViewById(R.id.loading);
//
//        loading.setAnimation("loading.json");
//        loading.playAnimation();
//        loading.loop(true);
//        loading.setVisibility(View.VISIBLE);
//
//        BottomSheetDialog dialog = new BottomSheetDialog(BaseActivity.this);
//        dialog.setContentView(view);
//        dialog.show();
//
//        final AudioCategoryResponse[] audioCategoryResponses = new AudioCategoryResponse[1];
//        Map<String, String> header = new HashMap<>();
//        header.put("x-token", Global.apikey);
//
//        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
//        Call<AudioCategoryResponse> call = webApi.getAllAudioCategory(header);
//        call.enqueue(new Callback<AudioCategoryResponse>() {
//            @Override
//            public void onResponse(Call<AudioCategoryResponse> call, Response<AudioCategoryResponse> response) {
//                loading.setVisibility(View.GONE);
//                if (response.code() == 200 && response.body().getSuccess()) {
//                    audioCategoryResponses[0] = response.body();
//
//                    recyclerView.setHasFixedSize(true);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(BaseActivity.this));
//
//                    MusicTrackAdapter adapter = new MusicTrackAdapter(BaseActivity.this, audioCategoryResponses[0], dialog);
//
//                    //setting adapter to recyclerview
//                    recyclerView.setAdapter(adapter);
//                } else {
//                    Utilss.showToast(BaseActivity.this, "Server Error", R.color.msg_fail);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AudioCategoryResponse> call, Throwable t) {
//                loading.setVisibility(View.GONE);
//                Utilss.showToast(BaseActivity.this, "Server Error", R.color.msg_fail);
//            }
//        });
//    }*/
}