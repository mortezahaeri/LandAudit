package ir.haeri.landaudit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DecimalFormat;

import diewald_shapeFile.files.shp.shapeTypes.ShpPolygon;

public class SketchView extends ImageView {

    double[][] boundingBox;

    double width;
    double height;

    public double centerX,mapCenterX;
    public double centerY,mapCenterY;
    Context ctx;
    public static int highlightPolygon;

    public SketchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx=context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (highlightPolygon>=0)
            try {
                ShpPolygon shape = ShapeView.shapefile.getSHP_shape(highlightPolygon);
                boundingBox = shape.getBoundingBox();

                double padding=10;
                double xLeft = boundingBox[0][0]-padding;
                double xRight = boundingBox[0][1]+padding;
                double yTop = boundingBox[1][1]+padding;
                double yBottom = boundingBox[1][0]-padding;

                width = xRight - xLeft;
                height = yTop - yBottom;
                mapCenterX = xLeft + width / 2;
                mapCenterY = yBottom + height / 2;

                centerX = mapCenterX;
                centerY = mapCenterY;

                double xShift = boundingBox[0][0]-padding;
                double yShift = -boundingBox[1][1]-padding;

                double xRatio = (canvas.getClipBounds().right - canvas.getClipBounds().left) / (xRight - xLeft);
                double yRatio = (canvas.getClipBounds().bottom - canvas.getClipBounds().top) / (yTop - yBottom);
                if (xRatio < yRatio) {
                    yRatio = xRatio;
                    yShift -= (((canvas.getClipBounds().bottom - canvas.getClipBounds().top) - yRatio * (yTop - yBottom))) / yRatio / 2;
                } else {
                    xRatio = yRatio;
                    xShift -= (((canvas.getClipBounds().right - canvas.getClipBounds().left) - xRatio * (xRight - xLeft))) / xRatio / 2;
                }
                xShift += centerX - mapCenterX;
                yShift += centerY - mapCenterY;

                int number_of_vertices = shape.getNumberOfPoints();

                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(5);

                int zoomFactor = 1;
                Path linePath;
                for (int j = 0; j < number_of_vertices - 1; j++) {
                    float x1 = (float) ((shape.getPoints()[j][0] - xShift) * xRatio * zoomFactor);
                    float y1 = (float) ((-shape.getPoints()[j][1] - yShift) * yRatio * zoomFactor);
                    float x2 = (float) ((shape.getPoints()[j + 1][0] - xShift) * xRatio * zoomFactor);
                    float y2 = (float) ((-shape.getPoints()[j + 1][1] - yShift) * yRatio * zoomFactor);
                    paint.setColor(Color.BLACK);
                    canvas.drawLine(x1, y1, x2, y2, paint);
                    double dist = Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));

                    linePath= new Path();
                    linePath.moveTo((x1+x2)/2,(y1+y2)/2);
                    linePath.lineTo(x2,y2);
                    paint.setColor(Color.RED);
                    DecimalFormat df = new DecimalFormat("#.#");
                    canvas.drawTextOnPath(df.format(dist),linePath,0,-10,paint);
                }

            } catch (Exception e) {
//            Toast.makeText(ctx,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        else{
            Toast.makeText(ctx, "عرصه انتخاب نشده است", Toast.LENGTH_LONG).show();

        }

    }
}
