package Multithreading.Challenges.SumOfElements;

public class SumOfElementsInArray {

    static volatile int sum = 0;
    static int[] arr = {1, 2, 3, 4, 5};
    private static int curr = 0;

    public static int pluck(){
        if(curr == arr.length){
            return -1;
        }
        return arr[curr++];
    }
    public static void main(String[] args) {
        Thread t1 = new Thread(new Worker("Worker 1"));
        Thread t2 = new Thread(new Worker("Worker 2"));
        Thread t3 = new Thread(new Worker("Worker 3"));
         t1.start();
        t2.start();
        t3.start();
        try {
             t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Sum of elements in array: " + sum);
    }
}

class Worker implements Runnable{
private final String name;
public Worker(String name){
    this.name = name;
}
    public void run(){
        while(true){
             synchronized(SumOfElementsInArray.class){
                int val = SumOfElementsInArray.pluck();
                if(val != -1){
                
                SumOfElementsInArray.sum += val;
                System.out.println("Worker " + name + " added " +val + " to sum");
                }
                else{
                    break;
                }
             }
        }
    }
}


    