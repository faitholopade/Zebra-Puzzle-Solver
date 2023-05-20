package main.java;

public class ConstraintEqualityVarVar extends Constraint {

    Variable v1, v2;

    public ConstraintEqualityVarVar(Variable v1, Variable v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public String toString() {
        return this.v1 + " = " + this.v2;
    }

    protected boolean isSatisfied() {
        for (int i = 0; i < v1.d.vals.length; i++) {
            if (contains(v1.d.vals[i], v2.d.vals)) {
                return true;
            }
        }
        return false;
    }

    protected boolean reduce() {
        // boolean reduced = false;
        for (int i = 0; i < v1.d.vals.length;) {
            if (!contains(v1.d.vals[i], v2.d.vals)) {
                v1.d.delete(v1.d.vals[i]);
                // reduced = true;
                // i--;
            } else {
                i++;
            }
        }
        for (int i = 0; i < v2.d.vals.length;) {
            if (!contains(v2.d.vals[i], v1.d.vals)) {
                v2.d.delete(v2.d.vals[i]);
                // reduced = true;
                // i--;
            } else {
                i++;
            }
        }

        if (this.v1.d.vals.length == 0 || this.v2.d.vals.length == 0) {
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