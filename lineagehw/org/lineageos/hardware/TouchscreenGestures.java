/*
 * Copyright (C) 2016 The CyanogenMod Project
 *           (C) 2017 The LineageOS Project
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

package org.lineageos.hardware;

import lineageos.hardware.TouchscreenGesture;

import org.lineageos.internal.util.FileUtils;

/**
 * Touchscreen gestures API
 *
 * A device may implement several touchscreen gestures for use while
 * the display is turned off, such as drawing alphabets and shapes.
 * These gestures can be interpreted by userspace to activate certain
 * actions and launch certain apps, such as to skip music tracks,
 * to turn on the flashlight, or to launch the camera app.
 *
 * This *should always* be supported by the hardware directly.
 * A lot of recent touch controllers have a firmware option for this.
 *
 * This API provides support for enumerating the gestures
 * supported by the touchscreen.
 */
public class TouchscreenGestures {

    private static final String[] GESTURE_PATHS = {
        "/proc/touchpanel/double_swipe_enable",
        "/proc/touchpanel/up_arrow_enable",
        "/proc/touchpanel/right_arrow_enable",
        "/proc/touchpanel/down_arrow_enable",
        "/proc/touchpanel/left_arrow_enable",
        "/proc/touchpanel/up_swipe_enable",
        "/proc/touchpanel/right_swipe_enable",
        "/proc/touchpanel/down_swipe_enable",
        "/proc/touchpanel/left_swipe_enable",
        "/proc/touchpanel/letter_m_enable",
        "/proc/touchpanel/letter_o_enable",
        "/proc/touchpanel/letter_s_enable",
        "/proc/touchpanel/letter_w_enable",
    };

    // Id, name, keycode
    private static final TouchscreenGesture[] TOUCHSCREEN_GESTURES = {
        new TouchscreenGesture(0, "Two fingers down swipe", 251),
        new TouchscreenGesture(1, "Up arrow", 252),
        new TouchscreenGesture(2, "Right arrow", 254),
        new TouchscreenGesture(3, "Down arrow", 255),
        new TouchscreenGesture(4, "Left arrow", 253),
        new TouchscreenGesture(5, "One finger up swipe", 66),
        new TouchscreenGesture(6, "One finger right swipe", 65),
        new TouchscreenGesture(7, "One finger down swipe", 64),
        new TouchscreenGesture(8, "One finger left swipe", 63),
        new TouchscreenGesture(9, "Letter M", 247),
        new TouchscreenGesture(10, "Letter O", 250),
        new TouchscreenGesture(11, "Letter S", 248),
        new TouchscreenGesture(12, "Letter W", 246),
    };

    /**
     * Whether device supports touchscreen gestures
     *
     * @return boolean Supported devices must return always true
     */
    public static boolean isSupported() {
        for (String path : GESTURE_PATHS) {
            if (!FileUtils.isFileWritable(path) ||
                    !FileUtils.isFileReadable(path)) {
                return false;
            }
        }
        return true;
    }

    /*
     * Get the list of available gestures. A mode has an integer
     * identifier and a string name.
     *
     * It is the responsibility of the upper layers to
     * map the name to a human-readable format or perform translation.
     */
    public static TouchscreenGesture[] getAvailableGestures() {
        return TOUCHSCREEN_GESTURES;
    }

    /**
     * This method allows to set the activation status of a gesture
     *
     * @param gesture The gesture to be activated
     *        state   The new activation status of the gesture
     * @return boolean Must be false if gesture is not supported
     *         or the operation failed; true in any other case.
     */
    public static boolean setGestureEnabled(
            final TouchscreenGesture gesture, final boolean state) {
        final String stateStr = state ? "1" : "0";
        return FileUtils.writeLine(GESTURE_PATHS[gesture.id], stateStr);
    }
}