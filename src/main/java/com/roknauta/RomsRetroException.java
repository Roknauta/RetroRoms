package com.roknauta;

public class RomsRetroException extends RuntimeException {

    public RomsRetroException(String fileName,Throwable tr) {
       super("Erro no arquivo: "+fileName,tr);
    }
}
