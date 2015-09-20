package dlmj.callup.Common.Factory;

import android.content.Context;
import android.content.res.TypedArray;

import dlmj.callup.R;

/**
 * Created by Two on 15/8/27.
 */
public class BackColorFactory {
    private int mLength;
    private int[] mColorList;

    public BackColorFactory(Context context){
        TypedArray colorArray = context.getResources().obtainTypedArray(R.array.scene_back_color);
        mLength = colorArray.length();
        mColorList = new int[mLength];
        for(int i = 0; i < mLength; i++){
            mColorList[i] = (colorArray.getColor(i, 0));
        }
        colorArray.recycle();
    }

    public int get(int position){
        return mColorList[position % 9];
    }
}
