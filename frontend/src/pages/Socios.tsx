import {useEffect, useState} from "react";
import type {Prestamo, Rol, Socio, Usuario} from "../Types.ts";
import {SocioService} from "../Services/SocioService.ts";
import {PrestamoService} from "../Services/PrestamoService.ts";
import * as React from "react";
import {Input} from "../component/Input.tsx";
import {Boton} from "../component/Boton.tsx";
import {SelectorSocio} from "../component/SelectorSocio.tsx";


export function Socios() {
    const [socios, setSocios] = useState<Socio[]>([]);
    const [socio, setSocio] = useState<Socio>({idSocio: 0, email: "", nombre: ""});
    const [prestamos, setPrestamos] = useState<Prestamo[]>([]);
    const [errorMessage, setErrorMessage] = useState<string | null>(null); //Mensajes de error personalizados
    const [visible, setVisible] = useState<boolean>(false);
    const [socioEnEdicion, setSocioEnEdicion] = useState<number>(0);
    const [idSelector, setIdSelector] = useState<number>(0);
    const [usuariosPendientes, setUsuariosPendientes] = useState<Usuario[]>([])
    const prestamoActivo = prestamos.filter(p => p.idSocio === idSelector && !p.fechaDevolucion);
    const prestamoAntiguo = prestamos.filter(p => p.idSocio === idSelector && p.fechaDevolucion);
    const rolActual: Rol = localStorage.getItem("rol") as unknown as Rol;
    const esAdmin = ["ADMIN"].includes(rolActual);


    async function cargarSocios() {
        try {
            const[socios, prestamos, usuariosPendientes] = await Promise.all([
                SocioService.listarSocios(),
                PrestamoService.listarPrestamos(),
                SocioService.usuariosPendientes()
            ]);
            setSocios(socios);
            setPrestamos(prestamos);
            setUsuariosPendientes(usuariosPendientes);
        } catch(error) {
            console.log("Error al cargar los socios y los prestamos",error);
            setErrorMessage("Error al cargar socios o prestamos");
        }
    }

    useEffect(() => {

        // eslint-disable-next-line react-hooks/set-state-in-effect
        cargarSocios();
    }, []);

    function aparecer(e: React.MouseEvent<HTMLButtonElement, MouseEvent>) {
        e.preventDefault();
        setVisible(!visible);
    }

    function onChangeSocio(e: React.ChangeEvent<HTMLInputElement>){
        setSocio({...socio, [e.target.name]: e.target.value});
    }

    function onChangeSelector(e: React.ChangeEvent<HTMLSelectElement>){
        setIdSelector(Number(e.target.value));
    }

    async function handleCrearSocio() {
        if(socio.nombre == "" || socio.email == "") return;
        try {
            if (socioEnEdicion) {
                await SocioService.actualizarSocio(socio.idSocio, socio);
            } else {
                await SocioService.crearSocio(socio);
            }
            setSocio({idSocio: 0, nombre: "", email: ""});
            setSocioEnEdicion(0)
        } catch (error){
            console.log("Error al crear socio",error);
            setErrorMessage("Error al crear socio");
        }
    }

    function handleEditarSocio(socio: Socio) {
        setSocio(socio);
        setSocioEnEdicion(socio.idSocio);
    }

    async function handleBorrarSocio(idSocio: number) {
        try {
            await SocioService.borrarSocio(idSocio);
            setSocios(sociosPrevios => sociosPrevios?.filter(socio => socio.idSocio !== idSocio));
        } catch(error) {
            console.log("Error al borrar al socio",error);
            setErrorMessage("Error al borrar al socio");
        }
    }

    async function activarSocio(id: number) {
        try {
            const usuario = usuariosPendientes.find(usuario => usuario.id === id);
            const nuevoSocio = usuario ? {idSocio: 0, nombre: "", email: usuario.email}: null;
            if (nuevoSocio) {
                await SocioService.activarSocio(nuevoSocio, id);
            } else {
                setErrorMessage("No se ha encontrado un usuario compatible");
                return;
            }
            await cargarSocios();

        } catch(error) {
            console.log("Error al activar el socio",error);
            setErrorMessage("Error al activar el socio");
        }
    }




    return (
        <>
            <div className="flex flex-col gap-3 max-w-xl mx-auto p-2 *:w-full *:min-w-0">
                {errorMessage && <p className="text-red-500 font-semibold">{errorMessage}</p>}
                <div className="flex flex-col gap-1">
                    <label htmlFor="nombre" className="text-sm font-medium text-slate-700">Nombre: </label>
                    <Input value={socio.nombre} onChange={onChangeSocio} name="nombre" placeholder="nombre" id="nombre" />
                </div>
                <div className="flex flex-col gap-1">
                    <label htmlFor="email" className="text-sm font-medium text-slate-700">Email: </label>
                    <Input value={socio.email} onChange={onChangeSocio} name="email" placeholder="email" id="email" />
                </div>
                <Boton onClick={handleCrearSocio}>{socioEnEdicion ? "Actualizar" : "Crear"}</Boton>
            </div>

            <section className="w-full max-w-4xl mx-auto mt-6 px-2">
                <Boton onClick={aparecer}>{visible ? "Cerrar formulario de edicion" : "Abrir formulario de edicion"}</Boton>

                {visible && (
                    <div className="mt-4 flex flex-col gap-4">
                        <h3 className="font-bold text-lg text-slate-800 px-1">Editar Socios</h3>

                        <div className="flex flex-col gap-3">
                            {socios?.map(s => (
                                <div key={s.idSocio} className="flex flex-col sm:flex-row gap-4 items-start sm:items-center justify-between border bg-linear-to-r from-amber-300 to-orange-400 rounded-xl p-4 shadow-xs">
                                    <div className="flex flex-wrap gap-x-4 gap-y-1 items-center text-sm md:text-base">
                                        <span className="bg-white/40 px-2 py-0.5 rounded font-mono text-xs font-bold">{s.idSocio}</span>
                                        <span className="font-semibold text-slate-900">{s.nombre}</span>
                                        <span className="text-slate-800 text-xs md:text-sm break-all">{s.email}</span>
                                    </div>
                                    <div className="flex gap-2 w-full sm:w-auto mt-2 sm:mt-0">
                                        <div className="grid grid-cols-2 gap-2 w-full sm:flex sm:w-auto">
                                            <Boton onClick={() => handleEditarSocio(s)}>Editar</Boton>
                                            <Boton onClick={() => handleBorrarSocio(s.idSocio)}>Borrar</Boton>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>

                        <div className="my-2">
                            <SelectorSocio socios={socios} idSocio={idSelector} onChangeSocio={onChangeSelector} />
                        </div>

                        {prestamoActivo[0] ? (
                            <div className="border-2 rounded-xl p-4 bg-linear-to-r from-amber-300 to-orange-400 shadow-xs">
                                <div className="grid grid-cols-1 xs:grid-cols-3 gap-3 text-sm">
                                    <div className="flex flex-col"><span className="text-xs opacity-75 font-semibold">Id Ejemplar</span><span className="font-medium">{prestamoActivo[0].idEjemplar}</span></div>
                                    <div className="flex flex-col"><span className="text-xs opacity-75 font-semibold">Fecha Inicio</span><span className="font-medium">{prestamoActivo[0].fechaInicio}</span></div>
                                    <div className="flex flex-col"><span className="text-xs opacity-75 font-semibold">Fecha Fin</span><span className="font-medium">{prestamoActivo[0].fechaLimite}</span></div>
                                </div>
                            </div>
                        ) : <p className="text-sm italic text-slate-500 p-2">No tiene prestamos</p>}

                        <div className="flex flex-col gap-3">
                            {prestamoAntiguo.map(p => (
                                <div key={p.idEjemplar} className="border-2 rounded-xl p-4 bg-linear-to-r from-amber-300 to-orange-400 shadow-xs">
                                    <div className="grid grid-cols-2 md:flex md:flex-row md:flex-wrap md:justify-between gap-3 text-sm">
                                        <div className="flex flex-col"><span className="text-xs opacity-75 font-semibold">Id Ejemplar</span><span className="font-medium">{p.idEjemplar}</span></div>
                                        <div className="flex flex-col"><span className="text-xs opacity-75 font-semibold">Fecha Inicio</span><span className="font-medium">{p.fechaInicio}</span></div>
                                        <div className="flex flex-col"><span className="text-xs opacity-75 font-semibold">Fecha Fin</span><span className="font-medium">{p.fechaLimite}</span></div>
                                        <div className="flex flex-col col-span-2 md:col-span-1"><span className="text-xs opacity-75 font-semibold">Fecha Devolución</span><span className="font-medium">{p.fechaDevolucion}</span></div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </section>
            {esAdmin && (
                <>
                    <div>
                        <h3 className={"font-bold text-lg text-slate-800 px-1 mb-4 flex items-center gap-2"}>Usuarios Pendientes</h3>
                    </div>
                    <ul className={"flex flex-col gap-3"}>
                    {usuariosPendientes?.map((usuario) => (

                        <li key={usuario.id} className={"flex flex-col sm:flex-row gap-4 items-start sm:items-center justify-between border  bg-linear-to-r from-amber-300 to-orange-400 border-orange-900 p-4 shadow-xs"}>
                            <div className={"flex flex-wrap gap-x-4 gap-y-1 items-center text-sm md:text-base w-full sm:w-auto"}>
                            <span className={"font-medium text-slate-800"}>{usuario.email}</span>
                            </div>
                            <div className={"w-full sm:w-auto mt-2 sm:mt-0"}>
                            <Boton onClick={() => activarSocio(usuario.id)}>Activar Socio</Boton>
                            </div>
                        </li>

                    ))}
                    </ul>
                </>
            )}
        </>
    );

}
