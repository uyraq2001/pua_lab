package relationship;

public class UnmatchingMatricesException extends Exception {
    UnmatchingMatricesException(){}

    @Override
    public String toString() {
        return "Matrices dimensions don't match";
    }
}