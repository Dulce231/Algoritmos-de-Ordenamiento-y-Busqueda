package PrimerParcial;

import javax.swing.JOptionPane;

public class TorresDeHanoi {

    // Método recursivo para resolver las Torres de Hanoi
    public static void moverDiscos(int n, char origen, char auxiliar, char destino) {
        if (n == 1) {
            JOptionPane.showMessageDialog(null, "Mover disco 1 de " + origen + " a " + destino);
            return;
        }

        moverDiscos(n - 1, origen, destino, auxiliar);

        JOptionPane.showMessageDialog(null, "Mover disco " + n + " de " + origen + " a " + destino);

        moverDiscos(n - 1, auxiliar, origen, destino);
    }

    public static void main(String[] args) {

        String input = JOptionPane.showInputDialog("Ingrese el número de discos:");
        int n = Integer.parseInt(input);

        JOptionPane.showMessageDialog(null, "Movimientos para resolver las Torres de Hanoi con " + n + " discos:");
        moverDiscos(n, 'A', 'B', 'C');
    }
}
