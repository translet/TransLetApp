package translet.transletapp;

public class Constants {
    public static final String SERVER_URL = "http://192.168.5.22:5000/translet";
    //public static final String accessURL = "http://ec2-52-43-208-107.us-west-2.compute.amazonaws.com:9090/auth/";

    //socket.io event labels
    public static final String EVENT_LOGIN = "login";
    public static final String EVENT_USER_JOINED = "user_joined";
    public static final String EVENT_USER_LEFT = "user_left";
    public static final String EVENT_SERVER_MESSAGE = "server_event";
    public static final String EVENT_SESSION_BROADCAST = "server_broadcast";
    public static final String EVENT_SESSION_INVITE = "session_invite";
    public static final String EVENT_SESSION_CLOSED = "session_closed";



    public static final String LOGIN_FAILED = "login attempt failed";
    public static final String EVENT_JOIN_SESSION = "join_session";
}
