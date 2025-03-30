package Connections;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteConnection {
    private static final DataSource datasource;
    private static final String URL = "jdbc:sqlite:banco.db";

    static {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);

        /// Configuracion de rendimiento
        config.setMaximumPoolSize(10); //Maximas conexiones en el pool
        config.setMinimumIdle(2); //Minimas inactivas en el pool
        config.setIdleTimeout(30000); //Tiempo para cerrar conexiones
        config.setConnectionTimeout(10000); //Timeout para obtener conexion
        config.setLeakDetectionThreshold(5000); //Deteccion de fugas de conexiones

        datasource = new HikariDataSource(config);
    }

    /// Metodo para obtener la conexion
    public static Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }

}
