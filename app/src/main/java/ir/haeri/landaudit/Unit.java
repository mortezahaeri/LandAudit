package ir.haeri.landaudit;

public class Unit {

    public Unit(int id,int building_id,String unit_name){
        _id=id;
        _building_id=building_id;
        _unit_name=unit_name;
    }

    private int _id;
    private int _building_id;
    private String _unit_name;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_building_id() {
        return _building_id;
    }

    public void set_building_id(int _building_id) {
        this._building_id = _building_id;
    }

    public String get_unit_name() {
        return _unit_name;
    }

    public void set_unit_name(String _unit_name) {
        this._unit_name = _unit_name;
    }
}
