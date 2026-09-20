
export const API_ROUTES = {
    LIBRO: {
      LISTAR: `/libro/listar`,
      CREAR: `/libro`,
      ACTUALIZAR: (isbn: string ) => `/libro/actualizar/${isbn}`,
      BORRAR: (isbn: string) => `/libro/borrar/${isbn}`,
    },
    EJEMPLAR: {
        LISTAR: `/ejemplar/listar`,
        CREAR: `/ejemplar`,
        BORRAR: (idEjemplar: number) => `/ejemplar/borrar/${idEjemplar}`,
    },
    PRESTAMO: {
        LISTAR: `/prestamo/listar`,
        CREAR: `/prestamo`,
        SOCIOS_ATRASADOS: `/prestamo/socios-atrasados`,
        LIBROS_MAS_PRESTAMOS: `/prestamo/libros-mas-prestados`,
        MESES_CON_MAS_PRESTAMOS: `/prestamo/meses-mas-prestamos`,
        PRESTAMOS_ACTIVOS_SOCIO: (idSocio: number) =>`/prestamo/prestamos-activos-socio?idSocio=${idSocio}`,
        ACTUALIZAR: (id: number) => `/prestamo/actualizar/${id}`,
        DEVOLVER: (idEjemplar: number) => `/prestamo/devolver/${idEjemplar}`,
        BORRAR: (id: number) => `/prestamo/borrar/${id}`,
    },
    SOCIOS: {
        LISTAR: `/socio/listar`,
        CREAR: `/socio`,
        ACTUALIZAR: (idSocio: number) => `/socio/actualizar/${idSocio}`,
        BORRAR: (idSocio: number) => `/socio/borrar/${idSocio}`,
        USUARIOS_PENDIENTES: `/socio/usuarios-pendientes`,
        ACTIVACION: (idUsuario: number) => `/socio/activacion/${idUsuario}`
    },
    AUTH: {
        LOGIN: `/auth/login`,
        REGISTER: `/auth/register`,
    }
}