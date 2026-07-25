package com.github.nightdev.kitEngine.utils;

import java.util.ArrayList;
import java.util.List;

public class CommandUtils {
    public static List<String> suggestions(String input, List<String> arguments) {
        List<String> e = new ArrayList<>();
        for (String arg : arguments) {
            if (arg.toLowerCase().startsWith(input.toLowerCase())) e.add(arg);
        }
        return e;
    }
}
