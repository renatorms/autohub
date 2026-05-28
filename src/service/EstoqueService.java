package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import exception.DadosInvalidosException;
import exception.VeiculoIndisponivelException;
import exception.VeiculoNaoEncontradoException;
import model.Veiculo;

public class EstoqueService {
    private List<Veiculo> estoque;

    public EstoqueService() {
        this.estoque = new ArrayList<>();
    }

    public void adicionar(Veiculo veiculo) throws DadosInvalidosException {
        validarVeiculo(veiculo);

        if (existePlaca(veiculo.getPlaca())) {
            throw new DadosInvalidosException("Já existe um veículo cadastrado com essa placa.");
        }

        estoque.add(veiculo);
    }

    public List<Veiculo> listarTodos() {
        return new ArrayList<>(estoque);
    }

    public List<Veiculo> buscar(String palavraChave)
            throws DadosInvalidosException, VeiculoNaoEncontradoException {
        validarPalavraChave(palavraChave);

        List<Veiculo> encontrados = new ArrayList<>();
        String termo = palavraChave.toLowerCase();

        for (Veiculo veiculo : estoque) {
            if (correspondeBusca(veiculo, termo)) {
                encontrados.add(veiculo);
            }
        }

        if (encontrados.isEmpty()) {
            throw new VeiculoNaoEncontradoException("Nenhum veículo encontrado com a palavra-chave informada.");
        }

        return encontrados;
    }

    public void remover(String palavraChave)
            throws DadosInvalidosException, VeiculoNaoEncontradoException {
        int indice = encontrarIndicePorPalavraChave(palavraChave);
        estoque.remove(indice);
    }

    public void editar(String palavraChave, Veiculo veiculoAtualizado)
            throws DadosInvalidosException, VeiculoNaoEncontradoException {
        int indice = encontrarIndicePorPalavraChave(palavraChave);

        validarVeiculo(veiculoAtualizado);

        if (existePlacaEmOutroVeiculo(veiculoAtualizado.getPlaca(), indice)) {
            throw new DadosInvalidosException("Já existe outro veículo cadastrado com essa placa.");
        }

        estoque.set(indice, veiculoAtualizado);
    }

    public void registrarVenda(String palavraChave)
            throws DadosInvalidosException, VeiculoNaoEncontradoException, VeiculoIndisponivelException {
        int indice = encontrarIndicePorPalavraChave(palavraChave);
        Veiculo veiculo = estoque.get(indice);

        if (!veiculo.podeSerVendido()) {
            throw new VeiculoIndisponivelException("Este veículo já foi vendido.");
        }

        veiculo.registrarVenda();
    }

    public void retornarAoEstoque(String palavraChave)
            throws DadosInvalidosException, VeiculoNaoEncontradoException {
        int indice = encontrarIndicePorPalavraChave(palavraChave);
        estoque.get(indice).retornarAoEstoque();
    }

    public void substituirEstoque(List<Veiculo> novoEstoque) throws DadosInvalidosException {
        if (novoEstoque == null) {
            throw new DadosInvalidosException("A lista de veículos não pode ser nula.");
        }

        List<Veiculo> estoqueValidado = new ArrayList<>();

        for (Veiculo veiculo : novoEstoque) {
            validarVeiculo(veiculo);
            estoqueValidado.add(veiculo);
        }

        this.estoque = estoqueValidado;
    }

    private int encontrarIndicePorPalavraChave(String palavraChave)
            throws DadosInvalidosException, VeiculoNaoEncontradoException {
        validarPalavraChave(palavraChave);

        String termo = palavraChave.toLowerCase();

        for (int i = 0; i < estoque.size(); i++) {
            if (correspondeBusca(estoque.get(i), termo)) {
                return i;
            }
        }

        throw new VeiculoNaoEncontradoException("Nenhum veículo encontrado com a palavra-chave informada.");
    }

    private boolean correspondeBusca(Veiculo veiculo, String termo) {
        return contem(veiculo.getMarca(), termo)
                || contem(veiculo.getModelo(), termo)
                || contem(veiculo.getCor(), termo)
                || contem(veiculo.getPlaca(), termo)
                || contem(veiculo.getTipo(), termo)
                || String.valueOf(veiculo.getAno()).contains(termo);
    }

    private boolean contem(String texto, String termo) {
        return texto != null && texto.toLowerCase().contains(termo);
    }

    private boolean existePlaca(String placa) {
        for (Veiculo veiculo : estoque) {
            if (veiculo.getPlaca().equalsIgnoreCase(placa)) {
                return true;
            }
        }

        return false;
    }

    private boolean existePlacaEmOutroVeiculo(String placa, int indiceIgnorado) {
        for (int i = 0; i < estoque.size(); i++) {
            if (i != indiceIgnorado && estoque.get(i).getPlaca().equalsIgnoreCase(placa)) {
                return true;
            }
        }

        return false;
    }

    private void validarPalavraChave(String palavraChave) throws DadosInvalidosException {
        if (palavraChave == null || palavraChave.trim().isEmpty()) {
            throw new DadosInvalidosException("A palavra-chave de busca não pode estar vazia.");
        }
    }

    private void validarVeiculo(Veiculo veiculo) throws DadosInvalidosException {
        if (veiculo == null) {
            throw new DadosInvalidosException("O veículo não pode ser nulo.");
        }

        if (campoVazio(veiculo.getMarca())) {
            throw new DadosInvalidosException("A marca do veículo é obrigatória.");
        }

        if (campoVazio(veiculo.getModelo())) {
            throw new DadosInvalidosException("O modelo do veículo é obrigatório.");
        }

        if (campoVazio(veiculo.getCor())) {
            throw new DadosInvalidosException("A cor do veículo é obrigatória.");
        }

        if (campoVazio(veiculo.getPlaca())) {
            throw new DadosInvalidosException("A placa do veículo é obrigatória.");
        }

        int anoAtual = LocalDate.now().getYear();

        if (veiculo.getAno() < 1900 || veiculo.getAno() > anoAtual + 1) {
            throw new DadosInvalidosException("O ano do veículo é inválido.");
        }

        if (veiculo.getQuilometragem() < 0) {
            throw new DadosInvalidosException("A quilometragem não pode ser negativa.");
        }

        if (veiculo.getPrecoBase() <= 0) {
            throw new DadosInvalidosException("O preço base deve ser maior que zero.");
        }
    }

    private boolean campoVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}