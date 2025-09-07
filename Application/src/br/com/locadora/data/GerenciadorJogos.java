package br.com.locadora.data;

import br.com.locadora.model.Jogo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class GerenciadorJogos {

    private final List<Jogo> listaDeJogos;
    private final Comparator<Jogo> comparadorPorId;
    private int proximoId;

    public GerenciadorJogos() {
        this.listaDeJogos = new ArrayList<>();
        this.comparadorPorId = Comparator.comparingInt(Jogo::getId);
        carregarDadosIniciais();
        this.proximoId = listaDeJogos.stream().mapToInt(Jogo::getId).max().orElse(100) + 1;
    }

    private int buscaBinariaManual(int id) {
        int inicio = 0;
        int fim = listaDeJogos.size() - 1;

        while (inicio <= fim) {
            int meio = inicio + (fim - inicio) / 2;

            Jogo jogoDoMeio = listaDeJogos.get(meio);

            if (jogoDoMeio.getId() == id) {
                return meio;
            }

            if (id < jogoDoMeio.getId()) {
                fim = meio - 1;
            } else {
                inicio = meio + 1;
            }
        }

        return -1;
    }

    public Optional<Jogo> buscarJogo(int id) {
        int index = buscaBinariaManual(id);

        return (index != -1) ? Optional.of(listaDeJogos.get(index)) : Optional.empty();
    }

    public void adicionarJogo(String titulo, String plataforma, String genero) {
        Jogo novoJogo = new Jogo(proximoId++, titulo, plataforma, genero);
        listaDeJogos.add(novoJogo);
        listaDeJogos.sort(comparadorPorId);
    }

    private void carregarDadosIniciais() {
        listaDeJogos.add(new Jogo(104, "Cyberpunk 2077", "PC", "RPG"));
        listaDeJogos.add(new Jogo(102, "Red Dead Redemption 2", "PS4", "Ação/Aventura"));
        listaDeJogos.add(new Jogo(101, "The Witcher 3: Wild Hunt", "PC", "RPG"));
        listaDeJogos.add(new Jogo(103, "The Legend of Zelda: Breath of the Wild", "Switch", "Aventura"));
        listaDeJogos.sort(comparadorPorId);
    }

    public List<Jogo> listarTodos() {
        return new ArrayList<>(listaDeJogos);
    }

    public void atualizarJogo(Jogo jogoAtualizado) {
        int index = buscaBinariaManual(jogoAtualizado.getId());
        if (index != -1) {
            listaDeJogos.set(index, jogoAtualizado);
        } else {
            System.err.println("Erro: Jogo não encontrado para atualização.");
        }
    }

    public void removerJogo(int id) {
        int index = buscaBinariaManual(id);
        if (index != -1) {
            listaDeJogos.remove(index);
        }
    }

    public boolean alugarJogo(int id) {
        Optional<Jogo> jogoOpt = buscarJogo(id);
        if (jogoOpt.isPresent() && jogoOpt.get().isDisponivel()) {
            jogoOpt.get().setDisponivel(false);
            return true;
        }
        return false;
    }

    public boolean devolverJogo(int id) {
        Optional<Jogo> jogoOpt = buscarJogo(id);
        if (jogoOpt.isPresent() && !jogoOpt.get().isDisponivel()) {
            jogoOpt.get().setDisponivel(true);
            return true;
        }
        return false;
    }
}