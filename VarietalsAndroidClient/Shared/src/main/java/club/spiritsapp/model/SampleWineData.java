package club.spiritsapp.model;

import java.util.ArrayList;

/**
 * Created by lnanek on 3/1/15.
 */
public class SampleWineData {


    public static ArrayList<Wine> wines = new ArrayList<Wine>();
    static {
        {
            final Wine wine0 = new Wine();
            wine0.name = "2005 CORDORNIU NAPA GRAND RESERVE, SPARKLING";
            wine0.varietalType = "SPARKLING";
            wine0.varietal = "CHARDONNAY";
            wines.add(wine0);
        }

        {
            final Wine wine3 = new Wine();
            wine3.name = "1981 CARTLIDGE & BROWNE WINERY, CHARDONNAY";
            wine3.varietalType = "WHITE";
            wine3.varietal = "CHARDONNAY";
            wines.add(wine3);
        }

        {
            final Wine wine1 = new Wine();
            wine1.name = "2010 JAMIESON RANCH VINEYARDS, COOMBSVILLE, CABERNET SAUVIGNON";
            wine1.varietalType = "RED";
            wine1.varietal = "CABERNET SAUVIGNON";
            wines.add(wine1);
        }

        {
            final Wine wine2 = new Wine();
            wine2.name = "2001 ARTESA VINEYARDS & WINERY, PINOT";
            wine2.varietalType = "RED";
            wine2.varietal = "PINOT NOIR";
            wines.add(wine2);
        }

        {
            final Wine wine4 = new Wine();
            wine4.name = "2006 REYNOLDS FAMILY WINERY, NAUGHTY STICKY";
            wine4.varietalType = "DESSERT";
            wine4.varietal = "CHARDONNAY";
            wines.add(wine4);
        }
    }

}
