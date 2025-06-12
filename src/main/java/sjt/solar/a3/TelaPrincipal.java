package sjt.solar.a3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class TelaPrincipal extends JFrame {

    private final Usuario usuario;
    private JTable tabelaEventos;
    private DefaultTableModel modeloTabela;

    private static final Color FUNDO = new Color(32, 38, 46);
    private static final Color AMARELO_ESCURO = new Color(204, 168, 0);
    private static final Color CINZA_ESCURO = new Color(44, 51, 61);
    private static final Color CINZA_CLARO = new Color(240, 240, 240);

    public TelaPrincipal(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Organizador Solare - Tela Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 520);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBackground(FUNDO);
        painelPrincipal.setLayout(new BorderLayout(0, 0));

        // Menu superior
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(FUNDO);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Saudação à esquerda
        JLabel saudacao = new JLabel("Olá " + usuario.getNome() + "!");
        saudacao.setFont(new Font("Segoe UI", Font.BOLD, 18));
        saudacao.setForeground(Color.WHITE);
// Gap à esquerda (por exemplo, 20px)
        saudacao.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        menuPanel.add(saudacao, BorderLayout.WEST);
        // Botões à direita
        JPanel botoesMenu = new JPanel();
        botoesMenu.setBackground(FUNDO);
        botoesMenu.setLayout(new BoxLayout(botoesMenu, BoxLayout.X_AXIS));

        JButton btnDados = new JButton("Seus dados");
        btnDados.setBackground(AMARELO_ESCURO);
        btnDados.setForeground(Color.WHITE);
        btnDados.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDados.setFocusPainted(false);
        btnDados.setPreferredSize(new Dimension(120, 25));
        btnDados.setMaximumSize(new Dimension(120, 25));
        btnDados.setMinimumSize(new Dimension(120, 25));
        btnDados.addActionListener(e -> {
            this.setVisible(false);
            sjt.solar.a3.eventos.DadosCadastrais.abrirTela(usuario);
        });

        botoesMenu.add(btnDados);
        botoesMenu.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(AMARELO_ESCURO);
        btnSair.setForeground(Color.WHITE);
        btnSair.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSair.setFocusPainted(false);
        btnSair.setPreferredSize(new Dimension(80, 25));
        btnSair.setMaximumSize(new Dimension(80, 25));
        btnSair.setMinimumSize(new Dimension(80, 25));
        btnSair.addActionListener(e -> {
            this.dispose();
            new TelaInicial().setVisible(true);
        });

        botoesMenu.add(btnSair);
        botoesMenu.add(Box.createRigidArea(new Dimension(10, 0)));

        menuPanel.add(botoesMenu, BorderLayout.EAST);

        painelPrincipal.add(menuPanel, BorderLayout.NORTH);

        // Painel central com botões de ação e tabela
        JPanel centroPanel = new JPanel();
        centroPanel.setBackground(FUNDO);
        centroPanel.setLayout(new BorderLayout(0, 20));
        centroPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Painel de botões de ação
        JPanel botoesAcoes = new JPanel();
        botoesAcoes.setBackground(FUNDO);
        botoesAcoes.setLayout(new BoxLayout(botoesAcoes, BoxLayout.X_AXIS));

        JButton btnCriar = new JButton("Criar Evento");
        btnCriar.setBackground(CINZA_ESCURO);
        btnCriar.setForeground(Color.WHITE);
        btnCriar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCriar.setFocusPainted(false);
        btnCriar.setPreferredSize(new Dimension(130, 36));
        btnCriar.setMaximumSize(new Dimension(130, 36));
        btnCriar.setMinimumSize(new Dimension(130, 36));
        btnCriar.addActionListener(e -> {
            this.setVisible(false);
            new sjt.solar.a3.eventos.CriarEvento(usuario, this).setVisible(true);
        });

        JButton btnEditar = new JButton("Editar Evento");
        btnEditar.setBackground(CINZA_ESCURO);
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnEditar.setFocusPainted(false);
        btnEditar.setPreferredSize(new Dimension(130, 36));
        btnEditar.setMaximumSize(new Dimension(130, 36));
        btnEditar.setMinimumSize(new Dimension(130, 36));
        btnEditar.addActionListener(e -> {
            this.setVisible(false);
            sjt.solar.a3.eventos.EditarEvento.abrirTela(usuario);
        });

        JButton btnExibir = new JButton("Ordenar");
        btnExibir.setBackground(CINZA_ESCURO);
        btnExibir.setForeground(Color.WHITE);
        btnExibir.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnExibir.setFocusPainted(false);
        btnExibir.setPreferredSize(new Dimension(130, 36));
        btnExibir.setMaximumSize(new Dimension(130, 36));
        btnExibir.setMinimumSize(new Dimension(130, 36));
        btnExibir.addActionListener(e -> {
            this.setVisible(false);
            new sjt.solar.a3.eventos.ExibirEventos(usuario).setVisible(true);
        });

        botoesAcoes.add(btnCriar);
        botoesAcoes.add(Box.createRigidArea(new Dimension(15, 0)));
        botoesAcoes.add(btnEditar);
        botoesAcoes.add(Box.createRigidArea(new Dimension(15, 0)));
        botoesAcoes.add(btnExibir);

        centroPanel.add(botoesAcoes, BorderLayout.NORTH);

        // Tabela de eventos
        modeloTabela = new DefaultTableModel(
                new Object[]{"Evento", "Descrição", "Data", "Horário", "Local"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaEventos = new JTable(modeloTabela);
        tabelaEventos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelaEventos.setRowHeight(28);
        tabelaEventos.setBackground(Color.WHITE);
        tabelaEventos.setForeground(Color.BLACK);

        // Cabeçalho customizado
        JTableHeader header = tabelaEventos.getTableHeader();
        header.setBackground(CINZA_CLARO);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));

        // Centraliza cabeçalho
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // Centraliza colunas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabelaEventos.getColumnCount(); i++) {
            tabelaEventos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollTabela = new JScrollPane(tabelaEventos);
        scrollTabela.setBorder(BorderFactory.createEmptyBorder());

        centroPanel.add(scrollTabela, BorderLayout.CENTER);

        painelPrincipal.add(centroPanel, BorderLayout.CENTER);

        setContentPane(painelPrincipal);

        carregarEventos();
    }

    // Torne público para ser chamado por CriarEvento
    public void carregarEventos() {
        modeloTabela.setRowCount(0);
        try (Connection conn = new sjt.solar.a3.Conexao().getConnection()) {
            String sql = "SELECT nomeEvento, descricao, data, horario, local FROM eventos WHERE idUsuario = ? ORDER BY id DESC LIMIT 20";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, usuario.getId());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String nomeEvento = rs.getString("nomeEvento");
                    if (nomeEvento == null) {
                        nomeEvento = "";
                    }

                    String descricao = rs.getString("descricao");
                    if (descricao == null) {
                        descricao = "";
                    }

                    // Formatação da data para dd/MM/yyyy
                    String data = "";
                    java.sql.Date dataSql = rs.getDate("data");
                    if (dataSql != null) {
                        data = new SimpleDateFormat("dd/MM/yyyy").format(dataSql);
                    }

                    // Formatação do horário para HH:mm
                    String horario = "";
                    java.sql.Time horarioSql = rs.getTime("horario");
                    if (horarioSql != null) {
                        horario = new SimpleDateFormat("HH:mm").format(horarioSql);
                    }

                    String local = rs.getString("local");

                    modeloTabela.addRow(new Object[]{nomeEvento, descricao, data, horario, local});
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar eventos: " + ex.getMessage());
        }
    }

    public static void abrirTela(Usuario usuario) {
        TelaPrincipal tela = new TelaPrincipal(usuario);
        tela.setVisible(true);
    }
}
