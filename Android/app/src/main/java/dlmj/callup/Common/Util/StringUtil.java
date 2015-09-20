package dlmj.callup.Common.Util;

/**
 * Created by Two on 15/9/10.
 */
public class StringUtil {
    public static int[] StringToIntArray(String string) {
        int num[] = new int[string.length()];
        String temp;
        for(int i = 0;i < string.length(); i++) {
            temp = "" + string.charAt(i);
            num[i] = Integer.parseInt(temp);
        }
        return num;
    }
}
