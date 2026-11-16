import java.util.*;
 class MySemaphore {
    private int permits;
    public MySemaphore(int permits) { //controls how many threads may enter at the same time.
        this.permits = permits;
    }
    public synchronized void acquire() {  // synchronized = one thread at each time
        while (permits == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        permits--;
    }
    public synchronized void release() {
        permits++;
        notifyAll();
    }
}

class Student extends Thread {  //extends Thread=makes every Student can run in parallel here(only 4) at time
    int id;
    int rounds;
    Student(int id, int rounds) {
        this.id = id;
        this.rounds = rounds;
    }

    public void run() {  // .start
 
        for (int i = 0; i < rounds; i++) {
          Main.pool.acquire();  //take one pc
            Main.mutex.acquire(); // just tell noone can take the pc in use untill it finishes
            Main.busy++; //tells us how many pcs in use 
            if (Main.busy > Main.maxbusy ){
                  Main.maxbusy = Main.busy;

            }
             Main.uses[id - 1]++;
            Main.total++;
            Main.mutex.release();
            try{
              Thread.sleep(20); //each student uses the computer for 20 ms in every round
            }
            catch (InterruptedException e){
            }
             Main.mutex.acquire(); //
            Main.busy--;          // these 3 tells that i the student finishes using a pc so he tell us its free now
            Main.mutex.release(); //
            Main.pool.release(); //the student leaves 'and another one enter'
        }
    }
}
public class Main {
static MySemaphore pool = new MySemaphore(4);
static MySemaphore mutex = new MySemaphore(1);
static int busy = 0;
static int maxbusy = 0;
static int total = 0;
static int[] uses = new int[9];
public static void main(String[] args)
 throws InterruptedException {
  System.out.println("==================================================");
  System.out.println(" NAME:JONATHAN MICHAEL    NO:14     CLASS:4TI");
  System.out.println("==================================================");
    Student[] students = new Student[9];
    int[] rounds = {3, 4, 2, 5, 3, 4, 2, 3, 4};
        for (int i = 1; i <= 9; i++) {
            students[i - 1] = new Student(i ,rounds[i-1]);
        }
        for (int i = 0; i < 9; i++) {
            students[i].start();
        }
        for (int i = 0; i < 9; i++) {
            students[i].join();
        }
        System.out.println("Total uses: " + total);


        for (int i = 0; i < 9; i++) {
            System.out.println("Student " + (i+1) + ": " + uses[i] + " uses");
        }

        System.out.println("Most in use at the same time: " + maxbusy);
     



}
}