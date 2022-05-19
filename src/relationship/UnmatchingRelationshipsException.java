package relationship;

public class UnmatchingRelationshipsException extends Exception {
    UnmatchingRelationshipsException() {
    }

    public String toString() {
        return "Relationships don't match";
    }
}
