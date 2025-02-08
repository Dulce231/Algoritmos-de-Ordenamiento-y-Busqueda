
package PrimerParcial;

import javax.swing.JOptionPane;

public class InvertirCadena {
    
    // Método recursivo para invertir una cadena
    public static String invertir(String str) {
        if (str.isEmpty()) {
            return str;
        }
        return invertir(str.substring(1)) + str.charAt(0);
    }
    
    public static void main(String[] args) {
        // Pedir al usuario una cadena de texto
        String input = JOptionPane.showInputDialog("Ingrese una cadena de texto:");
        
        // Invertir la cadena y mostrar el resultado
        String invertida = invertir(input);
        JOptionPane.showMessageDialog(null, "Cadena invertida: " + invertida);
    }
}
