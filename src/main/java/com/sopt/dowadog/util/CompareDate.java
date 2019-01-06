package com.sopt.dowadog.util;

import com.sopt.dowadog.model.domain.Community;

import java.time.LocalDateTime;
import java.util.Comparator;

public class CompareDate implements Comparator<Community> {
    @Override
    public int compare(Community o1, Community o2) {
        LocalDateTime temp1 = o1.getCreatedAt();
        LocalDateTime temp2 = o2.getCreatedAt();
        if(temp1.isAfter(temp2)){
            return -1;

        }else if(temp1.isEqual(temp2)){
            return  0;

        }else{
            return 1;

        }
    }
}
