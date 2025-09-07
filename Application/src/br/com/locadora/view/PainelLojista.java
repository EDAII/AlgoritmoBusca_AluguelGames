package br.com.locadora.view;

import br.com.locadora.data.GerenciadorJogos;
import br.com.locadora.model.Jogo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

public class PainelLojista extends JPanel {

    private final GerenciadorJogos gerenciador;
    private JTable tabelaJogos;
    private DefaultTableModel modeloTabela;

    private JTextField txtId;
    private JTextField txtTitulo;
    private JTextField txtPlataforma;
    private JTextField txtGenero;

    public PainelLojista(GerenciadorJogos gerenciador) {
        this.gerenciador = gerenciador;
        setLayout(new BorderLayout(10, 10));

        criarTabela();
        JScrollPane painelTabela = new JScrollPane(tabelaJogos);
        add(painelTabela, BorderLayout.CENTER);

        JPanel painelFormulario = criarPainelFormulario();
        add(painelFormulario, BorderLayout.EAST);

        atualizarTabela();
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtId = new JTextField(15);
        txtTitulo = new JTextField(15);
        txtPlataforma = new JTextField(15);
        txtGenero = new JTextField(15);

        txtId.setEditable(false);
        txtId.setBackground(new Color(230, 230, 230));
        txtId.setText("<Automático>");

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; form.add(txtId, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; form.add(txtTitulo, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Plataforma:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; form.add(txtPlataforma, gbc);
        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Gênero:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; form.add(txtGenero, gbc);

        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");
        JButton btnLimpar = new JButton("Limpar Campos");

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnLimpar);

        painel.add(form);
        painel.add(Box.createVerticalStrut(20));
        painel.add(painelBotoes);

        btnAdicionar.addActionListener(e -> adicionarJogo());
        btnAtualizar.addActionListener(e -> atualizarJogo());
        btnRemover.addActionListener(e -> removerJogo());
        btnLimpar.addActionListener(e -> limparCampos());

        return painel;
    }

    public void atualizarTabela() {
        modeloTabela.setRowCount(0);
        Collection<Jogo> jogos = gerenciador.listarTodos();
        for (Jogo jogo : jogos) {
            modeloTabela.addRow(new Object[]{
                    jogo.getId(),
                    jogo.getTitulo(),
                    jogo.getPlataforma(),
                    jogo.getGenero(),
                    jogo.isDisponivel() ? "Disponível" : "Alugado"
            });
        }
    }

    private void criarTabela() {
        String[] colunas = {"ID", "Título", "Plataforma", "Gênero", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaJogos = new JTable(modeloTabela);
        tabelaJogos.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tabelaJogos.getSelectedRow() != -1) {
                preencherFormularioComLinhaSelecionada();
            }
        });
    }

    private void preencherFormularioComLinhaSelecionada() {
        int linhaSelecionada = tabelaJogos.getSelectedRow();
        if (linhaSelecionada != -1) {
            txtId.setText(modeloTabela.getValueAt(linhaSelecionada, 0).toString());
            txtTitulo.setText(modeloTabela.getValueAt(linhaSelecionada, 1).toString());
            txtPlataforma.setText(modeloTabela.getValueAt(linhaSelecionada, 2).toString());
            txtGenero.setText(modeloTabela.getValueAt(linhaSelecionada, 3).toString());
        }
    }

    private void limparCampos() {
        txtId.setText("<Automático>");
        txtTitulo.setText("");
        txtPlataforma.setText("");
        txtGenero.setText("");
        tabelaJogos.clearSelection();
    }

    private void adicionarJogo() {
        String titulo = txtTitulo.getText();
        String plataforma = txtPlataforma.getText();
        String genero = txtGenero.getText();

        if (titulo.isEmpty() || plataforma.isEmpty() || genero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos (exceto ID) devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        gerenciador.adicionarJogo(titulo, plataforma, genero);

        // Notifica a janela principal para atualizar TODAS as abas
        Component parent = this.getTopLevelAncestor();
        if (parent instanceof TelaPrincipal) {
            ((TelaPrincipal) parent).atualizarTodasAsTabelas();
        }

        limparCampos();
    }

    private void atualizarJogo() {
        try {
            int id = Integer.parseInt(txtId.getText());
            Jogo jogoAtualizado = new Jogo(id, txtTitulo.getText(), txtPlataforma.getText(), txtGenero.getText());
            gerenciador.buscarJogo(id).ifPresent(jogoAntigo -> jogoAtualizado.setDisponivel(jogoAntigo.isDisponivel()));
            gerenciador.atualizarJogo(jogoAtualizado);

            Component parent = this.getTopLevelAncestor();
            if (parent instanceof TelaPrincipal) {
                ((TelaPrincipal) parent).atualizarTodasAsTabelas();
            }

            limparCampos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Selecione um item da tabela para atualizar.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerJogo() {
        int linhaSelecionada = tabelaJogos.getSelectedRow();
        if (linhaSelecionada != -1) {
            int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover o jogo permanentemente?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                gerenciador.removerJogo(id);
                Component parent = this.getTopLevelAncestor();
                if (parent instanceof TelaPrincipal) {
                    ((TelaPrincipal) parent).atualizarTodasAsTabelas();
                }
                limparCampos();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item da tabela para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}