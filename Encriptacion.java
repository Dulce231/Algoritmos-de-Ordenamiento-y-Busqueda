public class Encriptacion {

    //  César
    public static String encriptarCliente(String mensaje, int desplazamiento) {
        StringBuilder mensajeEncriptado = new StringBuilder();
        for (char c : mensaje.toCharArray()) {
            
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
              
                c = (char) ((c - base + desplazamiento) % 26 + base);
            }
            mensajeEncriptado.append(c);
        }
        return mensajeEncriptado.toString();
    }

    public static String desencriptarCliente(String mensaje, int desplazamiento) {
        
        return encriptarCliente(mensaje, 26 - desplazamiento);
    }

    // invertir el mensaje
    public static String encriptarServidor(String mensaje) {
        return new StringBuilder(mensaje).reverse().toString();
    }

    public static String desencriptarServidor(String mensaje) {
       
        return new StringBuilder(mensaje).reverse().toString();
    }
}
