import {API_ROUTES} from "./apiRoutes.ts";
import type { Prestamo, Socio} from "../Types.ts";
import {apiFetch} from "./apiFetch.ts";

export class PrestamoService {
    //Listar
    static async listarPrestamos(): Promise<Prestamo[]> {
        return apiFetch<Prestamo[]>(API_ROUTES.PRESTAMO.LISTAR);
    }

    //crear
    static async crearPrestamo(idSocio: number, idEjemplar: number): Promise<Prestamo> {
        return apiFetch<Prestamo>(API_ROUTES.PRESTAMO.CREAR, {
            method: 'POST',
            body: JSON.stringify({ idSocio, idEjemplar }),
        })
    }

    //Actualizar
    static async actualizarPrestamo(id: number): Promise<Prestamo> {
        return apiFetch<Prestamo>(API_ROUTES.PRESTAMO.ACTUALIZAR(id), {
            method: 'PUT',
            body: JSON.stringify(id)
        })
    }

    //devolver
    static async devolverPrestamo(idEjemplar: number): Promise<Prestamo> {
       return apiFetch<Prestamo>(API_ROUTES.PRESTAMO.DEVOLVER(idEjemplar), {
           method: 'PUT',
           body: JSON.stringify(idEjemplar)
       })
    }

    //Borrar
    static async borrarPrestammo(id: number): Promise<void> {
        return apiFetch<void>(API_ROUTES.PRESTAMO.BORRAR(id), {
            method: 'DELETE',
        })
    }

    //Socios Atrasados
    static async sociosAtrasados(): Promise<Socio[]> {
        return apiFetch<Socio[]>(API_ROUTES.PRESTAMO.SOCIOS_ATRASADOS);
    }

    //Libros con mas prestamos
    static async librosMasPrestamos(): Promise<string[]> {
        return apiFetch<string[]>(API_ROUTES.PRESTAMO.LIBROS_MAS_PRESTAMOS);
    }

    //Meses con mas prestamos
    static async mesesMasPrestamos(): Promise<Record<string, number>> {
       return apiFetch<Record<string, number>>(API_ROUTES.PRESTAMO.MESES_CON_MAS_PRESTAMOS);
    }

    //Prestamos activos de un socio
    static async prestamosActivosSocio(idSocio: number): Promise<Prestamo[]> {
       return apiFetch<Prestamo[]>(API_ROUTES.PRESTAMO.PRESTAMOS_ACTIVOS_SOCIO(idSocio));
    }
}