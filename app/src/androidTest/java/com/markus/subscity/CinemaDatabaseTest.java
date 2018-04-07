package com.markus.subscity;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.markus.subscity.api.entities.cinema.Cinema;
import com.markus.subscity.api.entities.cinema.Location;
import com.markus.subscity.db.DatabaseClient;
import com.markus.subscity.db.dao.CinemaDao;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;

import static junit.framework.Assert.assertTrue;

/**
 * @author Vitaliy Markus
 */
@RunWith(AndroidJUnit4.class)
public class CinemaDatabaseTest {

    @Test
    public void cinemaTest() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        DatabaseClient database = Room.databaseBuilder(appContext, DatabaseClient.class, "subs_city").build();
        final CinemaDao cinemaDao = database.getCinemaDao();
        final List<Cinema> cinemas = getCinemas();

        Boolean result = Completable.fromAction(() -> cinemaDao.saveCinemas(cinemas))
                .andThen(cinemaDao.getAllCinemas())
                .map(cinemas::equals)
                .blockingFirst();
        assertTrue(result);
    }

    private List<Cinema> getCinemas() {
        Cinema cinema1 = new Cinema(
                1,
                new Location("1", listOf("1", "11", "111"), 1.1, 11.11),
                listOf(1L, 11L, 111L),
                3,
                "1,11,111",
                listOf("1", "11", "111"),
                listOf("1", "11", "111")
        );
        Cinema cinema2 = new Cinema(
                2,
                new Location("2", listOf("2", "22", "222"), 2.2, 22.22),
                listOf(2L, 22L, 222L),
                3,
                "2,22,222",
                listOf("2", "22", "222"),
                listOf("2", "22", "222")
        );
        Cinema cinema3 = new Cinema(
                3,
                new Location("3", listOf("3", "33", "333"), 3.3, 33.33),
                listOf(3L, 33L, 333L),
                3,
                "3,33,333",
                listOf("3", "33", "333"),
                listOf("3", "33", "333")
        );
        Cinema cinema4 = new Cinema(
                4,
                new Location("4", listOf("4", "44", "444"), 4.4, 44.44),
                listOf(4L, 44L, 444L),
                3,
                "4,44,444",
                listOf("4", "44", "444"),
                listOf("4", "44", "444")
        );
        return listOf(cinema1, cinema2, cinema3, cinema4);
    }

    private <T> List<T> listOf(T... array) {
        return Arrays.asList(array);
    }
}
