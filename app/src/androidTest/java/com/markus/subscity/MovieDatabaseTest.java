package com.markus.subscity;

import androidx.room.Room;
import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.markus.subscity.api.entities.movie.Movie;
import com.markus.subscity.api.entities.movie.Rating;
import com.markus.subscity.api.entities.movie.Ratings;
import com.markus.subscity.api.entities.movie.Screenings;
import com.markus.subscity.api.entities.movie.Titles;
import com.markus.subscity.api.entities.movie.Trailers;
import com.markus.subscity.db.DatabaseClient;
import com.markus.subscity.db.dao.MovieDao;

import org.joda.time.DateTime;
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
public class MovieDatabaseTest {

    @Test
    public void movieTest() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DatabaseClient database = Room.databaseBuilder(appContext, DatabaseClient.class, "subs_city").build();
        final MovieDao movieDao = database.getMovieDao();
        final List<Movie> movies = getMovies();

        Boolean result = Completable.fromAction(() -> movieDao.saveMovies(movies))
                .andThen(movieDao.getAllMovies())
                .map(movies::equals)
                .blockingFirst();
        assertTrue(result);
    }

    private List<Movie> getMovies() {
        Movie movie1 = new Movie(
                1,
                1,
                listOf("1", "11", "111"),
                listOf("1", "11", "111"),
                DateTime.now(),
                "1,11,111",
                listOf("1", "11", "111"),
                1,
                listOf("1", "11", "111"),
                listOf("1", "11", "111"),
                "1,11,111",
                new Ratings(new Rating(11, 12, 13),
                        new Rating(14, 15, 26)),
                new Screenings(1, DateTime.now()),
                new Titles("11", "1111"),
                new Trailers("11", "1111"),
                1);
        Movie movie2 = new Movie(
                2,
                2,
                listOf("2", "22", "222"),
                listOf("2", "22", "222"),
                DateTime.now(),
                "2,22,222",
                listOf("2", "22", "222"),
                2,
                listOf("2", "22", "222"),
                listOf("2", "22", "222"),
                "2,22,222",
                new Ratings(new Rating(21, 22, 23),
                        new Rating(24, 25, 26)),
                new Screenings(2, DateTime.now()),
                new Titles("22", "2222"),
                new Trailers("22", "2222"),
                2);
        Movie movie3 = new Movie(
                3,
                3,
                listOf("3", "33", "333"),
                listOf("3", "33", "333"),
                DateTime.now(),
                "3,33,333",
                listOf("3", "33", "333"),
                3,
                listOf("3", "33", "333"),
                listOf("3", "33", "333"),
                "3,33,333",
                new Ratings(new Rating(31, 32, 33),
                        new Rating(34, 35, 36)),
                new Screenings(3, DateTime.now()),
                new Titles("33", "3333"),
                new Trailers("33", "3333"),
                3);
        Movie movie4 = new Movie(
                4,
                4,
                listOf("4", "44", "444"),
                listOf("4", "44", "444"),
                DateTime.now(),
                "4,44,444",
                listOf("4", "44", "444"),
                4,
                listOf("4", "44", "444"),
                listOf("4", "44", "444"),
                "4,44,444",
                new Ratings(new Rating(41, 42, 43),
                        new Rating(44, 45, 46)),
                new Screenings(4, DateTime.now()),
                new Titles("44", "4444"),
                new Trailers("44", "4444"),
                4);
        return listOf(movie1, movie2, movie3, movie4);
    }

    private <T> List<T> listOf(T... array) {
        return Arrays.asList(array);
    }
}
