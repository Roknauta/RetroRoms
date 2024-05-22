package com.roknauta;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class DetalheExecucao {

    private String sistema;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private Set<String> arquivosIgnorados;
    private Set<String> arquivosComErro;

    public DetalheExecucao(String sistema) {
        this.sistema = sistema;
        this.inicio = LocalDateTime.now();
        this.arquivosIgnorados = new HashSet<>();
        this.arquivosComErro = new HashSet<>();
    }
}
