import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Tela extends JFrame {

    // Elementos de treinamento
    int[] States  = {Q.NADA, Q.NADA, Q.OBSTACULO_BAIXO, Q.NADA, Q.OBSTACULO_CIMA, Q.NADA, Q.ESPINHO, Q.MOEDA, Q.NADA, Q.OBSTACULO_BAIXO, Q.MOEDA, Q.MOEDA, Q.OBSTACULO_CIMA, Q.ESPINHO, Q.OBSTACULO_CIMA, Q.OBSTACULO_BAIXO, Q.NADA };
    int[] Actions = {Q.PULA, Q.FRENTE};
    float[][] learning = new float[States.length][Actions.length];


    int item_desenha = 0;
    int max_item_desenha = States.length > 10 ? 10 : States.length;


    /////////////////////////////////////////////
    class DrawPanel extends JPanel implements KeyListener{


        public static final int ESPACO = 64;
        int deslocamento_inicial = ESPACO*5;


        private void doDrawing(Graphics g) {

            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.blue);

            Dimension size = getSize();
            Insets insets = getInsets();

            int w = size.width - insets.left - insets.right;
            int h = size.height - insets.top - insets.bottom;

            //// Desenha aqui ////
            for (int i=0; i<max_item_desenha && (i+item_desenha) < States.length; i++) {
                desenhaItem(States[i+item_desenha], deslocamento_inicial+i*ESPACO, g2d, h);
                desenhaAgente(Q.FRENTE, g2d, ESPACO*4, h);
            }

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
                step();
            }
        }

        private void step() {
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
