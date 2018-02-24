package ir.haeri.landaudit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PartButton extends Button implements View.OnClickListener{
    private int _unit_id;
    private int _part_id;
    private Context ctx;
    public PartButton(Context context,int unit_id,int part_id) {
        super(context);
        ctx=context;
        _unit_id=unit_id;
        _part_id=part_id;
        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("coord","PartButton::id="+_part_id);
        FragmentManager fm = ((FragmentActivity)ctx).getSupportFragmentManager();
        PartDetailFragment partDetailFragment = new PartDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("unit_id",_unit_id);
        bundle.putInt("part_id",_part_id);
        partDetailFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.frag_place,partDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}
