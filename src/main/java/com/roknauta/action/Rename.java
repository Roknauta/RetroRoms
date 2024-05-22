package com.roknauta.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roknauta.Sistema;
import com.roknauta.lpl.ItemsItem;
import com.roknauta.lpl.Playlist;
import com.roknauta.utils.AppFileUtils;
import com.roknauta.utils.AppUtils;
import com.roknauta.utils.CompressUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Rename {

    private final static String PLAYLIST_HOME = "/home/douglas/.config/retroarch/playlists";
    private final static String ROM_ROM = "/home/douglas/ROMS";
    private final static String TARGET_ROM = "/tmp/test";
    private final static String TARGET_ROM_REGION = "/home/douglas/ROMSREGION";

    public void process() throws IOException {
        renomear(Sistema.NDS);
    }

    private void coptyToRegionFolder(Sistema sistema) throws IOException {
        Playlist playlist = new ObjectMapper().readValue(new File(PLAYLIST_HOME, sistema.getDatFile()), Playlist.class);
        String destino = AppUtils.createNewFolder(TARGET_ROM_REGION, sistema.getFolder());
        for (File file : AppUtils.getFolderFromPath(ROM_ROM, sistema.getFolder(), "roms").listFiles()) {
            String crc = AppUtils.getCRC32(file);
            playlist.getItems().stream()
                .filter(item -> StringUtils.substringBefore(item.getCrc32(), "|").equalsIgnoreCase(crc)).findFirst()
                .ifPresent(itemPlaylist -> {
                    String folder = AppUtils.createNewFolder(destino);
                });
        }
    }

    private void removerDesconhecidos(Sistema sistema) throws IOException {
        Playlist playlist = new ObjectMapper().readValue(new File(PLAYLIST_HOME, sistema.getDatFile()), Playlist.class);
        for (File file : AppUtils.getFolderFromPath(ROM_ROM, sistema.getFolder(), "roms").listFiles()) {
            String crc = AppUtils.getCRC32(file);
            ItemsItem itemPlaylist = playlist.getItems().stream()
                .filter(item -> StringUtils.substringBefore(item.getCrc32(), "|").equalsIgnoreCase(crc)).findFirst()
                .orElse(null);
            if (itemPlaylist == null) {
                file.deleteOnExit();
            }
        }
    }

    private void renomear(Sistema sistema) throws IOException {
        Playlist playlist = new ObjectMapper().readValue(new File(PLAYLIST_HOME, sistema.getDatFile()), Playlist.class);
        for (File file : AppUtils.getFolderFromPath(ROM_ROM, sistema.getFolder(), "roms").listFiles()) {
            String crc = AppUtils.getCRC32(file);
            ItemsItem itemPlaylist = playlist.getItems().stream()
                .filter(item -> StringUtils.substringBefore(item.getCrc32(), "|").equalsIgnoreCase(crc)).findFirst()
                .orElse(null);
            if (itemPlaylist != null) {
                File atualFile = new File(itemPlaylist.getPath());
                File newFile = new File(atualFile.getParent(),
                    itemPlaylist.getLabel().concat(".").concat(FilenameUtils.getExtension(atualFile.getName())));
                FileUtils.moveFile(atualFile, newFile);
            }
        }
    }

    private void rename(Sistema sistema) throws IOException {
        System.out.println("Finalizando o sistema: " + sistema.name());
        List<ItemsItem> itens = getFinalList(sistema);
        String destino = AppUtils.createNewFolder(TARGET_ROM, sistema.getFolder());
        for (File file : new File(
            ROM_ROM + File.separator + sistema.getFolder() + File.separator + "roms").listFiles()) {
            String crc = AppUtils.getCRC32(file);
            ItemsItem itemPlaylist =
                itens.stream().filter(item -> StringUtils.substringBefore(item.getCrc32(), "|").equalsIgnoreCase(crc))
                    .findFirst().orElse(null);
            if (itemPlaylist != null) {
                File newFile = new File(destino,
                    itemPlaylist.getLabel().concat(".").concat(FilenameUtils.getExtension(file.getName())));
                System.out.println();
                /*if (!newFile.exists()) {
                    FileUtils.copyFile(file, newFile);
                    CompressUtils.compactarPara7z(newFile, destino);
                }*/
            }
        }
    }

    private List<ItemsItem> getFinalList(Sistema sistema) throws IOException {
        Playlist playlist = new ObjectMapper().readValue(new File(PLAYLIST_HOME, sistema.getDatFile()), Playlist.class);
        Map<String, List<ItemsItem>> mapeado = playlist.getItems().stream().filter(item -> isValidRom(item.getLabel()))
            .collect(Collectors.groupingBy(item -> AppFileUtils.getBaseName(item.getLabel())));
        List<ItemsItem> itensFinal = new ArrayList<>();
        mapeado.forEach((baseName, itens) -> {
            if (itens.size() > 1) {
                ItemsItem preferencial = getFirstOcorrence(itens);
                itensFinal.add(preferencial);
            } else {
                itensFinal.add(itens.getFirst());
            }
        });
        return itensFinal;
    }

    private ItemsItem getFirstOcorrence(List<ItemsItem> itens) {
        List<String> orderRegions = Arrays.stream(AppFileUtils.ACCEPTED_REGIONS.split(",")).toList();
        List<ItemsItem> itensPreferencial = new ArrayList<>();
        for (String orderRegion : orderRegions) {
            List<ItemsItem> ocorrences = getOcorrences(orderRegion, itens);
            if (!ocorrences.isEmpty()) {
                itensPreferencial.addAll(getOcorrences(orderRegion, itens));
                break;
            }
        }
        ItemsItem preferencial =
            itensPreferencial.stream().filter(item -> item.getLabel().matches("(.*)(\\(Rev [0-9]\\))"))
                .max(Comparator.comparing(ItemsItem::getLabel)).orElse(null);
        if (preferencial != null) {
            return preferencial;
        }
        itensPreferencial.sort(Comparator.comparing(ItemsItem::getLabel));
        return itensPreferencial.getFirst();
    }

    private List<ItemsItem> getOcorrences(String region, List<ItemsItem> itens) {
        return itens.stream().filter(item -> AppFileUtils.getRegionsList(item.getLabel()).contains(region))
            .collect(Collectors.toList());
    }

    private boolean isValidRom(String label) {
        String regions = AppFileUtils.getRegions(label);
        return AppFileUtils.isValid(label) && StringUtils.containsIgnoreCase(label, regions);
    }

}
