package Multithreading.DiningPhilosopherProblem.version_pp;

public class Philosopher implements Runnable{
    private final Chopstick lChopstick,rChopstick;
    private final String name;

    public Philosopher(String name, Chopstick lChopstick, Chopstick rChopstick) {
        this.name = name;
        this.lChopstick = lChopstick;
        this.rChopstick = rChopstick;
    }

    @Override
    public void run() {

        while (true) {
            try{
        System.out.println(this.name +"is thinking");
        // Thread.sleep(1000);
        Chopstick c1 = lChopstick,c2 = rChopstick;
        if(lChopstick.getId() < rChopstick.getId()){
             c1 = lChopstick;
             c2 = rChopstick;
        }
        else{
             c1 = lChopstick;
             c2 = rChopstick;
        }
        synchronized(c1){
            System.out.println(name +" got lock on "+ c1.getId());
            synchronized(c2){
                System.out.println(name + " is eating with " + lChopstick.getId() + " and " + rChopstick.getId());
                Thread.sleep(2000);
            }
        }
        System.out.println(name + "completed Eating");
    }

catch(Exception e){}
}

    }



}
