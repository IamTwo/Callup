package dlmj.callup.Common.Factory;

import android.content.Context;

import dlmj.callup.R;

/**
 * Created by Two on 15/9/25.
 */
public class FrequentFactory {
    private Context mContext;

    public FrequentFactory(Context context) {
        mContext = context;
    }

    public String getDay(int index) {
        switch (index) {
            case 0:
                return "Mon";
            case 1:
                return "Tue";
            case 2:
                return "Wed";
            case 3:
                return "Thur";
            case 4:
                return "Fri";
            case 5:
                return "Sat";
            case 6:
                return "Sun";
            default:
                return "";

        }
    }

    public String get(String frequentStr) {
        int day;
        String frequent = "";
        if (frequentStr.equals("1111111")) {
            return mContext.getString(R.string.everyday);
        }

        if (frequentStr.equals("1111100")) {
            return mContext.getString(R.string.weekdays);
        }

        if (frequentStr.equals("0000011")) {
            return mContext.getString(R.string.weekends);
        }

        for (int i = 0; i < frequentStr.length(); i++) {
            day = Integer.parseInt(frequentStr.charAt(i) + "");
            if (day > 0) {
                frequent += getDay(i) + " ";
            }
        }
        return frequent;
    }
}
