import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Tela extends JFrame {

    // Elementos de treinamento
    int[] States  = {Q.NADA, Q.OBSTACULO_BAIXO, Q.OBSTACULO_CIMA, Q.MOEDA, Q.ESPINHO, Q.NADA, Q.ESPINHO, Q.OBSTACULO_CIMA, Q.OBSTACULO_CIMA, Q.OBSTACULO_BAIXO, Q.MOEDA, Q.NADA};
    int[] Actions = {Q.PULA, Q.FRENTE};
    float[][] learning = new float[States.length][Actions.length];
    boolean treinando = true;


    int item_desenha = 0;
    int max_item_desenha = States.length > 10 ? 10 : States.length;


    /////////////////////////////////////////////
    class DrawPanel extends JPanel implements KeyListener{


        public static final int ESPACO = 64;
        int deslocamento_inicial = ESPACO*5;
        int count_steps = 0;
        float learning_rate = 0.3f;
        float discount_factor = 0.9f;

        private void doDrawing(Graphics g) {

            //////////////////

            int acaoAtual = Q.FRENTE;
            int estadoAtual = count_steps;

            double rand = Math.random();
            if (rand < 0.5) { acaoAtual = Q.PULA;}


            if (treinando) {
                learning[estadoAtual][acaoAtual] += learning_rate *
                        (Q.recompensa(States[estadoAtual], acaoAtual) + (discount_factor * Q.max(learning[estadoAtual + 1])) - learning[estadoAtual][acaoAtual]);
            } else {
                acaoAtual =   learning[count_steps][0] > learning[count_steps][1] ? Q.PULA : Q.FRENTE;
            }

            //////////////////

            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.blue);
            g2d.drawString(String.valueOf(Q.recompensa(States[estadoAtual], acaoAtual)), 20, 20);

            Dimension size = getSize();
            Insets insets = getInsets();

            int w = size.width - insets.left - insets.right;
            int h = size.height - insets.top - insets.bottom;

            //// Desenha aqui ////
            for (int i=0; i<max_item_desenha && (i+item_desenha) < States.length-1; i++) {
                desenhaItem(States[i+item_desenha], deslocamento_inicial+i*ESPACO, g2d, h);
                desenhaAgente(acaoAtual, g2d, ESPACO*5, h);
            }

            if (count_steps >= (States.length-2)) {
                count_steps = 0;
                deslocamento_inicial = ESPACO*5;

                Q.printLearning(States, Actions, learning);
            }

            count_steps += 1;
            deslocamento_inicial -= ESPACO;
        }

        private void desenhaAgente(int acao, Graphics2D g2d, int posicao, int h) {
            g2d.setColor(Color.blue);
            g2d.fillOval(posicao, acao == Q.PULA ? h-(ESPACO*3) : h-(ESPACO), ESPACO, ESPACO);
        }

        private void desenhaItem(int item, int deslocamento, Graphics2D g2d, int h) {

            if (deslocamento < 0) {
                return;
            }

            if (item == Q.NADA) {
                g2d.setColor(Color.white);
                g2d.fillRect(deslocamento, h-ESPACO, ESPACO, ESPACO);
            } else if (item == Q.OBSTACULO_BAIXO) {
                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRect(deslocamento, h-ESPACO, ESPACO, ESPACO);
            } else if (item == Q.OBSTACULO_CIMA) {
                g2d.setColor(Color.ORANGE);
                g2d.fillRect(deslocamento, h-(ESPACO*3), ESPACO, ESPACO);
            } else if (item == Q.ESPINHO) {
                g2d.setColor(Color.red);
                g2d.fillPolygon(new int[] {deslocamento,deslocamento+ESPACO, deslocamento+(ESPACO/2)}, new int[] {h,h,h-ESPACO}, 3);
            } else if (item == Q.MOEDA) {
                g2d.setColor(Color.yellow);
                g2d.fillOval(deslocamento, h-ESPACO, ESPACO, ESPACO);
            }


        }

        @Override
        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            doDrawing(g);
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_S) {
                treinando = true;
                step();
            } else
            if(e.getKeyCode() == KeyEvent.VK_E) {
                treinando = false;
                step();
            }
            if(e.getKeyCode() == KeyEvent.VK_T) {
                treinando = true;
                for (int i=0; i<10; i++) {
                    step();
                }
            }
        }

        private void step() {
            this.repaint();
        }
    }
    /////////////////////////////////////////////



    public Tela() {
        initUI();
    }

    public final void initUI() {

        DrawPanel dpnl = new DrawPanel();
        add(dpnl);

        setSize(640, 480);
        setResizable(false);
        setTitle("Points");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(dpnl);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Tela te = new Tela();
                te.setVisible(true);
            }
        });
    }
}
