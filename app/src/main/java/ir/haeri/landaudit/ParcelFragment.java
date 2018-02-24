package ir.haeri.landaudit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.renderscript.Float2;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import diewald_shapeFile.files.shp.shapeTypes.ShpPolygon;

public class ParcelFragment extends Fragment {
    private DatabaseHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("coord","ParcelFragment::onCreate");
        db=new DatabaseHandler(getActivity());
    }

    private EditText editNorth,editSouth,editEast,editWest;
    ParcelData parcelData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_parcel, container, false);

        editNorth = (EditText)view.findViewById(R.id.textNorth);
        editSouth = (EditText)view.findViewById(R.id.textSouth);
        editEast = (EditText)view.findViewById(R.id.textEast);
        editWest = (EditText)view.findViewById(R.id.textWest);

        Log.d("coord","data inserted = "+db.InsertData(SketchView.highlightPolygon));
        parcelData= db.GetData(SketchView.highlightPolygon);
        if (parcelData.north!=0)
            editNorth.setText(Float.toString(parcelData.north));
        if (parcelData.south!=0)
            editSouth.setText(Float.toString(parcelData.south));
        if (parcelData.east!=0)
            editEast.setText(Float.toString(parcelData.east));
        if (parcelData.west!=0)
            editWest.setText(Float.toString(parcelData.west));

        editNorth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                try {parcelData.north = Float.parseFloat(editNorth.getText().toString());}catch(NumberFormatException ne){}
                Log.d("coord","b="+b);
//                Log.d("coord","parcelData.north ="+parcelData.north );
                db.UpdateData(SketchView.highlightPolygon, parcelData);
            }
        });
        editSouth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                try {parcelData.south = Float.parseFloat(editSouth.getText().toString());}catch(NumberFormatException ne){}
                db.UpdateData(SketchView.highlightPolygon, parcelData);
            }
        });
        editEast.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                try {parcelData.east = Float.parseFloat(editEast.getText().toString());}catch(NumberFormatException ne){}
                db.UpdateData(SketchView.highlightPolygon, parcelData);
            }
        });
        editWest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                try {parcelData.west= Float.parseFloat(editWest.getText().toString());}catch(NumberFormatException ne){}
                db.UpdateData(SketchView.highlightPolygon, parcelData);
            }
        });

//        ShpPolygon shape = ShapeView.shapefile.getSHP_shape(SketchView.selectedPolygon);
//        String[] record = ShapeView.shapefile.getDBF_record(SketchView.selectedPolygon);
//
//        String res="";
//        for (String f:record)
//                res=res+","+f;
//        Log.d("coord","count = "+ShapeView.shapefile.getDBF_fieldCount() +":::"+ res);
//        Log.d("coord","test");
        return view;
    }

    @Override
    public void onDetach() {

        ParcelData parcelData= new ParcelData();
        try {parcelData.north = Float.parseFloat(editNorth.getText().toString());}catch(NumberFormatException ne){}
        try {parcelData.south = Float.parseFloat(editSouth.getText().toString());}catch(NumberFormatException ne){}
        try {parcelData.east = Float.parseFloat(editEast.getText().toString());}catch(NumberFormatException ne){}
        try {parcelData.west = Float.parseFloat(editWest.getText().toString());}catch(NumberFormatException ne){}
        db.UpdateData(SketchView.highlightPolygon, parcelData);
//Log.d("coord","ParcelFragment::detach-------");
        super.onDetach();
    }
}
