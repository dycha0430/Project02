package com.example.madcamp_project2.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.ActivityDetailTripBinding;
import com.example.madcamp_project2.ui.TripPlan;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class DetailTripActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDetailTripBinding binding;
    TripPlan tripPlan;

    TextView titleTextView;
    Button datePickerBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        Intent intent = getIntent();
        this.tripPlan = (TripPlan) intent.getSerializableExtra("tripPlan");

        titleTextView = findViewById(R.id.detailTitleTextView);
        datePickerBtn = findViewById(R.id.datePickerButton);

        titleTextView.setText(tripPlan.getTitle());
        datePickerBtn.setText(tripPlan.getStart_date() + " ~ " + tripPlan.getEnd_date());

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showDatePicker(View view) {
//        DialogFragment newFragment = new DatePickerFragment();
//        newFragment.show(getSupportFragmentManager(),"datePicker");

        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builderRange = MaterialDatePicker.Builder.dateRangePicker();
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();

        LocalDateTime min = LocalDateTime.now();
//        CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(min.get);
        CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(System.currentTimeMillis());
        ArrayList<CalendarConstraints.DateValidator> listValidators =
                new ArrayList<CalendarConstraints.DateValidator>();

        listValidators.add(dateValidatorMin);
        CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
        constraintsBuilderRange.setValidator(validators);

        builderRange.setCalendarConstraints(constraintsBuilderRange.build());
        MaterialDatePicker<Pair<Long, Long>> pickerRange = builderRange.build();
        pickerRange.show(getSupportFragmentManager(), pickerRange.toString());
    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (year_string + "/" + month_string + "/" + day_string);

        datePickerBtn.setText(dateMessage);

        // TODO 저장하기

        Toast.makeText(this,"Date: "+dateMessage,Toast.LENGTH_SHORT).show();
    }
}
