package objects;

public class Duplicate {
    private int lid;
    private int rid;

    public Duplicate() {}

    public Duplicate(int id1, int id2) {
        this.lid = Integer.min(id1,id2);
        this.rid = Integer.max(id1,id2);
    }

    public int getLid() {return lid;}
    public int getRid() {return rid;}



}
