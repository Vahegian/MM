package mm;

import mm.mainWindow.Frame;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static ExecutorService workers;
    public static void main(String[] args){
//      code is optimised for CPU with 2 Cores and 2.7GHz per core
        workers = Executors.newFixedThreadPool(10);
        new Frame();
    }
}
