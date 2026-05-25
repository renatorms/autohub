package model;

public class Moto extends Veiculo implements Negociavel {
    private static final long serialVersionUID = 1L;

    private int cilindradas;
    private boolean versaoCompleta;

    public Moto(String marca, String modelo, String cor, String placa, int ano, int quilometragem,
            double precoBase, int cilindradas, boolean versaoCompleta) {
        super(marca, modelo, cor, placa, ano, quilometragem, precoBase);
        this.cilindradas = cilindradas;
        this.versaoCompleta = versaoCompleta;
    }

    public int getCilindradas() {
        return cilindradas;
    }

    public boolean isVersaoCompleta() {
        return versaoCompleta;
    }

    public void setCilindradas(int cilindradas) {
        this.cilindradas = cilindradas;
    }

    public void setVersaoCompleta(boolean versaoCompleta) {
        this.versaoCompleta = versaoCompleta;
    }

    public String getVersao() {
        if (versaoCompleta) {
            return "Completa";
        }

        return "Básica";
    }

    @Override
    public double calcularPreco() {
        double precoFinal = getPrecoBase();

        precoFinal += cilindradas * 2.0;

        if (versaoCompleta) {
            precoFinal += 1500.0;
        }

        return precoFinal;
    }

    @Override
    public String getTipo() {
        return "Moto";
    }

    @Override
    public double calcularEntrada() {
        return calcularPreco() * 0.15;
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
