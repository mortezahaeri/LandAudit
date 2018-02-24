package ir.haeri.landaudit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class UnitButton extends Button implements View.OnClickListener {

    Context ctx;
    private int _unit_id;
    public UnitButton(Context context,int id) {
        super(context);
        ctx=context;
        _unit_id=id;
        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("coord","UnitButton::id="+_unit_id);
        FragmentManager fm = ((FragmentActivity)ctx).getSupportFragmentManager();
        PartsFragment partsFragment = new PartsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("unit_id",_unit_id);
        partsFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.frag_place,partsFragment)
                .addToBackStack(null)
                .commit();
    }
}
