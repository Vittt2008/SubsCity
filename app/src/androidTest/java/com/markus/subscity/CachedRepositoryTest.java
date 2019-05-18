package com.markus.subscity;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.util.Log;

import com.markus.subscity.providers.CityProvider;
import com.markus.subscity.providers.DatabaseProvider;
import com.markus.subscity.providers.DateTimeProvider;
import com.markus.subscity.providers.PreferencesProvider;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import static com.markus.subscity.TestCachedRepository.FROM_DB;
import static com.markus.subscity.TestCachedRepository.FROM_SERVER;

/**
 * @author Vitaliy Markus
 */
@RunWith(AndroidJUnit4.class)
public class CachedRepositoryTest {

    private static PeriodFormatter periodParser = new PeriodFormatterBuilder().appendDays().appendLiteral(":").appendHours().appendLiteral(":").appendMinutes().toFormatter();

    private static String[] syncTime = {"05:55", "10:05", "14:05", "19:00", "23:00"};

    private static String[] requestTime = {
            "0:00:00", "0:03:00", "0:05:00", "0:06:00", "0:09:00",
            "0:10:00", "0:12:00", "0:18:00", "0:19:00", "0:19:05",
            "0:20:00", "0:22:00", "1:03:00", "1:04:00", "1:06:00"
    };

    private static String[] result = {
            FROM_SERVER, FROM_DB, FROM_DB, FROM_SERVER, FROM_DB,
            FROM_DB, FROM_SERVER, FROM_SERVER, FROM_DB, FROM_SERVER,
            FROM_DB, FROM_DB, FROM_SERVER, FROM_DB, FROM_DB
    };

    @Test
    public void init() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        JodaTimeAndroid.init(appContext);

        TestDateTimeProvider dateTimeProvider = dateTimeProvider();
        TestCachedRepository cachedRepository = cachedRepository(appContext, dateTimeProvider);
        ArrayList<String> resultList = new ArrayList<>();

        cachedRepository.setTestSyncTime(syncTime);
        for (String it : requestTime) {
            DateTime now = calculateCurrentTime(it);
            dateTimeProvider.setNow(now);
            String data = cachedRepository.getData();
            resultList.add(data);
            Log.e("=== " + it + " ===", data);
        }

        String[] resultArray = new String[resultList.size()];
        resultArray = resultList.toArray(resultArray);

        boolean equals = Arrays.equals(resultArray, result);
        Assert.assertTrue(equals);
    }

    private DateTime calculateCurrentTime(String it) {
        DateTime now = DateTime.now().withTimeAtStartOfDay();
        DateTime time = now.plus(Period.parse(it, periodParser));
        return time;
    }

    private TestDateTimeProvider dateTimeProvider() {
        return new TestDateTimeProvider();
    }

    private TestCachedRepository cachedRepository(Context context, DateTimeProvider dateTimeProvider) {
        PreferencesProvider preferencesProvider = new PreferencesProvider(context);
        CityProvider cityProvider = new CityProvider(context, preferencesProvider);
        DatabaseProvider databaseProvider = new DatabaseProvider(context, cityProvider);
        TestCachedRepository cachedRepository = new TestCachedRepository(databaseProvider, dateTimeProvider);
        return cachedRepository;
    }

}
