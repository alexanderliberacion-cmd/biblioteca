import almacen.Almacen;
import modelo.*;
import static modelo.EstadoEjemplar.DISPONIBLE;

public static void main(String[] args) {

    //Almacenes y variables

    Almacen<Libro, String> almacenLibro = new Almacen<>();
    Almacen<Ejemplar,Integer> almacenEjemplar = new Almacen<>();
    Almacen<Socio,Integer> almacenSocio = new Almacen<>();
    Optional<Prestamo> prestamo = Optional.empty();
    List<Prestamo> prestamos = new ArrayList<>();

    //Menu
    Scanner sc = new Scanner(System.in);
    int opcion;

    do {
        System.out.println("--- Menu Principal ---");
        System.out.println("1. Crea tu cuenta de socio");
        System.out.println("2. Añade un libro");
        System.out.println("3. Añade un ejemplar");
        System.out.println("4. Crea un prestamo");
        System.out.println("5. Devolver un libro");
        System.out.println("6. Ver los socios atrasados");
        System.out.println("7. Top 5 prestamos");
        System.out.println("8. Meses con mas prestamos");

        opcion = sc.nextInt();
        switch (opcion) {
            case 1:
                    Socio socio = crearSocio(almacenSocio);
                    System.out.println("Socio creado correctamente" + " " + socio.id());
                    break;
            case 2:
                    añadirLibro(almacenLibro).ifPresentOrElse(
                        libro -> System.out.println("El libro ha sido añadido correctamente"),
                        () -> System.out.println("No se ha podido añadir el libro")
                );
                    break;

            case 3:
                    añadirEjemplar(almacenLibro, almacenEjemplar).ifPresentOrElse(
                        ejemplar -> System.out.println("El ejemplar ha sido añadido correctamente"),
                        () -> System.out.println("No se ha podido añadir el ejemplar")
                );
                    break;
            case 4:
                    System.out.println("Introduce tu id de socio");
                    Integer idSocio = sc.nextInt();
                    System.out.println("Introduce el id de ejemplar");
                    Integer idEjemplar = sc.nextInt();
                Integer finalIdEjemplar = idEjemplar;
                //Busca el socio para realizar el prestamo
                almacenSocio.buscarPorId(idSocio)
                            .flatMap(
                                    socioEncontrado -> almacenEjemplar.buscarPorId(finalIdEjemplar) //con el socio encontrado busca el ejemplar
                                    .flatMap ( // con el ejemplar encontrado crea el prestamo
                                           ejemplarEncontrado -> Prestamo.crearPrestamo(socioEncontrado, LocalDate.now(),
                                                   LocalDate.now().plusDays(15), ejemplarEncontrado))

                            )
                            // Y despues de crearlo si se ha creado manda un mensaje de exito y si no de fracaso.
                            .ifPresentOrElse(
                                    prestamoRealizado -> System.out.println("Prestamo realizado correctamente"),
                                    () -> System.out.println("Prestamo no realizado")
                            );
            case 5:
                    System.out.println("Introduce tu id de ejemplar");
                    idEjemplar = sc.nextInt();
                    //Devolveremos el libro
                devolverPrestamo(prestamos, idEjemplar, almacenEjemplar).ifPresentOrElse(
                        prestamoDevuelto -> System.out.println("Prestamo devuelto correctamente"),
                        () -> System.out.println("Prestamo no devuelto")
                );
                    break;
            case 6:
                System.out.println("Pulsa un boton para ver los socios atrasados");
                sc.nextLine();
                //Recorre todos los prestamos y imprime el nombre del que esta atrasado
                ReportesService.socioConPrestamosAtrasados(prestamos).forEach(socioAtrasado -> System.out.println(socioAtrasado.nombre()));
                break;
            case 7:
                System.out.println("Pulsa el boton para ver el top 5 prestamos");
                sc.nextLine();
                //Recorre los prestamos y imprime el top 5
                ReportesService.librosMasPrestados(prestamos).forEach(System.out::println);
                break;
            case 8:
                System.out.println("Pulsa el boton para ver los meses con mas prestamos");
                sc.nextLine();
                //Recorre los prestamos y imprime los meses que tienen mas
                ReportesService.mesesConMasPrestamos(prestamos).entrySet().forEach(System.out::println);
                break;
            default:
                    break;
        }
    } while(opcion != 9);
    sc.close();
}

public static Socio crearSocio(Almacen<Socio, Integer> almacenSocio)  {
    Scanner sc = new Scanner(System.in);
    //Pedimos informacion
    System.out.println("Introduce tu nombre");
    String nombre = sc.nextLine();

    System.out.println("Introduce tu email");
    String email = sc.nextLine();
    Integer id = almacenSocio.listarTodos().size() + 1; // Consigues el id mediante el tamaño mas 1
    Socio socio = new Socio(id, nombre, email); //Creamos el socio
    almacenSocio.guardar(socio); //Lo guardamos en el almacen
    return socio;
}

public static Optional<Libro> añadirLibro(Almacen<Libro, String> almacenLibro) {
    Scanner sc = new Scanner(System.in);

    //Pedimos los datos
    System.out.println("Introduce el ISBN del libro");
    String isbn = sc.nextLine();

    System.out.println("Introduce el nombre del libro");
    String nombre = sc.nextLine();

    System.out.println("Introduce el autor del libro");
    String autor = sc.nextLine();

    //Revisamos que el isbn no exista ya antes de guardar
    Libro libro = new Libro(isbn, nombre, autor);
    if(!almacenLibro.buscarPorId(isbn).isPresent()) {
        almacenLibro.guardar(libro);
    }

    return almacenLibro.buscarPorId(isbn);
}

public static Optional<Ejemplar> añadirEjemplar(Almacen<Libro, String> almacenLibro ,Almacen<Ejemplar, Integer> almacenEjemplar) {
    Scanner sc = new Scanner(System.in);
    //Pedimos los datos
    System.out.println("Introduce el ISBN del ejemplar");
    String isbn = sc.nextLine();
    if(!almacenLibro.buscarPorId(isbn).isPresent()) {
        return Optional.empty();
    }

    Integer id  = almacenEjemplar.listarTodos().size() + 1;
    Ejemplar ejemplar = new Ejemplar(isbn, DISPONIBLE, id);
    almacenEjemplar.guardar(ejemplar);
    return Optional.of(ejemplar);
}

public static Optional<Prestamo> devolverPrestamo(List<Prestamo> prestamos, Integer idEjemplar, Almacen<Ejemplar, Integer> almacenEjemplar) {
    //Busca el prestamo y lo filtra revisando si el prestamo coincide con el id y no esta devuelto
    Optional<Prestamo> prestamoEncontrado = prestamos.stream()
           .filter(prestamo -> prestamo.getEjemplar().getId().equals(idEjemplar) && !prestamo.estaDevuelto())
           .findFirst();

    //Busca si esta activo el prestamo y activa el metodo de devolver el libro, indicando la fecha.
    prestamoEncontrado.ifPresent(prestamoActivo -> {
        prestamoActivo.devolverLibro(LocalDate.now());
        almacenEjemplar.buscarPorId(idEjemplar).ifPresent(Ejemplar::marcarComoDisponible); //Lo marca como disponigle
    });
    return prestamoEncontrado;
}