package main.java;

import java.util.Arrays;

public class Domain {

    int[] vals;

    public Domain(int[] vals) {
        this.vals = vals;
    }

    public Domain(Domain d2) {
        vals = new int[d2.vals.length];
        for (int i = 0; i < vals.length; i++)
            this.vals[i] = d2.vals[i];
    }

    public void delete(int val) {
        int[] newArr = new int[vals.length - 1]; // new array with one less element

        int j = 0; // index for new array
        for (int i = 0; i < vals.length; i++) {
            if (vals[i] != val) {
                newArr[j] = vals[i]; // copy element to new array
                j++; // increment index for new array
            }
        }

        this.vals = newArr;
    }

    /**
     * @return
     */
    public String toString() {
        String result = "{";
        for (int i = 0; i < vals.length; i++)
            result += vals[i];
        result += "}";
        return result;
    }

    /**
     * @return
     */
    public Domain[] split() {
        Domain[] split = new Domain[2];
        switch (vals.length) {
            case 2:
                split[0] = new Domain(new int[] { vals[0] });
                split[1] = new Domain(new int[] { vals[1] });
                break;
            case 3:
                split[0] = new Domain(new int[] { vals[0] });
                split[1] = new Domain(new int[] { vals[1], vals[2] });
                break;
            case 4:
                split[0] = new Domain(new int[] { vals[0], vals[1] });
                split[1] = new Domain(new int[] { vals[2], vals[3] });
                break;
            case 5:
                split[0] = new Domain(new int[] { vals[0], vals[1] });
                split[1] = new Domain(new int[] { vals[2], vals[3], vals[4] });
                break;
        }
        return split;
    }

    /**
     * @return
     */
    public boolean isEmpty() {
        return vals.length == 0;
    }

    /**
     * @return
     */
    public boolean equals(Domain d2) {
        return Arrays.equals(this.vals, d2.vals);
    }

    /**
     * @return
     */
    public boolean isReducedToOnlyOneValue() {
        return vals.length == 1;
    }

    /**
     * @return
     */
    public static Domain largestDomain(Domain[] domains) {
        Domain largest = domains[0];
        for (int i = 1; i < domains.length; i++) {
            if (domains[i].vals.length > largest.vals.length)
                largest = domains[i];
        }
        return largest;
    }

    public boolean contains(int val, int[] vals) {
        for (int i = 0; i < vals.length; i++) {
            if (vals[i] == val)
                return true;
        }
        return false;
    }
}