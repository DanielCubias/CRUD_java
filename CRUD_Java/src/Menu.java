import java.util.Scanner;

public class Menu {

    public void solicitudes() {
        System.out.println("Bienvenidos a la BBDD");

        while (true) {
            mostrarMenu();
            boolean continuar = claseSwitch();
            if (!continuar) {
                break; // romperemos el bucle si el usuario escoge salir del programa
            }
        }

        System.out.println("Programa finalizado.");
    }

    public void mostrarMenu() {
        System.out.println("\nMenú Principal");
        System.out.println("1 - Consulta tots els artistes");
        System.out.println("2 - Consulta artistes pel seu nom");
        System.out.println("3 - Consulta els 5 primers almbums pel nom de l'artista");
        System.out.println("4 - Afegir un artista");
        System.out.println("5 - Modificar el nom d'un artista");
        System.out.println("6 - Borrar un artista");
        System.out.println("7 - Introduce la longitud del nombre del artista");
        System.out.println("8 - Sortir");
    }

    public boolean claseSwitch() {
        Crud micrud = new Crud();
        int a = VerificarNumero();

        switch (a) {
            case 1:
                micrud.mostrandoTodosLosArtistas();
                break;
            case 2:

                micrud.consultaArtistaNombre();
                break;
            case 3:

                micrud.consultaprimerAlbums();
                break;
            case 4:
                micrud.afegirArtista();
                break;
            case 5:
                micrud.modificarArtista();
                break;
            case 6:
                micrud.borrarArtista();
                break;
            case 7:
                micrud.longitudCaracter();
                break;
            case 8:
                System.out.println("\n..........SALIENDO..........");
                return false;

            default:
                System.out.println("Opción no válida");
        }

        return true; // continuar con el bucle
    }

    public int VerificarNumero() {
        Scanner escogerOpcion = new Scanner(System.in);
        int opcionEscogida = 0;

        while (true) {
            System.out.print("Introduce una opción entre el 1 y el 7: ");
            if (escogerOpcion.hasNextInt()) {
                opcionEscogida = escogerOpcion.nextInt();

                if (opcionEscogida >= 1 && opcionEscogida <= 9) {
                    System.out.println("La opción que has escogido es la numero " + opcionEscogida);
                    break;
                } else {
                    System.out.println("Debes escoger un número entre 1 y 7.");
                }
            } else {
                System.out.println("Debes escribir un numero, no un caracter.");
                escogerOpcion.next();
            }
        }

        return opcionEscogida;
    }
}
