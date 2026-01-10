package Multithreading.Challenges.FizzBuzz;

public class Main {
 public static int curr = 1;
 public volatile static  int limit = 30;
    public static void main(String[] args){
        Object lock = new Object();
        Fizz fizz = new Fizz(lock);
        Buzz buzz = new Buzz(lock);
        Plain plain = new Plain(lock);
        FizzBuzz fizzBuzz = new FizzBuzz(lock);
        Thread t2 = new Thread(fizz);
        Thread t3 = new Thread(buzz);
        Thread t4 = new Thread(plain);
        Thread t5 = new Thread(fizzBuzz);
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        

        try {
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();

    }
    
}}
