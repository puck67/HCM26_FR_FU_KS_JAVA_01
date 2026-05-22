package utils;

public class Constants {

    public static final String DB_URL = "jdbc:mysql://localhost:3306/certificate_management";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "12345";
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static final String SP_INSERT_CERTIFICATE = "{call insert_certificate(?,?,?,?,?,?,?)}";
    public static final String SP_GET_ALL_CERTIFICATES = "{call get_all_certificates()}";
    public static final String SP_UPDATE_CERTIFICATE = "{call update_certificate(?,?,?,?,?,?,?)}";
    public static final String SP_DELETE_CERTIFICATE = "{call delete_certificate(?)}";
    public static final String SP_FIND_CERTIFICATE_BY_ID = "{call find_certificate_by_id(?)}";

    public static final String SP_INSERT_USER = "{call insert_user(?,?,?,?,?)}";
    public static final String SP_FIND_USER_BY_ID = "{call find_user_by_id(?)}";

    public static final double SCORE_MIN = 0.0;
    public static final double SCORE_MAX = 4.0;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_GRAY = "\u001B[90m";

    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_RED_BOLD = "\u001B[1;31m";
    public static final String ANSI_GREEN_BOLD = "\u001B[1;32m";
    public static final String ANSI_YELLOW_BOLD = "\u001B[1;33m";
    public static final String ANSI_BLUE_BOLD = "\u001B[1;34m";
    public static final String ANSI_PURPLE_BOLD = "\u001B[1;35m";
    public static final String ANSI_CYAN_BOLD = "\u001B[1;36m";
    public static final String ANSI_WHITE_BOLD = "\u001B[1;37m";
}
