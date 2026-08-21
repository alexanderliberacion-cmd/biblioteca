import {API_ROUTES} from "./apiRoutes.ts";
import type {Libro} from "../Types.ts";

export class LibroService {
    //Listar Librso
    static async listarLibros(): Promise<Libro[]> {
        const response = await fetch(API_ROUTES.LIBRO.LISTAR as string);
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //Buscar
    static async buscarLibro (isbn: string): Promise<Libro | null>{
        const url: string = API_ROUTES.LIBRO.BUSCAR(isbn);
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //Crear
    static async crearLibro (libro: Libro): Promise<Libro> {
        const response = await fetch(API_ROUTES.LIBRO.CREAR as string, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(libro),
        });
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //actualizar
    static async actualizarLibro (isbn: string,libro: Libro): Promise<Libro> {
        const url: string = API_ROUTES.LIBRO.ACTUALIZAR(isbn);
        const response = await fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(libro),
        });
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //borrar
    static async borrarLibro (isbn: string): Promise<void> {
        const url: string = API_ROUTES.LIBRO.BORRAR(isbn);
        const response = await fetch(url, {
            method: 'DELETE',
        });
        if (!response.ok) {
            throw new Error(response.statusText);
        }
    }
}