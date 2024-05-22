import com.fasterxml.jackson.databind.ObjectMapper;
import com.roknauta.lpl.ItemsItem;
import com.roknauta.lpl.Playlist;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws IOException {
        test7z();
    }

    private static void arcade() throws IOException {
        List<String> games = Files.readAllLines(Paths.get("/mnt/1A7A2CA67A2C809D/Games/ROMS/Arcade/Games Mame.txt"));
        Playlist playlist = new ObjectMapper().readValue(new File("/home/douglas/.config/retroarch/playlists/MAME.lpl"),
            Playlist.class);
        List<String> onPlayList = new ArrayList<>();
        for (ItemsItem item : playlist.getItems()) {
            if (games.stream().anyMatch(game -> item.getLabel().contains(game) && !item.getLabel().contains("Japan"))) {
                onPlayList.add(item.getLabel());
                File rom = new File(item.getPath());
                FileUtils.copyFile(rom, new File("/home/douglas/ROMSOK/Arcade/", rom.getName()));
            }
        }
        onPlayList.forEach(System.out::println);
    }

    private static void test7z() {
        String extension = ".7z";
        String dir = "/home/douglas/ROMSOK/nds/";
        for (File rom:new File("/home/douglas/ROMS/nds/roms").listFiles()){
            String finalName = getCommand(FilenameUtils.getBaseName(rom.getName()), " ", "\\(", "\\)", "-", "\\.","'","!","&");
            System.out.println("7z a " + dir + finalName.concat(extension) + " " + finalName+"."+FilenameUtils.getExtension(rom.getName()));
        }
    }

    private static String getCommand(String nome, String... chars) {
        String finalNome = nome;
        for (String character : chars) {
            finalNome = finalNome.replaceAll(character, "\\\\" + character);
        }
        return finalNome;
    }

}
