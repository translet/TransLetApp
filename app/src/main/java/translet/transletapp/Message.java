package translet.transletapp;

public class Message {

    private String mMessage;
    private String mUsername;

    private Message() {}

    public String getMessage() {
        return mMessage;
    }

    public String getUsername() {
        return mUsername;
    }

    public static class Builder {
        private String mUsername;
        private String mMessage;

        public Builder(String uname, String msg) {
            mUsername = uname;
            mMessage = msg;
        }

        public Builder() {
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.mUsername = mUsername;
            message.mMessage = mMessage;
            return message;
        }
    }
}
