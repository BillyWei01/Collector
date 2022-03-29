package com.horizon.collector.setting.path;


import java.io.File;
import java.util.Comparator;

public class PathComparator implements Comparator<java.io.File> {

    public int compare(File lhs, File rhs) {
        if (lhs == rhs) {
            return 0;
        }
        if (lhs == null) {
            return -1;
        }
        if (rhs == null) {
            return 1;
        }
        return lhs.getPath().compareTo(rhs.getPath());
    }
}
