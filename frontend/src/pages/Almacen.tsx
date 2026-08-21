import {type Ejemplar, Estado, type Libro} from "../Types.ts";
import {useEffect, useState} from "react";
import * as React from "react";
import {Input} from "../component/Input.tsx";
import {Boton} from "../component/Boton.tsx";
import {LibroService} from "../Services/LibroService.ts";
import {SelectorLibro} from "../component/SelectorLibro.tsx";
import {EjemplarService} from "../Services/EjemplarService.ts";


export function Almacen() {
    const [libro, setLibro] = useState<Libro>({isbn: "", titulo: "", autor: ""});
    const [libros, setLibros] = useState<Libro[]>([]);
    const [errorMessage, setErrorMessage] = useState<string | null>(null); //Mensajes de error personalizados
    const [ejemplar, setEjemplar] = useState<Ejemplar>({idEjemplar: 0, isbn: "", estado: Estado.DISPONIBLE});
    const [ejemplarCantidad, setEjemplarCantidad] = useState<number>(1);
    const [ejemplares, setEjemplares] = useState<Ejemplar[]>([]);
    const [visible, setVisible] = useState<boolean>(false);
    const [isbnEnEdicion, setIsbnEnEdicion] = useState<string>("");

    useEffect(() => {
        async function cargarLibros() {
            try {
                const [libros, ejemplares] = await Promise.all([
                    LibroService.listarLibros(),
                    EjemplarService.listarEjemplares()
                ]);
                setLibros(libros);
                setEjemplares(ejemplares);
            } catch (error) {
                console.error("Error al cargar los ibros", error);
                setErrorMessage("No se ha podido cargar los libros");
            }
        }

        cargarLibros();
    }, []);

    function aparecer(e: React.MouseEvent<HTMLButtonElement, MouseEvent>) {
        e.preventDefault();
        setVisible(!visible);

    }

    function onChangeLibros(e: React.ChangeEvent<HTMLSelectElement>) {
        setEjemplar({...ejemplar, isbn: e.target.value});
    }

    function onChangeCantidad(e: React.ChangeEvent<HTMLInputElement>) {
        const parsed = parseInt(e.currentTarget.value);
        setEjemplarCantidad(parsed);
    }

    function onChangeLibro(e: React.ChangeEvent<HTMLInputElement>) {
        setLibro({...libro, [e.target.name]: e.target.value}); //
    }

    async function handleCrearEjemplar() {
        if (ejemplar.isbn === "") return;
        try {
            for (let i = 0; i < ejemplarCantidad; i++) {
                await EjemplarService.crearEjemplar(ejemplar);
            }
            setEjemplar({idEjemplar: 0, isbn: "", estado: Estado.DISPONIBLE});
        } catch (error) {
            console.error("Error creating exemplar:", error);
            setErrorMessage("Error al crear el ejemplar. Por favor, inténtalo de nuevo.");
        }
    }

    async function handleLibro() {
        if (libro.titulo === "" || libro.isbn === "" || libro.autor === "") return;
        try {
            if (isbnEnEdicion) {
                await LibroService.actualizarLibro(isbnEnEdicion, libro);
            } else {
                await LibroService.crearLibro(libro);
            }
            setLibro({isbn: "", titulo: "", autor: ""});
            setIsbnEnEdicion("");
        } catch (error) {
            console.error("Error creating book:", error);
            setErrorMessage("Error al crear el libro. Por favor, inténtalo de nuevo.");
        }
    }

    async function handleBorrarLibro(isbn: string) {
        try {
            await LibroService.borrarLibro(isbn);
            setLibros(await LibroService.listarLibros());
        } catch (error) {
            console.error("Error borrando el libro:", error);
            setErrorMessage("Error al borrar libro");
        }
    }

    async function handleBorrarEjemplar(idEjemplar: number) {
        try {
            const ejemplarId = ejemplares.find(ejemplar => ejemplar.idEjemplar === idEjemplar);
            if (ejemplarId?.estado === Estado.PRESTADO) {
                setErrorMessage("No se puede borrar un ejemplar prestado");
                return;
            }
            await EjemplarService.borrarEjemplar(idEjemplar);
            setEjemplares(await EjemplarService.listarEjemplares());
        } catch (error) {
            console.log("Error borrando el ejemplar", error);
            setErrorMessage("Error al borrar ejemplar");
        }
    }

    function handleEditarLibro(libro: Libro) {
        setLibro(libro);
        setIsbnEnEdicion(libro.isbn);
    }

    return (
        <>
            <div
                className="w-full max-w-xl mx-auto p-4 md:p-6 bg-linear-to-r from-amber-300 to-orange-400 rounded-2xl flex flex-col gap-6 shadow-lg">

                <div className="flex flex-col gap-3  backdrop-blur-sm p-4 rounded-xl shadow-xs">
                    <h3 className="font-bold text-base text-orange-950 pb-1 border-b border-orange-200/50">
                        {isbnEnEdicion ? "Modificar Libro" : "Registrar Libro"}
                    </h3>
                    {errorMessage && <p className="text-red-600 font-bold text-sm">{errorMessage}</p>}

                    <div className="flex flex-col gap-1">
                        <label htmlFor="isbn" className="text-xs font-bold text-slate-700">ISBN:</label>
                        <Input value={libro.isbn} onChange={onChangeLibro} name="isbn" placeholder="ISBN" id="isbn"/>
                    </div>

                    <div className="flex flex-col gap-1">
                        <label htmlFor="autor" className="text-xs font-bold text-slate-700">Autor:</label>
                        <Input value={libro.autor} onChange={onChangeLibro} name="autor" placeholder="Autor"
                               id="autor"/>
                    </div>

                    <div className="flex flex-col gap-1">
                        <label htmlFor="titulo" className="text-xs font-bold text-slate-700">Título:</label>
                        <Input value={libro.titulo} onChange={onChangeLibro} name="titulo" placeholder="Título"
                               id="titulo"/>
                    </div>

                    <div className="mt-1">
                        <Boton onClick={handleLibro}>{isbnEnEdicion ? "Guardar Cambios" : "Crear Libro"}</Boton>
                    </div>
                </div>

                <div className="flex flex-col gap-3  backdrop-blur-sm p-4 rounded-xl shadow-xs">
                    <h3 className="font-bold text-base text-orange-950 pb-1 border-b border-orange-200/50">
                        Añadir Ejemplares
                    </h3>
                    <div className="flex flex-col gap-1">
                        <label className="text-xs font-bold text-slate-700">Seleccionar Libro:</label>
                        <SelectorLibro libros={libros} isbn={ejemplar.isbn} onChangeLibros={onChangeLibros}/>
                    </div>

                    <div className="flex flex-col gap-1">
                        <label htmlFor="cantidad" className="text-xs font-bold text-slate-700">Elige la
                            cantidad:</label>
                        <Input value={ejemplarCantidad.toString()} onChange={onChangeCantidad} name="cantidad"
                               placeholder="1" type="number" id="cantidad"/>
                    </div>

                    <div className="mt-1">
                        <Boton onClick={handleCrearEjemplar}>Crear Ejemplar</Boton>
                    </div>
                </div>
            </div>

            <section className="w-full max-w-4xl mx-auto mt-6 px-4 mb-10">
                <div className="w-full sm:w-auto inline-block mb-4">
                    <Boton onClick={aparecer}>{visible ? "Ocultar Listas" : "Ver Listas"}</Boton>
                </div>

                {visible && (
                    <div className="flex flex-col gap-6">

                        <div className="flex flex-col gap-3">
                            <h3 className="font-bold text-lg text-slate-800 px-1">Lista de Libros</h3>
                            <ul className="flex flex-col gap-2">
                                {libros.map((l) => (
                                    <li key={l.isbn}
                                        className="flex flex-col sm:flex-row gap-3 items-start sm:items-center justify-between border bg-linear-to-r from-amber-300 to-orange-400 rounded-xl p-4 shadow-xs">
                                        <div className="flex flex-col gap-1 text-slate-900 w-full sm:w-auto">
                                            <span
                                                className="bg-white/40 px-2 py-0.5 rounded font-mono text-xs font-bold w-max">ISBN: {l.isbn}</span>
                                            <span
                                                className="font-bold text-base break-words text-orange-950">{l.titulo}</span>
                                            <span className="text-sm text-slate-800 italic">por {l.autor}</span>
                                        </div>
                                        <div
                                            className="grid grid-cols-2 gap-2 w-full sm:w-auto pt-2 sm:pt-0 border-t border-white/20 sm:border-0">
                                            <Boton onClick={() => handleEditarLibro(l)}>Editar</Boton>
                                            <Boton onClick={() => handleBorrarLibro(l.isbn)}>Borrar</Boton>
                                        </div>
                                    </li>
                                ))}
                            </ul>
                        </div>

                        <div className="flex flex-col gap-3">
                            <h3 className="font-bold text-lg text-slate-800 px-1">Lista de Ejemplares</h3>
                            <ul className="flex flex-col gap-2">
                                {ejemplares.map((ej) => (
                                    <li key={ej.idEjemplar}
                                        className="flex flex-col sm:flex-row gap-3 items-start sm:items-center justify-between border bg-linear-to-r from-amber-300 to-orange-400 rounded-xl p-4 shadow-xs">
                                        <div
                                            className="flex flex-row flex-wrap gap-x-4 gap-y-1 items-center text-sm text-slate-950">
                                            <span
                                                className="bg-white/50 px-2 py-0.5 rounded font-mono text-xs font-bold">ID: {ej.idEjemplar}</span>
                                            <span className="font-medium text-xs md:text-sm">ISBN: {ej.isbn}</span>
                                            <span
                                                className="bg-white/40 px-2.5 py-0.5 rounded-full text-xs font-bold font-mono">
                                            {ej.estado}
                                        </span>
                                        </div>
                                        <div
                                            className="w-full sm:w-auto pt-2 sm:pt-0 border-t border-white/20 sm:border-0">
                                            <Boton onClick={() => handleBorrarEjemplar(ej.idEjemplar)}>Borrar</Boton>
                                        </div>
                                    </li>
                                ))}
                            </ul>
                        </div>

                    </div>
                )}
            </section>
        </>
    );
}
