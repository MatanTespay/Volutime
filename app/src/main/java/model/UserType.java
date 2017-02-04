package model;

/**
 * Created by Faina0502 on 28/01/2017.
 */

public enum UserType {

    volType ("VOL"),
    orgtype ("ORG");

    private final String type;



    private UserType(String s) {
        type = s;
    }

    public boolean equalsType(String otherType){
        return  type.equals(otherType);
    }

    @Override
    public String toString() {
        return this.type;
    }
}
