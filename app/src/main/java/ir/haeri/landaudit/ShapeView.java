package ir.haeri.landaudit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.location.Location;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import diewald_shapeFile.files.shp.shapeTypes.ShpPolygon;
import diewald_shapeFile.files.shp.shapeTypes.ShpShape;
import diewald_shapeFile.shapeFile.ShapeFile;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.coords.MGRSCoord;
import gov.nasa.worldwind.geom.coords.UTMCoord;

public class ShapeView extends ImageView implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener{
    public String shapeFileName="Edare_Project";
    private Context ctx;
    private GestureDetectorCompat mDetector;

    public double centerX,mapCenterX;
    public double centerY,mapCenterY;
    public static  double zoomFactor=1;

    int number_of_shapes;
    public static ShapeFile shapefile;

    double width;
    double height;

    double[][] boundingBox,shiftedBoundingBox;
    public double xShift;
    public double yShift;

    public double xRatio=1;
    public double yRatio=1;

    public static double touchX=-1;
    public static double touchY=-1;

    public float prevX=0,prevY=0;
    private float moveX=0,moveY=0;
    DatabaseHandler db;

    private int selectedPolygon;

    public ShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx=context;
        db= new DatabaseHandler(ctx);

        mDetector=new GestureDetectorCompat(ctx,this);
        mDetector.setOnDoubleTapListener(this);

        File shpFile = new File(ctx.getCacheDir() + "/shape.shp");
        File shxFile = new File(ctx.getCacheDir() + "/shape.shx");
        File dbfFile = new File(ctx.getCacheDir() + "/shape.dbf");

