package pathfinder.model;

public final class Util {
    private Util(){ }

    public static void argMustMatch(boolean condition, String msg){
        if (!condition){
            throw new IllegalArgumentException(msg);
        }
    }

}
