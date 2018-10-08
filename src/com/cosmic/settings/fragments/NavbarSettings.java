/*
 * Copyright (C) 2018 Simplix
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
import android.content.res.Resources;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.Vibrator;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.R;

import com.cosmic.settings.preferences.ActionFragment;
import com.cosmic.settings.preferences.CustomSeekBarPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.hwkeys.ActionConstants;
import com.android.internal.util.hwkeys.ActionUtils;

public class NavbarSettings extends ActionFragment implements OnPreferenceChangeListener {
    
    //Keys
    private static final String NAVBARSHOW = "navigation_bar_show";
    
    private SwitchPreference mNavbarShow;
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.navbar_settings);

        final Resources res = getResources();
        final ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();
        
        final boolean needsNavbar = ActionUtils.hasNavbarByDefault(getActivity());
        mNavbarShow = (SwitchPreference) findPreference(NAVBARSHOW);
        
        boolean checkedvalue;
 		
 		if (needsNavbar)
 		{
 			if (Settings.System.getIntForUser(getContentResolver(),
                   Settings.System.NAVIGATION_BAR_SHOW, 1,
                   UserHandle.USER_CURRENT) == 1) {
                checkedvalue = true;
	        } else {
	        	checkedvalue = false;
	        }
 		} else {
 			if (Settings.System.getIntForUser(getContentResolver(),
                   Settings.System.NAVIGATION_BAR_SHOW, 0,
                   UserHandle.USER_CURRENT) == 1) {
                checkedvalue = true;
	        } else {
	        	checkedvalue = false;
	        }
 		}    
        mNavbarShow.setChecked(checkedvalue);
        mNavbarShow.setOnPreferenceChangeListener(this);
    }
    
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mNavbarShow) {
            boolean value = (Boolean) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                Settings.System.NAVIGATION_BAR_SHOW, value ? 1 : 0,
                UserHandle.USER_CURRENT);
            setActionPreferencesEnabled(!value);
            return true;
        }
        return false;
    }
    
    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.GALAXY;
    }
}
