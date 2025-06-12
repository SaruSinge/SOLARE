// Feito por Giovanna

package sjt.solar.a3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL = "jdbc:mysql://localhost:3306/solar_bd";
    private static final String USUARIO = "root";
    private static final String SENHA = "MySql0731!!";

    public static Connection getConnection() {
        try {
            Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexão estabelecida com sucesso");
            return conexao;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
