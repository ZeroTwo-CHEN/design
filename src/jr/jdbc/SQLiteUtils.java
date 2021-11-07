package jr.jdbc;

import java.sql.*;

public class SQLiteUtils {
    private Connection conn;
    private PreparedStatement ps;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        String url = "jdbc:sqlite:dishes.db";
        conn = DriverManager.getConnection(url);
        return conn;
    }

    public PreparedStatement getStatement(String sql) throws SQLException {
        Connection conn = getConnection();
        ps = conn.prepareStatement(sql);
        return ps;
    }

    public void close() {
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }

    SQLiteUtils() {
        try {
            var c = getConnection();
            var stat = c.createStatement();

            //创建表
            String sql = "CREATE TABLE IF NOT EXISTS DISHES"
                    + "(ID INTEGER NOT NULL constraint DISHES_pk primary key autoincrement,"
                    + "NAME TEXT NOT NULL,"
                    + "PRICE DOUBLE NOT NULL,"
                    + "CLASS TEXT,"
                    + "URL TEXT)";
            stat.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
