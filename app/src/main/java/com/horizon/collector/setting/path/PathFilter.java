package com.horizon.collector.setting.path;


import java.io.File;
import java.io.FileFilter;

public class PathFilter implements FileFilter {
    private boolean mShowHidden = false;

    public void setShowHidden(boolean showHidden) {
        mShowHidden = showHidden;
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return (mShowHidden || file.getName().charAt(0) != '.');
        }
        return false;
    }
}
