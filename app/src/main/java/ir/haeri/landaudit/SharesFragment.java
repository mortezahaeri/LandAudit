package ir.haeri.landaudit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SharesFragment extends Fragment {
    public SharesFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shares, container, false);
        ListView listShares = (ListView) view.findViewById(R.id.listShares);

        String[] values = new String[]{"آب","برق","گاز","تلفن"};
        ListViewAdapter listAdapter = new ListViewAdapter(getActivity(),-1,values);
        listShares.setAdapter(listAdapter);

        return view;
    }
    public class ListViewAdapter extends ArrayAdapter<String> {

        private final Context ctx;
        private final String[] values;
        public ListViewAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            ctx=context;
            values=objects;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflator = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView  = inflator.inflate(R.layout.item_list,parent,false);
            TextView textLabel = (TextView)rowView.findViewById(R.id.itemLabel);
            textLabel.setText(values[position]);

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
}
