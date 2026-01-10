package Multithreading.AlienMethod;

import java.util.List;

public class Movie {

    private final String id;
    private final List<Theater> theatres;

    public Movie(String id, List<Theater> theatres) {
        this.id = id;
        this.theatres = theatres;
    }

    public synchronized void addTheatres(Theater theatre){
        // synchronized (this){  // moved this synchronized block one level down ie inside method to avoid deadlock
            this.theatres.add(theatre);  // alien method // open calls
            System.out.println("Movie thread is adding theater in movie");
        // }
        theatre.addMovie(id);
    }

    public synchronized String getId(){
        return this.id;
    }
}
