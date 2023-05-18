package io.github.yuazer.zpokebackup.Utils;

import io.github.yuazer.zpokebackup.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YamlUtils {
    public static String getConfigMessage(String path) {
        return Main.getInstance().getConfig().getString(path).replace("&", "§");
    }
    public static int getConfigInt(String path) {
        try {
            return Main.getInstance().getConfig().getInt(path);
        } catch (NullPointerException e){
            return -1;
        }
    }
    public static List<String> getAllFile(String directoryPath, boolean isAddDirectory) {
        List<String> list = new ArrayList<>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (isAddDirectory) {
                    list.add(file.getName());
                }
                list.addAll(getAllFile(file.getName(),isAddDirectory));
            } else {
                list.add(file.getName());
            }
        }
        return list;
    }
}
