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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.widget.ViewPager2;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.ActivityDetailTripBinding;
import com.example.madcamp_project2.ui.Country;
import com.example.madcamp_project2.ui.CountryEnum;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.TripState;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class DetailTripActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, PlacesListener {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDetailTripBinding binding;
    TripPlan tripPlan;
    static final int zoom_pm = 15;

    TextView titleTextView, stateTextView;
    Button datePickerBtn;
    private MaterialViewPager mViewPager;
    private GoogleMap mMap;
    List<Marker> previous_marker = null;

    Spinner spinner;
    static final int COUNTRY_NUM = 14;
    public Country[] COUNTRIES = new Country[COUNTRY_NUM];

    final Geocoder geoCoder = new Geocoder(this);
    Context context;

    ViewPager2 viewPager2;

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

    public LatLng getFromLocationName(String name) {
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

    void initCountries() {
        COUNTRIES[0] = new Country(CountryEnum.SEOUL);
        COUNTRIES[1] = new Country(CountryEnum.INCHEON);
        COUNTRIES[2] = new Country(CountryEnum.BUSAN);
        COUNTRIES[3] = new Country(CountryEnum.JEJU);
        COUNTRIES[4] = new Country(CountryEnum.GYEONGGI);
        COUNTRIES[5] = new Country(CountryEnum.POHANG);
        COUNTRIES[6] = new Country(CountryEnum.GANGNEUNG);
        COUNTRIES[7] = new Country(CountryEnum.SOKCHO);
        COUNTRIES[8] = new Country(CountryEnum.DAEGU);
        COUNTRIES[9] = new Country(CountryEnum.GYEONGJU);
        COUNTRIES[10] = new Country(CountryEnum.YEOSU);
        COUNTRIES[11] = new Country(CountryEnum.JEONJU);
        COUNTRIES[12] = new Country(CountryEnum.CHUNCHEON);
        COUNTRIES[13] = new Country(CountryEnum.DAEJEON);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;
        previous_marker = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().hide();
        Intent intent = getIntent();
        this.tripPlan = (TripPlan) intent.getSerializableExtra("tripPlan");
        initCountries();

        stateTextView = findViewById(R.id.detailStateTextView);
        titleTextView = findViewById(R.id.detailTitleTextView);
        datePickerBtn = findViewById(R.id.datePickerButton);
        spinner = findViewById(R.id.spinner);
        viewPager2 = findViewById(R.id.viewPager);

        /* TODO Schedule View Pager */
        viewPager2.setAdapter(new ViewPagerAdapter(context, tripPlan.getSchedules(), tripPlan.getStart_date(), tripPlan.getEnd_date()));

        SpinnerAdapter adapter = new SpinnerAdapter(this, Arrays.asList(COUNTRIES.clone()));
        spinner.setAdapter(adapter);

        int position = 0;
        for (position = 0; position < COUNTRIES.length; position++) {
            if (COUNTRIES[position].getCountryEnum().toString().equals(tripPlan.getDestination().getCountryEnum().toString())) break;
        }
        spinner.setSelection(position);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO 장소 정보 TripPlan 데이터 변경
                CountryEnum countryName = getCountryEnum(spinner.getItemAtPosition(i).toString());
                tripPlan.setDestination(new Country(countryName));
                moveMap(countryName.toString());
                showPlaceInformation(getFromLocationName(countryName.toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        if (tripPlan.getState() == TripState.BEFORE) { stateTextView.setBackgroundColor(Color.parseColor("#FCBA03")); }
        else if (tripPlan.getState() == TripState.ING) { stateTextView.setBackgroundColor(Color.parseColor("#3842ff")); }
        else { stateTextView.setBackgroundColor(Color.parseColor("#909091")); }
        stateTextView.setText(tripPlan.getState().toString());
        titleTextView.setText(tripPlan.getTitle());

        DateFormat df = new SimpleDateFormat("yy.MM.dd");
        String start_date = df.format(tripPlan.getStart_date());
        String end_date = df.format(tripPlan.getEnd_date());
        datePickerBtn.setText(start_date + " ~ " + end_date);

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showDatePicker(View view) {
        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builderRange = MaterialDatePicker.Builder.dateRangePicker();
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();

        LocalDateTime min = LocalDateTime.now();

        CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(System.currentTimeMillis());
        ArrayList<CalendarConstraints.DateValidator> listValidators =
                new ArrayList<CalendarConstraints.DateValidator>();

        listValidators.add(dateValidatorMin);
        CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
        constraintsBuilderRange.setValidator(validators);

        builderRange.setCalendarConstraints(constraintsBuilderRange.build());
        MaterialDatePicker<Pair<Long, Long>> pickerRange = builderRange.build();
        pickerRange.show(getSupportFragmentManager(), pickerRange.toString());

        pickerRange.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                String start_date = getDate(selection.first, "yy/MM/dd");

                String end_date = getDate(selection.second, "yy/MM/dd");
                String dateMessage = start_date + " ~ " + end_date;

                // TODO 저장 필요
                datePickerBtn.setText(dateMessage);
            }
        });
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

    public void moveMap(String name) {
        LatLng loc = getFromLocationName(name);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom_pm));
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
                showPlaceInformation(getFromLocationName(tripPlan.getDestination().getName()));

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
                        Address a = addresses.get(0);
                        // Log.d("@@@@@@@@@@@@@@@@@@2", a.getAddressLine(0) + " " + a.getLocality() + " " + a.getCountryName() + " " + a.getPostalCode() + " " + a.getFeatureName());
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, tripPlan, marker.getTitle(), a.getAddressLine(0), viewPager2.getCurrentItem());
                        bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheet");
                        return true;
                    }
                });
            }
        });

    }

    public void showPlaceInformation(LatLng location) {
        mMap.clear();
        if (previous_marker != null) {
            previous_marker.clear();
        }


        // TODO 여기서 type 선택하면 뜨는 장소 종류 바꿀 수 있음!!! 레스토랑, 카페, 베이커리...
        new NRPlaces.Builder().listener(DetailTripActivity.this).key("AIzaSyCxJ6k5-LEXvvUQiHz0P5NNlv_WPbnx6iI")
                .latlng(location.latitude, location.longitude).radius(5000)
                .type(PlaceType.RESTAURANT).build().execute();
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
