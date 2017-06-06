package me.creepplays.asyncable.mysql;

import me.creepplays.asyncable.AsyncPromise;
import me.creepplays.asyncable.Consumer;
import me.creepplays.asyncable.Function;
import me.creepplays.asyncable.Promise;

import java.sql.*;

import static me.creepplays.asyncable.Asyncable.*;

public class AsyncableMySQL {

    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String database;

    private Connection connection;

    public AsyncableMySQL(String host, int port, String user, String password, String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public AsyncableMySQL(String host, String user, String password, String databsae) {
        this(host, 3306, user, password, databsae);
    }

    private void openConnection() {
        try {
            if(this.connection != null && !this.connection.isClosed()) {
                return;
            }

            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Promise<PreparedStatement, ResultSet> query(String query) {
        return this.prepareStatement(query).then((Function<PreparedStatement, ResultSet>) PreparedStatement::executeQuery);
    }

    public Promise<PreparedStatement, Integer> update(String query) {
        return this.prepareStatement(query).then((Function<PreparedStatement, Integer>) PreparedStatement::executeUpdate);
    }

    public AsyncPromise<PreparedStatement> prepareStatement(String query) {
        return async(() -> this.connection.prepareStatement(query));
    }

}
