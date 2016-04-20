package org.cmucreatelab.tasota.airprototype.uitesting;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import org.cmucreatelab.tasota.airprototype.R;
import org.cmucreatelab.tasota.airprototype.activities.readable_list.ReadableListActivity;
import org.cmucreatelab.tasota.airprototype.helpers.static_classes.Constants;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by mike on 4/20/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ReadableListActivityUITest extends ActivityInstrumentationTestCase2<ReadableListActivity> {

    @Rule
    public ActivityTestRule<ReadableListActivity> activityRule = new ActivityTestRule<>(
            ReadableListActivity.class);


    private void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "ReadableListActivityUITest failed to wait.");
        }
    }


    public ReadableListActivityUITest() {
        super(ReadableListActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    // Test functions


    @Test
    public void testEspressoNavigation() {
        // open menu and click on "Speck Login"
        Espresso.openContextualActionModeOverflowMenu();
        Espresso.onView(ViewMatchers.withText(Matchers.containsString("Speck Login"))).perform(ViewActions.click());

        // enter username, password, then click authenticate
        // TODO assumes logged out; test logged in status later
        Espresso.onView(ViewMatchers.withId(R.id.editTextLoginUsername)).perform(ViewActions.typeText("username"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.editTextLoginPassword)).perform(ViewActions.typeText("password"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin)).perform(ViewActions.click());

        // close Speck login
        Espresso.onView(ViewMatchers.withContentDescription(R.string.abc_action_bar_up_description)).perform(ViewActions.click());

        pause(1000);
    }
}
