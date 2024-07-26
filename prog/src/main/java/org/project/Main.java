package org.project;

import org.project.Logic.LogicSettings;
import org.project.Logic.embAsp.MyHandler;
import org.project.UI.View.ProjectView;

public class Main {
    static {
        String osName = System.getProperty("os.name");

        if (osName.contains("Windows")) {
            MyHandler.setRelPathToDLV2(LogicSettings.PATH_TO_DLV2_WINDOWS);
        } else if (osName.contains("Linux")) {
            MyHandler.setRelPathToDLV2(LogicSettings.PATH_TO_DLV2_LINUX);
        } else if (osName.contains("Mac OS X")) {
            MyHandler.setRelPathToDLV2(LogicSettings.PATH_TO_DLV2_MAC);
        } else {
            throw new IllegalArgumentException("Invalid OS");
        }

    }

    public static void main(String[] args) {
        ProjectView.getInstance();
    }
}
