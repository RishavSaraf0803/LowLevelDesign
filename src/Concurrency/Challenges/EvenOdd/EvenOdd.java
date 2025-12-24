package Multithreading.Challenges.EvenOdd;

 class EvenThread implements Runnable{
   private final int limit;
   private final String name;
   public EvenThread(int limit,String name){
    this.name = name;
    this.limit = limit;
   }
    public void run(){
        while(true){
    //    while(EvenOdd.curr % 2 == 1){}
       synchronized(EvenOdd.class){
        if(EvenOdd.curr > limit){
            break;
        }
        if(EvenOdd.curr % 2 == 0){
            System.out.println("EvenNumber printed by Even Thread : " + EvenOdd.curr + " by " + name);
            EvenOdd.curr++;
        }
       }
    }
}
 }
    
 class OddThread implements Runnable{
    private final String name;
    public OddThread(int limit,String name){
        this.name = name;
        this.limit = limit;
    }
    private final int limit;

    
    public void run(){
        // synchronized(EvenOdd.class){
        while(true){
    //   while(EvenOdd.curr % 2 == 0){}
         synchronized(EvenOdd.class){
            if(EvenOdd.curr > limit){
                break;
            }
            if(EvenOdd.curr % 2 ==1){
                System.out.println("OddNumber printed by Odd Thread : " + EvenOdd.curr + " by " + name);
                EvenOdd.curr++;
                EvenOdd.class.notifyAll();
                }
            
            else{
                try {
                    EvenOdd.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
      }
    // }
     }


public class EvenOdd{

    public static  int curr = 1;
    public static void main(String[] args) throws InterruptedException {
        int limit = 10;
        Thread evenThread = new Thread(new EvenThread(limit,"EvenThread1"));
        Thread evenThread2 = new Thread(new EvenThread(limit,"EvenThread2"));
        Thread evenThread3 = new Thread(new EvenThread(limit,"EvenThread3"));
        Thread oddThread = new Thread(new OddThread(limit,"OddThread1"));
        Thread oddThread2 = new Thread(new OddThread(limit,"OddThread2"));
        Thread oddThread3 = new Thread(new OddThread(limit,"OddThread3"));
         evenThread.start();
        oddThread.start();
        evenThread2.start();
        oddThread2.start();
        evenThread3.start();
        oddThread3.start();
       
    }

}
