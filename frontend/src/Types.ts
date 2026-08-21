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
    email: string
}

export interface Props {
    as?: React.ElementType;
}

export type BotonProps = Props & (
    React.ComponentPropsWithoutRef<'button'> | React.ComponentPropsWithoutRef<typeof Link>
);

export interface SelectorSocioProps {
    socios: Socio[],
    idSocio: number,
    onChangeSocio: (event: React.ChangeEvent<HTMLSelectElement>) => void,
}

export interface locationsItem {
    nombre: string,
    ruta: string
}

export const locationsObject: locationsItem[] = [
    {
        nombre: "Socios",
        ruta: "/socios"
    },
    {
        nombre: "Almacen",
        ruta: "/almacen"
    },
    {
        nombre: "Reportes",
        ruta: "/reportes"
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