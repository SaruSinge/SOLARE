package sjt.solar.a3.eventos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    public void criarEvento(Evento evento) throws Exception {
        if (evento.getNomeEvento() == null || evento.getNomeEvento().trim().isEmpty() ||
            evento.getNomeLista() == null || evento.getNomeLista().trim().isEmpty()) {
            throw new Exception("Nome do Evento e Lista que Pertence são obrigatórios.");
        }

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConexaoEventos.getConnection();
            String sql = "INSERT INTO evento (nomeEvento, nomeLista, descricao, data, hora, local) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, evento.getNomeEvento());
            stmt.setString(2, evento.getNomeLista());
            stmt.setString(3, evento.getDescricao());
            stmt.setDate(4, java.sql.Date.valueOf(evento.getData()));
            stmt.setString(5, evento.getHora());
            stmt.setString(6, evento.getLocal());
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Listar todos os eventos
    public List<Evento> listarEventos() throws Exception {
        List<Evento> eventos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConexaoEventos.getConnection();
            String sql = "SELECT * FROM evento";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Evento evento = new Evento(
                    rs.getInt("idEvento"), // Supondo que Evento tem um ID
                    rs.getString("nomeEvento"),
                    rs.getString("nomeLista"),
                    rs.getString("descricao"),
                    rs.getDate("data") != null ? rs.getDate("data").toString() : null,
                    rs.getString("hora"),
                    rs.getString("local")
                );
                eventos.add(evento);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        return eventos;
    }

    // Editar um evento
    public void editarEvento(Evento evento) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConexaoEventos.getConnection();
            String sql = "UPDATE evento SET nomeEvento=?, nomeLista=?, descricao=?, data=?, hora=?, local=? WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, evento.getNomeEvento());
            stmt.setString(2, evento.getNomeLista());
            stmt.setString(3, evento.getDescricao());
            stmt.setDate(4, java.sql.Date.valueOf(evento.getData()));
            stmt.setString(5, evento.getHora());
            stmt.setString(6, evento.getLocal());
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Deletar um evento
    public void deletarEvento(int idEvento) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConexaoEventos.getConnection();
            String sql = "DELETE FROM evento WHERE idEvento=? OR nomeEvento = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idEvento);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
}