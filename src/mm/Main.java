package mm;

import mm.loginpkg.LoginWindow;
import mm.startGui.RunTimeInfoWindow;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static ExecutorService workers;
    public static final int[] threadCount = {0};
    public static final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    public static void main(String[] args){
//      code is optimised for CPU with 2 Cores and 2.7GHz per core
        workers = Executors.newFixedThreadPool(10);
        startCountingThreads();
        new LoginWindow("LogIn", dim.width/2, dim.height/2);
//        new MainFrame();

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
                RunTimeInfoWindow rtif = new RunTimeInfoWindow(Toolkit.getDefaultToolkit().getScreenSize().width/2-120,Toolkit.getDefaultToolkit().getScreenSize().height-30);
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
