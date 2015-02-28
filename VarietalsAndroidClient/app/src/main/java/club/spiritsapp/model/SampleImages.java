package club.spiritsapp.model;

import club.spiritsapp.R;

/**
 * Created by lnanek on 2/28/15.
 */
public class SampleImages {

    public static final Integer[] sampleImageResourceIds = new Integer[] {
            R.drawable.sample_black_stallion,
            R.drawable.sample_jamiesonranchvineyards,
            R.drawable.sample_meal,
            R.drawable.sample_place_settings,
            R.drawable.sample_wine_bottle,
            R.drawable.sample_wine_glass,
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
