package ir.haeri.landaudit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

public class ShowImageActivity extends AppCompatActivity {

    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("position");
        ImageView showImage = (ImageView)findViewById(R.id.showImage);

        db= new DatabaseHandler(this);
        String base64Image = db.GetPic(SketchView.highlightPolygon,position);

        if (base64Image!=null) {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            showImage.setImageBitmap(decodedByte);
//            Log.d("coord", base64Image);
        }

    }
}
