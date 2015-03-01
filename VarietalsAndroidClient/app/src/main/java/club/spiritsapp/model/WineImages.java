package club.spiritsapp.model;

import club.spiritsapp.R;

/**
 * Created by lnanek on 2/28/15.
 */
public class WineImages {

    public static final Integer[] sampleImageResourceIds = new Integer[] {
        R.drawable.ic_white_wine,
        R.drawable.ic_red_wine_checkbox,
        R.drawable.ic_sparkling_wine_checkbox,
        R.drawable.ic_dessert_wine_checkbox,
R.drawable.ic_cabernet,
R.drawable.ic_chardonnay,
R.drawable.ic_dessert_wine,
R.drawable.ic_gewurztraminer,
R.drawable.ic_merlot,
R.drawable.ic_petitesirah,
R.drawable.ic_pinotgrigio,
R.drawable.ic_pinotnoir,
R.drawable.ic_red_wine,
R.drawable.ic_reisling,
R.drawable.ic_sparking_wine,
R.drawable.ic_syrah,
R.drawable.ic_tempranillo,
R.drawable.ic_viognier,
R.drawable.ic_white_wine,

    };

    private static int lastSampleIndexUsed = -1;

    public synchronized static int getNextSampleResourceId() {
        lastSampleIndexUsed++;
        if (lastSampleIndexUsed >= sampleImageResourceIds.length) {
            lastSampleIndexUsed = 0;
        }
        return sampleImageResourceIds[lastSampleIndexUsed];
    }

}
