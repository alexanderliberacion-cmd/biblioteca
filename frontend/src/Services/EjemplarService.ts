import {API_ROUTES} from "./apiRoutes.ts";
import type {Ejemplar} from "../Types.ts";

export class EjemplarService {

    //Listar
    static async listarEjemplares(): Promise<Ejemplar[]> {
        const response= await fetch(API_ROUTES.EJEMPLAR.LISTAR);
        if(!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //Buscar
    static async buscarEjemplar(idEjemplar: number): Promise<Ejemplar | null> {
        const url = API_ROUTES.EJEMPLAR.BUSCAR(idEjemplar);
        const response = await fetch(url);
        if(!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //Crear
    static async crearEjemplar(ejemplar: Ejemplar): Promise<Ejemplar> {
       const response = await fetch(API_ROUTES.EJEMPLAR.CREAR, {
           method: 'POST',
           headers: {
               'Content-Type': 'application/json'
           },
           body: JSON.stringify(ejemplar),
       });
       if(!response.ok) {
           throw new Error(response.statusText);
       }
       return response.json();
    }



    //Borrar
    static async borrarEjemplar(idEjemplar: number): Promise<void> {
        const url = API_ROUTES.EJEMPLAR.BORRAR(idEjemplar);
        const response = await fetch(url, {
            method: 'DELETE',
        });
        if(!response.ok) {
            throw new Error(response.statusText);
        }
    }
}