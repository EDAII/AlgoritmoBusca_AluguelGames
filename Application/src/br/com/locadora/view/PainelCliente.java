package br.com.locadora.view;

import br.com.locadora.data.GerenciadorJogos;
import br.com.locadora.model.Jogo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

public class PainelCliente extends JPanel {

    private final GerenciadorJogos gerenciador;
    private final TelaPrincipal telaPrincipal;
    private JTable tabelaJogos;
    private DefaultTableModel modeloTabela;
    private JButton btnAlugar, btnDevolver;

    public PainelCliente(GerenciadorJogos gerenciador, TelaPrincipal telaPrincipal) {
        this.gerenciador = gerenciador;
        this.telaPrincipal = telaPrincipal;
        setLayout(new BorderLayout(10, 10));

        criarTabela();
        JScrollPane painelTabela = new JScrollPane(tabelaJogos);
        add(painelTabela, BorderLayout.CENTER);

        criarPainelBotoes();
        atualizarTabela();
    }

    private void criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAlugar = new JButton("Alugar Jogo Selecionado");
        btnDevolver = new JButton("Devolver Jogo Selecionado");

        btnAlugar.setEnabled(false);
        btnDevolver.setEnabled(false);

        painel.add(btnAlugar);
        painel.add(btnDevolver);
        add(painel, BorderLayout.SOUTH);

        btnAlugar.addActionListener(e -> alugarJogo());
        btnDevolver.addActionListener(e -> devolverJogo());
    }

    private void criarTabela() {
        String[] colunas = {"ID", "Título", "Plataforma", "Gênero", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaJogos = new JTable(modeloTabela);

        tabelaJogos.getSelectionModel().addListSelectionListener(e -> {
            int linhaSelecionada = tabelaJogos.getSelectedRow();
            if (linhaSelecionada != -1) {
                String status = (String) modeloTabela.getValueAt(linhaSelecionada, 4);
                if (status.equals("Disponível")) {
                    btnAlugar.setEnabled(true);
                    btnDevolver.setEnabled(false);
                } else {
                    btnAlugar.setEnabled(false);
                    btnDevolver.setEnabled(true);
                }
            } else {
                btnAlugar.setEnabled(false);
                btnDevolver.setEnabled(false);
            }
        });
    }

    public void atualizarTabela() {
        int linhaSelecionada = tabelaJogos.getSelectedRow();

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

        if(linhaSelecionada >= 0 && linhaSelecionada < tabelaJogos.getRowCount()){
            tabelaJogos.setRowSelectionInterval(linhaSelecionada, linhaSelecionada);
        }
    }

    private void alugarJogo() {
        int linhaSelecionada = tabelaJogos.getSelectedRow();
        if (linhaSelecionada != -1) {
            int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            if (gerenciador.alugarJogo(id)) {
                JOptionPane.showMessageDialog(this, "Jogo alugado com sucesso!");
                telaPrincipal.atualizarTodasAsTabelas();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível alugar o jogo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void devolverJogo() {
        int linhaSelecionada = tabelaJogos.getSelectedRow();
        if (linhaSelecionada != -1) {
            int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            if (gerenciador.devolverJogo(id)) {
                JOptionPane.showMessageDialog(this, "Jogo devolvido com sucesso!");
                telaPrincipal.atualizarTodasAsTabelas();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível devolver o jogo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}