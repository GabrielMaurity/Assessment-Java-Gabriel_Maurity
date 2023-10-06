package Relatorio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class Partida {
    private String dataPartida;
    private String heroi;
    private String resultado;
    private String monstro;
    private int quantidadeDeRodadas;

    public Partida(String dataPartida, String heroi, String resultado, String monstro, int quantidadeDeRodadas) {
        this.dataPartida = dataPartida;
        this.heroi = heroi;
        this.resultado = resultado;
        this.monstro = monstro;
        this.quantidadeDeRodadas = quantidadeDeRodadas;
    }

    public String getDataPartida() {
        return dataPartida;
    }

    public String getHeroi() {
        return heroi;
    }

    public String getResultado() {
        return resultado;
    }

    public String getMonstro() {
        return monstro;
    }

    public int getQuantidadeDeRodadas() {
        return quantidadeDeRodadas;
    }
}

public class Relatorios {
    public static void gerarRelatorio(String nickname) {
        BufferedReader br = null;

        try {
            // Ler o arquivo CSV e armazenar as partidas em uma lista
            List<Partida> partidas = new ArrayList<>();
            br = new BufferedReader(new FileReader("temp/" + nickname + ".csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    String dataPartida = parts[0];
                    String heroi = parts[1];
                    String resultado = parts[2];
                    String monstro = parts[3];
                    int quantidadeDeRodadas = Integer.parseInt(parts[4]);
                    Partida partida = new Partida(dataPartida, heroi, resultado, monstro, quantidadeDeRodadas);
                    partidas.add(partida);
                }
            }

            // Calcular e imprimir os dados do jogador
            if (!partidas.isEmpty()) {
                calcularEImprimirDadosJogador(partidas);
            } else {
                System.out.println("Nenhuma partida encontrada para o jogador.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void calcularEImprimirDadosJogador(List<Partida> partidas) {
        Map<String, Integer> heroiMaisJogado = new HashMap<>();
        Map<String, Integer> monstroMaisEnfrentado = new HashMap<>();
        int totalPontos = 0;

        for (Partida partida : partidas) {
            // Calcular pontos da partida
            int pontos = 100 - partida.getQuantidadeDeRodadas();
            totalPontos += pontos;

            // Atualizar heroi mais jogado
            heroiMaisJogado.put(partida.getHeroi(), heroiMaisJogado.getOrDefault(partida.getHeroi(), 0) + 1);

            // Atualizar monstro mais enfrentado
            monstroMaisEnfrentado.put(partida.getMonstro(), monstroMaisEnfrentado.getOrDefault(partida.getMonstro(), 0) + 1);
        }

        // Encontrar heroi mais jogado
        String heroiMaisJogadoNome = "";
        int heroiMaisJogadoPartidas = 0;
        for (Map.Entry<String, Integer> entry : heroiMaisJogado.entrySet()) {
            if (entry.getValue() > heroiMaisJogadoPartidas) {
                heroiMaisJogadoNome = entry.getKey();
                heroiMaisJogadoPartidas = entry.getValue();
            }
        }

        // Encontrar monstro mais enfrentado
        String monstroMaisEnfrentadoNome = "";
        int monstroMaisEnfrentadoPartidas = 0;
        for (Map.Entry<String, Integer> entry : monstroMaisEnfrentado.entrySet()) {
            if (entry.getValue() > monstroMaisEnfrentadoPartidas) {
                monstroMaisEnfrentadoNome = entry.getKey();
                monstroMaisEnfrentadoPartidas = entry.getValue();
            }
        }

        // Imprimir os resultados
        System.out.println("Relatório do Jogador:");
        System.out.println("Total de Partidas: " + partidas.size());
        System.out.println("Total de Pontos: " + totalPontos);
        System.out.println("Herói Mais Jogado: " + heroiMaisJogadoNome + " (" + heroiMaisJogadoPartidas + " partidas)");
        System.out.println("Monstro Mais Enfrentado: " + monstroMaisEnfrentadoNome + " (" + monstroMaisEnfrentadoPartidas + " vezes)");
    }
}
