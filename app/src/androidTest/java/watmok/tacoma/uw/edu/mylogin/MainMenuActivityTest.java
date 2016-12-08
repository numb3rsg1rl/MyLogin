package watmok.tacoma.uw.edu.mylogin;

import android.app.Activity;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * espresso testing for MainMenuActivity, especially the buttons and their intents
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.DEFAULT)
public class MainMenuActivityTest {

    /**
     * always create a MainMenuActivity to work with
     */
    @Rule
    public ActivityTestRule<MainMenuActivity> mActivityRule =
            new ActivityTestRule<MainMenuActivity>(MainMenuActivity.class);

    /**
     * test the hikes near me button
     */
    @Test
    public void testMapsButton() {
        onView(withId(R.id.browse_hike_map_button)).perform(click());
        intended(hasComponent(hasShortClassName(".TrailMapActivity")));
    }

    /**
     * test the ViewAll button
     */
    @Test
    public void testViewAllButton() {
        onView(withId(R.id.view_all_hikes_button)).perform(click());
        intended(hasComponent(hasShortClassName(".HikeActivity")));
    }
    /**
     * test the ViewAll button
     */
    @Test
    public void testViewSavedButton() {
        onView(withId(R.id.view_favorite_hikes_button)).perform(click());
        intended(hasComponent(hasShortClassName(".FavoritesActivity")));
    }
    /**
     * test logout button
     */
    @Test
    public void testLogoutButton() {
        onView(withId(R.id.sign_out_button2)).perform(click());
        onView(withText("Logged Out")).inRoot(withDecorView(not(
                is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}
