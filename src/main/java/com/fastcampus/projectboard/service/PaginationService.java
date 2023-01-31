package com.fastcampus.projectboard.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPageNumber) {
        int startNumber = Math.max(0, currentPageNumber - BAR_LENGTH / 2);
        int endNumber = Math.min(totalPageNumber, startNumber + BAR_LENGTH);
        if(endNumber - startNumber < BAR_LENGTH) {
            if(startNumber == 0) {
                endNumber = Math.min(totalPageNumber, BAR_LENGTH);
            } else {
                startNumber = Math.max(0, totalPageNumber - BAR_LENGTH);
            }
        }

        return IntStream.range(startNumber, endNumber)
                .boxed()
                .toList();
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }

}
