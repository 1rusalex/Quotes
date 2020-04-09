package operations;

public class SubscribeOperation {

    public final String op = "subscribe";

    public String [] args;

    public SubscribeOperation(String...arguments) {
        this.args = arguments;
    }

}
