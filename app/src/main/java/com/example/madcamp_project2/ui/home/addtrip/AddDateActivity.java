package com.example.madcamp_project2.ui.home.addtrip;

import static com.example.madcamp_project2.ui.home.detail.DetailTripActivity.getDate;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.example.madcamp_project2.ui.Country;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class AddDateActivity extends AppCompatActivity {
    Context context;
    Country country;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        this.country = (Country) intent.getSerializableExtra("place");

        showDatePicker(getCurrentFocus());
        context = this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showDatePicker(View view) {
        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builderRange = MaterialDatePicker.Builder.dateRangePicker();
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();

        LocalDateTime min = LocalDateTime.now();
        min.minusDays(1);

        CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(min.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
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
                // TODO 저장 필요
//                tripPlan.setStart_date(new Date(selection.first));
//                tripPlan.setEnd_date(new Date(selection.second));

                Intent intent = new Intent(context, AddExtraActivity.class);
                intent.putExtra("place", country);
                intent.putExtra("start_date", selection.first);
                intent.putExtra("end_date", selection.second);
                startActivity(intent);
            }
        });
    }
}

