package relationship;

public class NotARelationshipException extends Exception{
    NotARelationshipException() {}

    public String toString() {
        return "This is not a matrix";
    }
}
