import {createContext, type PropsWithChildren, useContext, useEffect, useState} from "react";
import type {AuthContextType} from "./Types.ts";

//Crea el contexto
const AuthContext = createContext<AuthContextType>({sesionExpirada: false, resetSesionExpirada: () => {}});

//Esta se utiliza para notificar si la sesion esta expirada, esto se carga en el useffect.
let notificarSesionExpirada: (() => void) | null = null;

//Hook de useAuth que retorna el contexto actual
export const useAuth = () => {
    return useContext(AuthContext);
}

//Chequea si la sesion esta expirada para lanzarla en el 401 de apiFetch
export function dispararSesionExpirada() {
    if(notificarSesionExpirada !== null) {
        notificarSesionExpirada();
    }
}


export function AuthProvider({children}: PropsWithChildren) {
    const [sesionExpirada, setSesionExpirada] = useState<boolean>(false);
    useEffect(() => {
       notificarSesionExpirada = () => setSesionExpirada(true);
    }, []);

    function resetSesionExpirada() {
        setSesionExpirada(false);
    }

    return (
        <>
        <AuthContext.Provider value={{sesionExpirada, resetSesionExpirada}}>{children}</AuthContext.Provider>
        </>
    );
}
