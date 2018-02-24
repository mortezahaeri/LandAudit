package ir.haeri.landaudit;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AddressFragment extends Fragment {


    DatabaseHandler db;
    ListView listAddress;

    public AddressFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new DatabaseHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_address, container, false);
        listAddress = (ListView) view.findViewById(R.id.listAddress);
//        listAddress.setOnTouchListener(new View.OnTouchListener(){
//
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return true;
//            }
//
//        });

        String[] values = new String[]{"محل مشهور به : ","خیابان : ","میدان","بازار/پاساژ : ","کوچه : ","بن بست : ","پلاک آبی : ","کدپستی : ","تلفن : ","کد ملی : "};
        ListViewAdapter listAdapter = new ListViewAdapter(getActivity(),-1,values);
        listAddress.setAdapter(listAdapter);

//        listAddress.setFriction(ViewConfiguration.getScrollFriction()/20);

//        ArrayList<String> my_array = new ArrayList<>();
//        for (String data:values)
//            my_array.add(data);
//        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,my_array);
//        listAddress.setAdapter(adp);

        return view;
    }
    public int Request_Image_Capture = 1;
    public class ListViewAdapter extends ArrayAdapter<String> {

        private final Context ctx;
        private final String[] values;
        public ListViewAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            ctx=context;
            values=objects;
//            count1=0;
            db.InsertData(SketchView.highlightPolygon);
            address= db.GetAddress(SketchView.highlightPolygon);
            Log.d("coord","address="+address.get_Alley());
//            tag=0;



////            String CurrentFilePath=getActivity().getDatabasePath("map_audit.sqlite");
//            String NewFilePath="/sdcard/map_audit.sqlite";
//            try
//            {
//                File f = getActivity().getDatabasePath("map_audit.sqlite");// new File(CurrentFilePath);
//                FileInputStream i = new FileInputStream(f);
//                FileOutputStream o = new FileOutputStream(NewFilePath);
//                i.getChannel().transferTo(0, i.getChannel().size(), o.getChannel());
//            }
//            catch(IOException ex)
//            {Log.d("coord",ex.getMessage()); }

        }

//        int count1;
//        int tag;
        Address address;
        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflator = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView  = inflator.inflate(R.layout.item_list,parent,false);
            TextView textLabel = (TextView)rowView.findViewById(R.id.itemLabel);
            textLabel.setText(values[position]);
//            textLabel.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    hideSoftKeyboard(getActivity());
//                    return false;
//                }
//            });
            final EditText textRow = (EditText)rowView.findViewById(R.id.textRow);

//            textRow.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View view, boolean b) {
//                    if (!b)
//                        hideSoftKeyboard(getActivity());
//                }
//            });

//            textRow.setFocusableInTouchMode(true);
//            textRow.setScrollContainer(false);

//            Log.d("coord","address="+address);
            switch (position) {
                case 0:
                    textRow.setText(address.get_Alley());
//                    textRow.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case 1:
                    textRow.setText(address.get_Street());
                    break;
                case 2:
                    textRow.setText(address.get_Square());
                    break;
                case 3:
                    textRow.setText(address.get_Mall());
                    break;
                case 4:
                    textRow.setText(address.get_Kooche());
                    break;
                case 5:
                    textRow.setText(address.get_Deadend());
                    break;
                case 6:
                    textRow.setText(address.get_BlueID());
                    break;
                case 7:
                    textRow.setText(address.get_Postal());
                    break;
                case 8:
                    textRow.setText(address.get_Phone());
                    break;
                case 9:
                    textRow.setText(address.get_PersonID());
                    break;
            }
            textRow.setTag(position);//tag++);
            textRow .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    String fieldName="";
                    if (!b) {
                        switch ((int) view.getTag()) {
                            case 0:
//                                Log.d("coord","onFocusChange");
                                fieldName = "Alley";
                                address.set_Alley(((TextView) view).getText().toString());
                                break;
                            case 1:
                                fieldName = "Street";
                                address.set_Street(((TextView) view).getText().toString());
                                break;
                            case 2:
                                fieldName = "Square";
                                address.set_Square(((TextView) view).getText().toString());
                                break;
                            case 3:
                                fieldName = "Mall";
                                address.set_Mall(((TextView) view).getText().toString());
                                break;
                            case 4:
                                fieldName = "Kooche";
                                address.set_Kooche(((TextView) view).getText().toString());
                                break;
                            case 5:
                                fieldName = "Deadend";
                                address.set_Deadend(((TextView) view).getText().toString());
                                break;
                            case 6:
                                fieldName = "BlueID";
                                address.set_BlueID(((TextView) view).getText().toString());
                                break;
                            case 7:
                                fieldName = "Postal";
                                address.set_Postal(((TextView) view).getText().toString());
                                break;
                            case 8:
                                fieldName = "Phone";
                                address.set_Phone(((TextView) view).getText().toString());
                                break;
                            case 9:
                                fieldName = "PersonID";
                                address.set_PersonID(((TextView) view).getText().toString());
                                break;
                        }
                        db.UpdateAddress(SketchView.highlightPolygon, address);
                    }
//                    try {parcelData.north = Float.parseFloat(editNorth.getText().toString());}catch(NumberFormatException ne){}
                Log.d("coord",fieldName);
//                    db.UpdateData(SketchView.highlightPolygon, parcelData);
                }
            });

            final ImageView cameraImage = (ImageView)rowView.findViewById(R.id.cameraButton);
            cameraImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,position);
                }
            });

            final ImageView docImage = (ImageView)rowView.findViewById(R.id.docButton);
            docImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler db= new DatabaseHandler(ctx);
                    if (db.GetPic(SketchView.highlightPolygon,position)!=null) {
                        Intent showImageIntent = new Intent(ctx, ShowImageActivity.class);
                        showImageIntent.putExtra("position", position);
                        startActivity(showImageIntent);
                    }else{
                        Toast.makeText(ctx, "عکسی گرفته نشده است", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return rowView;
        }

    }
//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) activity.getSystemService(
//                        Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(
//                activity.getCurrentFocus().getWindowToken(), 0);
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode==Request_Image_Capture && resultCode==getActivity().RESULT_OK)
        if (resultCode==getActivity().RESULT_OK)
        {
            Bundle bundle=data.getExtras();

            int position = requestCode;

            Bitmap photo = (Bitmap) bundle.get("data");

            Bitmap resizedPhoto = Bitmap.createScaledBitmap(photo,300,300,false);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            resizedPhoto.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            byte[] newByteArray = Base64.decode(encoded,0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(newByteArray, 0, newByteArray.length);

            if (db.SavePic(SketchView.highlightPolygon,position,encoded))
                Log.d("coord","dddddd");//encoded);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
