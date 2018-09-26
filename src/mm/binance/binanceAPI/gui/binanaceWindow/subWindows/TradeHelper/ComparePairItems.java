package mm.binance.binanceAPI.gui.binanaceWindow.subWindows.TradeHelper;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ComparePairItems implements Comparator<PairItemsPanel> {
//    public int i ;
    public ComparePairItems() {
//        this.i = i;
    }

    @Override
    public int compare(PairItemsPanel o1, PairItemsPanel o2) {
        String line1 = o1.p1.getText();
        String line2 = o2.p1.getText();
        return line1.compareTo(line2);

    }

}
