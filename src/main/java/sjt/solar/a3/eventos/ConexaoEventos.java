// Feito por Guilherme

package sjt.solar.a3.eventos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoEventos {

    private static final String URL = "jdbc:mysql://localhost:3306/solar_bd";
    private static final String USUARIO = "root";
    private static final String SENHA = "MySql0731!!";

    public static Connection getConnection() {
        try {
            // Tenta criar a conexão com o banco de dados lá no MySQL
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            // Se conseguir, imprime confirmação no console (e n vai aparecer em tela)
            System.out.println("Conexão estabelecida com sucesso");
            return conn;
        } catch (SQLException e) {
            // Se falhar, imprime o erro e retorna null (todavia, é meio inútil pq o Conexao.java já ve isso certin)
            System.err.println(e.getMessage());
            return null;
        }
    }
}