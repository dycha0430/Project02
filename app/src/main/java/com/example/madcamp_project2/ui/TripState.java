package com.example.madcamp_project2.ui;

import androidx.annotation.NonNull;

public enum TripState {
    BEFORE {
        @NonNull
        @Override
        public String toString() {
            return "여행 준비 중";
        }
    },
    ING {
        @NonNull
        @Override
        public String toString() {
            return "여행 중";
        }
    },
    AFTER {
        @NonNull
        @Override
        public String toString() {
            return "여행 완료";
        }
    }
}
