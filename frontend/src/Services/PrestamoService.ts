import {API_ROUTES} from "./apiRoutes.ts";
import type { Prestamo, Socio} from "../Types.ts";

export class PrestamoService {
    //Listar
    static async listarPrestamos(): Promise<Prestamo[]> {
        const response = await fetch(API_ROUTES.PRESTAMO.LISTAR);
        if(!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //Buscar
    static async buscarPrestamo(id: number): Promise<Prestamo | null> {
        const response = await fetch(API_ROUTES.PRESTAMO.BUSCAR(id));
        if(!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //crear
    static async crearPrestamo(idSocio: number, idEjemplar: number): Promise<Prestamo> {
        const response = await fetch(`${API_ROUTES.PRESTAMO.CREAR}?idSocio=${idSocio}&idEjemplar=${idEjemplar}`, {
            method: 'POST',
        });
        if(!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //Actualizar
    static async actualizarPrestamo(id: number): Promise<Prestamo> {
        const response = await fetch(API_ROUTES.PRESTAMO.ACTUALIZAR(id), {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(id),
        });

        if(!response.ok) {
            throw new Error(response.statusText);
        }

        return response.json();
    }

    static async devolverPrestamo(idEjemplar: number): Promise<Prestamo> {
        const response = await fetch(API_ROUTES.PRESTAMO.DEVOLVER(idEjemplar), {
            method: 'PUT',
        });
        if(!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //Borrar
    static async borrarPrestammo(id: number): Promise<void> {
        const response = await fetch(API_ROUTES.PRESTAMO.BORRAR(id), {
            method: 'DELETE'
        });
        if(!response.ok) {
            throw new Error(response.statusText);
        }
    }

    //Socios Atrasados
    static async sociosAtrasados(): Promise<Socio[]> {
        const response = await fetch(API_ROUTES.PRESTAMO.SOCIOS_ATRASADOS);
        if(!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //Libros con mas prestamos
    static async librosMasPrestamos(): Promise<string[]> {
        const response = await fetch(API_ROUTES.PRESTAMO.LIBROS_MAS_PRESTAMOS);
        if(!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //Meses con mas prestamos
    static async mesesMasPrestamos(): Promise<Record<string, number>> {
        const response = await fetch(API_ROUTES.PRESTAMO.MESES_CON_MAS_PRESTAMOS);
        if(!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //Prestamos activos de un socio
    static async prestamosActivosSocio(idSocio: number): Promise<Prestamo[]> {
        const response = await fetch(API_ROUTES.PRESTAMO.PRESTAMOS_ACTIVOS_SOCIO(idSocio));
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }
}