package ir.haeri.landaudit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DB_NAME = "map_audit.sqlite";
    public static final String TABLE_LAND = "land_table";
    public static final String TABLE_UNIT = "unit_table";
    public static final String TABLE_PART = "part_table";

    private static final String KEY_ID="id";
    private static final String KEY_NORTH="North";
    private static final String KEY_SOUTH="South";
    private static final String KEY_EAST="East";
    private static final String KEY_WEST="West";

    private static final String KEY_ALLEY="Alley";
    private static final String KEY_ALLEY_PIC="AlleyPic";
    private static final String KEY_STREET="Street";
    private static final String KEY_STREET_PIC="StreetPic";
    private static final String KEY_SQUARE="Square";
    private static final String KEY_SQUARE_PIC="SquarePic";
    private static final String KEY_MALL="Mall";
    private static final String KEY_MALL_PIC="MallPic";
    private static final String KEY_KOOCHE="Kooche";
    private static final String KEY_KOOCHE_PIC="KoochePic";
    private static final String KEY_DEADEND="Deadend";
    private static final String KEY_DEADEND_PIC="DeadendPic";
    private static final String KEY_BLUEID="BlueID";
    private static final String KEY_BLUEID_PIC="BlueIDPic";
    private static final String KEY_POSTAL="Postal";
    private static final String KEY_POSTAL_PIC="PostalPic";
    private static final String KEY_PHONE="Phone";
    private static final String KEY_PHONE_PIC="PhonePic";
    private static final String KEY_PERSONID="PersonID";
    private static final String KEY_PERSONID_PIC="PersonIDPic";

    private static final String KEY2_ID="id";
    private static final String KEY2_BUILDINGID="building_id";
    private static final String KEY2_UNITNAME="unit_name";
    private static final String KEY2_AREA="area";

    private static final String KEY3_ID="id";
    private static final String KEY3_UNITID="unit_id";
    private static final String KEY3_PARTNAME="part_name";
    private static final String KEY3_GROUPCODE="group_code";
    private static final String KEY3_AREA="area";
    private static final String KEY3_USETYPE="use_type";
    private static final String KEY3_VIEW="view";
    private static final String KEY3_WATERSHARE="water_share";
    private static final String KEY3_POWERSHARE="power_share";
    private static final String KEY3_GASSHARE="gas_share";
    private static final String KEY3_USER="user";
    private static final String KEY3_USERTYPE="user_type";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_LAND_TABLE = "CREATE TABLE " +TABLE_LAND+"("
                +KEY_ID+" INTEGER PRIMARY KEY,"
                +KEY_NORTH+" REAL,"
                +KEY_SOUTH+" REAL,"
                +KEY_EAST+" REAL,"
                +KEY_WEST+" REAL,"
                +KEY_ALLEY+" TEXT,"
                +KEY_ALLEY_PIC+" TEXT,"
                +KEY_STREET+" TEXT,"
                +KEY_STREET_PIC+" TEXT,"
                +KEY_SQUARE+" TEXT,"
                +KEY_SQUARE_PIC+" TEXT,"
                +KEY_MALL+" TEXT,"
                +KEY_MALL_PIC+" TEXT,"
                +KEY_KOOCHE+" TEXT,"
                +KEY_KOOCHE_PIC+" TEXT,"
                +KEY_DEADEND+" TEXT,"
                +KEY_DEADEND_PIC+" TEXT,"
                +KEY_BLUEID+" TEXT,"
                +KEY_BLUEID_PIC+" TEXT,"
                +KEY_POSTAL+" TEXT,"
                +KEY_POSTAL_PIC+" TEXT,"
                +KEY_PHONE+" TEXT,"
                +KEY_PHONE_PIC+" TEXT,"
                +KEY_PERSONID+" TEXT,"
                +KEY_PERSONID_PIC+" TEXT"
                +")";
        String CREATE_UNIT_TABLE = "CREATE TABLE " +TABLE_UNIT+"("
                +KEY2_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +KEY2_BUILDINGID+" INTEGER NOT NULL ,"
                +KEY2_UNITNAME+" TEXT,"
                +KEY2_AREA+" REAL"
                +")";
        String CREATE_PART_TABLE = "CREATE TABLE " +TABLE_PART+"("
                +KEY3_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +KEY3_UNITID+" INTEGER NOT NULL ,"
                +KEY3_PARTNAME+" TEXT,"
                +KEY3_GROUPCODE+" TEXT,"
                +KEY3_AREA+" REAL,"
                +KEY3_USETYPE+" TEXT,"
                +KEY3_VIEW+" INTEGER,"
                +KEY3_WATERSHARE+" TEXT,"
                +KEY3_POWERSHARE+" TEXT,"
                +KEY3_GASSHARE+" TEXT,"
                +KEY3_USER+" TEXT,"
                +KEY3_USERTYPE+" TEXT"
                +")";

        sqLiteDatabase.execSQL(CREATE_LAND_TABLE);
        sqLiteDatabase.execSQL(CREATE_UNIT_TABLE);
        sqLiteDatabase.execSQL(CREATE_PART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_LAND);
        onCreate(sqLiteDatabase);
    }

    public boolean InsertData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID,id);

        long test = db.insert(TABLE_LAND,null,cv);
        if(test==-1)
            return false;
        else
            return true;
    }
    public Unit InsertUnitData(int building_id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("Select * from "+TABLE_UNIT+" where building_id=?",new String[]{Integer.toString(building_id)});
        String unitName = "واحد "+(cursor.getCount()+1);
        ContentValues cv = new ContentValues();
        cv.put(KEY2_UNITNAME,unitName);
        cv.put(KEY2_BUILDINGID,building_id);
        long result= db.insert(TABLE_UNIT,null,cv);
        if (result==-1)
            return null;
        else {
            cursor = db.rawQuery("Select max(id) from "+TABLE_UNIT,null);
            cursor.moveToFirst();
            cursor= db.rawQuery("Select * from "+TABLE_UNIT +" where id=?",new String[]{""+cursor.getInt(0)});
            cursor.moveToFirst();
            return new Unit(cursor.getInt(0),cursor.getInt(1),cursor.getString(2));
        }
    }
    public Unit[] GetUnits(int building_id){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("Select * from "+TABLE_UNIT+" where building_id=?",new String[]{Integer.toString(building_id)});

        Unit[] units= null;
        if (cursor.getCount()!=0)
        {
            units=new Unit[cursor.getCount()];
            int count=0;
            while (cursor.moveToNext()) {
                units[count++]=new Unit(cursor.getInt(0),cursor.getInt(1),cursor.getString(2));
            }
        }
        return units;
    }
    public int GetUnitByName(int building_id,String unit_name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+TABLE_UNIT+" where "+KEY2_BUILDINGID+"=? and "+KEY2_UNITNAME+"=?",new String[]{Integer.toString(building_id),unit_name});

        if (cursor.getCount()!=0)
        {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return -1;
    }
    public Part GetPart(long part_id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+TABLE_PART+" where "+KEY3_ID+"=?",new String[]{Long.toString(part_id)});
        if (cursor.getCount()!=0)
        {
            cursor.moveToFirst();

            return new Part(
                    cursor.getInt(0),   //id
                    cursor.getInt(1),   //unit_id
                    cursor.getString(2),//part_name
                    cursor.getString(3),//group_code
                    cursor.getDouble(4),//area
                    cursor.getString(5),//use_type
                    cursor.getInt(6),   //view
                    cursor.getString(7),//water_share
                    cursor.getString(8),//power_share
                    cursor.getString(9),//gas_share
                    cursor.getString(10),//user
                    cursor.getString(11) //user_type
                    );
        }
        return null;
    }
    public Part InsertPartData(int unit_id,String part_name){
        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery("Select * from "+TABLE_PART+" where "+KEY3_UNITID+"=?",new String[]{Integer.toString(unit_id)});
        ContentValues cv = new ContentValues();
        cv.put(KEY3_PARTNAME,part_name);
        cv.put(KEY3_UNITID,unit_id);

        long result= db.insert(TABLE_PART,null,cv);
        if (result==-1)
            return null;
        else
            return GetPart(result);
    }
    public String[] GetPartsName(int unit_id){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+TABLE_PART+" where "+KEY3_UNITID+"=?",new String[]{Integer.toString(unit_id)});

        String[] parts= null;
        if (cursor.getCount()!=0)
        {
            parts=new String[cursor.getCount()];
            int count=0;
//            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                parts[count++]=cursor.getString(2);
            }
        }
        return parts;
    }
    public Part[] GetParts(int unit_id){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+TABLE_PART+" where "+KEY3_UNITID+"=?",new String[]{Integer.toString(unit_id)});

        Part[] parts= null;
        if (cursor.getCount()!=0)
        {
            parts=new Part[cursor.getCount()];
            int count=0;
//            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                parts[count++]=new Part(
                        cursor.getInt(0),   //id
                        cursor.getInt(1),   //unit_id
                        cursor.getString(2),//part_name
                        cursor.getString(3),//group_code
                        cursor.getDouble(4),//area
                        cursor.getString(5),//use_type
                        cursor.getInt(6),   //view
                        cursor.getString(7),//water_share
                        cursor.getString(8),//power_share
                        cursor.getString(9),//gas_share
                        cursor.getString(10),//user
                        cursor.getString(11) //user_type
                );
            }
        }
        return parts;
    }
    public boolean UpdatePart(Part part){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY3_UNITID,part.get_UnitID());
        cv.put(KEY3_PARTNAME,part.get_PartName());
        cv.put(KEY3_GROUPCODE,part.get_GroupCode());
        cv.put(KEY3_AREA,part.get_Area());
        cv.put(KEY3_USETYPE,part.get_UseType());
        cv.put(KEY3_VIEW,part.get_View());
        cv.put(KEY3_WATERSHARE,part.get_WaterShare());
        cv.put(KEY3_POWERSHARE,part.get_PowerShare());
        cv.put(KEY3_GASSHARE,part.get_GasShare());
        cv.put(KEY3_USER,part.get_User());
        cv.put(KEY3_USERTYPE,part.get_UserType());
        long result= db.update(TABLE_PART,cv,"id=?",new String[]{Integer.toString(part.get_ID())});
        if(result<1)
            return  false;

        return true;
    }

    public boolean UpdateData(int id, ParcelData parcelData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NORTH,parcelData.north);
        cv.put(KEY_SOUTH,parcelData.south);
        cv.put(KEY_EAST,parcelData.east);
        cv.put(KEY_WEST,parcelData.west);
        long result= db.update(TABLE_LAND,cv,"id=?",new String[]{Integer.toString(id)});
        if(result<1)
            return  false;

        return true;
    }

    public ParcelData GetData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("Select * from "+TABLE_LAND+" where id=?",new String[]{Integer.toString(id)});
        ParcelData parcelData = new ParcelData();
