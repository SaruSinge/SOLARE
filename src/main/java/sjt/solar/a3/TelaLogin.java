//Feito por Giovanna

package sjt.solar.a3;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class TelaLogin extends JFrame {

    private final JTextField txtEmail; // Mudei pra final tá? ~Giovanna
    private final JPasswordField txtSenha;

    public TelaLogin() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(new Color(32, 38, 46));
        painel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel lblTitulo = new JLabel("Login");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        painel.add(lblTitulo);
        painel.add(Box.createVerticalStrut(30));

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        painel.add(txtEmail);

        painel.add(Box.createVerticalStrut(10));

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setForeground(Color.WHITE);
        lblSenha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSenha.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        painel.add(txtSenha);

        painel.add(Box.createVerticalStrut(20));

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBackground(new Color(204, 168, 0));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEntrar.setPreferredSize(new Dimension(180, 45));
        btnEntrar.setMaximumSize(new Dimension(180, 45));
        btnEntrar.setMinimumSize(new Dimension(180, 45));

        btnEntrar.addActionListener(e -> realizarLogin()); // Gi, aqui eu mudei pra o que o corretor tava falando ~Sa

        painel.add(btnEntrar);

        painel.add(Box.createVerticalStrut(15));

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(44, 51, 61));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnVoltar.setFocusPainted(false);
        btnVoltar.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnVoltar.addActionListener(e -> {
            new TelaInicial().setVisible(true);
            dispose();
        });

        painel.add(btnVoltar);

        setContentPane(painel);
    }

    private void realizarLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO(new Conexao());
            Usuario usuario = usuarioDAO.buscarUsuarioPorEmailESenha(email, senha);

            if (usuario != null && usuario.getId() > 0) {
                new TelaPrincipal(usuario).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Email ou senha incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao realizar login: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); // Realmente ficou melhor assim ~Samuel
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    } // Depois eu tiro ~ Gi
}