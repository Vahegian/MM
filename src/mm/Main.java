package mm;

import mm.mainWindow.Frame;
import mm.startGui.RunTimeInfoWindow;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static ExecutorService workers;
    public static final int[] threadCount = {0};
    public static void main(String[] args){
//      code is optimised for CPU with 2 Cores and 2.7GHz per core
        workers = Executors.newFixedThreadPool(10);
        startCountingThreads();
        new Frame();

    }

    public static void plusThread(){
        synchronized (Main.threadCount) {
            Main.threadCount[0] += 1;
        }
    }

    public static void minusThread(){
        synchronized (Main.threadCount) {
            Main.threadCount[0] -= 1;
        }
    }

    private static void startCountingThreads(){
        workers.execute(new Runnable() {
            @Override
            public void run() {
                threadCount[0]+=1;
                RunTimeInfoWindow rtif = new RunTimeInfoWindow();
                while (true){
                    synchronized (threadCount) {
                        rtif.setInfo("Threads used from the pool: ", threadCount[0]+"");
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
