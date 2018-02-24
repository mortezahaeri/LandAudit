package ir.haeri.landaudit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class PartDetailFragment extends Fragment {
    private int _unit_id;
    private int _part_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragView =inflater.inflate(R.layout.fragment_part_detail, container, false);
        Bundle bundle = getArguments();
        _unit_id = bundle.getInt("unit_id");
        _part_id = bundle.getInt("part_id");

        ListView listAddress = (ListView) fragView.findViewById(R.id.listPartDetail);
        String[] values = new String[]{"کد گروه","مساحت","نوع بهره برداری","استفاده کننده ملک","نوع مالکیت","اشتراک آب","اشتراک برق","اشتراک گاز"};
        ListViewAdapter listAdapter = new ListViewAdapter(getActivity(),-1,values);
        listAddress.setAdapter(listAdapter);
        return fragView;
    }

    public class ListViewAdapter extends ArrayAdapter<String> {

        private final Context ctx;
        private final String[] values;
        public ListViewAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            ctx=context;
            values=objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflator = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView  = inflator.inflate(R.layout.item_list,parent,false);
            TextView textLabel = (TextView)rowView.findViewById(R.id.itemLabel);
            textLabel.setText(values[position]);

            final DatabaseHandler db=new DatabaseHandler(getActivity());
            final Part part= db.GetPart(_part_id);
            final EditText textRow = (EditText)rowView.findViewById(R.id.textRow);
            switch (position) {
                case 0:
                    textRow.setText(part.get_GroupCode());
                    break;
                case 1:
                    textRow.setText(""+part.get_Area());
                    break;
                case 2:
                    textRow.setText(part.get_UseType());
                    break;
                case 3:
                    textRow.setText(part.get_User());
                    break;
                case 4:
                    textRow.setText(part.get_UserType());
                    break;
                case 5:
                    textRow.setText(part.get_WaterShare());
                    break;
                case 6:
                    textRow.setText(part.get_PowerShare());
                    break;
                case 7:
                    textRow.setText(part.get_GasShare());
                    break;
            }
            textRow.setTag(position);//tag++);
            textRow .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    String fieldName="";
                    if (b) {
                        switch ((int) view.getTag()) {
                            case 0:
                                part.set_GroupCode(((TextView) view).getText().toString());
                                break;
                            case 1:
                                part.set_Area(Double.parseDouble(((TextView) view).getText().toString()));
                                break;
                            case 2:
                                part.set_UseType(((TextView) view).getText().toString());
                                break;
                            case 3:
                                part.set_User(((TextView) view).getText().toString());
                                break;
                            case 4:
                                part.set_UserType(((TextView) view).getText().toString());
                                break;
                            case 5:
                                part.set_WaterShare(((TextView) view).getText().toString());
                                break;
                            case 6:
                                part.set_PowerShare(((TextView) view).getText().toString());
                                break;
                            case 7:
                                part.set_GasShare(((TextView) view).getText().toString());
                                break;
                        }
                        db.UpdatePart(part);
                    }
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
//                    DatabaseHandler db= new DatabaseHandler(ctx);
//                    if (db.GetPic(SketchView.highlightPolygon,position)!=null) {
//                        Intent showImageIntent = new Intent(ctx, ShowImageActivity.class);
//                        showImageIntent.putExtra("position", position);
//                        startActivity(showImageIntent);
//                    }else{
//                        Toast.makeText(ctx, "عکسی گرفته نشده است", Toast.LENGTH_SHORT).show();
//                    }
                }
            });
//            db.close();
            return rowView;
        }

    }
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

//            if (db.SavePic(SketchView.highlightPolygon,position,encoded))
//                Log.d("coord","dddddd");//encoded);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
