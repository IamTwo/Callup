package dlmj.callup.BusinessLogic.Handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * Created by Two on 15/9/10.
 */
public class MoveHandler extends Handler {
    private int mStepX = 0;
    private boolean mIsInAnimation = false;
    private View mView;
    private int mFromX;
    private int mToX;

    private final int mDurationStep = 10;
    private final int mDuration = 100;

    @Override
    public void handleMessage(Message msg){
        super.handleMessage(msg);

        if(mStepX == 0) {
            if(mIsInAnimation) {
                return;
            }
            mIsInAnimation = true;
            mView = (View)msg.obj;
            mFromX = msg.arg1;
            mToX = msg.arg2;
            mStepX = (int)((mToX - mFromX) * mDurationStep * 1.0 / mDuration);
            if(mStepX < 0 && mStepX > -1){
                mStepX = -1;
            }else if(mStepX > 0 && mStepX < 1) {
                mStepX = 1;
            }

            if(Math.abs(mToX - mFromX) < 10) {
                mView.scrollTo(mToX, 0);
                animationOver();
                return;
            }
        }

        mFromX += mStepX;
        boolean isLastStep = (mStepX > 0 && mFromX > mToX) ||
                (mStepX < 0 && mFromX < mToX);

        if(isLastStep){
            mFromX = mToX;
        }

        mView.scrollTo(mFromX, 0);
        mView.invalidate();

        if (!isLastStep) {
            this.sendEmptyMessageDelayed(0, mDurationStep);
        } else {
            animationOver();
        }
    }

    private void animationOver() {
        mIsInAnimation = false;
        mStepX = 0;
    }
}
