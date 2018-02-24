package ir.haeri.landaudit;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.PersistableBundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    ShapeView mapView;
    GestureDetector mDetector;

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;

    private LocationManager lm;
    Toolbar toolbar;

    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (ShapeView)findViewById(R.id.mapView);

        fragmentManager = getSupportFragmentManager();

        mDetector = new GestureDetector(this,this);

        mNavigationDrawerItemTitles=getResources().getStringArray(R.array.page_list);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList=(ListView)findViewById(R.id.left_drawer);

        setupToolbar();

        DataModel[] drawerItem = new DataModel[4];
        drawerItem[0] = new DataModel(R.drawable.fixtures,mNavigationDrawerItemTitles[0]);
        drawerItem[1] = new DataModel(R.drawable.fixtures,mNavigationDrawerItemTitles[1]);
        drawerItem[2] = new DataModel(R.drawable.fixtures,mNavigationDrawerItemTitles[2]);
        drawerItem[3] = new DataModel(R.drawable.fixtures,mNavigationDrawerItemTitles[3]);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this,R.layout.list_view_item_row,drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();
        mDrawerList.setItemChecked(0, true);
        setTitle(mNavigationDrawerItemTitles[0]);

        final FragmentManager fm= getSupportFragmentManager();
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                mapView.invalidate();
            }
        });

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
            Toast.makeText(this, "دوربین فعال وجود ندارد", Toast.LENGTH_LONG).show();

        lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener =new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
            return;
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,0.01f,locationListener);

        SketchView.highlightPolygon=-1;



    }

    private class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            mapView.GPS_Location=location;
            mapView.invalidate();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            selectItem(position);
        }
    }
    private void selectItem(int position){
        Fragment fragment = null;

//        Log.d("coord","0:fragment==null  =  "+(fragmentManager.findFragmentByTag("MapFragment")==null));

        String fragTag="";
        switch (position){
            case 0:
                fragment = fragmentManager.findFragmentByTag("MapFragment");
                fragTag = "MapFragment";
                if (fragment==null)
                    fragment = new MapFragment();
                break;
            case 1:
                fragment = fragmentManager.findFragmentByTag("ParcelFragment");
                fragmentManager.beginTransaction().remove(fragment);
                fragTag = "ParcelFragment";
//                if (fragment==null)
                    fragment = new ParcelFragment();
                break;
            case 2:
                fragment = fragmentManager.findFragmentByTag("AddressFragment");
                fragTag = "AddressFragment";
                if (fragment==null)
                    fragment = new AddressFragment();
                break;
            case 3:
                fragTag = "UnitsFragment";
                fragment = fragmentManager.findFragmentByTag(fragTag);
                if (fragment==null)
                    fragment = new UnitsFragment();
                break;
            default:
                break;
        }
//        Log.d("coord","fragmentTag = "+fragTag +",(fragment !=null)="+(fragment !=null));
        if (fragment !=null){
//            Log.d("coord","1:fragment==null  =  "+(fragmentManager.findFragmentByTag("MapFragment")==null));
            fragmentManager.beginTransaction().replace(R.id.frag_place,fragment,fragTag)
                    .addToBackStack(null)
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
//            Log.d("coord","2:fragment==null  =  "+(fragmentManager.findFragmentByTag("MapFragment")==null));

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }else{
            Log.e("coord","Error in creating fragment");
        }


    }
    void setupDrawerToggle(){
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.drawable.ic_drawer,R.string.app_name,R.string.app_name);
        mDrawerToggle.syncState();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return  true;
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void onZoomInClick(View view){

        ShapeView.zoomFactor++;
        mapView.invalidate();
    }

    public void onZoomOutClick(View view){
        ShapeView.zoomFactor--;
        mapView.invalidate();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.d("coord","MainActivity::onTouchEvent");
        return this.mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
//        Log.d("coord","MainActivity::onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return true;
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
////        Log.d("coord","v = "+v);
//        if (v>1000) {
//            if (getSupportFragmentManager().getBackStackEntryCount() != 0)
//                getSupportFragmentManager().popBackStack();
//        }
//        else if (v<1000) {
////            Log.d("coord","v <1000");
//            Log.d("coord","getFragmentManager().getBackStackEntryCount()="+getSupportFragmentManager().getBackStackEntryCount());
//            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
//
//                AddressFragment addressFragment = new AddressFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.frag_place, addressFragment,"AddressFragment")
//                        .addToBackStack(null)
//                        .commit();
//
//                getSupportFragmentManager().executePendingTransactions();
//            }
//        }

        return true;
    }

}
