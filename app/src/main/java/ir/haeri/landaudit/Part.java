package ir.haeri.landaudit;

public class Part {
    private int    _ID;
    private int    _UnitID;
    private String _PartName;
    private String _GroupCode;
    private double _Area;
    private String _UseType;
    private int    _View;
    private String _WaterShare;
    private String _PowerShare;
    private String _GasShare;
    private String _User;
    private String _UserType;

    public Part(int ID,int UnitID,String PartName,String GroupCode,double Area,String UseType,int View,String WaterShare,String PowerShare,String GasShare,String User,String UserType){
        _ID=ID;
        _UnitID=UnitID;
        _PartName=PartName;
        _GroupCode=GroupCode;
        _Area=Area;
        _UseType=UseType;
        _View=View;
        _WaterShare=WaterShare;
        _PowerShare=PowerShare;
        _GasShare=GasShare;
        _User=User;
        _UserType=UserType;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public int get_UnitID() {
        return _UnitID;
    }

    public void set_UnitID(int _UnitID) {
        this._UnitID = _UnitID;
    }

    public String get_PartName() {
        return _PartName;
    }

    public void set_PartName(String _PartName) {
        this._PartName = _PartName;
    }

    public String get_GroupCode() {
        return _GroupCode;
    }

    public void set_GroupCode(String _GroupCode) {
        this._GroupCode = _GroupCode;
    }

    public double get_Area() {
        return _Area;
    }

    public void set_Area(double _Area) {
        this._Area = _Area;
    }

    public String get_UseType() {
        return _UseType;
    }

    public void set_UseType(String _UseType) {
        this._UseType = _UseType;
    }

    public int get_View() {
        return _View;
    }

    public void set_View(int _View) {
        this._View = _View;
    }

    public String get_WaterShare() {
        return _WaterShare;
    }

    public void set_WaterShare(String _WaterShare) {
        this._WaterShare = _WaterShare;
    }

    public String get_PowerShare() {
        return _PowerShare;
    }

    public void set_PowerShare(String _PowerShare) {
        this._PowerShare = _PowerShare;
    }

    public String get_GasShare() {
        return _GasShare;
    }

    public void set_GasShare(String _GasShare) {
        this._GasShare = _GasShare;
    }

    public String get_User() {
        return _User;
    }

    public void set_User(String _User) {
        this._User = _User;
    }

    public String get_UserType() {
        return _UserType;
    }

    public void set_UserType(String _UserType) {
        this._UserType = _UserType;
    }
}
