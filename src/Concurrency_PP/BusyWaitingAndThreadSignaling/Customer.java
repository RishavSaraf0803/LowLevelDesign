package Multithreading.BusyWaitingAndThreadSignaling;

public class Customer implements Runnable{
    private int number;
    TickingBoard tickingBoard;
    Customer(int number, TickingBoard tickingBoard){
        this.number = number;
        this.tickingBoard = tickingBoard;
    }
    public int getNumber(){
        return number;
    }
    public void run(){
        synchronized(tickingBoard){
            while(!tickingBoard.isMyTurn(this)){
                try {
                    tickingBoard.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Customer " + number + " is working");
            tickingBoard.notifyAll();
        }
    }
}