        if (!shpFile.exists())
            try{
                InputStream is = ctx.getAssets().open(shapeFileName+".shp");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                FileOutputStream fos = new FileOutputStream(shpFile);
                fos.write(buffer);
                fos.close();

                is = ctx.getAssets().open(shapeFileName+".shx");
                size = is.available();
                buffer = new byte[size];
                is.read(buffer);
                is.close();
                fos = new FileOutputStream(shxFile);
                fos.write(buffer);
                fos.close();

                is = ctx.getAssets().open(shapeFileName+".dbf");
                size = is.available();
                buffer = new byte[size];
                is.read(buffer);
                is.close();
                fos = new FileOutputStream(dbfFile);
                fos.write(buffer);
                fos.close();
            }catch(Exception e){
                Log.d("coord",e.getMessage());
            }
        try {
            shapefile = new ShapeFile(ctx.getCacheDir().getPath(), "shape").READ();

            ShpShape.Type shape_type = shapefile.getSHP_shapeType();

            number_of_shapes = shapefile.getSHP_shapeCount();
            int number_of_fields = shapefile.getDBF_fieldCount();
            boundingBox = shapefile.getSHP_boundingBox();

            Display display = ((Activity)ctx).getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics= new DisplayMetrics();
            display.getMetrics(outMetrics);

            shiftedBoundingBox = new double[2][2];
            xRatio = outMetrics.widthPixels/(boundingBox[0][1]-boundingBox[0][0]);
            yRatio = outMetrics.heightPixels/(boundingBox[1][1]-boundingBox[1][0]);
            if (xRatio<yRatio)
                yRatio = xRatio;
            else
                xRatio = yRatio;

            double xLeft = boundingBox[0][0];
            double xRight = boundingBox[0][1];
            double xMean = (xLeft + xRight) / 2;
            double yTop = boundingBox[1][1];
            double yBottom = boundingBox[1][0];
            double yMean = (yTop + yBottom) / 2;
            width = xRight - xLeft;
            height = yTop - yBottom;
            mapCenterX = xLeft + width / 2;
            mapCenterY = yBottom + height / 2;
            centerX = mapCenterX;
            centerY = mapCenterY;

            shiftedBoundingBox[0][0]=mapCenterX-outMetrics.widthPixels/xRatio/2;
            shiftedBoundingBox[0][1]=mapCenterX+outMetrics.widthPixels/xRatio/2;
            shiftedBoundingBox[1][0]=mapCenterY-outMetrics.heightPixels/yRatio/2;
            shiftedBoundingBox[1][1]=mapCenterY+outMetrics.heightPixels/yRatio/2;

            GPS_Location= null;
        }catch(Exception e){
//            Toast.makeText(ctx,e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
    public Location GPS_Location;
    private void DrawShape(Canvas canvas){
        try {

            double[][] viewBound= new double[2][2];

            viewBound[0][0]=centerX-(width/zoomFactor)/2;
            viewBound[0][1]=centerX+(width/zoomFactor)/2;
            viewBound[1][0]=centerY-(height/zoomFactor)/2;
            viewBound[1][1]=centerY+(height/zoomFactor)/2;

            if (viewBound[0][0]>shiftedBoundingBox[0][1]) {
                centerX = shiftedBoundingBox[0][1] + (width / zoomFactor) / 2;
//                Log.d("coord","return x 1");
                return;
            }
            if (viewBound[0][1]<shiftedBoundingBox[0][0]) {
                centerX = shiftedBoundingBox[0][0] - (width / zoomFactor) / 2;
                return;
            }
            if (viewBound[1][0]>shiftedBoundingBox[1][1]) {
                centerY = shiftedBoundingBox[1][1] + (height / zoomFactor) / 2;
                return;
            }
            if (viewBound[1][1]<shiftedBoundingBox[1][0]) {
                centerY = shiftedBoundingBox[1][0] - (height / zoomFactor) / 2;
                return;
            }
//            Log.d("coord","bigger x = "+(viewBound[0][0]<shiftedBoundingBox[0][1]));
//            Log.d("coord","smaller x= "+(viewBound[0][1]<shiftedBoundingBox[0][0]));
//            Log.d("coord","bigger y = "+(viewBound[1][0]<shiftedBoundingBox[1][1]));
//            Log.d("coord","smaller y= "+(viewBound[1][1]>shiftedBoundingBox[1][0]));

            xShift = boundingBox[0][0];
            yShift = -boundingBox[1][1];

            mapCenterX+= centerX - mapCenterX;
            mapCenterY+= (centerY - mapCenterY);
            xShift=mapCenterX-(canvas.getWidth()/Math.pow(2,zoomFactor))/xRatio;
            yShift=-(mapCenterY+(canvas.getHeight()/Math.pow(2,zoomFactor))/yRatio);

            double minDist = 1e32;
            selectedPolygon=-1;

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            for(int i = 0; i < number_of_shapes; i++){
                ShpPolygon shape    = shapefile.getSHP_shape(i);
                String[] shape_info = shapefile.getDBF_record(i);

                ShpShape.Type type     = shape.getShapeType();
                int number_of_vertices = shape.getNumberOfPoints();
                int number_of_polylines = shape.getNumberOfParts();
                int record_number      = shape.getRecordNumber();

                double[][] shapeBound=shape.getBoundingBox();

                Path polygonPath = new Path();

                double[][] selectBound= new double[2][2];
                double selectionRadius=10;
                selectBound[0][0]=touchX-(selectionRadius/zoomFactor)/2;
                selectBound[0][1]=touchX+(selectionRadius/zoomFactor)/2;
                selectBound[1][0]=touchY-(selectionRadius/zoomFactor)/2;
                selectBound[1][1]=touchY+(selectionRadius/zoomFactor)/2;

                if ((shapeBound[0][1]>selectBound[0][0] && shapeBound[0][0]<selectBound[0][1]) && (shapeBound[1][1]>selectBound[1][0] && shapeBound[1][0]<selectBound[1][1])) {
                    int crossCount=0;
                    for (int j=0;j<number_of_vertices-1;j++) {

                        double m = (shape.getPoints()[j][1]-shape.getPoints()[j+1][1])/(shape.getPoints()[j][0]-shape.getPoints()[j+1][0]);
                        double intersecY = m*touchX-m*shape.getPoints()[j][0]+shape.getPoints()[j][1];
                        if (touchY>intersecY)
                            if (shape.getPoints()[j][1]<shape.getPoints()[j+1][1]) {
                                if (intersecY > shape.getPoints()[j][1] && intersecY < shape.getPoints()[j + 1][1])
                                    crossCount++;
                            }else{
                                if (intersecY > shape.getPoints()[j+1][1] && intersecY < shape.getPoints()[j][1])
                                    crossCount++;
                            }

                    }

                    if (crossCount-Math.floor(crossCount/2)*2!=0)
                        selectedPolygon=i;
//                    paint.setColor(Color.RED);
                }
                else
                    paint.setColor(Color.BLACK);


                for (int j=0;j<number_of_vertices-1;j++) {
                    float x1=(float)((shape.getPoints()[j][0]-xShift)*xRatio*zoomFactor);
                    float y1=(float)((-shape.getPoints()[j][1]-yShift)*yRatio*zoomFactor);
                    float x2=(float)((shape.getPoints()[j+1][0]-xShift)*xRatio*zoomFactor);
                    float y2=(float)((-shape.getPoints()[j+1][1]-yShift)*yRatio*zoomFactor);

                    if (SketchView.highlightPolygon==i) {
                        paint.setColor(Color.RED);
                        paint.setStrokeWidth(2);
                    }
                    else {
                        paint.setColor(Color.BLACK);
                        paint.setStrokeWidth(1);
                    }

                    canvas.drawLine(x1,y1,x2,y2,paint);

//                    if (j==0)
//                        polygonPath.moveTo(x1,y1);
//                    polygonPath.lineTo(x2,y2);

                }

//                Paint polygonPaint = new Paint();
//                if (db.GetData(i)==null)
//                    polygonPaint.setColor(Color.RED);
//                else
//                    polygonPaint.setColor(Color.GREEN);
//                polygonPaint.setStyle(Paint.Style.FILL);
//                canvas.drawPath(polygonPath,polygonPaint);
            }




            if (GPS_Location!=null) {
                Angle latitude = Angle.fromDegrees(GPS_Location.getLatitude());

                Angle longitude = Angle.fromDegrees(GPS_Location.getLongitude());

                MGRSCoord mGRSCoord =MGRSCoord.fromLatLon(latitude, longitude);

                UTMCoord mUTMCoord  = UTMCoord.fromLatLon(latitude,longitude);
                float x1=(float)((mUTMCoord.getEasting()-xShift)*xRatio*zoomFactor);
                float y1=(float)((-mUTMCoord.getNorthing()-yShift)*yRatio*zoomFactor);
                paint.setColor(Color.RED);
                canvas.drawCircle(x1,y1,3,paint);
                paint.setAlpha(50);
                canvas.drawCircle(x1,y1,10,paint);
//                canvas.drawPoint(x1,y1,paint);
//                canvas.drawText(mUTMCoord.toString(), 100,100,paint);
            }

        } catch (Exception ex) {
            Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {

        centerX-=moveX/(xRatio*zoomFactor);
        centerY+=moveY/(yRatio*zoomFactor);
        moveX=0;
        moveY=0;

        DrawShape(canvas);
    }

    private boolean pointer2Down=false;
    private float pointer1DownX=-10,pointer1DownY=-10;
    private float pointer2DownX=-10,pointer2DownY=-10;
    private float baseLength=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN)
        {
            pointer1DownX=event.getX(0);
            pointer1DownY=event.getY(0);

            prevX=event.getX(0);
            prevY=event.getY(0);

            touchX=event.getX(0)/(xRatio*zoomFactor)+xShift;
            touchY=-(event.getY(0)/(yRatio*zoomFactor)+yShift);
        }
        if (event.getAction()==MotionEvent.ACTION_MOVE)
        {
            pointer1DownX=event.getX(0);
            pointer1DownY=event.getY(0);

            moveX=event.getX(0)-prevX;
            moveY=event.getY(0)-prevY;
            prevX=event.getX(0);
            prevY=event.getY(0);
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_POINTER_DOWN:
                pointer2Down=true;
                pointer2DownX=event.getX(1);
                pointer2DownY=event.getY(1);
                baseLength = (float)Math.sqrt(Math.pow(pointer2DownX-pointer1DownX,2)+Math.pow(pointer2DownY-pointer1DownY,2));
                Log.d("coord","event.getX(0) = "+event.getX(0)+",event.getX(1) = "+event.getX(1));
                Log.d("coord","ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                pointer2Down=false;
            case MotionEvent.ACTION_MOVE:
                if (pointer2Down) {
                    float scale = (float) (Math.sqrt(Math.pow(pointer2DownX - pointer1DownX, 2) + Math.pow(pointer2DownY - pointer1DownY, 2)) / baseLength);
                    baseLength = (float)Math.sqrt(Math.pow(pointer2DownX-pointer1DownX,2)+Math.pow(pointer2DownY-pointer1DownY,2));
                    zoomFactor*=scale;
                    Log.d("coord", "scale = " + scale);
                }
                break;
        }
        invalidate();
        return this.mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        if (selectedPolygon>=0) {
            SketchView.highlightPolygon=selectedPolygon;
//            ParcelFragment parcelFragment = new ParcelFragment();
//
//            ((AppCompatActivity) ctx).getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.frag_place, parcelFragment,"ParcelFragment")
//                    .addToBackStack(null)
//                    .commit();
//            ((AppCompatActivity) ctx).getSupportFragmentManager().executePendingTransactions();
        }

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return true;
    }
}
