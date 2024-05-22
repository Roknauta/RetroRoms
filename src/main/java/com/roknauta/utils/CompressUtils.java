package com.roknauta.utils;

import com.roknauta.DetalheExecucao;
import com.roknauta.RomsRetroException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Paths;
import java.util.function.Predicate;

public class CompressUtils {

    public static void descompactarArquivoCompactado(File sourceFile, String targetDir, Predicate<String> arquivoValido,
        DetalheExecucao detalheExecucao) {
        if (isZip(sourceFile)) {
            extractFromZip(sourceFile, targetDir, arquivoValido, detalheExecucao);
        } else if (is7zip(sourceFile)) {
            extractFrom7z(sourceFile, targetDir, arquivoValido, detalheExecucao);
        }
    }

    private static void extractFromZip(File sourceFile, String targetDir, Predicate<String> arquivoValido,
        DetalheExecucao detalheExecucao) {
        try (ArchiveInputStream<? extends ArchiveEntry> ais = new ArchiveStreamFactory().createArchiveInputStream(
            ArchiveStreamFactory.ZIP, new FileInputStream(sourceFile))) {
            ArchiveEntry entry = ais.getNextEntry();
            while (entry != null) {
                extrairArquivoParaDiretorio(entry, targetDir, arquivoValido, ais, detalheExecucao);
                entry = ais.getNextEntry();
            }
        } catch (Exception e) {
            detalheExecucao.getArquivosComErro().add(sourceFile.getName());
        }
    }

    private static void extractFrom7z(File sourceFile, String targetDir, Predicate<String> arquivoValido,
        DetalheExecucao detalheExecucao) {
        try (SevenZFile sevenZFile = SevenZFile.builder().setFile(sourceFile).get()) {
            SevenZArchiveEntry entry = sevenZFile.getNextEntry();
            while (entry != null) {
                extrairArquivoParaDiretorio(entry, targetDir, arquivoValido, sevenZFile.getInputStream(entry), detalheExecucao);
                entry = sevenZFile.getNextEntry();
            }
        } catch (Exception e) {
            detalheExecucao.getArquivosComErro().add(sourceFile.getName());
        }
    }

    private static void extrairArquivoParaDiretorio(ArchiveEntry entry, String targetDir, Predicate<String> arquivoValido,
        InputStream inputStream, DetalheExecucao detalheExecucao) throws IOException {
        if (!entry.isDirectory() && (arquivoValido.test(entry.getName()) || isZipOr7z(entry.getName()))) {
            File destFile = new File(targetDir, Paths.get(entry.getName()).getFileName().toString());
            try {
                salvarEntry(destFile, inputStream);
            } catch (Exception e) {
                detalheExecucao.getArquivosComErro().add(entry.getName());
            }
            if (isCompactedFile(destFile)) {
                extractFromZip(destFile, targetDir, arquivoValido, detalheExecucao);
                destFile.delete();
            } else {
                AppUtils.renomearParaCheckSum(destFile, targetDir);
            }
        } else if (!entry.isDirectory()) {
            detalheExecucao.getArquivosIgnorados().add(entry.getName());
        }
    }

    private static boolean isZipOr7z(String fileName) {
        return FilenameUtils.getExtension(fileName).equals("7z") || FilenameUtils.getExtension(fileName).equals("zip");
    }

    private static void salvarEntry(File file, InputStream inputStream) {
        //System.out.println("Descompactando o arquivo: "+file.getAbsolutePath());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            IOUtils.copy(inputStream, fos);
        } catch (IOException e) {
            throw new RomsRetroException(file.getName(), e);
        }
    }

    public static boolean isCompactedFile(File file) {
        return isZip(file) || is7zip(file);
    }

    public static boolean isZip(File file) {
        return FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(ArchiveStreamFactory.ZIP);
    }

    public static boolean is7zip(File file) {
        return FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(ArchiveStreamFactory.SEVEN_Z);
    }

    public static void compactarPara7z(File arquivoParaCompactar, String destination) throws IOException {
        File compactado = new File(destination, FilenameUtils.getBaseName(arquivoParaCompactar.getName()) + ".7z");
        if (!compactado.exists()) {
            try (SevenZOutputFile out = new SevenZOutputFile(compactado)) {
                SevenZArchiveEntry entry = out.createArchiveEntry(arquivoParaCompactar, arquivoParaCompactar.getName());
                out.putArchiveEntry(entry);
                try (FileInputStream fis = new FileInputStream(arquivoParaCompactar)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                }
                out.closeArchiveEntry();
                arquivoParaCompactar.delete();
            }
        }
    }

}
