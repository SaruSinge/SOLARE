//Por Giovanna

package sjt.solar.a3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UsuarioDAO {

    public UsuarioDAO(Conexao conexao) { //Vai receber do Conexao que eu fiz ~ Giovanna
        // Vo deixar vazio pq ele deixou vazio no vídeo, mas se precisar eu mudo ~ Giovanna
        // O Copilot sugeriu colocar o Conexao aqui, mas eu não sei se é necessário, então vou deixar assim por enquanto ~ Giovanna
    }

    // Retorna o usuário com o id preenchido pra usar ~ Giovanna
    public Usuario cadastrarUsuario(Usuario usuario) throws Exception {
        String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();// Pra resolver, coloquei essa condição assim, foi o que eu entendi do vídeo. Se tiver errado, me avisa ~ Giovanna
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }
            return usuario;
        }
    }

    // Outros métodos do DAO...
    public void atualizarUsuario(Usuario usuario) throws Exception {
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setInt(4, usuario.getId());
            stmt.executeUpdate();
        }
    }

    public Usuario buscarUsuarioPorEmailESenha(String email, String senha) throws Exception {
        String sql = "SELECT id, nome, email, senha FROM usuario WHERE email = ? AND senha = ?";
        try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha")
                );
                return usuario;
            }
            return null;
        }
    }

    public void excluirUsuario(int id) throws Exception {
        String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

}
