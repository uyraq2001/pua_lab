package relationship;

public class NotABinaryOperationException extends Exception{
        NotABinaryOperationException() {}

        public String toString() {
            return "This is not a binary operation";
        }
}
