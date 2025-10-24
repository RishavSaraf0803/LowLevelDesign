package Multithreading.BusyWaitingAndThreadSignaling;

public class TickingBoard {
    private int number;
    TickingBoard(int number){
        this.number = number;
    }

    public boolean isMyTurn(Customer customer){
        return customer.getNumber() == number;
    }

    public void incrementNumber(){
        number++;
    }

    public int getNumber(){
        return number;
    }
}
