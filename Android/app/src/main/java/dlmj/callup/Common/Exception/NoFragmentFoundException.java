package dlmj.callup.Common.Exception;
import android.content.Context;

import dlmj.callup.Common.Factory.FragmentFactory;
import dlmj.callup.R;

/**
 * Created by Two on 15/9/15.
 */
public class NoFragmentFoundException extends Exception{
    public NoFragmentFoundException(Context context, FragmentFactory.FragmentName fragmentName) {
        super(String.format(context.getString(R.string.error_fragment_not_found), fragmentName));
    }
}
