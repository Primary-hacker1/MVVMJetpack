package com.justsafe.libview.nav;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

/**
 * 导航器优化(fragment保活)
 */
@Navigator.Name("hold_state_fragment")
public class HoldStateNavigator extends FragmentNavigator {
    private final Context context;
    private final FragmentManager manager;
    private final int containerId;

    public HoldStateNavigator(@NonNull Context context, @NonNull FragmentManager manager, int containerId) {
        super(context, manager, containerId);
        this.context = context;
        this.manager = manager;
        this.containerId = containerId;
    }

    @Nullable
    @Override
    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {
        String tag = String.valueOf(destination.getId());
        FragmentTransaction transaction = manager.beginTransaction();
        boolean initialNavigate = false;
        Fragment currentFragment = manager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
//            transaction.detach(currentFragment);
            transaction.hide(currentFragment);
        } else {
            initialNavigate = true;
        }
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment == null) {
            String className = destination.getClassName();
            fragment = manager.getFragmentFactory().instantiate(context.getClassLoader(), className);
            transaction.add(containerId, fragment, tag);
        } else {
//            transaction.attach(fragment);
            transaction.show(fragment);
        }

        transaction.setPrimaryNavigationFragment(fragment);
        transaction.setReorderingAllowed(true);
        transaction.commitNow();
        return initialNavigate ? destination : null;
    }
}
