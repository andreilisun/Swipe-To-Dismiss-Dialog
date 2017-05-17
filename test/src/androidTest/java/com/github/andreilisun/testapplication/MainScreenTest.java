package com.github.andreilisun.testapplication;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MainScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void showDialog() {
        onView(withId(R.id.btn_show_dialog))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(doesNotExist());
        onView(withId(R.id.tv_dialog_title)).check(matches(isDisplayed()));
    }

    @Test
    public void dismissDialog() throws Throwable {
        onView(withId(R.id.btn_show_dialog))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(doesNotExist());

        onView(withId(R.id.tv_dialog_title)).check(matches(isDisplayed()));

        mainActivityTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivityTestRule.getActivity().dismissDialog();
            }
        });

        onView(withId(R.id.tv_dialog_title)).check(doesNotExist());
        onView(withId(R.id.btn_show_dialog)).check(matches(isDisplayed()));
    }
}