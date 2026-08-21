import {useEffect, useState} from "react";
import type {Libro, Socio} from "../Types.ts";
import * as React from "react";
import {PrestamoService} from "../Services/PrestamoService.ts";
import {Boton} from "../component/Boton.tsx";
import {LibroService} from "../Services/LibroService.ts";

export function Reportes() {

    const [socios, setSocios] = useState<Socio[]>([]);
    const [libroClaves, setLibroClaves] = useState<string[]>([]);
    const [libros, setLibros] = useState<Libro[]>([]);
    const [meses, setMeses] = useState<Record<string, number>>({});
    const [errorMessage, setErrorMessage] = useState<string | null>(null); //Mensajes de error personalizados
    const [visible, setVisible] = useState<boolean>(false);

    useEffect(() => {
        async function cargarDatos() {
            try {
                const [socios, libroClaves, meses, libros] = await Promise.all([
                    await PrestamoService.sociosAtrasados(),
                    await PrestamoService.librosMasPrestamos(),
                    await PrestamoService.mesesMasPrestamos(),
                    await LibroService.listarLibros()
                ]);
                setMeses(meses);
                setLibros(libros);
                setLibroClaves(libroClaves);
                setSocios(socios);
            } catch (error) {
                console.error("Error al cargar los datos", error);
                setErrorMessage("Error al cargar los datos");
            }
        }

        cargarDatos();
    }, []);


    function aparecer(e: React.MouseEvent<HTMLButtonElement, MouseEvent>) {
        e.preventDefault();
        setVisible(!visible);
    }

    return (
        <>
            {errorMessage && <p className="text-red-500 font-semibold mb-4 px-2">{errorMessage}</p>}
            <Boton onClick={aparecer}>{visible ? "Ocultar reportes" : "Mostrar reportes"}</Boton>

            {visible && (
                <div
                    className="mt-6 bg-linear-to-r from-amber-300 to-orange-400 p-4 md:p-6 rounded-2xl flex flex-col gap-6 w-full max-w-4xl mx-auto shadow-lg">

                    <section className=" backdrop-blur-sm p-4 md:p-5 rounded-xl shadow-xs">
                        <h3 className="text-lg font-bold text-orange-950 mb-3 pb-2 border-b border-orange-200/50">
                            Socios Atrasados
                        </h3>
                        <ul className="flex flex-col gap-2">
                            {socios.map((s) => (
                                <li key={s.idSocio}
                                    className="flex flex-col sm:flex-row justify-between items-start sm:items-center p-3 bg-white/50 rounded-lg gap-1 sm:gap-4">
                                    <span className="font-medium text-slate-800 break-all">{s.nombre}</span>
                                    <span className="text-sm text-slate-600 break-all">{s.email}</span>
                                </li>
                            ))}
                        </ul>
                    </section>

                    <section className=" backdrop-blur-sm p-4 md:p-5 rounded-xl shadow-xs">
                        <h3 className="text-lg font-bold text-orange-950 mb-3 pb-2 border-b border-orange-200/50">
                            Libros con más préstamos
                        </h3>
                        <ul className="flex flex-col gap-2">
                            {libroClaves.map((l) => {
                                const libroEncontrado = libros.find(libro => libro.isbn === l);
                                return libroEncontrado && (
                                    <li key={libroEncontrado.isbn}
                                        className="flex flex-col sm:flex-row justify-between items-start sm:items-center p-3 bg-white/50 rounded-lg gap-1 sm:gap-4">
                                        <span className="font-medium text-slate-800">{libroEncontrado.titulo}</span>
                                        <span
                                            className="text-sm italic bg-orange-100 text-orange-800 font-bold rounded-full px-3 py-1">por {libroEncontrado.autor}</span>
                                    </li>
                                );
                            })}
                        </ul>
                    </section>

                    <section className=" backdrop-blur-sm p-4 md:p-5 rounded-xl shadow-xs">
                        <h3 className="text-lg font-bold text-orange-950 mb-3 pb-2 border-b border-orange-200/50">
                            Meses con más préstamos
                        </h3>
                        <ul className="flex flex-col gap-2">
                            {Object.entries(meses).map(([mes, cantidad]) => (
                                <li key={mes}
                                    className="flex flex-row justify-between items-center p-3 bg-white/50 rounded-lg">
                                    <strong className="capitalize text-slate-800">{mes}</strong>
                                    <span
                                        className="text-xs md:text-sm bg-orange-100 text-orange-800 font-bold px-3 py-1 rounded-full whitespace-nowrap">
                                    {cantidad} préstamos
                                </span>
                                </li>
                            ))}
                        </ul>
                    </section>

                </div>
            )}
        </>
    );
}