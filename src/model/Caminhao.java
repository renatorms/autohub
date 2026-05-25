package model;

public class Caminhao extends Veiculo implements Negociavel {
    private static final long serialVersionUID = 1L;

    private double capacidadeCarga;
    private int quantidadeEixos;

    public Caminhao(String marca, String modelo, String cor, String placa, int ano, int quilometragem,
            double precoBase, double capacidadeCarga, int quantidadeEixos) {
        super(marca, modelo, cor, placa, ano, quilometragem, precoBase);
        this.capacidadeCarga = capacidadeCarga;
        this.quantidadeEixos = quantidadeEixos;
    }

    public double getCapacidadeCarga() {
        return capacidadeCarga;
    }

    public int getQuantidadeEixos() {
        return quantidadeEixos;
    }

    public void setCapacidadeCarga(double capacidadeCarga) {
        this.capacidadeCarga = capacidadeCarga;
    }

    public void setQuantidadeEixos(int quantidadeEixos) {
        this.quantidadeEixos = quantidadeEixos;
    }

    @Override
    public double calcularPreco() {
        double precoFinal = getPrecoBase();

        precoFinal += capacidadeCarga * 0.05;
        precoFinal += quantidadeEixos * 20000;

        return precoFinal;
    }

    @Override
    public String getTipo() {
        return "Caminhão";
    }

    @Override
    public double calcularEntrada() {
        return calcularPreco() * 0.30;
    }

    @Override
    public double calcularParcela(int quantidadeParcelas) {
        if (quantidadeParcelas <= 0) {
            throw new IllegalArgumentException("A quantidade de parcelas dever ser maior que zero");
        }

        double valorFinanciado = calcularPreco() - calcularEntrada();
        return valorFinanciado / quantidadeParcelas;
    }

}
