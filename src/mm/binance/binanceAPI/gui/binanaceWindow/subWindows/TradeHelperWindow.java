package mm.binance.binanceAPI.gui.binanaceWindow.subWindows;

import mm.binance.binanceAPI.BinanceController;
import mm.customObjects.SideFrame;

public class TradeHelperWindow extends SideFrame implements Runnable{
    private String TAG = "Binance/TradeHelper";
    private BinanceController bc;

    public TradeHelperWindow(String title, int lx, int ly,BinanceController bc) {
        super(title, lx-240, ly-240, 480, 480);
        windowNumber[0]=2;
        this.bc=bc;

        setVisible(true);
        progress[0]=110;
        windowsStates[windowNumber[0]]=true;
    }

    private void makeGui(){

    }




    @Override
    public void run() {
        while (windowsStates[windowNumber[0]]) {
            try {
                checkState(this);
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.err.println(TAG+" > Thread Stopped");
        threadStateToDefault(this);

    }
}
