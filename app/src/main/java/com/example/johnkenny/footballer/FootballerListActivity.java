package com.example.johnkenny.footballer;

import android.support.v4.app.Fragment;

/**
 * Created by johnkenny on 26/11/2016.
 */
public class FootballerListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment()
    {

        return new FootballerListFragment();
    }

}
