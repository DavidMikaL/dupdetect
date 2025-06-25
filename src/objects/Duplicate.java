package objects;

import java.util.Objects;

public class Duplicate implements Comparable<Duplicate>{
    private int lid;
    private int rid;

    public Duplicate() {}

    public Duplicate(int id1, int id2) {
        this.lid = Integer.min(id1,id2);
        this.rid = Integer.max(id1,id2);
    }

    public int getLid() {return lid;}
    public int getRid() {return rid;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Duplicate)) return false;
        Duplicate key = (Duplicate) o;
        return lid == key.lid && rid == key.rid;
    }

    @Override
    public int compareTo(Duplicate other) {
        int result = Integer.compare(this.lid, other.lid);
        if (result != 0) return result;
        return Integer.compare(this.rid, other.rid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lid, rid);
    }
}
