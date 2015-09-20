package dlmj.callup.Common.Model;

/**
 * Created by Two on 15/8/13.
 */
public class Level {
    public int mImageResource;
    public int mStringResource;

    public Level(int imageResource, int stringResource){
        mImageResource = imageResource;
        mStringResource = stringResource;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public int getDescription() {
        return mStringResource;
    }
}
