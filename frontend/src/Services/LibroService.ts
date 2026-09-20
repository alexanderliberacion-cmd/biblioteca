import {API_ROUTES} from "./apiRoutes.ts";
import type {Libro} from "../Types.ts";
import {apiFetch} from "./apiFetch.ts";

export class LibroService {
    //Listar Libros
    static async listarLibros(): Promise<Libro[]> {
       return apiFetch<Libro[]>(API_ROUTES.LIBRO.LISTAR);
    }

    //Crear
    static async crearLibro (libro: Libro): Promise<Libro> {
        return apiFetch<Libro>(API_ROUTES.LIBRO.CREAR, {
            method: "POST",
            body: JSON.stringify(libro),
        })
    }

    //actualizar
    static async actualizarLibro (isbn: string,libro: Libro): Promise<Libro> {
        return apiFetch<Libro>(API_ROUTES.LIBRO.ACTUALIZAR(isbn), {
            method: "PUT",
            body: JSON.stringify(libro),
        })
    }

    //borrar
    static async borrarLibro (isbn: string): Promise<void> {
        return apiFetch<void>(API_ROUTES.LIBRO.BORRAR(isbn), {
            method: 'DELETE',
        })
    }
}