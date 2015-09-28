package dlmj.callup.UI.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

import dlmj.callup.Common.Interfaces.ChangeVibrationTimeListener;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.R;

/**
 * Created by Two on 15/9/28.
 */
public class CancelBombDialog extends Dialog {
    private final static String TAG = "CancelBombDialog";
    Random mRandom = new Random();
    private TextView mCalculationTextView;
    private EditText mResultEditText;
    private Button mSubmitButton;
    private int mResult = 0;
    private ChangeVibrationTimeListener mChangeVibrationTimeListener;

    public CancelBombDialog(Context context) {
        super(context, R.style.AlertDialog);
    }

    public void setChangeVibrationTimeListener(ChangeVibrationTimeListener changeVibrationTimeListener) {
        mChangeVibrationTimeListener = changeVibrationTimeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_bomb);
        findView();
        setListener();
    }

    private void findView() {
        mCalculationTextView = (TextView) findViewById(R.id.calculationTextView);
        mResultEditText = (EditText) findViewById(R.id.resultEditText);
        mSubmitButton = (Button) findViewById(R.id.submitButton);
    }

    private void setListener() {
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultStr = mResultEditText.getText().toString();
                LogUtil.d(TAG, resultStr);
                try {
                    int result = Integer.parseInt(resultStr);
                    if(result == mResult) {
                        mChangeVibrationTimeListener.closeCancelBombDialog();
                    } else{
                        CustomAlertDialog dialog = new CustomAlertDialog(getContext(),
                                getContext().getString(R.string.error_result));
                        dialog.show();
                    }
                } catch (Exception ex) {
                    CustomAlertDialog dialog = new CustomAlertDialog(getContext(), ex.getMessage());
                    dialog.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        int firstNum = mRandom.nextInt(100);
        int secondNum = mRandom.nextInt(100);
        mResult = firstNum * secondNum;
        mCalculationTextView.setText(firstNum + " * " + secondNum + " = ");
    }
}
