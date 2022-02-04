package com.meest.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;
import com.meest.responses.CampaignUpdateResponse;
import com.meest.meestbhart.register.fragment.profile.model.UploadimageResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.google.android.gms.maps.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.tom.range.slider.RangeSliderView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class UpdateCampaignActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Button btn_update;

    AutoCompleteTextView autoCompleteTextView_up;
    private RelativeLayout layout_upload_img_up;
    private TextView DailyBudget_seekNum_up;
    private static final int PICK_IMAGE = 100;
    private ImageView imv_uploadImg_up;
    private Uri imageUri;
    private Spinner spinGender_up, spinCalltoAction_up;
    private TextView txt_date_select_up, txt_rs_up;

    private RangeSliderView rangeSeekBar_up;

    private String startDate;
    private String endDate;

    private SeekBar DailyBudget_seekbar_up;
    private String minAge, maxAge;

    final Calendar dateSelected = Calendar.getInstance();

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyDqJfGPGdvELTNmrAFW3nDLOrvVP0FXTxo";

    EditText ed_enterAds_head_up, edt_enter_add_details_up, edt_web_site_link_up;

    private Button privew;
    private ImageView back;

    String imgUrl = "";

    Switch switch_map_up;
    MapView map_view_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_campaign);


//        btn_update=findViewById(R.id.btn_update);
//
//        switch_map_up=findViewById(R.id.switch_map_up);
//
//        map_view_up=findViewById(R.id.map_view_up);

        switch_map_up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    map_view_up.setVisibility(View.VISIBLE);
                } else {
                    map_view_up.setVisibility(View.GONE);
                }
            }
        });

//        txt_rs_up=findViewById(R.id.txt_rs_up);
//
////         = findViewById(R.id.btn_payAmt);
////        btn_privew_campaign = findViewById(R.id.btn_privew_campaign);
//        ed_enterAds_head_up = findViewById(R.id.ed_enterAds_head_up);
//        edt_enter_add_details_up = findViewById(R.id.edt_enter_add_details_up);
//        edt_web_site_link_up = findViewById(R.id.edt_web_site_link_up);
//
//
//        DailyBudget_seekbar_up = findViewById(R.id.DailyBudget_seekbar_up);
//        DailyBudget_seekNum_up=findViewById(R.id.DailyBudget_seekNum_up);

        DailyBudget_seekbar_up.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                String seekbarValue = String.valueOf(i);
                DailyBudget_seekNum_up.setText(seekbarValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgUrl.length() > 0 && imgUrl.contains("http")) {
                    validate();

                } else {
                    Toast.makeText(UpdateCampaignActivity.this, getString(R.string.please_select), Toast.LENGTH_SHORT).show();
                }

            }
        });


