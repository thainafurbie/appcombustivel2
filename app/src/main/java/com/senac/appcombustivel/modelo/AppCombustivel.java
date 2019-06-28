package com.senac.appcombustivel.modelo;

public class AppCombustivel {
    private int id_abastecimento;
    private int km_atual;
    private String tipo;
    private int lts_combustivel;
    private int consumo_kml;

    public int getId_abastecimento() {
        return id_abastecimento;
    }

    public void setId_abastecimento(int id_abastecimento) {
        this.id_abastecimento = id_abastecimento;
    }

    public int getKm_atual() {
        return km_atual;
    }

    public void setKm_atual(int km_atual) {
        this.km_atual = km_atual;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getLts_combustivel() {
        return lts_combustivel;
    }

    public void setLts_combustivel(int lts_combustivel) {
        this.lts_combustivel = lts_combustivel;
    }

    public int getConsumo_kml() {
        return consumo_kml;
    }

    public void setConsumo_kml(int consumo_kml) {
        this.consumo_kml = consumo_kml;
    }

    public AppCombustivel (int id_abastecimento, int km_atual, String tipo, int lts_combustivel, int consumo_kml) {
        this.id_abastecimento = id_abastecimento;
        this.km_atual = km_atual;
        this.tipo = tipo;
        this.lts_combustivel = lts_combustivel;
        this.consumo_kml = lts_combustivel;

    }


}
