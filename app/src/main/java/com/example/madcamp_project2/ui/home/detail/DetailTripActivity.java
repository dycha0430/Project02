package com.example.madcamp_project2.ui.home.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.widget.ViewPager2;

import com.example.madcamp_project2.LoginActivity;
import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.MyAPI;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.ActivityDetailTripBinding;
import com.example.madcamp_project2.ui.Country;
import com.example.madcamp_project2.ui.CountryEnum;
import com.example.madcamp_project2.ui.Schedule;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.TripState;
import com.example.madcamp_project2.ui.home.HomeFragment;
import com.example.madcamp_project2.ui.home.ScheduleComparator;
import com.example.madcamp_project2.ui.home.addtrip.Travel.UpdateTravel;
import com.example.madcamp_project2.ui.home.addtrip.Travel.userTravel;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTripActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, PlacesListener {
    private ActivityDetailTripBinding binding;
    TripPlan tripPlan;
    static final int zoom_pm = 15;

    EditText titleTextView;
    TextView stateTextView;
    Button datePickerBtn;
    private MaterialViewPager mViewPager;
    private static GoogleMap mMap;
    List<Marker> previous_marker = null;

    Spinner spinner;
    Spinner placeSpinner;

    static Geocoder geoCoder;
    static Context context;
    static List<Polyline>[] polylines;
    static Marker mCurrentMarker;

    ViewPager2 viewPager2;
    private static int placeTypeIndex = 0;

    private String[] placeTypes = {"레스토랑", "카페", "베이커리", "숙소", "주점"};

    public static CountryEnum getCountryEnum(String str) {
            if (str.equals("서울")) {
                return CountryEnum.SEOUL;
            } else if (str.equals("인천")) {
                return CountryEnum.INCHEON;
            } else if (str.equals("부산")) {
                return CountryEnum.BUSAN;
            } else if (str.equals("제주")) {
                return CountryEnum.JEJU;
            } else if (str.equals("경기")) {
                return CountryEnum.GYEONGGI;
            } else if (str.equals("포항")) {
                return CountryEnum.POHANG;
            } else if (str.equals("강릉")) {
                return CountryEnum.GANGNEUNG;
            } else if (str.equals("속초")) {
                return CountryEnum.SOKCHO;
            } else if (str.equals("대구")) {
                return CountryEnum.DAEGU;
            } else if (str.equals("경주")) {
                return CountryEnum.GYEONGJU;
            } else if (str.equals("여수")) {
                return CountryEnum.YEOSU;
            } else if (str.equals("전주")) {
                return CountryEnum.JEONJU;
            } else if (str.equals("춘천")) {
                return CountryEnum.CHUNCHEON;
            } else if (str.equals("대전")) {
                return CountryEnum.DAEJEON;
            }

            return CountryEnum.SEOUL;
    }

    public String getFromLocation(long latitude, long longitude, int i) {
        List<Address> list = null;
        try {
            list = geoCoder.getFromLocation(latitude, longitude, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list != null) {
            if (list.size() == 0) {
                return null;
            } else {
                return list.get(0).getAddressLine(0);
            }
        }

        return null;
    }

    public static LatLng getFromLocationName(String name) {
        List<Address> list = null;
        try {
            list = geoCoder.getFromLocationName(name, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list != null) {
            if (list.size() == 0) {
                return null;
            } else {
                return new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
            }
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("$$$$$$$$$$$$$$$$$44", "RESUME!!");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;
        geoCoder = new Geocoder(this);
        previous_marker = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().hide();
        Intent intent = getIntent();
//        this.tripPlan = (TripPlan) intent.getSerializableExtra("tripPlan");
        int idx = intent.getIntExtra("tripPlan", 0);
        this.tripPlan = HomeFragment.tripPlanList.get(idx);

        stateTextView = findViewById(R.id.detailStateTextView);
        titleTextView = findViewById(R.id.detailTitleTextView);
        datePickerBtn = findViewById(R.id.datePickerButton);
        spinner = findViewById(R.id.spinner);
        placeSpinner = findViewById(R.id.place_spinner);
        viewPager2 = findViewById(R.id.viewPager);

        for (ArrayList<Schedule> schedules : tripPlan.getSchedules()) {
            Collections.sort(schedules, new ScheduleComparator());
        }

        polylines = new ArrayList[tripPlan.getSchedules().length];
        for (int i = 0; i < tripPlan.getSchedules().length; i++) {
            polylines[i] = new ArrayList<>();
        }

        /* TODO Schedule View Pager */
        viewPager2.setAdapter(new ViewPagerAdapter(context, tripPlan));

        SpinnerAdapter adapter = new SpinnerAdapter(this, Arrays.asList(MainActivity.COUNTRIES.clone()));
        spinner.setAdapter(adapter);

        int position = 0;
        for (position = 0; position < MainActivity.COUNTRIES.length; position++) {
            if (MainActivity.COUNTRIES[position].getCountryEnum().toString().equals(tripPlan.getDestination().getCountryEnum().toString())) break;
        }
        spinner.setSelection(position);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO 장소 정보 TripPlan 데이터 변경
                CountryEnum countryName = getCountryEnum(spinner.getItemAtPosition(i).toString());
                tripPlan.setDestination(new Country(countryName));

                UpdateTravel updateTravel = new UpdateTravel(tripPlan.getTravel_id(), tripPlan.getTitle(), countryName.toString());
                String token = "";

                String file_path = MainActivity.get_filepath();
                JSONParser parser = new JSONParser();

                try {
                    FileReader reader = new FileReader(file_path+"/userinfo.json");
                    Object obj = parser.parse(reader);
                    JSONObject jsonObject = (JSONObject) obj;
                    reader.close();

                    token = jsonObject.get("token").toString();
                }
                catch (IOException | ParseException e) {
                    e.printStackTrace();
                }

                MyAPI myapi = LoginActivity.get_MyAPI();
                Call<UpdateTravel> post_updateTravel = myapi.post_updateTravel("Bearer " + token, updateTravel);

                post_updateTravel.enqueue(new Callback<UpdateTravel>() {
                    @Override
                    public void onResponse(Call<UpdateTravel> call, Response<UpdateTravel> response) {
                        if(response.isSuccessful()) {
                            Log.d("Update Travel", "SUCCESS");
                        }
                        else {
                            Log.d("Update Travel", "FAILED");
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateTravel> call, Throwable t) {
                        Log.d("Update Travel", "FAILED");
                    }
                });

                moveMap(countryName.toString(), countryName.toString());
                showPlaceInformation(getFromLocationName(countryName.toString()), placeTypeIndex);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        PlaceSpinnerAdapter adapter1 = new PlaceSpinnerAdapter(this, placeTypes);
        placeSpinner.setAdapter(adapter1);

        placeSpinner.setSelection(0);
        placeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                showPlaceInformation(getFromLocationName(tripPlan.getDestination().getName()), i);
                placeTypeIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        if (tripPlan.getState() == TripState.BEFORE) { stateTextView.setBackgroundColor(context.getResources().getColor(R.color.before)); }
        else if (tripPlan.getState() == TripState.ING) {
            stateTextView.setBackgroundColor(context.getResources().getColor(R.color.ing));
        }
        else { stateTextView.setBackgroundColor(context.getResources().getColor(R.color.after)); }
        stateTextView.setText(tripPlan.getState().toString());
        titleTextView.setText(tripPlan.getTitle());

        DateFormat df = new SimpleDateFormat("yy.MM.dd");
        String start_date = df.format(tripPlan.getStart_date());
        String end_date = df.format(tripPlan.getEnd_date());
        datePickerBtn.setText(start_date + " ~ " + end_date);

        titleTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // TODO Travel title update on DB
                tripPlan.setTitle(charSequence.toString());

                UpdateTravel updateTravel = new UpdateTravel(tripPlan.getTravel_id(), charSequence.toString(), tripPlan.getDestination().getName());
                String token = "";

                String file_path = MainActivity.get_filepath();
                JSONParser parser = new JSONParser();

                try {
                    FileReader reader = new FileReader(file_path+"/userinfo.json");
                    Object obj = parser.parse(reader);
                    JSONObject jsonObject = (JSONObject) obj;
                    reader.close();

                    token = jsonObject.get("token").toString();
                }
                catch (IOException | ParseException e) {
                    e.printStackTrace();
                }

                MyAPI myapi = LoginActivity.get_MyAPI();
                Call<UpdateTravel> post_updateTravel = myapi.post_updateTravel("Bearer " + token, updateTravel);

                post_updateTravel.enqueue(new Callback<UpdateTravel>() {
                    @Override
                    public void onResponse(Call<UpdateTravel> call, Response<UpdateTravel> response) {
                        if(response.isSuccessful()) {
                            Log.d("Update Travel", "SUCCESS");
                        }
                        else {
                            Log.d("Update Travel", "FAILED");
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateTravel> call, Throwable t) {
                        Log.d("Update Travel", "FAILED");
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // 한번 계획 생성하면 날짜 다시 바꿀 수는 없게 해야할 듯 showDatePicker는 생성시 사용
//        datePickerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDatePicker(view);
//            }
//        });
    }



    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static void moveMap(String address, String name) {
        LatLng loc = getFromLocationName(address);

        if (loc == null) {
            Toast.makeText(context, "위치를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom_pm));

        if (mCurrentMarker != null) mCurrentMarker.remove();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(loc);
        markerOptions.title(name);
        mCurrentMarker = mMap.addMarker(markerOptions);

    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        Context a = this.getBaseContext();
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(getFromLocationName(tripPlan.getDestination().getName())));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom_pm));
                showPlaceInformation(getFromLocationName(tripPlan.getDestination().getName()), placeTypeIndex);

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        double lat = marker.getPosition().latitude;
                        double lon = marker.getPosition().longitude;

                        Log.d("**************", tripPlan.getSchedule(viewPager2.getCurrentItem()).size() + "");

                        List<Address> addresses = null;
                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(lat, lon, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (!addresses.isEmpty()) {
                            Address a = addresses.get(0);
                            // Log.d("@@@@@@@@@@@@@@@@@@2", a.getAddressLine(0) + " " + a.getLocality() + " " + a.getCountryName() + " " + a.getPostalCode() + " " + a.getFeatureName());
                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, tripPlan, marker.getTitle(), a.getAddressLine(0), viewPager2.getCurrentItem());
                            bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheet");
                        } else {
                            Toast.makeText(context, "주소 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });
            }
        });

    }

    public void showPlaceInformation(LatLng location, int placeType) {
        mMap.clear();
        if (previous_marker != null) {
            previous_marker.clear();
        }

        String place = PlaceType.RESTAURANT;
        switch (placeType) {
            case 0:
                place = PlaceType.RESTAURANT;
                break;
            case 1:
                place = PlaceType.CAFE;
                break;
            case 2:
                place = PlaceType.BAKERY;
                break;
            case 3:
                place = PlaceType.LODGING;
                break;
            case 4:
                place = PlaceType.BAR;
        }
      
        new NRPlaces.Builder().listener(DetailTripActivity.this).key("AIzaSyCxJ6k5-LEXvvUQiHz0P5NNlv_WPbnx6iI")
                .latlng(location.latitude, location.longitude).radius(5000)
                .type(place).build().execute();
    }

    public static void drawPath(LatLng startLatLng, LatLng endLatLng, int day){        //polyline을 그려주는 메소드
        PolylineOptions options = new PolylineOptions().add(startLatLng).add(endLatLng).width(15).color(Color.RED).geodesic(true);
        polylines[day].add(mMap.addPolyline(options));
    }



    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(final List<Place> places) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (noman.googleplaces.Place place : places) {
                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());

                    BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.location);
                    Bitmap b = bitmapDrawable.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 80, 80, false);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                    Marker item = mMap.addMarker(markerOptions);
                    previous_marker.add(item);
                }
            }

        });
    }

    @Override
    public void onPlacesFinished() {

    }
}
