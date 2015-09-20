package dlmj.callup.UI.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dlmj.callup.R;

/**
 * Created by Two on 15/8/22.
 */
public class CustomAlertDialog extends Dialog {
    private String mErrorMessage;

    public CustomAlertDialog(Context context, String errorMessage) {
        super(context);
        mErrorMessage = errorMessage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);

        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(okClickListener);
        TextView errorTextView = (TextView)findViewById(R.id.errorTextView);
        errorTextView.setText(mErrorMessage);
    }

    private View.OnClickListener okClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CustomAlertDialog.this.dismiss();
        }
    };
}
