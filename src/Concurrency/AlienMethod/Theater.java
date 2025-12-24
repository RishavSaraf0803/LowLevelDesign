package Multithreading.AlienMethod;

import java.util.List;
import java.util.ArrayList;

public class Theater {
    private final List<String> movieIds;

    public Theater() {
        this.movieIds = new ArrayList<>();
    }

    public synchronized void addMovie(String movieId) {
        movieIds.add(movieId);
    }

    public boolean isMovieAvailable(Movie movie){
        boolean result;
        synchronized(this){     // moved this synchronized block from method level to more granular level
            result = this.movieIds.contains(movie.getId());
            System.out.print(result);
        }
        return result;
    }
    
}
