import {API_ROUTES} from "./apiRoutes.ts";
import type {Ejemplar} from "../Types.ts";
import {apiFetch} from "./apiFetch.ts";

export class EjemplarService {

    //Listar
    static async listarEjemplares(): Promise<Ejemplar[]> {
        return apiFetch<Ejemplar[]>(API_ROUTES.EJEMPLAR.LISTAR);
    }


    //Crear
    static async crearEjemplar(ejemplar: Ejemplar): Promise<Ejemplar> {
       return apiFetch<Ejemplar>(API_ROUTES.EJEMPLAR.CREAR, {
           method: 'POST',
           body: JSON.stringify(ejemplar),
       });
    }

    //Borrar
    static async borrarEjemplar(idEjemplar: number): Promise<void> {
        return apiFetch<void>(API_ROUTES.EJEMPLAR.BORRAR(idEjemplar), {
            method: 'DELETE',
        });
    }
}