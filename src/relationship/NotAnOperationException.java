package relationship;

public class NotAnOperationException extends Exception{
    NotAnOperationException() {}

    public String toString() {
        return "This is not matrix of an algebraic operation";
    }
}
