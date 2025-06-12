//Feito por Samuel e Guilherme

package sjt.solar.a3.eventos;

// É bastante biblioteca, mas é necessário para criar a tela de eventos :<
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.SwingUtilities;
import javax.swing.text.MaskFormatter;

import sjt.solar.a3.TelaPrincipal;
import sjt.solar.a3.Usuario;

public class CriarEvento extends JFrame {

    public CriarEvento(Usuario usuario, TelaPrincipal telaPrincipal) {
        setTitle("Crie seu Evento");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Ao fechar a janela, reabre a TelaPrincipal e atualiza a tabela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (telaPrincipal != null) {
                    telaPrincipal.setVisible(true);
                    telaPrincipal.carregarEventos(); // Atualiza a tabela ao voltar
                }
            }
        });

        // Cores
        Color fundo = new Color(32, 38, 46);
        Color campo = new Color(44, 51, 61);
        Color fonte = Color.WHITE;
        Color cinzaEscuro = new Color(44, 51, 61);

        // Painel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(fundo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Título
        JLabel titulo = new JLabel("Crie seu Evento");
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

        // Nome do Evento
        JLabel lblNome = new JLabel("Nome do Evento: *");
        lblNome.setForeground(fonte);
        lblNome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(lblNome, gbc);

        JTextField txtNome = new JTextField();
        txtNome.setBackground(campo);
        txtNome.setForeground(fonte);
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtNome.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 75)));
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        mainPanel.add(txtNome, gbc);

        gbc.gridwidth = 1;

        // Tipo/Lista
        JLabel lblLista = new JLabel("Marcador/Lista:");
        lblLista.setForeground(fonte);
        lblLista.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(lblLista, gbc);

        JTextField txtLista = new JTextField();
        txtLista.setBackground(campo);
        txtLista.setForeground(fonte);
        txtLista.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtLista.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 75)));
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        mainPanel.add(txtLista, gbc);

        gbc.gridwidth = 1;

// ...código anterior...
// Descrição
        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setForeground(fonte);
        lblDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridy = 3;
        gbc.gridx = 0;
        mainPanel.add(lblDescricao, gbc);

// Campo de descrição com altura 5x maior que os demais campos
        JTextArea txtDescricao = new JTextArea();
        txtDescricao.setBackground(campo);
        txtDescricao.setForeground(fonte);
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 75)));

// Calcula altura 5x maior que um campo padrão
        int alturaPadrao = txtNome.getPreferredSize().height;
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        scrollDescricao.setPreferredSize(new Dimension(0, alturaPadrao * 5 + 10));

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollDescricao, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;

