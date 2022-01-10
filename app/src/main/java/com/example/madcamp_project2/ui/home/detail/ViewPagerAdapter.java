package com.example.madcamp_project2.ui.home.detail;

import static com.example.madcamp_project2.ui.home.detail.DetailTripActivity.getFromLocationName;
import static com.example.madcamp_project2.ui.home.detail.DetailTripActivity.mCurrentMarker;
import static com.example.madcamp_project2.ui.home.detail.DetailTripActivity.placeTypeIndex;
import static com.example.madcamp_project2.ui.home.detail.DetailTripActivity.polylines;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.StringPrepParseException;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.Schedule;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.home.ScheduleAdapter;
import com.example.madcamp_project2.ui.home.ScheduleComparator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolderPage> {

    static Context context;
    ArrayList<Schedule>[] schedules;
    int days;
    Date start_date;
    public static ScheduleAdapter[] scheduleAdapters;
    TripPlan tripPlan;
    static final int zoom_pm = 15;

    public void setTripPlan(TripPlan tripPlan) {
        this.tripPlan = tripPlan;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    ViewPagerAdapter(Context context, TripPlan tripPlan) {
        this.context = context;

        ArrayList<Schedule>[] schedules =tripPlan.getSchedules();
        this.schedules = schedules;

        days = tripPlan.getDuration();
        start_date = tripPlan.getStart_date();
        this.tripPlan = tripPlan;

        scheduleAdapters = new ScheduleAdapter[days];
        for (int i = 0; i < days; i++) {
            scheduleAdapters[i] = new ScheduleAdapter(context, schedules[i], i, tripPlan);
        }
    }

    @NonNull
    @Override
    public ViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewpager, parent, false);
        return new ViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPage holder, int position) {
        ArrayList<Schedule> schedule = schedules[position];

        Calendar cal = Calendar.getInstance();
        cal.setTime(start_date);
        cal.add(Calendar.DATE, position);

        Log.d("%%%****", position + " ");

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd (E)");
        holder.dateTextView.setText(sdf.format(cal.getTime()));
        holder.recyclerView.setAdapter(scheduleAdapters[position]);
        Collections.sort(schedule, new ScheduleComparator());
        holder.day = position;

        int startIdx = 0;
        for (startIdx = 0; startIdx < schedule.size(); startIdx++) {
            Schedule thisSchedule = schedule.get(startIdx);
            LatLng latLng = getFromLocationName(thisSchedule.getPlace().getAddress());
            if (latLng != null) break;
        }

        Log.d("@@@@@@@@@@@@@2", schedule.size() + " " + startIdx);

        if (schedule.size() > startIdx) {
            Schedule beforeSchedule = schedule.get(startIdx);
            for (int i = startIdx + 1; i < schedule.size(); i++) {
                Schedule thisSchedule = schedule.get(i);
                LatLng startLatLng = getFromLocationName(beforeSchedule.getPlace().getAddress());
                LatLng endLatLng = getFromLocationName(thisSchedule.getPlace().getAddress());
                if (endLatLng != null) {
                    holder.drawPath(startLatLng, endLatLng, position);
                    beforeSchedule = thisSchedule;
                }
            }
        }

        holder.moveMap(tripPlan.getDestination().getName(), tripPlan.getDestination().getName());
        holder.showPlaceInformation(getFromLocationName(tripPlan.getDestination().getName()), placeTypeIndex);

        holder.addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBottomSheetDialog addBottomSheetDialog = new AddBottomSheetDialog(context, tripPlan, position);
                addBottomSheetDialog.show(((FragmentActivity)context).getSupportFragmentManager(), "bottomSheet");
            }
        });

        holder.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO 데이터베이스에서 새로 스케줄 받아오는 것도 해야함
                Collections.sort(schedule, new ScheduleComparator());
                scheduleAdapters[position].setSchedules(schedule);
                scheduleAdapters[position].notifyDataSetChanged();
                holder.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return schedules.length;
    }

    public static class ViewHolderPage extends RecyclerView.ViewHolder implements OnMapReadyCallback, PlacesListener {

        TextView dateTextView;
        RecyclerView recyclerView;
        Button addScheduleBtn;
        SwipeRefreshLayout swipeRefreshLayout;
        GoogleMap mMap;
        int day;
        List<Marker> previous_marker = null;
        DetailTripActivity.ServiceHandler serviceHandler;

        public ViewHolderPage(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.schedule_date_text_view);
            recyclerView = itemView.findViewById(R.id.schedule_recycler_view);
            addScheduleBtn = itemView.findViewById(R.id.schedule_add_btn);
            swipeRefreshLayout = itemView.findViewById(R.id.swipe_layout);

            SupportMapFragment mapFragment = (SupportMapFragment) ((AppCompatActivity)context).getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            previous_marker = new ArrayList<>();
            day = 0;
            serviceHandler = new DetailTripActivity.ServiceHandler();
        }


        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap = googleMap;
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
                                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, tripPlan, marker.getTitle(), a.getAddressLine(0), day);
                                bottomSheetDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "bottomSheet");
                            } else {
                                Toast.makeText(context, "주소 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }

                            return true;
                        }
                    });
                }
            });

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
            new NRPlaces.Builder().listener(ViewHolderPage.this).key("AIzaSyCxJ6k5-LEXvvUQiHz0P5NNlv_WPbnx6iI")
                    .latlng(location.latitude, location.longitude).radius(5000)
                    .type(place).build().execute();
        }

        public void drawPath(LatLng startLatLng, LatLng endLatLng, int day){        //polyline을 그려주는 메소드
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
            serviceHandler.run();
        }

        @Override
        public void onPlacesFinished() {

        }
    }






}
