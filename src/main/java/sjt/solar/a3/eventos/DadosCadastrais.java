package sjt.solar.a3.eventos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sjt.solar.a3.Conexao;
import sjt.solar.a3.TelaPrincipal;
import sjt.solar.a3.Usuario;
import sjt.solar.a3.UsuarioDAO;

public class DadosCadastrais extends JFrame {

    private final Usuario usuario;
    private JLabel lblNome, lblEmail, lblSenha;
    private boolean voltando = false; // Flag para evitar abrir TelaPrincipal duas vezes

    public DadosCadastrais(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Dados Cadastrais do Usuário");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Ao fechar pelo X, volta para TelaPrincipal (mas só se não já estiver voltando)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (!voltando) {
                    TelaPrincipal.abrirTela(usuario);
                }
            }
        });

        // Painel de informações com gap de 10px no topo
        JPanel panel = new JPanel(new GridLayout(8, 1, 0, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // 10px gap no topo

        panel.add(new JLabel("<html><b>ID:</b></html>", JLabel.CENTER));
        panel.add(new JLabel(String.valueOf(usuario.getId()), JLabel.CENTER));
        panel.add(new JLabel("<html><b>Nome:</b></html>", JLabel.CENTER));
        lblNome = new JLabel(usuario.getNome(), JLabel.CENTER);
        panel.add(lblNome);
        panel.add(new JLabel("<html><b>Email:</b></html>", JLabel.CENTER));
        lblEmail = new JLabel(usuario.getEmail(), JLabel.CENTER);
        panel.add(lblEmail);
        panel.add(new JLabel("<html><b>Senha:</b></html>", JLabel.CENTER));
        lblSenha = new JLabel(usuario.getSenha(), JLabel.CENTER);
        panel.add(lblSenha);

        // Painel dos botões com layout igual para todos e gap de 10px acima
        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new GridLayout(3, 1, 0, 8)); // 3 linhas, 8px entre botões
        botoesPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40)); // gap lateral e entre info/botões

        JButton btnEditar = new JButton("Editar Dados");
        btnEditar.addActionListener(e -> editarUsuario());

        JButton btnEncerrar = new JButton("Encerrar Conta");
        btnEncerrar.addActionListener(e -> encerrarConta());

        JButton btnVoltar = new JButton("Voltar para Tela Principal");
        btnVoltar.addActionListener(e -> voltarParaTelaPrincipal());

        Dimension btnSize = new Dimension(200, 36);
        for (JButton btn : new JButton[]{btnEditar, btnEncerrar, btnVoltar}) {
            btn.setPreferredSize(btnSize);
            btn.setMaximumSize(btnSize);
            btn.setMinimumSize(btnSize);
        }

        botoesPanel.add(btnEditar);
        botoesPanel.add(btnEncerrar);
        botoesPanel.add(btnVoltar);

        // Painel wrapper para organizar tudo
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.NORTH);
        wrapper.add(botoesPanel, BorderLayout.CENTER);

        add(wrapper);
    }

    private void editarUsuario() {
        String novoNome = javax.swing.JOptionPane.showInputDialog(this, "Novo nome:", usuario.getNome());
        if (novoNome != null && !novoNome.trim().isEmpty()) {
            usuario.setNome(novoNome);
        }

        String novoEmail = javax.swing.JOptionPane.showInputDialog(this, "Novo email:", usuario.getEmail());
        if (novoEmail != null && !novoEmail.trim().isEmpty()) {
            usuario.setEmail(novoEmail);
        }

        String novaSenha = javax.swing.JOptionPane.showInputDialog(this, "Nova senha:", usuario.getSenha());
        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            usuario.setSenha(novaSenha);
        }

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO(new Conexao());
            usuarioDAO.atualizarUsuario(usuario);
            // Atualiza os labels na tela
            lblNome.setText(usuario.getNome());
            lblEmail.setText(usuario.getEmail());
            lblSenha.setText(usuario.getSenha());
            javax.swing.JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Erro ao atualizar dados: " + ex.getMessage());
        }
    }

    private void encerrarConta() {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja encerrar sua conta? Esta ação não pode ser desfeita, e todos os seus dados salvos não poderão ser mais acessados.",
                "Confirmar exclusão", javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try {
                Conexao conexao = new Conexao();
                UsuarioDAO usuarioDAO = new UsuarioDAO(conexao);
                Connection conn = conexao.getConnection();

                // Primeiro, apaga todos os eventos do usuário
                String sqlEventos = "DELETE FROM eventos WHERE idUsuario = ?";
                try (PreparedStatement stmtEventos = conn.prepareStatement(sqlEventos)) {
                    stmtEventos.setInt(1, usuario.getId());
                    stmtEventos.executeUpdate();
                }

                // Depois, apaga o usuário
                String sqlUsuario = "DELETE FROM usuario WHERE id = ?";
                try (PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario)) {
                    stmtUsuario.setInt(1, usuario.getId());
                    stmtUsuario.executeUpdate();
                }

                conn.close();

                javax.swing.JOptionPane.showMessageDialog(this, "Conta encerrada com sucesso!");
                voltando = true;
                this.dispose();
                new sjt.solar.a3.TelaLogin().setVisible(true);
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Erro ao encerrar conta: " + ex.getMessage());
            }
        }
    }

    // Método para voltar para a TelaPrincipal
    private void voltarParaTelaPrincipal() {
        voltando = true;
        TelaPrincipal.abrirTela(usuario);
        dispose();
    }

    public static void abrirTela(Usuario usuario) {
        DadosCadastrais tela = new DadosCadastrais(usuario);
        tela.setVisible(true);
    }
}
