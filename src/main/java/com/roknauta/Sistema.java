package com.roknauta;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Sistema {

    NES("Nintendo - NES", "nes, unif, unf","Nintendo - Nintendo Entertainment System.lpl"),
    SNES("Nintendo - SNES", "smc, fig, sfc, gd3, gd7, dx2, bsx, swc","Nintendo - Super Nintendo Entertainment System.lpl"),
    GB("Nintendo - Game Boy", "gb","Nintendo - Game Boy.lpl"),
    GBC("Nintendo - Game Boy Color", "gbc","Nintendo - Game Boy Color.lpl"),
    GBA("Nintendo - Game Boy Advance", "gba","Nintendo - Game Boy Advance.lpl"),
    N64("Nintendo - 64", "z64,n64,v64","Nintendo - Nintendo 64.lpl"),
    NDS("Nintendo - DS", "nds,bin","Nintendo - Nintendo DS.lpl"),
    MASTER_SYSTEM("Sega - Master System", "bin,sms","Sega - Master System - Mark III.lpl"),
    MEGA_DRIVE("Sega - Mega Drive", "bin,gen,md,sg,smd","Sega - Mega Drive - Genesis.lpl"),
    SEGAX("Sega - 32x", "32x,smd,bin,md","Sega - 32X.lpl"),
    GAME_GEAR("Sega - Game Gear", "bin,gg","Sega - Game Gear.lpl"),
    ATARI_2600("Atari - 2600", "a26,bin","Atari - 2600.lpl"),
    ATARI_5200("Atari - 5200", "rom, xfd, atr, atx, cdm, cas, car, bin, a52, xex","Atari - 5200.lpl"),
    ATARI_7800("Atari - 7800", "a78,bin","Atari - 7800.lpl"),
    ATARI_LYNX("Atari - Lynx", "lnx","Atari - Lynx.lpl"),
    ATARI_JAGUAR("Atari - Jaguar", "cue, j64, jag, cof, abs, cdi, rom","Atari - Jaguar.lpl"),
    COLECO_VISION("Coleco - ColecoVision", "bin, col, rom","Coleco - ColecoVision.lpl"),
    COMODORE_64("Commodore - 64", "d64, d81, crt, prg, tap, t64, m3u","Commodore - 64.lpl"),
    CHANNEL_F("Fairchild - Channel F", "bin, chf, rom","Fairchild - Channel F.lpl"),
    ODYSSEY2("Magnavox - Odyssey 2", "bin","Magnavox - Odyssey2.lpl"),
    NEO_GEO_CD("SNK - Neo Geo CD", "cue,iso,chd","Magnavox - Odyssey2.lpl"),
    NEO_GEO_POCKET("SNK - Neo Geo Pocket Color", "ngc","Magnavox - Odyssey2.lpl"),
    PC_ENGINE("NEC - PC Engine", "bin,pce","NEC - PC Engine - TurboGrafx 16.lpl"),
    PC_ENGINE_CD("NEC - PC Engine CD", "pce, cue, ccd, iso, img, chd","NEC - PC Engine CD - TurboGrafx-CD.lpl");

    private final String folder;
    private final String extensions;
    private final String datFile;


    public List<String> getExtensions() {
        return Arrays.stream(this.extensions.split(",")).map(String::trim).toList();
    }
}
