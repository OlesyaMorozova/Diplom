package data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataHelperSQL {

    private DataHelperSQL() {
    }

    private static final String DB_URL = System.getProperty("db.url");

    @SneakyThrows
    public static Connection getConnection() {
        return DriverManager.getConnection(DB_URL, "app", "pass");
    }

    @SneakyThrows
    public static void clear() {
        QueryRunner runner = new QueryRunner();
        var connection = getConnection();
        runner.execute(connection, "DELETE FROM credit_request_entity");
        runner.execute(connection, "DELETE FROM order_entity");
        runner.execute(connection, "DELETE FROM payment_entity");
    }

    @SneakyThrows
    public static String getDebitStatus() {
        QueryRunner runner = new QueryRunner();
        var sqlStatus = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        var connection = getConnection();
        return runner.query(connection, sqlStatus, new ScalarHandler<String>());
    }

    @SneakyThrows
    public static String getCreditStatus() {
        QueryRunner runner = new QueryRunner();
        var sqlStatus = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        var connection = getConnection();
        return runner.query(connection, sqlStatus, new ScalarHandler<String>());
    }

    @SneakyThrows
    public static String getDebitOrderEntryId() {
        QueryRunner runner = new QueryRunner();
        var sqlStatus = "SELECT payment_id FROM order_entity ORDER BY created DESC LIMIT 1";
        var connection = getConnection();
        return runner.query(connection, sqlStatus, new ScalarHandler<String>());
    }

    @SneakyThrows
    public static String getDebitPaymentID() {
        QueryRunner runner = new QueryRunner();
        var sql = "SELECT transaction_id FROM payment_entity ORDER BY created DESC LIMIT 1";
        var connection = getConnection();
        return runner.query(connection, sql, new ScalarHandler<String>());
    }

    @SneakyThrows
    public static String getCreditRequestReEntryId() {
        QueryRunner runner = new QueryRunner();
        var sql = "SELECT bank_id FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        var connection = getConnection();
        return runner.query(connection, sql, new ScalarHandler<String>());
    }

    @SneakyThrows
    public static String getCreditOrderEntryId() {
        QueryRunner runner = new QueryRunner();
        var sql = "SELECT credit_id FROM order_entity ORDER BY created DESC LIMIT 1";
        var connection = getConnection();
        return runner.query(connection, sql, new ScalarHandler<String>());
    }
}