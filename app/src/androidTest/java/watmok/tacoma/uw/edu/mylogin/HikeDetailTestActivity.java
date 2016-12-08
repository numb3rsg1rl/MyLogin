package watmok.tacoma.uw.edu.mylogin;



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
 * espresso testing for Hike Detail Activity, especially the buttons and their intents.
 * Currently not functional because the lack of FavoritesDataBaseAdapter when test is created
 * causes an exception.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HikeDetailTestActivity {

    /**
     * always create a HikeDetail Activity to work with
     */
    @Rule
    public ActivityTestRule<HikeDetailActivity> mActivityRule =
            new ActivityTestRule<HikeDetailActivity>(HikeDetailActivity.class);


    /**
     *Test that the button opens specificTrailMapsActivity
     */
    @Test
    public void testOpenMapButton() {

        onView(withId(R.id.map_button)).perform(click());
        intended(hasComponent(hasShortClassName(".SpecificTrailMapsActivity")));

    }

    /**
     * Tests performance of post message button, review_box editText and submit button
     */
    @Test
    public void testSubmitReview() {
        onView(withId(R.id.post_review_button)).perform(click());
        onView(withId(R.id.review_box)).perform(typeText("Good"));
        onView(withId(R.id.review_box)).check(matches(withText("Good")));
        onView(withId(R.id.submit_button)).perform(click());

        onView(withId(R.id.review_box)).check(matches(withText("")));

        onView(withText("Review Submitted")).inRoot(withDecorView(not(
                is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    /**
     *checks the Logout Menu Item
     */
    @Test
    public void testLogout() {
        onView(withId(R.id.logout_item)).perform(click());

        onView(withText("Logged Out")).inRoot(withDecorView(not(
                is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        intended(hasComponent(hasShortClassName(".MainActivity")));
    }
}
