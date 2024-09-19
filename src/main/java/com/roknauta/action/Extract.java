package com.roknauta.action;

import com.roknauta.DetalheExecucao;
import com.roknauta.Sistema;
import com.roknauta.utils.AppUtils;
import com.roknauta.utils.CompressUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Predicate;

public class Extract {

    private static final String DEST_BASE = "/home/douglas/roms";
    private static final String PACKS = "/run/media/douglas/Seagate/Games/Packs";

    public void processar(Sistema sistema) throws IOException {
        if (!Paths.get(DEST_BASE + File.separator + sistema.getFolder() + File.separator + "Info da Execução.txt")
            .toFile().exists()) {
            processarSistema(sistema);
        }
    }

    public void processar() throws IOException {
        for (Sistema sistema : Sistema.values()) {
            processar(sistema);
        }
    }

    private void processarSistema(Sistema sistema) throws IOException {
        DetalheExecucao detalheExecucao = new DetalheExecucao(sistema.getFolder());
        System.out.println("Processando o sistema: " + sistema.name());
        String destino = DEST_BASE + File.separator + sistema.getFolder();
        String destinoRoms = destino + File.separator + "roms";
        criarTarget(destinoRoms);

        Predicate<String> arquivoValido =
            nomeArquivo -> sistema.getExtensions().contains(FilenameUtils.getExtension(nomeArquivo).toLowerCase());
        for (File file : FileUtils.listFiles(new File(PACKS + File.separator + sistema.getFolder()), null, true)) {
            if (CompressUtils.isCompactedFile(file)) {
                CompressUtils.descompactarArquivoCompactado(file, destinoRoms, arquivoValido, detalheExecucao);
            } else if (arquivoValido.test(file.getName())) {
                AppUtils.renomearParaCheckSum(file, destinoRoms);
            }
        }
        finalizarExecucao(detalheExecucao, destino);
    }

    private void finalizarExecucao(DetalheExecucao detalheExecucao, String destino) throws IOException {
        detalheExecucao.setFim(LocalDateTime.now());
        File resumo = new File(destino + File.separator + "Info da Execução.txt");
        resumo.createNewFile();
        String builder = "Duração total da execução: " + AppUtils.formatDuration(
            Duration.between(detalheExecucao.getInicio(),
                detalheExecucao.getFim())) + "\n\n" + "Arquivos com erro de descompactar:\n" + String.join("\n",
            detalheExecucao.getArquivosComErro()) + "\n\n" + "Arquivos ignorados:\n" + String.join("\n",
            detalheExecucao.getArquivosIgnorados()) + "\n";
        Files.writeString(Paths.get(resumo.getPath()), builder, StandardCharsets.UTF_8);
    }

    private void criarTarget(String targetDir) {
        File diretorio = new File(targetDir);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }

}
