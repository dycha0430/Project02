package com.example.madcamp_project2.ui;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Country implements Serializable {
    String name;
    // Icon Image

    public Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
/*
public enum Country {
    SEOUL {
        @NonNull
        @Override
        public String toString() {
            return "서울";
        }
    },
    INCHEON {
        @NonNull
        @Override
        public String toString() {
            return "인천";
        }
    },
    BUSAN {
        @NonNull
        @Override
        public String toString() {
            return "부산";
        }
    },
    JEJU {
        @NonNull
        @Override
        public String toString() {
            return "제주";
        }
    },
    GYEONGGI {
        @NonNull
        @Override
        public String toString() {
            return "경기";
        }
    },
    POHANG {
        @NonNull
        @Override
        public String toString() {
            return "포항";
        }
    },
    GANGNEUNG {
        @NonNull
        @Override
        public String toString() {
            return "강릉";
        }
    },
    SOKCHO {
        @NonNull
        @Override
        public String toString() {
            return "속초";
        }
    },
    DAEGU {
        @NonNull
        @Override
        public String toString() {
            return "대구";
        }
    },
    GYEONGJU {
        @NonNull
        @Override
        public String toString() {
            return "경주";
        }
    },
    YEOSU {
        @NonNull
        @Override
        public String toString() {
            return "여수";
        }
    },
    JEONJU {
        @NonNull
        @Override
        public String toString() {
            return "전주";
        }
    },
    CHUNCHEON {
        @NonNull
        @Override
        public String toString() {
            return "춘천";
        }
    },
    DAEJEON {
        @NonNull
        @Override
        public String toString() {
            return "대전";
        }
    }
}
 */