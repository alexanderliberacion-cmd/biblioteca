import * as React from "react";
import type {Link} from "react-router";
import type {ReactNode} from "react";



export interface Ejemplar  {
    idEjemplar: number,
    estado: EstadoTipo,
    isbn: string
}

export const Estado = {
    DISPONIBLE: "DISPONIBLE",
    PRESTADO: "PRESTADO"
} as const;

export type EstadoTipo = typeof Estado[keyof typeof Estado];

export interface Libro {
    isbn: string,
    titulo: string,
    autor: string
}

export interface Prestamo {
    fechaInicio: string,
    fechaLimite: string,
    fechaDevolucion?: string,
    idSocio: number,
    idEjemplar: number
}

export interface Socio {
    idSocio: number,
    nombre: string,
    email: string,
    usuario?: Usuario | null
}

export interface Props {
    as?: React.ElementType;
}

export type BotonProps = Props & (
    React.ComponentPropsWithoutRef<'button'> | React.ComponentPropsWithoutRef<typeof Link>
);

export interface SelectorSocioProps {
    socios: Socio[],
    usuarios?: Usuario[],
    idSocio: number,
    onChangeSocio: (event: React.ChangeEvent<HTMLSelectElement>) => void,
}

export interface locationsItem {
    nombre: string,
    ruta: string,
    requiredRoles?: Rol[]
}

export const locationsObject: locationsItem[] = [
    {
        nombre: "Socios",
        ruta: "/socios",
        requiredRoles: ["ADMIN","SOCIO"]
    },
    {
        nombre: "Almacen",
        ruta: "/almacen",
        requiredRoles: ["ADMIN"]
    },
    {
        nombre: "Reportes",
        ruta: "/reportes",
        requiredRoles: ["ADMIN"]
    }];

export interface ListaProps {
    children: ReactNode
}

export interface InputProps{
    value: string | number,
    onChange: (event: React.ChangeEvent<HTMLInputElement>) => void,
    name: string,
    placeholder: string,
    type?: 'text' | 'password' | 'email' | 'number',
    id?: string
}

export interface SelectorLibroProps {
    libros: Libro[],
    isbn: string
    onChangeLibros: (event: React.ChangeEvent<HTMLSelectElement>) => void,
}

export interface auth {
    email: string,
    rol: string,
    token: string
}

export interface Usuario {
    id: number,
    email: string,
    rol: string,
    socioId?: number
}

export type Rol = 'BASIC' | 'SOCIO' | 'ADMIN';


export interface ProtectedRoutesProps {
    requiredRoles?: Rol[],
}

export interface AuthContextType {
    sesionExpirada: boolean,
    resetSesionExpirada: ()  => void
}