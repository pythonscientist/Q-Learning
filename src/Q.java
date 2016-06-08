public class Q {

    public static int NADA = 0;
    public static int OBSTACULO_BAIXO = 1;
    public static int OBSTACULO_CIMA = 2;
    public static int MOEDA = 3;
    public static int ESPINHO = 4;

    public static int PULA = 0;
    public static int FRENTE = 1;

    public static int[][] valorRecompensa = {{0, 0}, {1, -1}, {-1, 1}, {0, 2}, {2, -1}};

    public static void qlearning(int[] States, int[] Actions, float learning_rate, float discount_factor, float[][] learning) {
          for (int i=0; i<States.length-1; i++) {
            int acaoAtual = FRENTE;
            int estadoAtual = i;

            double rand = Math.random();
            if (rand < 0.5) { acaoAtual = PULA;}

            learning[estadoAtual][acaoAtual] += learning_rate *
                    (recompensa(States[estadoAtual], acaoAtual) + (discount_factor * max(learning[estadoAtual+1])) - learning[estadoAtual][acaoAtual]);
              printLearning(States, Actions, learning);
        }

    }

    private static float max(float[] valoresAcoes) {
        if (valoresAcoes[PULA] > valoresAcoes[FRENTE]) {
            return valoresAcoes[PULA];
        }
        return  valoresAcoes[FRENTE];
    }

    public static int recompensa(int parteMapa, int acao) {
        return valorRecompensa[parteMapa][acao];
    }


    public static void main(String[] args) {
        int[] States  = {NADA, NADA, OBSTACULO_BAIXO, NADA, OBSTACULO_CIMA, NADA, ESPINHO, MOEDA, NADA, OBSTACULO_BAIXO, MOEDA, MOEDA, OBSTACULO_CIMA, ESPINHO, OBSTACULO_CIMA, OBSTACULO_BAIXO, NADA };
        int[] Actions = {PULA, FRENTE};
        float[][] learning = new float[States.length][Actions.length];

        int quantidade_treinamento = 100;

        for (int x=0; x<quantidade_treinamento; x++) {
            treinaEpisodio(States, Actions, learning);
            printLearning(States, Actions, learning);
        }
    }

    public static void treinaEpisodio(int[] States, int[] Actions, float[][] learning) {
        qlearning(States, Actions, 0.3f, 0.9f, learning);
    }

    public static void printLearning(int[] states, int[] actions, float[][] learning) {
        for (int i = 0; i < actions.length; i++) {

            if (i == PULA) {
                System.out.print("PULA   -> ");
            } else {
                System.out.print("FRENTE -> ");
            }

            for (int j = 0; j < states.length; j++) {
                System.out.print(String.format("%.2f ", learning[j][i]));
            }
            System.out.println();
        }
        System.out.println();
    }


}
