package persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import exception.FalhaPersistenciaException;
import model.Veiculo;

public class EstoqueRepository {

    public void salvar(List<Veiculo> estoque, String caminhoArquivo) throws FalhaPersistenciaException {
        if (estoque == null) {
            throw new FalhaPersistenciaException("Não foi possível salvar: estoque nulo.");
        }

        File arquivo = new File(caminhoArquivo);
        File pasta = arquivo.getParentFile();

        if (pasta != null && !pasta.exists()) {
            boolean criada = pasta.mkdirs();

            if (!criada) {
                throw new FalhaPersistenciaException("Não foi possível criar a pasta para salvar o arquivo.");
            }
        }

        try (ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            saida.writeObject(estoque);
        } catch (IOException erro) {
            throw new FalhaPersistenciaException("Erro ao salvar os dados do estoque.", erro);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Veiculo> carregar(String caminhoArquivo) throws FalhaPersistenciaException {
        File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(arquivo))) {
            Object objeto = entrada.readObject();

            if (!(objeto instanceof List<?>)) {
                throw new FalhaPersistenciaException("O arquivo não contém uma lista de veículos válida.");
            }

            return (List<Veiculo>) objeto;
        } catch (IOException erro) {
            throw new FalhaPersistenciaException("Erro ao carregar os dados do estoque.", erro);
        } catch (ClassNotFoundException erro) {
            throw new FalhaPersistenciaException("O arquivo contém dados incompatíveis com o sistema.", erro);
        } catch (ClassCastException erro) {
            throw new FalhaPersistenciaException("O arquivo possui dados em formato inválido.", erro);
        }
    }
}