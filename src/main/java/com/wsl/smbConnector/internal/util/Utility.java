package com.wsl.smbConnector.internal.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class Utility {

    private static final ArrayList<String> FLAT_FILES = new ArrayList(Arrays.asList("txt", "csv"));

    private Utility() {
    }

    /**
     * @param oldPath
     * @return path with that only contains / as file separator
     */
    public static String fixPath(String oldPath) {

        oldPath = oldPath.replace("\\", "/");

        StringBuilder sb = new StringBuilder(oldPath.replaceAll("//", "/"));

        if (oldPath.endsWith("/")) {
            sb.replace(sb.length() - 1, sb.length(), "");
        }
        return sb.toString();
    }

    /**
     * @param filePath path to the file
     * @return names of the file parent directory and subdirectories
     */
    public static List<String> listFileParentDirs(String filePath) {
        List<String> dirs = new ArrayList<>();

        String[] subDirs = filePath.split(Pattern.quote(java.io.File.separator));

        for (int i = 0; i < subDirs.length - 1; i++) {
            if (!"".equals(subDirs[i]))
                dirs.add(subDirs[i]);
        }
        return dirs;
    }

    public static String getFilename(String path) {
        String[] split = fixPath(path).split(Pattern.quote(java.io.File.separator));
        return split[split.length - 1];
    }


    public static boolean isFlatFile(String file) {
        return FLAT_FILES.contains(FilenameUtils.getExtension(file));
    }


}
