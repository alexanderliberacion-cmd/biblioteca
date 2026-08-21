import {Boton} from "../component/Boton.tsx";
import { useEffect, useState} from "react";
import {type Ejemplar, type Libro, type Socio, type Prestamo, Estado} from "../Types.ts";
import {SocioService} from "../Services/SocioService.ts";
import {EjemplarService} from "../Services/EjemplarService.ts";
import {LibroService} from "../Services/LibroService.ts";
import * as React from "react";
import {PrestamoService} from "../Services/PrestamoService.ts";
import {SelectorSocio} from "../component/SelectorSocio.tsx";




export function Catalogo() {
    const [libros, setLibros] = useState<Libro[]>([]);
    const [ejemplares, setEjemplares] = useState<Ejemplar[]>([]);
    const [socios, setSocios] = useState<Socio[]>([]);
    const [visible, setVisible] = useState<boolean>(false);
    const [idSocio, setIdSocio] = useState<number>(0);
    const [idEjemplar, setIdEjemplar] = useState<number>(0);
    const [errorMessage, setErrorMessage] = useState<string | null>(null); //Mensajes de error personalizados
    const [idSocioDevolucion, setIdSocioDevolucion] = useState<number>(0);
    const [prestamosActivos, setPrestamosActivos] = useState<Prestamo[]>([]);
    const[idEjemplarDevuelto, setIdEjemplarDevuelto] = useState<number>(0);

    //Variable que revisa los ejemplares disponibles
    const ejemplarDisponibles = ejemplares.filter(e => e.estado === Estado.DISPONIBLE);



    //Carga los datos de inicio
    useEffect(() => {
        async function cargarDatos() {
            try {
                const [libros, ejemplares, socios] = await Promise.all([
                    LibroService.listarLibros(),
                    EjemplarService.listarEjemplares(),
                    SocioService.listarSocios()
                ]);
                setLibros(libros);
                setEjemplares(ejemplares);
                setSocios(socios);
            } catch (error) {
                console.error("Error al cargar los datos:", error);
            }
        }
        cargarDatos();
    }, [])


    function aparecer(e: React.MouseEvent<HTMLButtonElement, MouseEvent>) {
        e.preventDefault();
        setVisible(!visible);

    }

    function onChangeIdSocio(e: React.ChangeEvent<HTMLSelectElement>) {
        const parsed = parseInt(e. currentTarget.value);
        setIdSocio(parsed);
    }

    function onChangeIdEjemplar(e: React.ChangeEvent<HTMLSelectElement>) {
        const parsed = parseInt(e.currentTarget.value);
        setIdEjemplar(parsed);
    }

    //Maneja el crear prestamo.
    async function handleRealizarPrestamo(){
        if (idSocio === 0 || idEjemplar === 0) return;
        try {
           await PrestamoService.crearPrestamo(idSocio, idEjemplar);
           setEjemplares(await EjemplarService.listarEjemplares());
           setIdEjemplar(0); //Resetea el idEjemplar
           setIdSocio(0); //Resetea el idSocio
        } catch (error) {
           console.error("Error al Realizar prestamo", error);
           setErrorMessage("Error al realizar el prestamo. Por favor, inténtalo de nuevo.");
        }
    }

    async function onChangeIdSocioDevolucion(e: React.ChangeEvent<HTMLSelectElement>) {
        const parsed = parseInt(e.currentTarget.value);
        const backendCall = await PrestamoService.prestamosActivosSocio(parsed);
        setIdSocioDevolucion(parsed);
        setPrestamosActivos(backendCall);
    }

    function onChangeIdEjemplarDevuelto(e: React.ChangeEvent<HTMLSelectElement>) {
        const parsed = parseInt(e.currentTarget.value);
        setIdEjemplarDevuelto(parsed);
    }

    async function handleDevolverPrestamo(){
        if(idEjemplarDevuelto === 0) return;

            try {
                await PrestamoService.devolverPrestamo(idEjemplarDevuelto);
                setEjemplares(await EjemplarService.listarEjemplares());
                setPrestamosActivos(await PrestamoService.prestamosActivosSocio(idSocioDevolucion));
                setIdEjemplarDevuelto(0); //Resetea el idEjemplarDevuelto

            } catch (error) {
                console.error("Error devolver prestamo", error);
                setErrorMessage("Error al devolver el prestamo. Por favor, inténtalo de nuevo.");
            }

    }

    return (
        <>
            <div className="w-full max-w-4xl mx-auto p-4 md:p-8 flex flex-col gap-6">

                <section className="w-full bg-linear-to-r from-amber-300 to-orange-400 p-5 rounded-2xl shadow-lg">
                    <h2 className="pb-3 font-bold text-orange-950 text-xl border-b border-orange-200/40 mb-4">
                        Ejemplares
                    </h2>
                    <div className="mb-4">
                        <Boton onClick={aparecer}>{visible ? "Ocultar disponibles" : "Ver disponibles"}</Boton>
                    </div>

                    {visible && (
                        <ul className="flex flex-col gap-2 mt-2">
                            {libros.map(libro => {
                                const disponibles = ejemplares.filter(e => e.isbn === libro.isbn && e.estado === Estado.DISPONIBLE).length;
                                if (disponibles === 0) return null;
                                return (
                                    <li key={libro.isbn} className="p-3 bg-amber-400 rounded-xl text-sm md:text-base text-slate-800 flex justify-between items-center shadow-xs">
                                        <span className="font-semibold truncate mr-2">{libro.titulo}</span>
                                        <span className="text-orange-800 text-xs font-bold px-2.5 py-1 rounded-full whitespace-nowrap">
                                        ({disponibles})
                                    </span>
                                    </li>
                                );
                            })}
                        </ul>
                    )}
                </section>

                <section className="w-full bg-linear-to-r from-amber-300 to-orange-400 p-5 rounded-2xl shadow-lg">
                    <h2 className="pb-3 font-bold text-orange-950 text-xl border-b border-orange-200/40 mb-4">
                        Préstamos
                    </h2>

                    {errorMessage && (
                        <p className="text-red-600 font-bold text-sm  p-2.5 rounded-lg mb-4 text-center shadow-xs">
                            {errorMessage}
                        </p>
                    )}

                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">

                        <div className="backdrop-blur-sm p-4 rounded-xl flex flex-col gap-3 shadow-xs">
                            <h3 className="text-sm font-bold text-orange-950 uppercase tracking-wider pb-1 border-b border-orange-100/50">
                                Realizar Préstamo
                            </h3>

                            <div className="flex flex-col gap-1.5">
                                <label className="text-xs font-bold text-slate-600">Socio:</label>
                                <SelectorSocio socios={socios} idSocio={idSocio} onChangeSocio={onChangeIdSocio} />
                            </div>

                            <div className="flex flex-col gap-1.5">
                                <label className="text-xs font-bold text-slate-600">Ejemplar:</label>
                                <select
                                    value={idEjemplar}
                                    onChange={onChangeIdEjemplar}
                                    className="w-full p-2.5 border-2  bg-linear-to-r from-amber-300 to-orange-400 text-slate-900 font-bold rounded-lg text-sm transition-transform active:scale-98"
                                >
                                    <option value={0}>Selecciona un ejemplar</option>
                                    {ejemplarDisponibles.map(e => {
                                        const libro = libros.find(l => l.isbn === e.isbn);
                                        return <option key={e.idEjemplar} value={e.idEjemplar}>{libro?.titulo ?? "Libro no encontrado"}</option>
                                    })}
                                </select>
                            </div>

                            <div className="mt-2">
                                <Boton onClick={handleRealizarPrestamo}>Realizar</Boton>
                            </div>
                        </div>

                        <div className="backdrop-blur-sm p-4 rounded-xl flex flex-col gap-3 shadow-xs">
                            <h3 className="text-sm font-bold text-orange-950 uppercase tracking-wider pb-1 border-b border-orange-100/50">
                                Devolver Préstamo
                            </h3>

                            <div className="flex flex-col gap-1.5">
                                <label className="text-xs font-bold text-slate-600">Socio:</label>
                                <SelectorSocio socios={socios} idSocio={idSocioDevolucion} onChangeSocio={onChangeIdSocioDevolucion} />
                            </div>

                            <div className="flex flex-col gap-1.5">
                                <label className="text-xs font-bold text-slate-600">Ejemplar:</label>
                                <select
                                    value={idEjemplarDevuelto}
                                    onChange={onChangeIdEjemplarDevuelto}
                                    className="w-full p-2.5 border-2 bg-linear-to-r from-amber-300 to-orange-400 text-slate-900 font-bold rounded-lg text-sm transition-transform active:scale-98"
                                >
                                    <option value={0}>Selecciona un ejemplar</option>
                                    {prestamosActivos.map(p => {
                                        const ejemplarEncontrado = ejemplares.find(e => e.idEjemplar === p.idEjemplar);
                                        const libroEncontrado = libros.find(l => l.isbn === ejemplarEncontrado?.isbn);
                                        return <option key={p.idEjemplar} value={p.idEjemplar} className={"bg-linear-to-r from-amber-300 to-orange-400"}>{p.idEjemplar} - {libroEncontrado?.titulo ?? "libro no encontrado"}</option>
                                    })}
                                </select>
                            </div>

                            <div className="mt-2">
                                <Boton onClick={handleDevolverPrestamo}>Devolver</Boton>
                            </div>
                        </div>

                    </div>
                </section>

            </div>
        </>
    );

}
