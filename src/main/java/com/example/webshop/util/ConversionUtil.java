package com.example.webshop.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ConversionUtil {

    private ConversionUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> Page<T> getPage(Pageable page, List<T> collection) {
        final int start = (int) page.getOffset();
        final int end = Math.min((start + page.getPageSize()), collection.size());
        return new PageImpl<>(collection.subList(start, end), page, collection.size());
    }
}