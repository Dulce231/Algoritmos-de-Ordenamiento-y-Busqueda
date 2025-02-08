package PrimerParcial;

import javax.swing.JOptionPane;

public class Fibonacci {
    
    // Método recursivo para calcular el término n de la serie Fibonacci
    public static int fibonacci(int n) {
        if (n <= 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }
    
    public static void main(String[] args) {
        
        String input = JOptionPane.showInputDialog("Ingrese el número de términos de Fibonacci:");
        int n = Integer.parseInt(input);
        
       
        StringBuilder serie = new StringBuilder("Serie de Fibonacci: ");
        for (int i = 0; i < n; i++) {
            serie.append(fibonacci(i)).append(", ");
        }
        
       
        JOptionPane.showMessageDialog(null, serie.toString());
    }
}
