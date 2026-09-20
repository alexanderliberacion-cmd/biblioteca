import {dispararSesionExpirada} from "../AuthProvider.tsx";

const BASE_URL = import.meta.env.VITE_API_URL;

export async function apiFetch<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
    const token = localStorage.getItem("token"); //agarra el token
    //construye la respuesta uniendo la url base, el endpoint, las opciones de peticion de esa llamada y los headers.
    const response  = await fetch(`${BASE_URL}${endpoint}`, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...(token && {
                Authorization: `Bearer ${token}`, //Añade el bearer al token.
            }),
            ...options.headers, //trae las opciones de los headers.
        },
    });


    //Si no tiene contenido
    if (response.status === 204) {
        return undefined as T;
    }

    //Si no esta autorizado
    if(response.status === 401) {
        localStorage.removeItem("token");
        dispararSesionExpirada();
        throw new Error("UNAUTHORIZED");
    }

    //Si no funciona error
    if (!response.ok) {
        throw new Error(response.statusText);
    }

    return await response.json();
}