//        rangeSeekBar_up = (RangeSliderView) findViewById(R.id.range_seekbar_up); // initiate the Seekbar

        rangeSeekBar_up.setOnValueChangedListener(new RangeSliderView.OnValueChangedListener() {
            @Override
            public void onValueChanged(int minValue, int maxValue) {

                minAge = String.valueOf(minValue);
                maxAge = String.valueOf(minValue);
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


//        txt_date_select_up = findViewById(R.id.txt_date_select_up);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                dateSelected.set(Calendar.YEAR, year);
                dateSelected.set(Calendar.MONTH, monthOfYear);
                dateSelected.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
                startDate = simpleDateFormat.format(dateSelected.getTime());


                DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        dateSelected.set(Calendar.YEAR, year);
                        dateSelected.set(Calendar.MONTH, monthOfYear);
                        dateSelected.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        endDate = simpleDateFormat.format(dateSelected.getTime());


                        txt_date_select_up.setText(startDate + " - " + endDate);
                    }
                };
                new DatePickerDialog(UpdateCampaignActivity.this, date2,
                        dateSelected.get(Calendar.YEAR),
                        dateSelected.get(Calendar.MONTH),
                        dateSelected.get(Calendar.DAY_OF_MONTH)).show();
            }
        };

        txt_date_select_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(UpdateCampaignActivity.this, date,
                        dateSelected.get(Calendar.YEAR),
                        dateSelected.get(Calendar.MONTH),
                        dateSelected.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
//
//        autoCompleteTextView_up = findViewById(R.id.autoCompleteTextView_up);
//
//        autoCompleteTextView_up.setAdapter(new UpdateCampaignActivity.GooglePlacesAutocompleteAdapter(UpdateCampaignActivity.this, R.layout.list_item));
//        autoCompleteTextView_up.setOnItemClickListener(this);
//
//        layout_upload_img_up = findViewById(R.id.layout_upload_img_up);
//        imv_uploadImg_up = findViewById(R.id. imv_uploadImg_up);
//
//        spinCalltoAction_up = findViewById(R.id.spinCalltoAction_up);
//        ArrayAdapter<String> adapter_cta = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, getResources()
//                .getStringArray(R.array.CalltoActionValue));//setting the country_array to spinner
//        adapter_cta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinCalltoAction_up.setAdapter(adapter_cta);


//        spinGender_up = (Spinner) findViewById(R.id.spinGender_up);//fetch the spinner from layout file
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, getResources()
//                .getStringArray(R.array.GenderValue));//setting the country_array to spinner
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinGender_up.setAdapter(adapter);
//

//if you want to set any action you can do in this listener
//        spinGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int position, long id) {
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//            }
//        });


//        back = (ImageView) findViewById(R.id.img_back_CreateNewCampaign_up);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//        layout_upload_img_up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                startActivityForResult(gallery, PICK_IMAGE);
//
//            }
//        });
    }

    private void validate() {
        String head = ed_enterAds_head_up.getText().toString();
        String details = edt_enter_add_details_up.getText().toString();
        String weblink = edt_web_site_link_up.getText().toString();
        String callToaction = spinCalltoAction_up.getSelectedItem().toString();
        String gender = spinGender_up.getSelectedItem().toString();

        String location = autoCompleteTextView_up.getText().toString();
        String dailyBudget = DailyBudget_seekNum_up.getText().toString();
        // String totalAmt=txt_rs.getText().toString();

        if (TextUtils.isEmpty(head)) {
            ed_enterAds_head_up.setError("Your heading!");
            return;
        } else if (TextUtils.isEmpty(details)) {
            edt_enter_add_details_up.setError("Your details!");
            return;
        } else if (TextUtils.isEmpty(weblink)) {
            edt_web_site_link_up.setError("Your Website link!");
            return;
        } else if (TextUtils.isEmpty(callToaction)) {
            Toast.makeText(this, getResources().getString(R.string.please_select), Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(gender)) {
            Toast.makeText(this, getResources().getString(R.string.please_select), Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(location)) {
            autoCompleteTextView_up.setError("Enter location!");
            return;
        } else if (TextUtils.isEmpty(dailyBudget)) {
            DailyBudget_seekNum_up.setError("Select daily budget!");
            return;
        }

//        else if(TextUtils.isEmpty(totalAmt)) {
//           // txt_rs.setError("Enter location!");
//            return;
//        }

        else {

            getUpdateData();
        }
    }

    private void getUpdateData() {
        Map<String, String> header = new HashMap<>();

        header.put("x-token", SharedPrefreances.getSharedPreferenceString(UpdateCampaignActivity.this, SharedPrefreances.AUTH_TOKEN));
        // body
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("fileUrl",imgUrl)
//                .addFormDataPart("campaignTitle", ed_enterAds_head_up.getText().toString())
//                .addFormDataPart("campaignText", edt_enter_add_details_up.getText().toString())
//                .addFormDataPart("websiteUrl", edt_web_site_link_up.getText().toString())
//                .addFormDataPart("callToAction", spinCalltoAction_up.getSelectedItem().toString())
//                .addFormDataPart("gender", spinGender_up.getSelectedItem().toString())
//                .addFormDataPart("startAge", minAge)
//                .addFormDataPart("endAge", maxAge)
//                .addFormDataPart("location", autoCompleteTextView_up.getText().toString())
//                .addFormDataPart("campaignType", edt_enter_add_details_up.getText().toString())
//                .addFormDataPart("otherImageVideos", edt_enter_add_details_up.getText().toString())
//                .addFormDataPart("startDate", startDate)
//                .addFormDataPart("endDate", endDate)
//                .addFormDataPart("dailyBudget", DailyBudget_seekNum_up.getText().toString())
//                // .addFormDataPart("totalAmmount", txt_rs.getText().toString())
////                .addFormDataPart("addedBy",)
////                .addFormDataPart("userId",)
////                .addFormDataPart("campaignStatus",)
////                .addFormDataPart("isPreview",)
////                .addFormDataPart("duplicate",)
//
//                .build();


        HashMap<String, Object> body = new HashMap<>();
        body.put("id", "campaignId");
        body.put("campaignStatus", "Drafted");

        WebApi webApi2 = ApiUtils.getClient().create(WebApi.class);
        Call<CampaignUpdateResponse> call2 = webApi2.getupdatecampaign(header, body);

        call2.enqueue(new Callback<CampaignUpdateResponse>() {
            @Override
            public void onResponse(Call<CampaignUpdateResponse> call2, Response<CampaignUpdateResponse> response) {
                try {

                    if (response.code() == 200 && response.body().getSuccess()) {
                        Toast.makeText(UpdateCampaignActivity.this, getString(R.string.send_sucessfull), Toast.LENGTH_SHORT).show();
                    } else {

                        Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CampaignUpdateResponse> call2, Throwable t) {
                Utilss.showToast(UpdateCampaignActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imv_uploadImg_up.setImageURI(imageUri);

            uploadImage(imageUri.getPath());

        }
    }


    private void uploadImage(String path) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), path);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<UploadimageResponse> call = webApi.uploadImageRequest(map, body);
        call.enqueue(new Callback<UploadimageResponse>() {
            @Override
            public void onResponse(Call<UploadimageResponse> call, Response<UploadimageResponse> response) {
                try {
                    // image.setVisibility(View.GONE);
                    if (response.code() == 200 && response.body().getSuccess()) {
                        imgUrl = response.body().getData().getUrl();
                    } else {
                        imgUrl = "";
                        Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    imgUrl = "";
                }
            }

            @Override
            public void onFailure(Call<UploadimageResponse> call, Throwable t) {

                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }


    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("LongLogTag")
    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:gr");
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
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
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

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
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


}
