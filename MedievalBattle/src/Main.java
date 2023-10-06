import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import Relatorio.Relatorios;


class Personagem {
    private String nome;
    private int pontosDeVida;
    private int forca;
    private int defesa;
    private int agilidade;
    private int fatorDano;

    public Personagem(String nome, int pontosDeVida, int forca, int defesa, int agilidade, int fatorDano) {
        this.nome = nome;
        this.pontosDeVida = pontosDeVida;
        this.forca = forca;
        this.defesa = defesa;
        this.agilidade = agilidade;
        this.fatorDano = fatorDano;
    }

    public String getNome() {
        return nome;
    }

    public int getPontosDeVida() {
        return pontosDeVida;
    }

    public int getForca() {
        return forca;
    }

    public int getDefesa() {
        return defesa;
    }

    public int getAgilidade() {
        return agilidade;
    }

    public int getFatorDano() {
        return fatorDano;
    }

    public void receberDano(int dano) {
        pontosDeVida -= dano;
        if (pontosDeVida < 0) {
            pontosDeVida = 0;
        }
    }

    public boolean estaVivo() {
        return pontosDeVida > 0;
    }
}

public class Main {
    private static final int LADOS_DADO = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem-vindo ao MEDIEVAL BATTLE");
        System.out.print("Digite seu nickname: ");
        String nickname = scanner.nextLine();

        // Escolher classe do herói
        System.out.println("Escolha sua classe: ");
        System.out.println("1. Guerreiro");
        System.out.println("2. Bárbaro");
        System.out.println("3. Paladino");
        int classeEscolhida = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        Personagem heroi = null;

        switch (classeEscolhida) {
            case 1:
                heroi = new Personagem("Guerreiro", 12, 4, 3, 3, 2);
                break;
            case 2:
                heroi = new Personagem("Bárbaro", 13, 6, 1, 3, 2);
                break;
            case 3:
                heroi = new Personagem("Paladino", 15, 2, 5, 5, 2);
                break;
            default:
                System.out.println("Classe Inválida");
                System.exit(0);
        }

        Random random = new Random();

        String[] monstros = {"Orc", "Morto Vivo", "Kobold"};
        String monstro = monstros[random.nextInt(monstros.length)];

        int rodadas = 0;

        System.out.println("------------------------------");

        while (heroi.estaVivo()) {
            rodadas++;

            int iniciativaHeroi = random.nextInt(LADOS_DADO) + 1 + heroi.getAgilidade();
            int iniciativaMonstro = random.nextInt(LADOS_DADO) + 1 + 1;

            System.out.println("Rodada " + rodadas);
            System.out.println(heroi.getNome() + " (PV: " + heroi.getPontosDeVida() + ") vs " + monstro);

            if (iniciativaHeroi > iniciativaMonstro) {
                int fatorAtaqueHeroi = random.nextInt(LADOS_DADO) + 1 + heroi.getAgilidade() + heroi.getForca();
                int fatorDefesaMonstro = random.nextInt(LADOS_DADO) + 1 + 1;

                if (fatorAtaqueHeroi > fatorDefesaMonstro) {
                    int danoHeroi = 0;
                    for (int i = 0; i < heroi.getFatorDano(); i++) {
                        danoHeroi += random.nextInt(4) + 1;
                    }

                    int pontosDeVidaMonstro = 0;
                    int fatorDanoMonstro = 0;

                    switch (monstro) {
                        case "Orc":
                            pontosDeVidaMonstro = 20;
                            fatorDanoMonstro = random.nextInt(8) + 1;
                            break;
                        case "Morto Vivo":
                            pontosDeVidaMonstro = 25;
                            fatorDanoMonstro = random.nextInt(4) + 1;
                            break;
                        case "Kobold":
                            pontosDeVidaMonstro = 20;
                            fatorDanoMonstro = random.nextInt(2) + 1;
                            break;
                    }

                    int danoMonstro = 0;
                    for (int i = 0; i < fatorDanoMonstro; i++) {
                        danoMonstro += random.nextInt(4) + 1;
                    }

                    System.out.println(heroi.getNome() + " ataca " + monstro + " e causa " + danoHeroi + " de dano.");
                    System.out.println(monstro + " ataca " + heroi.getNome() + " e causa " + danoMonstro + " de dano.");

                    heroi.receberDano(danoMonstro);

                    System.out.println("Pontos de Vida do " + heroi.getNome() + ": " + heroi.getPontosDeVida());
                    System.out.println();
                }
            } else if (iniciativaHeroi < iniciativaMonstro) {
                int fatorAtaqueMonstro = random.nextInt(8) + 1;
                int fatorDefesaHeroi = random.nextInt(LADOS_DADO) + 1 + heroi.getAgilidade() + heroi.getDefesa();

                if (fatorAtaqueMonstro > fatorDefesaHeroi) {
                    int danoMonstro = 0;
                    for (int i = 0; i < 2; i++) {
                        danoMonstro += random.nextInt(4) + 1;
                    }

                    System.out.println(monstro + " ataca " + heroi.getNome() + " e causa " + danoMonstro + " de dano.");

                    heroi.receberDano(danoMonstro);

                    System.out.println("Pontos de Vida do " + heroi.getNome() + ": " + heroi.getPontosDeVida());
                    System.out.println();
                }
            } else {
                System.out.println("Empate na iniciativa, repetindo a rodada.");
            }

            if (!heroi.estaVivo()) {
                System.out.println(heroi.getNome() + " foi derrotado por " + monstro + "!");
                break;
            }
        }

        System.out.println("------------------------------");

        // Gravar no arquivo CSV
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataPartida = dateFormat.format(new Date());
        String resultado = heroi.estaVivo() ? "GANHOU" : "PERDEU";

        String linhaCSV = dataPartida + ";" + heroi.getNome() + ";" + resultado + ";" + monstro + ";" + rodadas;

        try {
            FileWriter writer = new FileWriter("temp/" + nickname + ".csv", true);
            writer.write(linhaCSV + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print("Deseja gerar um relatório? (S/N): ");
        String opcao = scanner.nextLine();
        if (opcao.equalsIgnoreCase("S")) {
            // Iniciar o programa de relatórios com o nickname
            Relatorios relatorios = new Relatorios();
            relatorios.gerarRelatorio(nickname);

        }

        scanner.close();
    }
}