// ...continuação do código...
        // Data
        JLabel lblData = new JLabel("Data do evento:");
        lblData.setForeground(fonte);
        lblData.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridy = 4;
        gbc.gridx = 0;
        mainPanel.add(lblData, gbc);

        final JFormattedTextField txtData;
        JFormattedTextField tempTxtData;
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            tempTxtData = new JFormattedTextField(dateMask);
        } catch (ParseException ex) {
            tempTxtData = new JFormattedTextField();
        }
        txtData = tempTxtData;
        txtData.setBackground(campo);
        txtData.setForeground(fonte);
        txtData.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtData.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 75)));
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        mainPanel.add(txtData, gbc);

        // Horário
        JLabel lblHorario = new JLabel("Horário:");
        lblHorario.setForeground(fonte);
        lblHorario.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 2;
        mainPanel.add(lblHorario, gbc);

        final JFormattedTextField txtHorario;
        JFormattedTextField tempTxtHorario;
        try {
            MaskFormatter hourMask = new MaskFormatter("##:##");
            hourMask.setPlaceholderCharacter('_');
            tempTxtHorario = new JFormattedTextField(hourMask);
        } catch (ParseException ex) {
            tempTxtHorario = new JFormattedTextField();
        }
        txtHorario = tempTxtHorario;
        txtHorario.setBackground(campo);
        txtHorario.setForeground(fonte);
        txtHorario.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtHorario.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 75)));
        gbc.gridx = 3;
        mainPanel.add(txtHorario, gbc);

        // Local
        JLabel lblLocal = new JLabel("Local:");
        lblLocal.setForeground(fonte);
        lblLocal.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridy = 5;
        gbc.gridx = 0;
        mainPanel.add(lblLocal, gbc);

        JTextField txtLocal = new JTextField();
        txtLocal.setBackground(campo);
        txtLocal.setForeground(fonte);
        txtLocal.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtLocal.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 75)));
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        mainPanel.add(txtLocal, gbc);

        // Botão Salvar Evento
        JButton btnSalvar = new JButton("Salvar Evento");
        btnSalvar.setBackground(new Color(204, 168, 0));
        btnSalvar.setForeground(fonte);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(btnSalvar, gbc);

        // Botão Voltar
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(cinzaEscuro);
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnVoltar.setFocusPainted(false);
        btnVoltar.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(btnVoltar, gbc);

        // Ação do botão Voltar
        btnVoltar.addActionListener(e -> {
            this.dispose();
            if (telaPrincipal != null) {
                telaPrincipal.setVisible(true);
                telaPrincipal.carregarEventos();
            }
        });

        // Ação do botão Salvar Evento
        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String lista = txtLista.getText().trim();
            String descricao = txtDescricao.getText().trim();
            String local = txtLocal.getText().trim();
            String dataStr = txtData.getText().trim();
            String horario = txtHorario.getText().trim();

            // Apenas nome é obrigatório
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo Nome é obrigatório!");
                return;
            }

            // Validação de data (se preenchida)
            Date dataSql = null;
            if (!dataStr.replaceAll("[_/]", "").isEmpty()) {
                String[] partes = dataStr.split("/");
                if (partes.length == 3) {
                    try {
                        int dia = Integer.parseInt(partes[0]);
                        int mes = Integer.parseInt(partes[1]);
                        if (dia < 1 || dia > 31) {
                            JOptionPane.showMessageDialog(this, "Dia inválido! Use um valor entre 1 e 31.");
                            return;
                        }
                        if (mes < 1 || mes > 12) {
                            JOptionPane.showMessageDialog(this, "Mês inválido! Use um valor entre 1 e 12.");
                            return;
                        }
                        java.util.Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
                        dataSql = new Date(utilDate.getTime());
                    } catch (NumberFormatException | ParseException ex) {
                        JOptionPane.showMessageDialog(this, "Data inválida! Use o formato dd/MM/yyyy.");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Data inválida! Use o formato dd/MM/yyyy.");
                    return;
                }
            }

            // Validação de horário (se preenchido)
            if (!horario.equals("__:__") && !horario.isEmpty()) {
                String[] partes = horario.split(":");
                if (partes.length == 2) {
                    try {
                        int horas = Integer.parseInt(partes[0]);
                        int minutos = Integer.parseInt(partes[1]);
                        if (horas < 0 || horas > 23) {
                            JOptionPane.showMessageDialog(this, "Hora inválida! Use um valor entre 00 e 23.");
                            return;
                        }
                        if (minutos < 0 || minutos > 59) {
                            JOptionPane.showMessageDialog(this, "Minutos inválidos! Use um valor entre 00 e 59.");
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Horário inválido! Use o formato HH:mm.");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Horário inválido! Use o formato HH:mm.");
                    return;
                }
            }

            // Aqui vai inserir o evento no banco de dados ~ Guilherme
            try (Connection conn = sjt.solar.a3.Conexao.getConnection()) {
                String sql = "INSERT INTO eventos (nomeEvento, nomeLista, descricao, data, horario, local, idUsuario) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
                    stmt.setInt(7, usuario.getId());
                    stmt.executeUpdate();
                }
                this.dispose();
                if (telaPrincipal != null) {
                    telaPrincipal.setVisible(true);
                    telaPrincipal.carregarEventos(); // Atualiza a tabela ao voltar
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar evento: " + ex.getMessage());
            }
        });

        setContentPane(mainPanel);
    }


    // Método main para testar a tela de criação de eventos - Copilot feito por mim ~ Guilherme
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "Abra esta tela passando um objeto Usuario válido!");
            // Exemplo de uso correto:
            // Usuario usuario = new Usuario(1, "nome", ...);
            // TelaPrincipal telaPrincipal = new TelaPrincipal(usuario);
            // new CriarEvento(usuario, telaPrincipal).setVisible(true);
        });
    }
}
