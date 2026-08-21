const BASE_URL = "http://localhost:8080";

export const API_ROUTES = {
    LIBRO: {
      LISTAR: `${BASE_URL}/libro`,
      BUSCAR: (isbn: string) =>`${BASE_URL}/libro/${isbn}`,
      CREAR: `${BASE_URL}/libro`,
      ACTUALIZAR: (isbn: string ) => `${BASE_URL}/libro/${isbn}`,
      BORRAR: (isbn: string) => `${BASE_URL}/libro/${isbn}`,
    },
    EJEMPLAR: {
        LISTAR: `${BASE_URL}/ejemplar`,
        BUSCAR: (idEjemplar: number) => `${BASE_URL}/ejemplar/${idEjemplar}`,
        CREAR: `${BASE_URL}/ejemplar`,
        BORRAR: (idEjemplar: number) => `${BASE_URL}/ejemplar/${idEjemplar}`,
    },
    PRESTAMO: {
        LISTAR: `${BASE_URL}/prestamo`,
        BUSCAR: (id: number) => `${BASE_URL}/prestamo/${id}`,
        CREAR: `${BASE_URL}/prestamo`,
        SOCIOS_ATRASADOS: `${BASE_URL}/prestamo/socios-atrasados`,
        LIBROS_MAS_PRESTAMOS: `${BASE_URL}/prestamo/libros-mas-prestados`,
        MESES_CON_MAS_PRESTAMOS: `${BASE_URL}/prestamo/meses-mas-prestamos`,
        PRESTAMOS_ACTIVOS_SOCIO: (idSocio: number) =>`${BASE_URL}/prestamo/prestamos-activos-socio?idSocio=${idSocio}`,
        ACTUALIZAR: (id: number) => `${BASE_URL}/prestamo/${id}`,
        DEVOLVER: (idEjemplar: number) => `${BASE_URL}/prestamo/devolver?idEjemplar=${idEjemplar}`,
        BORRAR: (id: number) => `${BASE_URL}/prestamo/${id}`,
    },
    SOCIOS: {
        LISTAR: `${BASE_URL}/socio`,
        BUSCAR: (idSocio: number) => `${BASE_URL}/socio/${idSocio}`,
        CREAR: `${BASE_URL}/socio`,
        ACTUALIZAR: (idSocio: number) => `${BASE_URL}/socio/${idSocio}`,
        BORRAR: (idSocio: number) => `${BASE_URL}/socio/${idSocio}`,
    }
}