package main.java;

public class ConstraintDifferenceVarVar extends Constraint {

    Variable v1, v2;

    public ConstraintDifferenceVarVar(Variable v1, Variable v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public String toString() {
        return this.v1 + " != " + this.v2;
    }

    protected boolean isSatisfied() {
        if (this.v1.d.vals.length == 1 && this.v2.d.vals.length == 1) {
            if (this.v1.d.vals[0] == this.v2.d.vals[0]) {
                return false;
            }
        }
        return true;
    }

    protected boolean reduce() {
        if (this.v1.d.vals.length == 1 || this.v2.d.vals.length == 1) {
            if (v1.d.vals.length >= v2.d.vals.length) {
                if (contains(v2.d.vals[0], v1.d.vals)) {
                    v1.d.delete(v2.d.vals[0]);
                }
            } else {
                if (contains(v1.d.vals[0], v2.d.vals)) {
                    v2.d.delete(v1.d.vals[0]);
                }
            }
        }
        if (v1.d.vals.length == 0 || v2.d.vals.length == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean contains(int val, int[] vals) {
        for (int i = 0; i < vals.length; i++) {
            if (vals[i] == val)
                return true;
        }
        return false;
    }
}
