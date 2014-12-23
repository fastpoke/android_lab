package org.fastpoke.pomf;

import android.widget.TextView;

/**
  Created by fastpoke on 17.11.2014.
**/

public class Random {
    //TODO convert this to superclass
    public static void randomLeft(LeftActivity activity) {
        int rand = new java.util.Random().nextInt(899) + 100;
        activity.tvView = (TextView) activity.findViewById(R.id.tvView);
        activity.tvView.setText(Integer.toString(rand));
    }
    public static void randomRight(RightActivity activity) {
        int rand = new java.util.Random().nextInt(899) + 100;
        activity.tvView = (TextView) activity.findViewById(R.id.tvView);
        activity.tvView.setText(Integer.toString(rand));
    }
}
