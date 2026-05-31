package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import exception.DadosInvalidosException;
import exception.FalhaPersistenciaException;
import exception.VeiculoIndisponivelException;
import exception.VeiculoNaoEncontradoException;
import model.Caminhao;
import model.Carro;
import model.Moto;
import model.Negociavel;
import model.Veiculo;
import persistence.EstoqueRepository;
import service.EstoqueService;

public class AutoHubFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final String CAMINHO_ARQUIVO = "dados/estoque.dat";

    private EstoqueService estoqueService;
    private EstoqueRepository estoqueRepository;

    private JTextArea areaResultado;
    private JTextField campoBusca;

    public AutoHubFrame() {
        this.estoqueService = new EstoqueService();
        this.estoqueRepository = new EstoqueRepository();

        setTitle("AutoHub - Sistema de Revenda de Veículos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        configurarComponentes();
        carregarDadosAutomaticamente();
        atualizarListagem();
    }

    private void configurarComponentes() {
        setLayout(new BorderLayout());

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(areaResultado);
        add(scrollPane, BorderLayout.CENTER);

        JPanel painelSuperior = new JPanel(new BorderLayout());

        campoBusca = new JTextField();
        campoBusca.setToolTipText("Digite marca, modelo, placa, cor, tipo ou ano para buscar");

        painelSuperior.add(new javax.swing.JLabel(" Buscar por palavra-chave: "), BorderLayout.WEST);
        painelSuperior.add(campoBusca, BorderLayout.CENTER);

        add(painelSuperior, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new GridLayout(3, 4));

        JButton botaoAdicionarCarro = new JButton("Adicionar Carro");
        JButton botaoAdicionarMoto = new JButton("Adicionar Moto");
        JButton botaoAdicionarCaminhao = new JButton("Adicionar Caminhão");
        JButton botaoBuscar = new JButton("Buscar");

        JButton botaoEditar = new JButton("Editar");
        JButton botaoRemover = new JButton("Remover");
        JButton botaoVenda = new JButton("Registrar Venda");
        JButton botaoRetornar = new JButton("Retornar ao Estoque");

        JButton botaoFinanciamento = new JButton("Simular Parcelas");
        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoCarregar = new JButton("Carregar");
        JButton botaoListar = new JButton("Listar Todos");

        painelBotoes.add(botaoAdicionarCarro);
        painelBotoes.add(botaoAdicionarMoto);
        painelBotoes.add(botaoAdicionarCaminhao);
        painelBotoes.add(botaoBuscar);

        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoRemover);
        painelBotoes.add(botaoVenda);
        painelBotoes.add(botaoRetornar);

        painelBotoes.add(botaoFinanciamento);
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoCarregar);
        painelBotoes.add(botaoListar);

        add(painelBotoes, BorderLayout.SOUTH);

        botaoAdicionarCarro.addActionListener(e -> adicionarCarro());
        botaoAdicionarMoto.addActionListener(e -> adicionarMoto());
        botaoAdicionarCaminhao.addActionListener(e -> adicionarCaminhao());
        botaoBuscar.addActionListener(e -> buscarVeiculos());

        botaoEditar.addActionListener(e -> editarVeiculo());
        botaoRemover.addActionListener(e -> removerVeiculo());
        botaoVenda.addActionListener(e -> registrarVenda());
        botaoRetornar.addActionListener(e -> retornarAoEstoque());

        botaoFinanciamento.addActionListener(e -> simularFinanciamento());
        botaoSalvar.addActionListener(e -> salvarDados());
        botaoCarregar.addActionListener(e -> carregarDados());
        botaoListar.addActionListener(e -> atualizarListagem());
    }

    private void adicionarCarro() {
        try {
            Carro carro = criarCarro();

            estoqueService.adicionar(carro);
            salvarDadosAutomaticamente();

            JOptionPane.showMessageDialog(this, "Carro cadastrado com sucesso.");
            atualizarListagem();
        } catch (DadosInvalidosException erro) {
            mostrarErro("Erro de validação", erro.getMessage());
        } catch (NumberFormatException erro) {
            mostrarErro("Erro de formato", "Informe valores numéricos válidos.");
        }
    }

    private void adicionarMoto() {
        try {
            Moto moto = criarMoto();

            estoqueService.adicionar(moto);
            salvarDadosAutomaticamente();

            JOptionPane.showMessageDialog(this, "Moto cadastrada com sucesso.");
            atualizarListagem();
        } catch (DadosInvalidosException erro) {
            mostrarErro("Erro de validação", erro.getMessage());
        } catch (NumberFormatException erro) {
            mostrarErro("Erro de formato", "Informe valores numéricos válidos.");
        }
    }

    private void adicionarCaminhao() {
        try {
            Caminhao caminhao = criarCaminhao();

            estoqueService.adicionar(caminhao);
            salvarDadosAutomaticamente();

            JOptionPane.showMessageDialog(this, "Caminhão cadastrado com sucesso.");
            atualizarListagem();
        } catch (DadosInvalidosException erro) {
            mostrarErro("Erro de validação", erro.getMessage());
        } catch (NumberFormatException erro) {
            mostrarErro("Erro de formato", "Informe valores numéricos válidos.");
        }
    }

    private void buscarVeiculos() {
        try {
            String palavraChave = obterPalavraChave();
            List<Veiculo> encontrados = estoqueService.buscar(palavraChave);

            exibirVeiculos(encontrados);
        } catch (DadosInvalidosException | VeiculoNaoEncontradoException erro) {
            mostrarErro("Erro na busca", erro.getMessage());
        }
    }

    private void removerVeiculo() {
        try {
            String palavraChave = obterPalavraChave();

            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente remover o primeiro veículo encontrado?",
                    "Confirmar remoção",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                estoqueService.remover(palavraChave);
                salvarDadosAutomaticamente();

                JOptionPane.showMessageDialog(this, "Veículo removido com sucesso.");
                atualizarListagem();
            }
        } catch (DadosInvalidosException | VeiculoNaoEncontradoException erro) {
            mostrarErro("Erro ao remover", erro.getMessage());
        }
    }

    private void editarVeiculo() {
        try {
            String palavraChave = obterPalavraChave();
            Veiculo veiculoOriginal = estoqueService.buscar(palavraChave).get(0);

            Veiculo veiculoAtualizado = criarVeiculoPeloTipo(veiculoOriginal.getTipo());

            if (veiculoOriginal.isVendido()) {
                veiculoAtualizado.registrarVenda();
            }

            estoqueService.editar(palavraChave, veiculoAtualizado);
            salvarDadosAutomaticamente();

            JOptionPane.showMessageDialog(this, "Veículo editado com sucesso.");
            atualizarListagem();
        } catch (DadosInvalidosException | VeiculoNaoEncontradoException erro) {
            mostrarErro("Erro ao editar", erro.getMessage());
        } catch (NumberFormatException erro) {
            mostrarErro("Erro de formato", "Informe valores numéricos válidos.");
        } catch (IllegalArgumentException erro) {
            mostrarErro("Erro ao editar", erro.getMessage());
        }
    }

    private void registrarVenda() {
        try {
            String palavraChave = obterPalavraChave();

            estoqueService.registrarVenda(palavraChave);
            salvarDadosAutomaticamente();

            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso.");
            atualizarListagem();
        } catch (DadosInvalidosException | VeiculoNaoEncontradoException | VeiculoIndisponivelException erro) {
            mostrarErro("Erro ao registrar venda", erro.getMessage());
        }
    }

    private void retornarAoEstoque() {
        try {
            String palavraChave = obterPalavraChave();

            estoqueService.retornarAoEstoque(palavraChave);
            salvarDadosAutomaticamente();

            JOptionPane.showMessageDialog(this, "Veículo retornou ao estoque.");
            atualizarListagem();
        } catch (DadosInvalidosException | VeiculoNaoEncontradoException erro) {
            mostrarErro("Erro ao retornar ao estoque", erro.getMessage());
        }
    }

    private void simularFinanciamento() {
        try {
            String palavraChave = obterPalavraChave();
            Veiculo veiculo = estoqueService.buscar(palavraChave).get(0);

            if (!(veiculo instanceof Negociavel)) {
                mostrarErro("Erro", "Este veículo não pode ser negociado.");
                return;
            }

            int quantidadeParcelas = Integer.parseInt(JOptionPane.showInputDialog(this, "Quantidade de parcelas:"));

            Negociavel negociavel = (Negociavel) veiculo;

            double entrada = negociavel.calcularEntrada();
            double parcela = negociavel.calcularParcela(quantidadeParcelas);

            JOptionPane.showMessageDialog(this,
                    "Veículo: " + veiculo.getDescricao()
                            + "\nPreço final: " + formatarDinheiro(veiculo.calcularPreco())
                            + "\nEntrada: " + formatarDinheiro(entrada)
                            + "\nParcelas: " + quantidadeParcelas
                            + "\nValor da parcela: " + formatarDinheiro(parcela));
        } catch (DadosInvalidosException | VeiculoNaoEncontradoException erro) {
            mostrarErro("Erro na simulação", erro.getMessage());
        } catch (NumberFormatException erro) {
            mostrarErro("Erro de formato", "Informe uma quantidade de parcelas válida.");
        } catch (IllegalArgumentException erro) {
            mostrarErro("Erro de valor", erro.getMessage());
        }
    }

    private void salvarDados() {
        try {
            estoqueRepository.salvar(estoqueService.listarTodos(), CAMINHO_ARQUIVO);
            JOptionPane.showMessageDialog(this, "Dados salvos com sucesso.");
        } catch (FalhaPersistenciaException erro) {
            mostrarErro("Erro ao salvar", erro.getMessage());
        }
    }

    private void carregarDados() {
        try {
            List<Veiculo> estoqueCarregado = estoqueRepository.carregar(CAMINHO_ARQUIVO);
            estoqueService.substituirEstoque(estoqueCarregado);

            JOptionPane.showMessageDialog(this, "Dados carregados com sucesso.");
            atualizarListagem();
        } catch (FalhaPersistenciaException | DadosInvalidosException erro) {
            mostrarErro("Erro ao carregar", erro.getMessage());
        }
    }

    private void salvarDadosAutomaticamente() {
        try {
            estoqueRepository.salvar(estoqueService.listarTodos(), CAMINHO_ARQUIVO);
        } catch (FalhaPersistenciaException erro) {
            mostrarErro("Erro ao salvar automaticamente", erro.getMessage());
        }
    }

    private void carregarDadosAutomaticamente() {
        try {
            List<Veiculo> estoqueCarregado = estoqueRepository.carregar(CAMINHO_ARQUIVO);
            estoqueService.substituirEstoque(estoqueCarregado);
        } catch (FalhaPersistenciaException | DadosInvalidosException erro) {
            mostrarErro("Erro ao carregar dados", erro.getMessage());
        }
    }

    private void atualizarListagem() {
        List<Veiculo> veiculos = estoqueService.listarTodos();
        exibirVeiculos(veiculos);
    }

    private void exibirVeiculos(List<Veiculo> veiculos) {
        if (veiculos.isEmpty()) {
            areaResultado.setText("Nenhum veículo cadastrado.");
            return;
        }

        StringBuilder texto = new StringBuilder();

        for (Veiculo veiculo : veiculos) {
            texto.append(formatarVeiculo(veiculo));
            texto.append("\n--------------------------------------------------\n");
        }

        areaResultado.setText(texto.toString());
    }

    private String formatarVeiculo(Veiculo veiculo) {
        StringBuilder texto = new StringBuilder();

        texto.append("Tipo: ").append(veiculo.getTipo()).append("\n");
        texto.append("Marca: ").append(veiculo.getMarca()).append("\n");
        texto.append("Modelo: ").append(veiculo.getModelo()).append("\n");
        texto.append("Ano: ").append(veiculo.getAno()).append("\n");
        texto.append("Cor: ").append(veiculo.getCor()).append("\n");
        texto.append("Placa: ").append(veiculo.getPlaca()).append("\n");
        texto.append("Quilometragem: ").append(veiculo.getQuilometragem()).append(" km\n");
        texto.append("Preço base: ").append(formatarDinheiro(veiculo.getPrecoBase())).append("\n");
        texto.append("Preço final: ").append(formatarDinheiro(veiculo.calcularPreco())).append("\n");
        texto.append("Situação: ").append(veiculo.isVendido() ? "Vendido" : "Disponível").append("\n");

        if (veiculo instanceof Carro) {
            Carro carro = (Carro) veiculo;
            texto.append("Portas: ").append(carro.getQuantidadePortas()).append("\n");
            texto.append("Câmbio: ").append(carro.isAutomatico() ? "Automático" : "Manual").append("\n");
        } else if (veiculo instanceof Moto) {
            Moto moto = (Moto) veiculo;
            texto.append("Cilindradas: ").append(moto.getCilindradas()).append("\n");
            texto.append("Versão: ").append(moto.getVersao()).append("\n");
        } else if (veiculo instanceof Caminhao) {
            Caminhao caminhao = (Caminhao) veiculo;
            texto.append("Capacidade de carga: ").append(caminhao.getCapacidadeCarga()).append("\n");
            texto.append("Quantidade de eixos: ").append(caminhao.getQuantidadeEixos()).append("\n");
        }

        return texto.toString();
    }

    private Carro criarCarro() {
        String marca = solicitarTexto("Marca:");
        String modelo = solicitarTexto("Modelo:");
        String cor = solicitarTexto("Cor:");
        String placa = solicitarTexto("Placa:");
        int ano = solicitarInteiro("Ano:");
        int quilometragem = solicitarInteiro("Quilometragem:");
        double precoBase = solicitarDouble("Preço base:");
        int quantidadePortas = solicitarInteiro("Quantidade de portas:");
        boolean automatico = solicitarBooleano("Automático? (s/n):");

        return new Carro(marca, modelo, cor, placa, ano, quilometragem, precoBase, quantidadePortas, automatico);
    }

    private Moto criarMoto() {
        String marca = solicitarTexto("Marca:");
        String modelo = solicitarTexto("Modelo:");
        String cor = solicitarTexto("Cor:");
        String placa = solicitarTexto("Placa:");
        int ano = solicitarInteiro("Ano:");
        int quilometragem = solicitarInteiro("Quilometragem:");
        double precoBase = solicitarDouble("Preço base:");
        int cilindradas = solicitarInteiro("Cilindradas:");
        boolean versaoCompleta = solicitarBooleano("Versão completa? (s/n):");

        return new Moto(marca, modelo, cor, placa, ano, quilometragem, precoBase, cilindradas, versaoCompleta);
    }

    private Caminhao criarCaminhao() {
        String marca = solicitarTexto("Marca:");
        String modelo = solicitarTexto("Modelo:");
        String cor = solicitarTexto("Cor:");
        String placa = solicitarTexto("Placa:");
        int ano = solicitarInteiro("Ano:");
        int quilometragem = solicitarInteiro("Quilometragem:");
        double precoBase = solicitarDouble("Preço base:");
        double capacidadeCarga = solicitarDouble("Capacidade de carga:");
        int quantidadeEixos = solicitarInteiro("Quantidade de eixos:");

        return new Caminhao(marca, modelo, cor, placa, ano, quilometragem, precoBase, capacidadeCarga,
                quantidadeEixos);
    }

    private Veiculo criarVeiculoPeloTipo(String tipo) {
        if ("Carro".equalsIgnoreCase(tipo)) {
            return criarCarro();
        }

        if ("Moto".equalsIgnoreCase(tipo)) {
            return criarMoto();
        }

        if ("Caminhão".equalsIgnoreCase(tipo) || "Caminhao".equalsIgnoreCase(tipo)) {
            return criarCaminhao();
        }

        throw new IllegalArgumentException("Tipo de veículo inválido.");
    }

    private String obterPalavraChave() {
        return campoBusca.getText();
    }

    private String solicitarTexto(String mensagem) {
        return JOptionPane.showInputDialog(this, mensagem);
    }

    private int solicitarInteiro(String mensagem) {
        return Integer.parseInt(JOptionPane.showInputDialog(this, mensagem));
    }

    private double solicitarDouble(String mensagem) {
        return Double.parseDouble(JOptionPane.showInputDialog(this, mensagem));
    }

    private boolean solicitarBooleano(String mensagem) {
        String resposta = JOptionPane.showInputDialog(this, mensagem);
        return resposta != null && resposta.equalsIgnoreCase("s");
    }

    private String formatarDinheiro(double valor) {
        return String.format("R$ %.2f", valor);
    }

    private void mostrarErro(String titulo, String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, JOptionPane.ERROR_MESSAGE);
    }
}