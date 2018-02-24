package ir.haeri.landaudit;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class FloorsDialogClass extends Dialog implements View.OnClickListener {

    public FloorsDialogClass(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.floors_dialog);
    }

    @Override
    public void onClick(View view) {

    }
}
