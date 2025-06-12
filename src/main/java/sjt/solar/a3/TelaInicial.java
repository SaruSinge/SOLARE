// Feita por Giovanna

package sjt.solar.a3;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class TelaInicial extends javax.swing.JFrame {

    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;

    private static final Color FUNDO = new Color(32, 38, 46);
    private static final Color AMARELO_ESCURO = new Color(204, 168, 0);
    private static final Color FONTE = Color.WHITE;

    public TelaInicial() { //Depois só me explica o que é esse coiso aq ~ Giovanna
        initComponents();
    }
    // Tirei daqui aquele negócio que veio do NetBeans ~ Samuel
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("TelaInicial");

        JPanel painelCentral = new JPanel();
        painelCentral.setBackground(FUNDO);
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        painelCentral.add(Box.createVerticalGlue());

        // Título
        jLabel1.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 28));
        jLabel1.setForeground(FONTE);
        jLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLabel1.setText("BEM VINDO AO ORGANIZADOR SOLARE");

        // Mensagem embaixo lá
        jLabel2.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 20));
        jLabel2.setForeground(FONTE);
        jLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLabel2.setText("Crie e registre seus eventos e tarefas");

        jLabel3.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 20));
        jLabel3.setForeground(FONTE);
        jLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLabel3.setText("para se organizar melhor!");

        // Botão Entrar (Vou deixa tudo nesse formato, e ce sabe que foi eu, blz? ~ Samuel)
        jToggleButton1.setBackground(AMARELO_ESCURO);
        jToggleButton1.setForeground(FONTE);
        jToggleButton1.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18));
        jToggleButton1.setText("Entrar");
        jToggleButton1.setFocusPainted(false);
        jToggleButton1.setAlignmentX(Component.CENTER_ALIGNMENT);
        jToggleButton1.setMaximumSize(new Dimension(224, 49));
        jToggleButton1.setPreferredSize(new Dimension(224, 49));
        jToggleButton1.addActionListener(evt -> jToggleButton1ActionPerformed(evt));

        // Botão Cadastre-se
        jToggleButton2.setBackground(AMARELO_ESCURO);
        jToggleButton2.setForeground(FONTE);
        jToggleButton2.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 18));
        jToggleButton2.setText("Cadastre-se");
        jToggleButton2.setFocusPainted(false);
        jToggleButton2.setAlignmentX(Component.CENTER_ALIGNMENT);
        jToggleButton2.setMaximumSize(new Dimension(224, 49));
        jToggleButton2.setPreferredSize(new Dimension(224, 49));
        jToggleButton2.addActionListener(evt -> jToggleButton2ActionPerformed(evt));

        painelCentral.add(jLabel1);
        painelCentral.add(Box.createVerticalStrut(30));
        painelCentral.add(jLabel2);
        painelCentral.add(Box.createVerticalStrut(8));
        painelCentral.add(jLabel3);
        painelCentral.add(Box.createVerticalStrut(60));
        painelCentral.add(jToggleButton1);
        painelCentral.add(Box.createVerticalStrut(18));
        painelCentral.add(jToggleButton2);
        painelCentral.add(Box.createVerticalGlue());

        setContentPane(painelCentral);
        setSize(new java.awt.Dimension(700, 500));
        setLocationRelativeTo(null);
    }

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) { // Deixa esse "evt" aí, que é o evento do botão, pq ele ferra todo o código se tirar ~ Giovanna
        try {
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, e); // Ele pediu p mudar isso, mas não sei se tá certo... pelo menos tá funfando ~ Samuel
        }
    }

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        TelaCadastro telaCadastro = new TelaCadastro();
        telaCadastro.setVisible(true);
        this.dispose();
    }

    // Mas esse deixa, tira? Faz oq? ~ Gi
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex); // Mudei que nem o outro, pq tava me irritando com esse erro ~ Samuel
        }

        java.awt.EventQueue.invokeLater(() -> new TelaInicial().setVisible(true));
    }
}