package sjt.solar.a3.eventos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import sjt.solar.a3.Conexao;
import sjt.solar.a3.TelaPrincipal;
import sjt.solar.a3.Usuario;

public class EditarEvento extends JFrame {

    private Usuario usuario;
    private boolean reabrindo = false;

    private static final Color FUNDO = new Color(32, 38, 46);
    private static final Color AMARELO_ESCURO = new Color(204, 168, 0);
    private static final Color FONTE = Color.WHITE;
    private static final Color CINZA_CLARO = new Color(230, 230, 230);
    private static final Color CINZA_ESCURO = new Color(44, 51, 61);

    private java.util.List<Evento> eventosOriginais = new ArrayList<>();
    private JPanel eventosPanel;
    private JTextField campoBusca;
    private JLabel lblNenhumResultado;

    public static void abrirTela(Usuario usuario) {
        new EditarEvento(usuario).setVisible(true);
    }

    public void atualizarListaEventos() {
        reabrindo = true;
        this.dispose();
        EditarEvento.abrirTela(usuario);
    }

    public EditarEvento(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Editar Eventos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(540, 540);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (!reabrindo) {
                    new TelaPrincipal(usuario).setVisible(true);
                }
            }
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(FUNDO);

        // Painel de busca
        JPanel buscaPanel = new JPanel();
        buscaPanel.setLayout(new BoxLayout(buscaPanel, BoxLayout.Y_AXIS));
        buscaPanel.setBackground(FUNDO);
        buscaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buscaPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        // Frase alinhada à esquerda do campo de busca
        JLabel lblBusca = new JLabel("Digite o nome do evento que deseja editar:");
        lblBusca.setForeground(FONTE);
        lblBusca.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblBusca.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Campo de busca igual ao campo "nome" de CriarEvento
        campoBusca = new JTextField();
        campoBusca.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoBusca.setBackground(CINZA_CLARO);
        campoBusca.setForeground(Color.BLACK);
        campoBusca.setMargin(new Insets(12, 8, 12, 8));
        campoBusca.setAlignmentX(Component.LEFT_ALIGNMENT);
        campoBusca.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 75)));
        campoBusca.setPreferredSize(new Dimension(320, 36));
        campoBusca.setMaximumSize(new Dimension(320, 36));
        campoBusca.setMinimumSize(new Dimension(320, 36));

        // Botão "Apagar pesquisa" menor
        JButton btnApagarPesquisa = new JButton("Apagar pesquisa");
        btnApagarPesquisa.setBackground(CINZA_ESCURO);
        btnApagarPesquisa.setForeground(Color.WHITE);
        btnApagarPesquisa.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnApagarPesquisa.setFocusPainted(false);
        btnApagarPesquisa.setPreferredSize(new Dimension(140, 32));
        btnApagarPesquisa.setMaximumSize(new Dimension(140, 32));
        btnApagarPesquisa.setMinimumSize(new Dimension(140, 32));
        btnApagarPesquisa.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnApagarPesquisa.addActionListener(e -> {
            campoBusca.setText("");
            filtrarEventos();
        });

        // Painel horizontal para botão apagar pesquisa
        JPanel painelBtnApagar = new JPanel();
        painelBtnApagar.setLayout(new BoxLayout(painelBtnApagar, BoxLayout.X_AXIS));
        painelBtnApagar.setOpaque(false);
        painelBtnApagar.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelBtnApagar.add(btnApagarPesquisa);

        lblNenhumResultado = new JLabel("Nenhum resultado encontrado");
        lblNenhumResultado.setForeground(FONTE);
        lblNenhumResultado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNenhumResultado.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblNenhumResultado.setVisible(false);

        buscaPanel.add(lblBusca);
        buscaPanel.add(Box.createRigidArea(new Dimension(0, 4))); // gap de 4px
        buscaPanel.add(campoBusca);
        buscaPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        buscaPanel.add(painelBtnApagar);
        buscaPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        buscaPanel.add(lblNenhumResultado);

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buscaPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        eventosPanel = new JPanel();
        eventosPanel.setLayout(new BoxLayout(eventosPanel, BoxLayout.Y_AXIS));
        eventosPanel.setBackground(FUNDO);

        JScrollPane scrollPane = new JScrollPane(eventosPanel);
        scrollPane.getViewport().setBackground(FUNDO);
        scrollPane.setBorder(null);

        mainPanel.add(scrollPane);

        // Botão "Voltar" centralizado no fim da página
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(CINZA_ESCURO);
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVoltar.setFocusPainted(false);
        btnVoltar.setPreferredSize(new Dimension(140, 32));
        btnVoltar.setMaximumSize(new Dimension(140, 32));
        btnVoltar.setMinimumSize(new Dimension(140, 32));

        // Painel para centralizar o botão Voltar
        JPanel painelVoltar = new JPanel();
        painelVoltar.setLayout(new BoxLayout(painelVoltar, BoxLayout.X_AXIS));
        painelVoltar.setOpaque(false);
        painelVoltar.add(Box.createHorizontalGlue());
        painelVoltar.add(btnVoltar);
        painelVoltar.add(Box.createHorizontalGlue());

        btnVoltar.addActionListener(e -> {
            reabrindo = true;
            this.dispose();
            new TelaPrincipal(usuario).setVisible(true);
        });

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(painelVoltar);
        mainPanel.add(Box.createVerticalStrut(10));

        setContentPane(mainPanel);

        eventosOriginais = buscarEventosDoUsuario(usuario.getId());
        Collections.sort(eventosOriginais, Comparator.comparingInt((Evento e) -> e.id).reversed());
        exibirEventos(eventosOriginais);

        campoBusca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarEventos();
            }
        });
    }

    private void filtrarEventos() {
        String texto = campoBusca.getText().trim().toLowerCase();
        if (texto.isEmpty()) {
            lblNenhumResultado.setVisible(false);
            exibirEventos(eventosOriginais);
            return;
        }
        java.util.List<Evento> filtrados = new ArrayList<>();
        for (Evento evento : eventosOriginais) {
            if (evento.nomeEvento.toLowerCase().contains(texto)) {
                filtrados.add(evento);
            }
        }
        if (filtrados.isEmpty()) {
            eventosPanel.removeAll();
            lblNenhumResultado.setVisible(true);
            eventosPanel.revalidate();
            eventosPanel.repaint();
        } else {
            lblNenhumResultado.setVisible(false);
            exibirEventos(filtrados);
        }
    }

    private void exibirEventos(java.util.List<Evento> eventos) {
        eventosPanel.removeAll();

        if (eventos.isEmpty()) {
            JLabel lblNenhum = new JLabel("<html><b>Nenhum evento encontrado.</b></html>");
            lblNenhum.setHorizontalAlignment(JLabel.CENTER);
            lblNenhum.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblNenhum.setForeground(FONTE);
            eventosPanel.add(Box.createVerticalGlue());
            eventosPanel.add(lblNenhum);
            eventosPanel.add(Box.createVerticalGlue());
        } else {
            for (Evento evento : eventos) {
                JPanel linha = new JPanel(new BorderLayout());
                linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                linha.setBackground(FUNDO);

                JPanel centro = new JPanel(new BorderLayout());
                centro.setOpaque(false);

                String nomeExibido = evento.nomeEvento.length() > 20
                        ? evento.nomeEvento.substring(0, 20) + "..."
                        : evento.nomeEvento;

                JLabel lblNome = new JLabel(nomeExibido, JLabel.CENTER);
                lblNome.setFont(new Font("Segoe UI", Font.BOLD, 16));
                lblNome.setForeground(FONTE);
                lblNome.setAlignmentY(Component.CENTER_ALIGNMENT);

                centro.add(lblNome, BorderLayout.CENTER);

                linha.add(centro, BorderLayout.CENTER);

                JPanel botoesPanel = new JPanel();
                botoesPanel.setLayout(new BoxLayout(botoesPanel, BoxLayout.X_AXIS));
                botoesPanel.setOpaque(false);

                JButton btnEditar = new JButton("Editar");
                JButton btnApagar = new JButton("Apagar");

                Dimension btnSize = new Dimension(90, 32);
                for (JButton btn : new JButton[]{btnEditar, btnApagar}) {
                    btn.setBackground(AMARELO_ESCURO);
                    btn.setForeground(Color.WHITE);
                    btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    btn.setFocusPainted(false);
                    btn.setPreferredSize(btnSize);
                    btn.setMaximumSize(btnSize);
                    btn.setMinimumSize(btnSize);
                }

                btnEditar.addActionListener((ActionEvent e) -> {
                    this.setVisible(false);
                    EditandoEvento.abrirTela(usuario, evento.id, this);
                });

                btnApagar.addActionListener((ActionEvent e) -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Tem certeza que deseja apagar esse evento?",
                            "Confirmação",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        reabrindo = true;
                        apagarEvento(evento.id);
                        this.dispose();
                        EditarEvento.abrirTela(usuario);
                    }
                });

                botoesPanel.add(btnEditar);
                botoesPanel.add(Box.createRigidArea(new Dimension(5, 0)));
                botoesPanel.add(btnApagar);

                linha.add(botoesPanel, BorderLayout.EAST);

                eventosPanel.add(linha);
                eventosPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        eventosPanel.revalidate();
        eventosPanel.repaint();
    }

    private java.util.List<Evento> buscarEventosDoUsuario(int idUsuario) {
        java.util.List<Evento> eventos = new ArrayList<>();
        try (Connection conn = new Conexao().getConnection()) {
            String sql = "SELECT id, nomeEvento FROM eventos WHERE idUsuario = ? ORDER BY id DESC";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idUsuario);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    eventos.add(new Evento(rs.getInt("id"), rs.getString("nomeEvento")));
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar eventos: " + ex.getMessage());
        }
        return eventos;
    }

    private void apagarEvento(int idEvento) {
        try (Connection conn = new Conexao().getConnection()) {
            String sql = "DELETE FROM eventos WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idEvento);
                stmt.executeUpdate();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao apagar evento: " + ex.getMessage());
        }
    }

    private static class Evento {
        int id;
        String nomeEvento;

        Evento(int id, String nomeEvento) {
            this.id = id;
            this.nomeEvento = nomeEvento;
        }
    }
}