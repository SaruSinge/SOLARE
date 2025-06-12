package sjt.solar.a3.eventos;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import sjt.solar.a3.Conexao;
import sjt.solar.a3.TelaPrincipal;
import sjt.solar.a3.Usuario;

public class ExibirEventos extends JFrame {

    private Usuario usuario;
    private JPanel mainPanel;
    private JPanel eventosPanel;
    private List<Evento> eventosOriginais = new ArrayList<>();
    private boolean voltando = false; // Flag para evitar abrir TelaPrincipal duas vezes

    // Cores iguais à tela EditandoEvento
    private static final Color FUNDO = new Color(32, 38, 46);
    private static final Color FONTE = Color.WHITE;
    private static final Color AMARELO_ESCURO = new Color(204, 168, 0);
    private static final Color CINZA_ESCURO = new Color(44, 51, 61);

    public static void abrirTela(Usuario usuario) {
        new ExibirEventos(usuario).setVisible(true);
    }

    public ExibirEventos(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Meus Eventos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);

        // Ao fechar a janela pelo X, volta para TelaPrincipal (mas só se não já estiver voltando)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (!voltando) {
                    new TelaPrincipal(usuario).setVisible(true);
                }
            }
        });

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(FUNDO);

        // Painel centralizador
        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setBackground(FUNDO);
        painelCentro.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botão Voltar
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(AMARELO_ESCURO);
        btnVoltar.setForeground(FONTE);
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnVoltar.setFocusPainted(false);
        Dimension botaoDim = new Dimension(150, 32);
        Dimension voltarDim = new Dimension(botaoDim.width * 3 + 20, 40);
        btnVoltar.setMaximumSize(voltarDim);
        btnVoltar.setPreferredSize(voltarDim);
        btnVoltar.setMinimumSize(voltarDim);
        btnVoltar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVoltar.addActionListener((ActionEvent e) -> {
            voltando = true;
            this.dispose();
            new TelaPrincipal(usuario).setVisible(true);
        });

        painelCentro.add(Box.createRigidArea(new Dimension(0, 10)));
        painelCentro.add(btnVoltar);
        painelCentro.add(Box.createRigidArea(new Dimension(0, 10)));

        // Painel dos botões de ordenação centralizados
        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new BoxLayout(botoesPanel, BoxLayout.X_AXIS));
        botoesPanel.setBackground(FUNDO);
        botoesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnOrdemNome = new JButton("Ordenar por nome");
        JButton btnRecente = new JButton("Recente");
        JButton btnPorLista = new JButton("Por Lista");

        Font botaoFont = new Font("Segoe UI", Font.PLAIN, 14);

        for (JButton btn : Arrays.asList(btnOrdemNome, btnRecente, btnPorLista)) {
            btn.setFont(botaoFont);
            btn.setBackground(CINZA_ESCURO);
            btn.setForeground(FONTE);
            btn.setFocusPainted(false);
            btn.setMaximumSize(botaoDim);
            btn.setPreferredSize(botaoDim);
            btn.setMinimumSize(botaoDim);
        }

        // Não altere a flag voltando nos botões de ordenação
        btnOrdemNome.addActionListener(e -> {
            List<Evento> ordenados = new ArrayList<>(eventosOriginais);
            ordenados.sort(Comparator.comparing(ev -> ev.nomeEvento != null ? ev.nomeEvento.toLowerCase() : ""));
            exibirEventos(ordenados);
        });

        btnRecente.addActionListener(e -> {
            eventosOriginais = buscarEventosDoUsuario(usuario.getId());
            exibirEventos(eventosOriginais);
        });

        btnPorLista.addActionListener(e -> {
            List<Evento> ordenados = new ArrayList<>(eventosOriginais);
            ordenados.sort(Comparator.comparing(ev -> ev.nomeLista != null ? ev.nomeLista.toLowerCase() : ""));
            exibirEventos(ordenados);
        });

        botoesPanel.add(btnOrdemNome);
        botoesPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        botoesPanel.add(btnRecente);
        botoesPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        botoesPanel.add(btnPorLista);

        painelCentro.add(botoesPanel);
        painelCentro.add(Box.createRigidArea(new Dimension(0, 15)));

        // Painel onde os eventos serão exibidos
        eventosPanel = new JPanel();
        eventosPanel.setLayout(new BoxLayout(eventosPanel, BoxLayout.Y_AXIS));
        eventosPanel.setBackground(FUNDO);
        eventosPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(eventosPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(FUNDO);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Painel para alinhar eventos à esquerda, mas manter botões centralizados
        JPanel painelEventosAlinhado = new JPanel();
        painelEventosAlinhado.setLayout(new BoxLayout(painelEventosAlinhado, BoxLayout.X_AXIS));
        painelEventosAlinhado.setBackground(FUNDO);
        painelEventosAlinhado.add(Box.createRigidArea(new Dimension(30, 0))); // margem esquerda igual aos botões
        painelEventosAlinhado.add(scrollPane);

        painelCentro.add(painelEventosAlinhado);

        mainPanel.add(painelCentro);

        // Carrega eventos do banco
        eventosOriginais = buscarEventosDoUsuario(usuario.getId());

        // Exibe inicialmente por ordem de inserção (como vieram do banco)
        exibirEventos(eventosOriginais);

        setContentPane(mainPanel);
    }

    private void exibirEventos(List<Evento> eventos) {
        eventosPanel.removeAll();

        if (eventos.isEmpty()) {
            JPanel centralPanel = new JPanel();
            centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
            centralPanel.setBackground(FUNDO);
            centralPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblNenhum = new JLabel("<html><b>Nenhum evento encontrado.</b></html>", SwingConstants.CENTER);
            lblNenhum.setForeground(FONTE);
            lblNenhum.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblNenhum.setAlignmentX(Component.CENTER_ALIGNMENT);

            centralPanel.add(Box.createVerticalGlue());
            centralPanel.add(lblNenhum);
            centralPanel.add(Box.createVerticalGlue());

            eventosPanel.setLayout(new BoxLayout(eventosPanel, BoxLayout.Y_AXIS));
            eventosPanel.add(Box.createVerticalGlue());
            eventosPanel.add(centralPanel);
            eventosPanel.add(Box.createVerticalGlue());
        } else {
            for (Evento evento : eventos) {
                JPanel painelEvento = new JPanel();
                painelEvento.setLayout(new BoxLayout(painelEvento, BoxLayout.Y_AXIS));
                painelEvento.setBackground(FUNDO);
                painelEvento.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                painelEvento.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Nome do evento e lista (negrito)
                JLabel lblNome = new JLabel("<html><b>" + evento.nomeEvento + " - " + (evento.nomeLista != null ? evento.nomeLista : "") + "</b></html>");
                lblNome.setFont(lblNome.getFont().deriveFont(Font.BOLD, 18f));
                lblNome.setForeground(FONTE);
                painelEvento.add(lblNome);

                // Descrição
                JLabel lblDescricao = new JLabel(evento.descricao != null ? evento.descricao : "");
                lblDescricao.setFont(lblDescricao.getFont().deriveFont(Font.PLAIN, 15f));
                lblDescricao.setForeground(FONTE);
                painelEvento.add(lblDescricao);

                // Data - Horário - Local (itálico)
                String dataStr = evento.data != null ? evento.data : "";
                String horarioStr = evento.horario != null ? evento.horario : "";
                String localStr = evento.local != null ? evento.local : "";
                JLabel lblInfo = new JLabel("<html><i>" + dataStr + " - " + horarioStr + " - " + localStr + "</i></html>");
                lblInfo.setFont(lblInfo.getFont().deriveFont(Font.ITALIC, 14f));
                lblInfo.setForeground(FONTE);
                painelEvento.add(lblInfo);

                eventosPanel.add(painelEvento);
                eventosPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Gap de 10px
            }
        }

        eventosPanel.revalidate();
        eventosPanel.repaint();
    }

    private List<Evento> buscarEventosDoUsuario(int idUsuario) {
        List<Evento> eventos = new ArrayList<>();
        try (Connection conn = new Conexao().getConnection()) {
            // Ordena por dataAlteracao DESC, depois dataCriacao DESC (mais recente primeiro)
            String sql = "SELECT nomeEvento, nomeLista, descricao, data, horario, local "
                    + "FROM eventos WHERE idUsuario = ? "
                    + "ORDER BY COALESCE(dataAlteracao, dataCriacao, NOW()) DESC";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idUsuario);
                ResultSet rs = stmt.executeQuery();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                while (rs.next()) {
                    String dataFormatada = "";
                    java.sql.Date dataSql = rs.getDate("data");
                    if (dataSql != null) {
                        dataFormatada = sdf.format(dataSql);
                    }
                    eventos.add(new Evento(
                            rs.getString("nomeEvento"),
                            rs.getString("nomeLista"),
                            rs.getString("descricao"),
                            dataFormatada,
                            rs.getString("horario"),
                            rs.getString("local")
                    ));
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar eventos: " + ex.getMessage());
        }
        return eventos;
    }

    // Utilitário para comparar datas no formato dd/MM/yyyy
    private int compararDatas(String dataA, String dataB) {
        if (dataA == null && dataB == null) {
            return 0;
        }
        if (dataA == null) {
            return -1;
        }
        if (dataB == null) {
            return 1;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dA = sdf.parse(dataA);
            Date dB = sdf.parse(dataB);
            return dA.compareTo(dB);
        } catch (Exception e) {
            return 0;
        }
    }

    // Utilitário para comparar horários no formato HH:mm
    private int compararHorarios(String horaA, String horaB) {
        if (horaA == null && horaB == null) {
            return 0;
        }
        if (horaA == null) {
            return -1;
        }
        if (horaB == null) {
            return 1;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date hA = sdf.parse(horaA);
            Date hB = sdf.parse(horaB);
            return hA.compareTo(hB);
        } catch (Exception e) {
            return 0;
        }
    }

    // Classe interna para representar um evento
    private static class Evento {

        String nomeEvento;
        String nomeLista;
        String descricao;
        String data;
        String horario;
        String local;

        Evento(String nomeEvento, String nomeLista, String descricao, String data, String horario, String local) {
            this.nomeEvento = nomeEvento;
            this.nomeLista = nomeLista;
            this.descricao = descricao;
            this.data = data;
            this.horario = horario;
            this.local = local;
        }
    }
}
