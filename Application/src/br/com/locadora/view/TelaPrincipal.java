package br.com.locadora.view;

import br.com.locadora.data.GerenciadorJogos;
import javax.swing.*;

public class TelaPrincipal extends JFrame {

    private final GerenciadorJogos gerenciador;
    private PainelLojista painelLojista;
    private PainelCliente painelCliente;

    public TelaPrincipal() {
        super("Sistema de Aluguel de Jogos");

        this.gerenciador = new GerenciadorJogos();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);

        JTabbedPane abas = new JTabbedPane();

        painelLojista = new PainelLojista(gerenciador);
        painelCliente = new PainelCliente(gerenciador, this); // Passa a referência da tela principal

        abas.addTab("Área do Lojista", painelLojista);
        abas.addTab("Área do Cliente", painelCliente);

        add(abas);
    }

    public void atualizarTodasAsTabelas() {
        painelLojista.atualizarTabela();
        painelCliente.atualizarTabela();
    }
}