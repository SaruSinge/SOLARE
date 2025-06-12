// Feito por Guilherme e Samuel

package sjt.solar.a3.eventos;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

import sjt.solar.a3.Conexao;
import sjt.solar.a3.Usuario;

public class EditandoEvento extends JFrame {

    private Usuario usuario;
    private int eventoId;

    // Esse aqui são dos campos para informar os dados do evento ~ Guilherme
    private JTextField txtNome;
    private JTextField txtLista;
    private JTextArea txtDescricao;
    private JFormattedTextField txtData;
    private JFormattedTextField txtHorario;
    private JTextField txtLocal;

    // Método para ser chamado por outras classes ~ Guilherme
    public static void abrirTela(Usuario usuario, int eventoId, EditarEvento telaEditarEvento) {
        new EditandoEvento(usuario, eventoId, telaEditarEvento).setVisible(true);
    } // ele tem que receber o usuário, o ID do evento e a tela de edição para atualizar a lista de eventos depois de editar

    public EditandoEvento(Usuario usuario, int eventoId, EditarEvento telaEditarEvento) {
        this.usuario = usuario;
        this.eventoId = eventoId;
        setTitle("Editar Evento");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

        // Cores do layout ~ Samuel
        Color fundo = new Color(32, 38, 46);
        Color campo = new Color(44, 51, 61);
        Color fonte = Color.WHITE;
        Color cinzaEscuro = new Color(44, 51, 61);

        // Painel principal ~ GUilherme
        // Eu mudei o layout para "GridBagLayout" para ficar mais flexível e bonito ~ samuel

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(fundo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Título
        JLabel titulo = new JLabel("Editar Evento");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(fonte);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titulo, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;

        // Campos do evento ~ Guilherme ~ Samuel
        JLabel lblNome = new JLabel("Nome do Evento:");
        lblNome.setForeground(fonte);
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(lblNome, gbc);

        txtNome = new JTextField();
        txtNome.setBackground(campo);
        txtNome.setForeground(fonte);
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtNome.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 75)));
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        mainPanel.add(txtNome, gbc);

        gbc.gridwidth = 1;

        JLabel lblLista = new JLabel("Tipo/Lista:");
        lblLista.setForeground(fonte);
        lblLista.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(lblLista, gbc);

        txtLista = new JTextField();
        txtLista.setBackground(campo);
        txtLista.setForeground(fonte);
        txtLista.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtLista.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 75)));
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        mainPanel.add(txtLista, gbc);

        gbc.gridwidth = 1;

        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setForeground(fonte);
        lblDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridy = 3;
        gbc.gridx = 0;
        mainPanel.add(lblDescricao, gbc);

        txtDescricao = new JTextArea(7, 20);
        txtDescricao.setBackground(campo);
        txtDescricao.setForeground(fonte);
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 75)));
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollDescricao, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;

        JLabel lblData = new JLabel("Data:");
        lblData.setForeground(fonte);
        lblData.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridy = 4;
        gbc.gridx = 0;
        mainPanel.add(lblData, gbc);

        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            txtData = new JFormattedTextField(dateMask);
        } catch (ParseException ex) {
            txtData = new JFormattedTextField();
        }
        txtData.setBackground(campo);
        txtData.setForeground(fonte);
        txtData.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtData.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 75)));
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        mainPanel.add(txtData, gbc);

        JLabel lblHorario = new JLabel("Horário:");
        lblHorario.setForeground(fonte);
        lblHorario.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 2;
        mainPanel.add(lblHorario, gbc);

        try {
            MaskFormatter hourMask = new MaskFormatter("##:##");
            hourMask.setPlaceholderCharacter('_');
            txtHorario = new JFormattedTextField(hourMask);
        } catch (ParseException ex) {
            txtHorario = new JFormattedTextField();
        }
        txtHorario.setBackground(campo);
        txtHorario.setForeground(fonte);
        txtHorario.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtHorario.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 75)));
        gbc.gridx = 3;
        mainPanel.add(txtHorario, gbc);

        JLabel lblLocal = new JLabel("Local:");
        lblLocal.setForeground(fonte);
        lblLocal.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridy = 5;
        gbc.gridx = 0;
        mainPanel.add(lblLocal, gbc);

        txtLocal = new JTextField();
        txtLocal.setBackground(campo);
        txtLocal.setForeground(fonte);
        txtLocal.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtLocal.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 75)));
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        mainPanel.add(txtLocal, gbc);

        // Painel de botões no fim da página ~Samuel
        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(fundo);
        painelBotoes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 10));

        JButton btnSalvar = new JButton("Salvar Modificação");
        btnSalvar.setBackground(cinzaEscuro);
        btnSalvar.setForeground(fonte);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSalvar.setFocusPainted(false);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(cinzaEscuro);
        btnVoltar.setForeground(fonte);
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnVoltar.setFocusPainted(false);

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnVoltar);

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(painelBotoes, gbc);

        // Carrega os dados do evento selecionado
        carregarDadosEvento();

        // Botão Voltar
        btnVoltar.addActionListener((ActionEvent e) -> {
            this.dispose();
            if (telaEditarEvento != null) {
                telaEditarEvento.atualizarListaEventos();
            }
        });

        // Acionamento do botão Salvar Modificação ~ Guilherme
        btnSalvar.addActionListener((ActionEvent e) -> {
            String nome = txtNome.getText().trim();
            String lista = txtLista.getText().trim();
            String descricao = txtDescricao.getText().trim();
            String local = txtLocal.getText().trim();
            String dataStr = txtData.getText().trim();
            String horario = txtHorario.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo Nome do Evento é obrigatório!");
                return;
            }

            Date dataSql = null;
            if (!dataStr.replaceAll("[_/]", "").isEmpty()) {
                try {
                    java.util.Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
                    dataSql = new Date(utilDate.getTime());
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Data inválida! Use o formato dd/MM/yyyy.");
                    return;
                }
            }

            try (Connection conn = Conexao.getConnection()) {
                String sql = "UPDATE eventos SET nomeEvento=?, nomeLista=?, descricao=?, data=?, horario=?, local=? WHERE id=? AND idUsuario=?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nome);
                    stmt.setString(2, lista.isEmpty() ? null : lista);
                    stmt.setString(3, descricao.isEmpty() ? null : descricao);
                    if (dataSql != null) {
                        stmt.setDate(4, dataSql);
                    } else {
                        stmt.setNull(4, java.sql.Types.DATE);
                    }
                    if (horario.equals("__:__") || horario.isEmpty()) {
                        stmt.setNull(5, java.sql.Types.TIME);
                    } else {
                        stmt.setString(5, horario);
                    }
                    stmt.setString(6, local.isEmpty() ? null : local);
                    stmt.setInt(7, eventoId);
                    stmt.setInt(8, usuario.getId());
                    stmt.executeUpdate();
                }
                this.dispose();
                if (telaEditarEvento != null) {
                    telaEditarEvento.atualizarListaEventos();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar evento: " + ex.getMessage());
            }
        });

        setContentPane(mainPanel);
    }

    // Carrega os dados do evento selecionado nos campos ~ Guilherme 
    // Ageitei o erro do SQL, era eventos, não evento ~ Samuel
    private void carregarDadosEvento() {
        try (Connection conn = Conexao.getConnection()) {
            String sql = "SELECT * FROM eventos WHERE id = ? AND idUsuario = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, eventoId);
                stmt.setInt(2, usuario.getId());
                try (java.sql.ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        txtNome.setText(rs.getString("nomeEvento"));
                        txtLista.setText(rs.getString("nomeLista"));
                        txtDescricao.setText(rs.getString("descricao"));
                        java.sql.Date data = rs.getDate("data");
                        if (data != null) {
                            txtData.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(data));
                        }
                        String horario = rs.getString("horario");
                        if (horario != null) {
                            txtHorario.setText(horario);
                        }
                        txtLocal.setText(rs.getString("local"));
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do evento: " + ex.getMessage());
        }
    }
}