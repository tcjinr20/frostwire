/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2018, FrostWire(R). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frostwire.light.util;

import com.frostwire.light.Constants;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public final class FrostWireUtils {

    private static final boolean IS_RUNNING_FROM_SOURCE = new File("build.gradle").exists();

    /**
     * Make sure the constructor can never be called.
     */
    private FrostWireUtils() {
    }

    /**
     * Returns the current version number of FrostWire as
     * a string, e.g., "5.2.9".
     */
    public static String getFrostWireVersion() {
        return Constants.FROSTWIRE_VERSION_STRING;
    }

    public static int getBuildNumber() {
        return Constants.FROSTWIRE_BUILD;
    }

    public static boolean isIsRunningFromSource() { return IS_RUNNING_FROM_SOURCE; }

    /**
     * Returns the path of the FrostWire.jar executable.
     * For a windows binary distribution this is the same path as FrostWire.exe since those files live together.
     */
    public static String getFrostWireJarPath() {
        return new File(FrostWireUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
    }

    /**
     * Returns the root folder from which all Saved/Shared/etc..
     * folders should be placed.
     */
    public static File getFrostWireRootFolder() {
        String root = null;

        if (OSUtils.isWindowsVista() || OSUtils.isWindows7()) {
            root = SystemUtils.getSpecialPath(SystemUtils.SpecialLocations.DOWNLOADS);
        } else if (OSUtils.isWindows()) {
            root = SystemUtils.getSpecialPath(SystemUtils.SpecialLocations.DOCUMENTS);
        }

        if (root == null || "".equals(root))
            root = CommonUtils.getUserHomeDir().getPath();

        return new File(root, "FrostWire");
    }

    public static Set<File> getFrostWire4SaveDirectories() {
        Set<File> result = new HashSet<>();

        try {
            File settingFile = new File(CommonUtils.getFrostWire4UserSettingsDir(), "frostwire.props");
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(settingFile);
            props.load(fis);
            IOUtils.closeQuietly(fis);

            if (props.containsKey("DIRECTORY_FOR_SAVING_FILES")) {
                result.add(new File(props.getProperty("DIRECTORY_FOR_SAVING_FILES")));
            }

            String[] types = new String[]{"document", "application", "audio", "video", "image"};

            for (String type : types) {
                String key = "DIRECTORY_FOR_SAVING_" + type + "_FILES";
                if (props.containsKey(key)) {
                    result.add(new File(props.getProperty(key)));
                }
            }

        } catch (Exception e) {
            // ignore
        }

        return result;
    }
}
