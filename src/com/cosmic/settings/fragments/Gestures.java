/*
 * Copyright (C) 2017 Cosmic-OS Project
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

package com.cosmic.settings.fragments;

import android.content.ContentResolver;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.support.v7.preference.Preference;
import android.support.v14.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.gestures.GestureSettings;

public class Gestures extends GestureSettings {
    private static final String TAG = "Gestures";
    private static final String DOUBLE_TAP_STATUS_BAR_TO_SLEEP = "double_tap_sleep_gesture";
    private static final String DOUBLE_TAP_LOCK_SCREEN_TO_SLEEP = "double_tap_sleep_anywhere";
    private static final String SWIPE_FP_DISMISS = "gesture_swipe_dismiss_fingerprint";

    private ContentResolver mContentResolver;
    private SwitchPreference mDoubleTapStatusBarToSleep;
    private SwitchPreference mDoubleTapLockScreenToSleep;
    private SwitchPreference mFpSwipeDismiss;

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.GALAXY;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        mContentResolver = getActivity().getContentResolver();
        mDoubleTapStatusBarToSleep = (SwitchPreference) findPreference(DOUBLE_TAP_STATUS_BAR_TO_SLEEP);
        mDoubleTapLockScreenToSleep = (SwitchPreference) findPreference(DOUBLE_TAP_LOCK_SCREEN_TO_SLEEP);
        mFpSwipeDismiss = (SwitchPreference) findPreference(SWIPE_FP_DISMISS);
        updatePreferences();
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (mDoubleTapStatusBarToSleep == preference) {
            updateDoubleTapStatusBarToSleep(true);
            return true;
        } else if (mDoubleTapLockScreenToSleep == preference) {
            updateDoubleTapLockScreenToSleep(true);
            return true;
        } else if (mFpSwipeDismiss == preference) {
        	updateFpSwipe(true);
        	return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    private void updateDoubleTapStatusBarToSleep(boolean clicked) {
        if (clicked) {
            Settings.System.putIntForUser(mContentResolver,
                    Settings.System.DOUBLE_TAP_SLEEP_GESTURE,
                    (mDoubleTapStatusBarToSleep.isChecked() ? 1 : 0),
                    UserHandle.USER_CURRENT);
        }
        boolean isDoubleTapStatusBarToSleep = (Settings.System.getIntForUser(
                mContentResolver, Settings.System.DOUBLE_TAP_SLEEP_GESTURE,
                0, UserHandle.USER_CURRENT) == 1);
        mDoubleTapStatusBarToSleep.setChecked(isDoubleTapStatusBarToSleep);
    }

    private void updateDoubleTapLockScreenToSleep(boolean clicked) {
        if (clicked) {
            Settings.System.putIntForUser(mContentResolver,
                    Settings.System.DOUBLE_TAP_SLEEP_ANYWHERE,
                    (mDoubleTapLockScreenToSleep.isChecked() ? 1 : 0),
                    UserHandle.USER_CURRENT);
        }
        boolean isDoubleTapLockScreenToSleep = (Settings.System.getIntForUser(
                mContentResolver, Settings.System.DOUBLE_TAP_SLEEP_ANYWHERE,
                0, UserHandle.USER_CURRENT) == 1);
        mDoubleTapLockScreenToSleep.setChecked(isDoubleTapLockScreenToSleep);
    }
    
    private void updateFpSwipe(boolean clicked) {
        if (clicked) {
            Settings.Secure.putInt(mContentResolver,
                    Settings.Secure.FP_SWIPE_TO_DISMISS_NOTIFICATIONS,
                    (mFpSwipeDismiss.isChecked() ? 1 : 0));
        }
        boolean isFpSwipeDismiss = (Settings.Secure.getInt(
                mContentResolver, Settings.Secure.FP_SWIPE_TO_DISMISS_NOTIFICATIONS,
                0) == 1);
        mFpSwipeDismiss.setChecked(isFpSwipeDismiss);
    }

    private void updatePreferences() {
        updateDoubleTapStatusBarToSleep(false);
        updateDoubleTapLockScreenToSleep(false);
        updateFpSwipe(false);
    }
}
