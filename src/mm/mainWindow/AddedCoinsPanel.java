package mm.mainWindow;

import com.lucadev.coinmarketcap.model.CoinMarket;
import mm.coinCap.api.CoinCollector;
import mm.customObjects.Colors;

import javax.swing.*;
import java.util.Map;

// All bought coins will appear here
public class AddedCoinsPanel extends JPanel implements Runnable {
    private CoinCollector coinCollector;
    public AddedCoinsPanel(CoinCollector coinCollector, Colors colors){
        this.coinCollector = coinCollector;
        setLayout(null);
        setBackground(colors.white);

    }

    private void addComponents(){
        Map<String,CoinMarket> markets = coinCollector.getMarkets();
        for(String coin : markets.keySet()){

        }
    }

    @Override
    public void run() {

    }
}
