package me.zeepic.evil.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtil {

    public static String titleCase(String string) {
        String[] split = string.split(" ");
        return Arrays
                .stream(split)
                .map(part ->
                        part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

}