//        Log.d("coord", "count = " + cursor.getCount()+","+(cursor!=null));
        if (cursor.getCount()!=0) {
            cursor.moveToFirst();
//            Log.d("coord", "id = " + cursor.getInt(0));
            parcelData.id = cursor.getInt(0);
            parcelData.north = cursor.getFloat(1);
            parcelData.south = cursor.getFloat(2);
            parcelData.east = cursor.getFloat(3);
            parcelData.west = cursor.getFloat(4);
//            Log.d("coord", "parcelData.north = " + parcelData.north);
            return parcelData;
        }
        return null;
    }
    public String GetPic(int id,int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("Select * from "+TABLE_LAND+" where id=?",new String[]{Integer.toString(id)});
//        Log.d("coord","DatabaseHandler :: position = "+position);
        if (cursor.getCount()!=0) {
            cursor.moveToFirst();
            return cursor.getString(position*2+1 + 5);
        }

        return "";

    }
    public Address GetAddress(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("Select * from "+TABLE_LAND+" where id=?",new String[]{Integer.toString(id)});
        if (cursor.getCount()!=0) {
            cursor.moveToFirst();
            Address address = new Address(
                    (cursor.getString(5)!=null?cursor.getString(5):"")  //  Alley
                    ,(cursor.getString(6) !=null?cursor.getString(6) :"")//  AlleyPic
                    ,(cursor.getString(7) !=null?cursor.getString(7) :"")//  Street
                    ,(cursor.getString(8) !=null?cursor.getString(8) :"")//  StreetPic
                    ,(cursor.getString(9) !=null?cursor.getString(9) :"")//  Square
                    ,(cursor.getString(10)!=null?cursor.getString(10):"") //  SquarePic
                    ,(cursor.getString(11)!=null?cursor.getString(11):"") //  Mall
                    ,(cursor.getString(12)!=null?cursor.getString(12):"") //  MallPic
                    ,(cursor.getString(13)!=null?cursor.getString(13):"") //  Kooche
                    ,(cursor.getString(14)!=null?cursor.getString(14):"") //  KoochePic
                    ,(cursor.getString(15)!=null?cursor.getString(15):"") //  Deadend
                    ,(cursor.getString(16)!=null?cursor.getString(16):"") //  DeadendPic
                    ,(cursor.getString(17)!=null?cursor.getString(17):"") //  BlueID
                    ,(cursor.getString(18)!=null?cursor.getString(18):"") //  BlueIDPic
                    ,(cursor.getString(19)!=null?cursor.getString(19):"") //  Postal
                    ,(cursor.getString(20)!=null?cursor.getString(20):"") //  PostalPic
                    ,(cursor.getString(21)!=null?cursor.getString(21):"") //  Phone
                    ,(cursor.getString(22)!=null?cursor.getString(22):"") //  PhonePic
                    ,(cursor.getString(23)!=null?cursor.getString(23):"") //  PersonID
                    ,(cursor.getString(24)!=null?cursor.getString(24):"") //  PersonIDPic
            );
            return address;
        }

        return null;

    }

    public boolean UpdateAddress(int id, Address address){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ALLEY,address.get_Alley());
        cv.put(KEY_ALLEY_PIC   ,address.get_AlleyPic());
        cv.put(KEY_STREET      ,address.get_Street());
        cv.put(KEY_STREET_PIC  ,address.get_StreetPic());
        cv.put(KEY_SQUARE      ,address.get_Square());
        cv.put(KEY_SQUARE_PIC  ,address.get_SquarePic());
        cv.put(KEY_MALL        ,address.get_Mall());
        cv.put(KEY_MALL_PIC    ,address.get_MallPic());
        cv.put(KEY_KOOCHE      ,address.get_Kooche());
        cv.put(KEY_KOOCHE_PIC  ,address.get_KoochePic());
        cv.put(KEY_DEADEND     ,address.get_Deadend());
        cv.put(KEY_DEADEND_PIC ,address.get_DeadendPic());
        cv.put(KEY_BLUEID      ,address.get_BlueID());
        cv.put(KEY_BLUEID_PIC  ,address.get_BlueIDPic());
        cv.put(KEY_POSTAL      ,address.get_Postal());
        cv.put(KEY_POSTAL_PIC  ,address.get_PostalPic());
        cv.put(KEY_PHONE       ,address.get_Phone());
        cv.put(KEY_PHONE_PIC   ,address.get_PhonePic());
        cv.put(KEY_PERSONID    ,address.get_PersonID());
        cv.put(KEY_PERSONID_PIC,address.get_PersonIDPic());
        long result= db.update(TABLE_LAND,cv,"id=?",new String[]{Integer.toString(id)});
        if(result<1)
            return  false;

        return true;
    }

    public boolean SavePic(int id,int position,String base64Pic){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        switch (position){
            case 0:
                cv.put(KEY_ALLEY_PIC,base64Pic);
                break;
            case 1:
                cv.put(KEY_STREET_PIC,base64Pic);
                break;
            case 2:
                cv.put(KEY_SQUARE_PIC,base64Pic);
                break;
            case 3:
                cv.put(KEY_MALL_PIC,base64Pic);
                break;
            case 4:
                cv.put(KEY_KOOCHE_PIC,base64Pic);
                break;
            case 5:
                cv.put(KEY_DEADEND_PIC,base64Pic);
                break;
            case 6:
                cv.put(KEY_BLUEID_PIC,base64Pic);
                break;
            case 7:
                cv.put(KEY_POSTAL_PIC,base64Pic);
                break;
            case 8:
                cv.put(KEY_PHONE_PIC,base64Pic);
                break;
            case 9:
                cv.put(KEY_PERSONID_PIC,base64Pic);
                break;
        }

        long result= db.update(TABLE_LAND,cv,"id=?",new String[]{Integer.toString(id)});
//        Log.d("coord","result = "+result);
        if(result<1)
            return  false;

        return true;
    }
}
