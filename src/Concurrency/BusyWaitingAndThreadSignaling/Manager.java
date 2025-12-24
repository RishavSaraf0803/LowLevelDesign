package Multithreading.BusyWaitingAndThreadSignaling;

public class Manager implements Runnable{

    private TickingBoard tickingBoard;
    Manager(TickingBoard tickingBoard){
        this.tickingBoard = tickingBoard;
    }
    public void run(){
        while(true){
        synchronized(tickingBoard){
            System.out.println("Manager is working");
                tickingBoard.incrementNumber();
                tickingBoard.notifyAll();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

