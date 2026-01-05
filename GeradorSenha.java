import java.security.SecureRandom;
import java.util.Scanner;

public class GeradorSenha {

    // Caracteres permitidos
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SecureRandom random = new SecureRandom();

        System.out.println("--- GERADOR DE SENHAS ---");
        System.out.print("Quantos caracteres a senha deve ter? ");
        int tamanho = scanner.nextInt();

        if (tamanho <= 0) {
            System.out.println("Tamanho inválido.");
            return;
        }

        StringBuilder senha = new StringBuilder();

        for (int i = 0; i < tamanho; i++) {
            int index = random.nextInt(CHARS.length());
            senha.append(CHARS.charAt(index));
        }

        System.out.println("\nSua nova senha: " + senha.toString());
        scanner.close();
    }
}