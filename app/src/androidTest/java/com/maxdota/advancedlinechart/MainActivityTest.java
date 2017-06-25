package com.maxdota.advancedlinechart;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.maxdota.advancedlinechart.linechart.LineChart;
import com.maxdota.advancedlinechart.linechart.LineChartData;
import com.maxdota.advancedlinechart.linechart.LineChartPoint;
import com.maxdota.advancedlinechart.model.Portfolio;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final double COMPARE_PRECISION = 0.000001;
    private static final int LOADING_TIMEOUT = 10000;
    private static final int CHECKING_INTERVAL = 200;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void loadFirebaseData() throws Exception {
        MainActivity mainActivity = rule.getActivity();
        assertEquals("MainActivity", mainActivity.getLocalClassName());

        ArrayList<Portfolio> portfolios = waitUntilPortfoliosAreLoaded(mainActivity);
        assertEquals(3, portfolios.size());
        assertEquals(365, portfolios.get(0).getNavs().size());
        assertEquals(363, portfolios.get(1).getNavs().size());
        assertEquals(214, portfolios.get(2).getNavs().size());
    }

    @Test
    public void displayDaily() throws Exception {
        MainActivity mainActivity = rule.getActivity();
        LineChart lineChart = (LineChart) getPrivateField(MainActivity.class, "mLineChart", mainActivity);
        waitUntilPortfoliosAreLoaded(mainActivity);

        ArrayList<LineChartData> lines = (ArrayList<LineChartData>) getPrivateField(LineChart.class, "mLines", lineChart);
        assertEquals(9837.51504, lines.get(0).getPoints().get(0).getValue(), COMPARE_PRECISION);
        assertEquals(28081.30377, lines.get(0).getPoints().get(364).getValue(), COMPARE_PRECISION);
        assertEquals(3343.478364, lines.get(1).getPoints().get(0).getValue(), COMPARE_PRECISION);
        assertEquals(9604.178836, lines.get(1).getPoints().get(362).getValue(), COMPARE_PRECISION);
        assertEquals(8020.501217, lines.get(2).getPoints().get(0).getValue(), COMPARE_PRECISION);
        assertEquals(13863.76005, lines.get(2).getPoints().get(213).getValue(), COMPARE_PRECISION);
    }

    @Test
    public void displayMonthly() throws Exception {
        MainActivity mainActivity = rule.getActivity();
        assertEquals(mainActivity.getLocalClassName(), "MainActivity");

        LineChart lineChart = (LineChart) getPrivateField(MainActivity.class, "mLineChart", mainActivity);
        onView(withId(R.id.monthly_check)).perform(click());
        waitUntilPortfoliosAreLoaded(mainActivity);

        ArrayList<LineChartData> lines = (ArrayList<LineChartData>) getPrivateField(LineChart.class, "mLines", lineChart);
        List<LineChartPoint> points1 = lines.get(0).getPoints();
        List<LineChartPoint> points2 = lines.get(1).getPoints();
        List<LineChartPoint> points3 = lines.get(2).getPoints();
        assertEquals(12, points1.size());
        assertEquals(12, points2.size());
        assertEquals(7, points3.size());
        assertEquals(10836.20771, points1.get(0).getValue(), COMPARE_PRECISION);
        assertEquals(28081.30377, points1.get(11).getValue(), COMPARE_PRECISION);
        assertEquals(3771.571533, points2.get(0).getValue(), COMPARE_PRECISION);
        assertEquals(0, points2.get(8).getValue(), COMPARE_PRECISION);
        assertEquals(9604.178836, points2.get(11).getValue(), COMPARE_PRECISION);
        assertEquals(8620.210126, points3.get(0).getValue(), COMPARE_PRECISION);
        assertEquals(13863.76005, points3.get(6).getValue(), COMPARE_PRECISION);

        ArrayList<String> labels = (ArrayList<String>) getPrivateField(LineChart.class, "mLabels", lineChart);
        assertEquals(12, labels.size());
        assertEquals("2017-01", labels.get(0));
        assertEquals("2017-07", labels.get(6));
        assertEquals("2017-12", labels.get(11));
    }

    @Test
    public void displayQuarterly() throws Exception {
        MainActivity mainActivity = rule.getActivity();
        assertEquals(mainActivity.getLocalClassName(), "MainActivity");

        LineChart lineChart = (LineChart) getPrivateField(MainActivity.class, "mLineChart", mainActivity);
        onView(withId(R.id.quarterly_check)).perform(click());
        waitUntilPortfoliosAreLoaded(mainActivity);

        ArrayList<LineChartData> lines = (ArrayList<LineChartData>) getPrivateField(LineChart.class, "mLines", lineChart);
        List<LineChartPoint> points1 = lines.get(0).getPoints();
        List<LineChartPoint> points2 = lines.get(1).getPoints();
        List<LineChartPoint> points3 = lines.get(2).getPoints();
        assertEquals(4, points1.size());
        assertEquals(4, points2.size());
        assertEquals(3, points3.size());
        assertEquals(13236.42168, points1.get(0).getValue(), COMPARE_PRECISION);
        assertEquals(28081.30377, points1.get(3).getValue(), COMPARE_PRECISION);
        assertEquals(4675.290286, points2.get(0).getValue(), COMPARE_PRECISION);
        assertEquals(0, points2.get(2).getValue(), COMPARE_PRECISION);
        assertEquals(9604.178836, points2.get(3).getValue(), COMPARE_PRECISION);
        assertEquals(8620.210126, points3.get(0).getValue(), COMPARE_PRECISION);
        assertEquals(13863.76005, points3.get(2).getValue(), COMPARE_PRECISION);

        ArrayList<String> labels = (ArrayList<String>) getPrivateField(LineChart.class, "mLabels", lineChart);
        assertEquals(4, labels.size());
        assertEquals("Quarter 1", labels.get(0));
        assertEquals("Quarter 2", labels.get(1));
        assertEquals("Quarter 3", labels.get(2));
        assertEquals("Quarter 4", labels.get(3));
    }

    private Object getPrivateField(Class<?> privateClass, String fieldName, Object object) throws Exception {
        Field portfoliosField = privateClass.getDeclaredField(fieldName);
        portfoliosField.setAccessible(true);
        return portfoliosField.get(object);
    }

    private ArrayList<Portfolio> waitUntilPortfoliosAreLoaded(MainActivity mainActivity) throws Exception {
        Field portfoliosField = MainActivity.class.getDeclaredField("mPortfolios");
        portfoliosField.setAccessible(true);

        double timeout = 0;
        ArrayList<Portfolio> portfolios = (ArrayList<Portfolio>) portfoliosField.get(mainActivity);
        // wait until portfolios are initialized
        while (portfolios == null) {
            portfolios = (ArrayList<Portfolio>) portfoliosField.get(mainActivity);
            Thread.sleep(CHECKING_INTERVAL);
            timeout += CHECKING_INTERVAL;
            if (timeout > LOADING_TIMEOUT) {
                Assert.fail("Load Firebase data fails (increase loading timeout if Internet is slow)");
                break;
            }
        }
        return portfolios;
    }
}
