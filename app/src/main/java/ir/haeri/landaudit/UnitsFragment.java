package ir.haeri.landaudit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class UnitsFragment extends Fragment {
    public UnitsFragment() {
    }
    DatabaseHandler db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_units, container, false);
        ImageView addOwnerButton  = (ImageView)fragView.findViewById(R.id.addUnit);
        final ViewGroup unitContainer = (ViewGroup)fragView.findViewById(R.id.unitContainer);
        final DatabaseHandler db= new DatabaseHandler(getActivity());
        if (SketchView.highlightPolygon!=-1) {
            Unit[] units = db.GetUnits(SketchView.highlightPolygon);
            if (units!=null)
                for (Unit unit : units) {
                    UnitButton unitButton = new UnitButton(getActivity(),unit.get_id());
                    unitButton.setText(unit.get_unit_name());
                    unitContainer.addView(unitButton);
                }
        }
        addOwnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SketchView.highlightPolygon!=-1) {
                    Unit unit =db.InsertUnitData(SketchView.highlightPolygon);
                    UnitButton unitButton = new UnitButton(getActivity(),unit.get_id());
                    unitButton.setText(unit.get_unit_name());
                    unitContainer.addView(unitButton);
                }
                else
                    Toast.makeText(getActivity(), "عرصه انتخاب نشده است", Toast.LENGTH_LONG).show();

            }
        });
        return fragView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
