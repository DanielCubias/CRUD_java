import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

class Crud {

    Scanner scaner = new Scanner(System.in);

    String url = "jdbc:postgresql://localhost:5432/chinook_v2";
    String usuario = "postgres";
    String password = "123456789";// contraseña de la base de datos y de pgadmin

    App app = new App();

    public void longitudCaracter() {
        try (Connection connection = DriverManager.getConnection(url, usuario, password)) {

            Statement statement = connection.createStatement();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Introduce la cantidad de caracteres para filtrar artistas: ");
            int longitud = scanner.nextInt();

            String sql = "SELECT * FROM artist";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String nombre = resultSet.getString("name");
                if (nombre.length() == longitud) {
                    System.out.println("ID " + resultSet.getInt("artist_id") + ", Nombre: " + nombre);
                }
            }

        } catch (SQLException e) {
            System.out.println("ERROR EN LA CONSULTA O LA CONEXIÓN: " + e.getMessage());
        }
    }

    public void mostrandoTodosLosArtistas() {

        try (Connection connection = DriverManager.getConnection(url, usuario, password)) {
            // Establecemos la conexión a la BBDD
            Statement statement = connection.createStatement(); // Statement para ejecutar la consulta SQL
            String sql = "SELECT * FROM artist";
            ResultSet resultSet = statement.executeQuery(sql);

            int contador = 0;

            while (resultSet.next()) { // Avanza a la siguiente fila y devuelve false cuando no hay más datos
                int id = resultSet.getInt("artist_id");
                String nom = resultSet.getString("name");
                System.out.println("ID " + id + ", Nombre: " + nom);

                contador++;

                if (contador % 10 == 0) {
                    System.out.println("Mostrando " + contador + " artistas. Presiona Enter para continuar...");
                    try {
                        System.in.read(); // Espera la entrada del usuario antes de continuar
                    } catch (IOException e) {

                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("ERROR EN LA CONSULTA O LA CONEXIÓN: " + e.getMessage());
        }
    }

    public void consultaArtistaNombre() {

        System.out.println("Dinos el nombre del artista a buscar ");
        scaner = new Scanner(System.in);
        boolean comprobador = true;
        String nombreArtista = "";

        while (comprobador) {
            nombreArtista = scaner.nextLine();
            if (nombreArtista.length() > 2) {

                System.out.println("El nombre introducido es : " + nombreArtista);
                System.out.println("\n......REALIZANDO CONSULTA......\n");
                comprobador = false;

            } else {
                System.out.println("\nDebes de introducir mas de un caracter");

            }

        }

        try (Connection connection2 = DriverManager.getConnection(url, usuario, password)) {

            String sql = "SELECT * FROM artist WHERE name ILIKE ?"; // ILIKE es para que no distinga entre mayusculas y
            // minusculas";

            PreparedStatement consulta = connection2.prepareStatement(sql); // Cuando quiero realizar una buscqueda
                                                                            // ,pero que el usaurio itroduzca el nombre
                                                                            // o lo que quiera buscar
            // debo utilizar el PreparedStatement, ya que esto me prevendra inyecciones sql,
            // osea que me lo hace mas seguro
            consulta.setString(1, "%" + nombreArtista + "%"); // el ? es un marcador de posicion, y el 1 es el
            // con el % me buscara coincidencias"
            // porque pongo un 1 y no un 4 o otro numero?? , porque
            // hace referencia al name = ?, si tuviera por ejemplo
            // name = ? , id = ? , en el parameterIndex tendria que poner el 2. y asi
            // dependeindo de cuantos paramentros tenga

            ResultSet resultSet = consulta.executeQuery();
            boolean hayResultadoArista = false;
            while (resultSet.next()) {
                hayResultadoArista = true;
                int id = resultSet.getInt("artist_id");
                String nom = resultSet.getString("name");
                System.out.println(" ID : " + id + ", nom : " + nom);

            }

            if (!hayResultadoArista) {
                System.out.println("No se ha encontrado ningun artista con ese nombre ");
            }

        } catch (SQLException e) {
            System.out.println("Error en la consulta " + e.getMessage());

        }

    }

    public void consultaprimerAlbums() {

        System.out.println("Introduce el nombre del artista");
        boolean comprobarLONG = true;
        String nombreAlbunArtista = "";

        while (comprobarLONG) {

            scaner = new Scanner(System.in);
            nombreAlbunArtista = scaner.nextLine();
            if (nombreAlbunArtista.length() > 2) {
                System.out.println("\nNombre de artista introducido es : " + nombreAlbunArtista + " \n");
                System.out.println("\n----------Mostrando album----------");
                comprobarLONG = false;
            } else {
                System.out.println("debes de introducir al menos 2 caracteres");
            }
        }

        try (Connection connection3 = DriverManager.getConnection(url, usuario, password)) {

            // CON LA SIGUINETE CONSULTA, LO QUE HARA SERA MOSTRAR TANTOS DATOS DEL ARITSTA
            // COMO DEL ALBUM Y PARA ELLO LO QUE HARE SERA UN JOIN

            String sql = "SELECT al.album_id AS ID_ALBUM, al.title AS NOM_ALBUM, ar.name AS NOM_ARTISTA " +
                    "FROM artist ar " +
                    "JOIN album al ON ar.artist_id = al.artist_id " +
                    "WHERE ar.name LIKE ? " +
                    "ORDER BY al.album_id ASC " +
                    "LIMIT 5;";

            PreparedStatement consulta3 = connection3.prepareStatement(sql);
            consulta3.setString(1, "%" + nombreAlbunArtista + "%");
            ResultSet resultSet = consulta3.executeQuery();

            boolean resultado = false;
            while (resultSet.next()) {
                resultado = true;
                String idAlbum = resultSet.getString("ID_ALBUM");
                String nom_album = resultSet.getString("NOM_ALBUM");
                String nom_Artista = resultSet.getString("NOM_ARTISTA");
                System.out
                        .println("ID_ALBUM: " + idAlbum + ", NOM_ALBUM : " + nom_album + ", Nom_Artista :"
                                + nom_Artista);
            }

            if (!resultado) {
                System.out.println("Artista no encontrado o no tiene album");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void afegirArtista() {

        System.out.println("Introduce el nombre del artista en la bbdd");
        boolean comprobarLong3 = true;
        String InsertarArtista = "";

        while (comprobarLong3) {

            scaner = new Scanner(System.in);
            InsertarArtista = scaner.nextLine();
            if (InsertarArtista.length() > 2) {
                System.out.println("\nNombre de artista introducido es : " + InsertarArtista + " \n");
                System.out.println("\n----------INSERTANDO ARTISTA----------\n");
                comprobarLong3 = false;
            } else {
                System.out.println("debes de introducir al menos 2 caracteres");
            }
        }

        try (Connection connection4 = DriverManager.getConnection(url, usuario, password)) {
            // obtenemos el ultimo artista que se ha añadido
            String selectUltimoArista = "SELECT MAX(artist_id) FROM artist"; // con MAX, obtenemos el valor maximo de
                                                                             // artist_id en la tabla
            PreparedStatement seleccionandoArtista = connection4.prepareStatement(selectUltimoArista);
            ResultSet resultSet = seleccionandoArtista.executeQuery();

            int artistaNuevoConID = 1; // Valor por defecto si la tabla está vacía
            if (resultSet.next()) {
                artistaNuevoConID = resultSet.getInt(1) + 1; // Sumar 1 al último artist_id

            }

            // Insertamo el nuevo artista con el artist_id calculado a partir del ultimo
            // añadido
            String sql = "INSERT INTO artist (artist_id, name) VALUES (?, ?)";
            PreparedStatement insertardatos = connection4.prepareStatement(sql);
            insertardatos.setInt(1, artistaNuevoConID); // Establecer el nuevo artist_id
            insertardatos.setString(2, InsertarArtista); // Establecer el nombre del artista
            int filasInsertadas = insertardatos.executeUpdate(); // con executeUpdate, si ña pongo en imprimir me dara 1
                                                                 // , porque sera el numero de artistas insertadps, pero
                                                                 // si por ejemplo
            // quisiera eliminar 2 artistas, me devolveria 2, ya que son los artistas que se
            // borraron

            // cONFIRMAMOS SI SE HA PODIDO AÑADIR EL NUEVO ARITSTS
            if (filasInsertadas > 0) {
                System.out.println("Artista Agregado correctamente");
                System.out.println(" ID NUEVO ARTISTA : " + artistaNuevoConID + " Nombre : " + InsertarArtista);
            } else {
                System.out.println("No se pudo insertar el artista");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public void modificarArtista() {
        int artistaID = 0;

        while (artistaID <= 0) {
            System.out.print("Introduce el ID del artista a modificar: ");
            try {
                artistaID = scaner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Debes introducir un número entero válido.");
                scaner.next(); // limpiar todas las entradas invalidas
            }
        }
        scaner.nextLine(); // limpiar salto de línea

        String sqlSelect = "SELECT name FROM artist WHERE artist_id = ?";
        String sqlUpdate = "UPDATE artist SET name = ? WHERE artist_id = ?";

        try (Connection connection = DriverManager.getConnection(url, usuario, password);
                PreparedStatement seleccionArtista = connection.prepareStatement(sqlSelect);
                PreparedStatement modificandoArtista = connection.prepareStatement(sqlUpdate)) {

            seleccionArtista.setInt(1, artistaID);
            ResultSet resultSet = seleccionArtista.executeQuery();

            if (!resultSet.next()) {// si no hya resultado
                System.out.println("No se encontró un artista con ese ID.");
                return;
            }

            String nombreActual = resultSet.getString("name");
            System.out.println("Artista actual: " + nombreActual);
            System.out.print("Introduce el nuevo nombre del artista: ");
            String nuevoNombre = scaner.nextLine();

            modificandoArtista.setString(1, nuevoNombre);
            modificandoArtista.setInt(2, artistaID);

            if (modificandoArtista.executeUpdate() > 0) {
                System.out.println("Nombre actualizado correctamente.");
                System.out.println("Nuevo nombre del artista: " + nuevoNombre);
            } else {
                System.out.println("No se pudo actualizar el nombre del artista.");
            }

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    public void borrarArtista() {

        int artistaIDEliminar = 0;

        while (artistaIDEliminar <= 0) {
            System.out.print("Introduce el ID del artista a eliminar: ");
            try {
                artistaIDEliminar = scaner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Debes introducir un número entero válido.");
                scaner.next();
            }
        }

        scaner.nextLine(); // limpiar salto de línea
        // a continuacion he creado dos consultas porque me estaba dando error al querer
        // eliminar solo el artista, me decia que ese artista tenia albumns en el
        // registro
        // asi que tendre que eliminar todos los albumn que este artista tiene
        String EliminarAristasql = "DELETE FROM artist WHERE artist_id = ?";

        try (Connection connection = DriverManager.getConnection(url, usuario, password)) {

            // Eliminar artista
            try (PreparedStatement deleteArtist = connection.prepareStatement(EliminarAristasql)) {
                deleteArtist.setInt(1, artistaIDEliminar);
                int filasAfectadas = deleteArtist.executeUpdate();

                if (filasAfectadas > 0) {
                    System.out.println("El artista han sido eliminados correctamente.");
                } else {
                    System.out.println("No se encontró ningún artista con ese ID.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }
}