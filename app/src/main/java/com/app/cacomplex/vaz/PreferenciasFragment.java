package com.app.cacomplex.vaz;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import androidx.annotation.Nullable;

/**
 * Created by Windows 10 on 19/06/2017.
 */

public class PreferenciasFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }


}
