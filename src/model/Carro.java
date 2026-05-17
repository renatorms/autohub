package model;

public class Carro extends Veiculo implements Negociavel {
    private static final long serialVersionUID = 1L;

    private int quantidadePortas;
    private boolean automatico;

    public Carro(String marca, String modelo, String cor, String placa, int ano, int quilometragem,
            double precoBase, int quantidadePortas, boolean automatico) {
        super(marca, modelo, cor, placa, ano, quilometragem, precoBase);
        this.quantidadePortas = quantidadePortas;
        this.automatico = automatico;
    }

    public int getQuantidadePortas() {
        return quantidadePortas;
    }

    public boolean isAutomatico() {
        return automatico;
    }

    public void setQuantidadePortas(int quantidadePortas) {
        this.quantidadePortas = quantidadePortas;
    }

    public void setAutomatico(boolean automatico) {
        this.automatico = automatico;
    }

    @Override
    public double calcularPreco() {
        double precoFinal = getPrecoBase();

        if (automatico) {
            precoFinal += 2500.0;
        }

        if (quantidadePortas >= 4) {
            precoFinal += 1500.0;
        }

        return precoFinal;
    }

    @Override
    public String getTipo() {
        return "Carro";
    }

    @Override
    public double calcularEntrada() {
        return calcularPreco() * 0.20;
    }

    @Override
    public double calcularParcela(int quantidadeParcelas) {
        if (quantidadeParcelas <= 0) {
            throw new IllegalArgumentException("A quantidade de parcelas deve ser maior que zero.");
        }

        double valorFinanciado = calcularPreco() - calcularEntrada();
        return valorFinanciado / quantidadeParcelas;
    }
}
