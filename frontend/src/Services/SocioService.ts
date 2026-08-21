import {API_ROUTES} from "./apiRoutes.ts";
import type{Socio} from "../Types.ts";

export class SocioService {
    //Listar
    static async listarSocios(): Promise<Socio[]> {
        const response = await fetch(API_ROUTES.SOCIOS.LISTAR);
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //Buscar
    static async buscarSocio(idSocio: number): Promise<Socio | null> {
        const response = await fetch(API_ROUTES.SOCIOS.BUSCAR(idSocio));
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //Crear
    static async crearSocio(socio: Socio): Promise<Socio> {
        //Esto separa el id del socio del objeto. Y despues envia o el objeto sin el id o el socio completo si tiene un id diferente
        const {idSocio, ...socioData} = socio;
        const bodyToSend = idSocio === 0 ? socioData : socio;

        const response = await fetch(API_ROUTES.SOCIOS.CREAR, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bodyToSend),
        });
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //Actualizar
    static async actualizarSocio(idSocio: number, socio: Socio): Promise<Socio> {
        const response = await fetch(API_ROUTES.SOCIOS.ACTUALIZAR(idSocio), {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(socio)
        });
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    //Borrar
    static async borrarSocio(idSocio: number): Promise<void> {
        const response = await fetch(API_ROUTES.SOCIOS.BORRAR(idSocio), {
            method: 'DELETE'
        });
        if (!response.ok) {
            throw new Error(response.statusText);
        }
    }
}