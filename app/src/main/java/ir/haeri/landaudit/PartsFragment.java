package ir.haeri.landaudit;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class PartsFragment extends Fragment {

    private int _unit_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.fragment_parts, container, false);

        Bundle bundle = getArguments();
        _unit_id = bundle.getInt("unit_id");

        ImageView addPartButton = (ImageView)fragView.findViewById(R.id.addPart);
        final ViewGroup partContainer = (ViewGroup)fragView.findViewById(R.id.partContainer);
        final DatabaseHandler db= new DatabaseHandler(getActivity());
        Part[] parts = db.GetParts(_unit_id);
        if (parts!=null)
            for (Part part : parts) {
                PartButton partButton = new PartButton(getActivity(),_unit_id,part.get_ID());
                partButton.setText(part.get_PartName());
                partContainer.addView(partButton);
            }

        addPartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FloorsDialogClass floorDialog = new FloorsDialogClass(getActivity());
                floorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                floorDialog.show();
                floorDialog.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RadioGroup radioGroup = (RadioGroup)floorDialog.findViewById(R.id.radioFloors);
                        int selectedID = radioGroup.getCheckedRadioButtonId();
                        String floorName=  "";
                        switch (selectedID){
                            case R.id.basement:
                                floorName="زیرزمین";
                                break;
                            case R.id.FirstFloor:
                                floorName="طبقه اول";
                                break;
                            case R.id.SecondFloor:
                                floorName="طبقه دوم";
                                break;
                            case R.id.ThirdFloor:
                                floorName="طبقه سوم";
                                break;
                        }
                        Part part = db.InsertPartData(_unit_id,floorName);
                        PartButton partButton = new PartButton(getActivity(),_unit_id,part.get_ID());
                        partButton.setText(floorName);
                        partContainer.addView(partButton);
                        floorDialog.dismiss();
                    }
                });
            }
        });
        return fragView;
    }

}
