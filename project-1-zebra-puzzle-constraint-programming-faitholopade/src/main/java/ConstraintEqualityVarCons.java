package main.java;

public class ConstraintEqualityVarCons extends Constraint {

    Variable v;
    int cons;

    public ConstraintEqualityVarCons(Variable v, int cons) {
        this.v = v;
        this.cons = cons;
    }

    public String toString() {
        return this.v + " = " + this.cons;
    }

    protected boolean isSatisfied() {
        return contains(cons, v.d.vals);
    }

    protected boolean reduce() {
        for (int i = 0; i < v.d.vals.length;) {
            if (v.d.vals[i] != cons) {
                v.d.delete(v.d.vals[i]);
                // i--;
            } else {
                i++;
            }
        }
        if (v.d.vals.length == 0) {
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
