// Feito por GUilherme e Samuel

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

    private final JPanel mainPanel; // mudei pra final, pq tava pedindo ~ Samuel
    private final JPanel eventosPanel; // mudei pra final, pq tava pedindo ~ Samuel
    private List<Evento> eventosOriginais = new ArrayList<>();
    private boolean voltando = false; // O mesmo que o outro lá no dados cadastro ~ Guilherme

    // Da pro gasto ~ Samuel
    private static final Color FUNDO = new Color(32, 38, 46);
    private static final Color FONTE = Color.WHITE;
    private static final Color AMARELO_ESCURO = new Color(204, 168, 0);
    private static final Color CINZA_ESCURO = new Color(44, 51, 61);

    public static void abrirTela(Usuario usuario) { // Vo deixar aq e não mexa ~ Guilherme
        new ExibirEventos(usuario).setVisible(true);
    }

    public ExibirEventos(Usuario usuario) {
        setTitle("Meus Eventos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);

        // Fechando pelo X, volta pra TelaPrincipal (Foi o que eu entendi) ~ Guilherme
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (!voltando) {
                    new TelaPrincipal(usuario).setVisible(true);
                }
            }
        });
        // Não mexe em time que tá ganhando kakakkakakak ~ Samuel

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(FUNDO);

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


        // Painel dos botões de ordenação lá de cima ~ Samuel
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

        // Não altere nada aq sem me falar. Eu mal dormi por causa dessa birósca ~ Samuel
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

        JPanel painelEventosAlinhado = new JPanel();
        painelEventosAlinhado.setLayout(new BoxLayout(painelEventosAlinhado, BoxLayout.X_AXIS));
        painelEventosAlinhado.setBackground(FUNDO);
        painelEventosAlinhado.add(Box.createRigidArea(new Dimension(30, 0))); // margem esquerda igual aos botões
        painelEventosAlinhado.add(scrollPane);

        painelCentro.add(painelEventosAlinhado);

        mainPanel.add(painelCentro);

        eventosOriginais = buscarEventosDoUsuario(usuario.getId());

        exibirEventos(eventosOriginais);

        setContentPane(mainPanel);
    }

    // Método pra exibir os eventos no painel que tá configurado ~ Guilherme
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
                eventosPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
            }
        }

        eventosPanel.revalidate();
        eventosPanel.repaint();
    }

    private List<Evento> buscarEventosDoUsuario(int idUsuario) {
        List<Evento> eventos = new ArrayList<>();
        try (Connection conn = Conexao.getConnection()) {
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

    private static class Evento { // Não mexe, por favor, confia em mim~ Samuel

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
