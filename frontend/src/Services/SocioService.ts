import {API_ROUTES} from "./apiRoutes.ts";
import type {Socio, Usuario} from "../Types.ts";
import {apiFetch} from "./apiFetch.ts";

export class SocioService {
    //Listar
    static async listarSocios(): Promise<Socio[]> {
        return apiFetch<Socio[]>(API_ROUTES.SOCIOS.LISTAR);
    }

    //Crear
    static async crearSocio(socio: Socio): Promise<Socio> {
        //Esto separa el id del socio del objeto. Y despues envia o el objeto sin el id o el socio completo si tiene un id diferente
        const {idSocio, ...socioData} = socio;
        const bodyToSend = idSocio === 0 ? socioData : socio;

       return apiFetch(API_ROUTES.SOCIOS.CREAR, {
           method: 'POST',
           body: JSON.stringify(bodyToSend),
       })
    }

    //Actualizar
    static async actualizarSocio(idSocio: number, socio: Socio): Promise<Socio> {
        return apiFetch<Socio>(API_ROUTES.SOCIOS.ACTUALIZAR(idSocio), {
            method: 'PUT',
            body: JSON.stringify(socio),
        });
    }

    //Borrar
    static async borrarSocio(idSocio: number): Promise<void> {
        return apiFetch<void>(API_ROUTES.SOCIOS.BORRAR(idSocio), {
            method: 'DELETE',
        })
    }

    //Usuarios pendientes
    static async usuariosPendientes(): Promise<Usuario[]> {
        return apiFetch<Usuario[]>(API_ROUTES.SOCIOS.USUARIOS_PENDIENTES);
    }

    //Activacion
    static async activarSocio(socio: Socio, idUsuario: number): Promise<Socio> {
        return apiFetch<Socio>(API_ROUTES.SOCIOS.ACTIVACION(idUsuario), {
            method: "PATCH",
            body: JSON.stringify(socio)
        })
    }
}