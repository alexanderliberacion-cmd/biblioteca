import {afterEach, describe, it, expect, vi} from "vitest";
import { cleanup, render, screen} from "@testing-library/react";
import {Catalogo} from "../pages/Catalogo.tsx";
import {LibroService} from "../Services/LibroService.ts";
import {EjemplarService} from "../Services/EjemplarService.ts";
import {SocioService} from "../Services/SocioService.ts";
import type {Ejemplar, Libro, Prestamo, Socio} from "../Types.ts";
import {MemoryRouter} from "react-router";
import {userEvent} from "@testing-library/user-event/dist/cjs/setup/index.js";
import {PrestamoService} from "../Services/PrestamoService.ts";


const librosMock: Libro[] = [
    { isbn: "123", titulo: "Libro de prueba", autor: "Autor de prueba" }
];
const ejemplarMock: Ejemplar[] = [
    { idEjemplar: 1, isbn: "123", estado: "DISPONIBLE" }
];

const prestamoMock: Prestamo = {fechaInicio: "21-02-2000", fechaLimite: "21-03-2001", idSocio: 1, idEjemplar: 1};

const socioMock: Socio = {idSocio: 1, nombre: "alexander", email: "alexanderliberacion@gmail.com"};

vi.mock("../Services/LibroService.ts");
vi.mock("../Services/EjemplarService.ts");
vi.mock("../Services/SocioService.ts");
vi.mock("../Services/PrestamoService.ts");
vi.mocked(LibroService.listarLibros).mockResolvedValue(librosMock);
vi.mocked(EjemplarService.listarEjemplares).mockResolvedValue(ejemplarMock);
vi.mocked(SocioService.listarSocios).mockResolvedValue([socioMock]);
vi.mocked(PrestamoService.crearPrestamo).mockResolvedValue(prestamoMock);

describe("Catalogo", () => {
    afterEach(cleanup);

    it("Should render", () => {
        render(
            <MemoryRouter>
                <Catalogo />
            </MemoryRouter>
        );
    })

    it("Should load the books and copies", async () => {
        const [libros, ejemplares, ] = await Promise.all([
            LibroService.listarLibros(),
            EjemplarService.listarEjemplares(),
        ]);
        render(
            <MemoryRouter>
                <Catalogo />
            </MemoryRouter>
        );
        expect(libros.length).toBeGreaterThan(0);
        expect(ejemplares.length).toBeGreaterThan(0);
        const boton = screen.getByRole("button", {name: /Ver Disponibles/i});
        const user = userEvent.setup();
        await user.click(boton);
        const titulo = await screen.findByText(new RegExp(libros[0].titulo, "i"));
        expect(titulo).toBeInTheDocument();
    })

    it("Should do a loan", async () => {
        localStorage.setItem("rol", "SOCIO");

        render(
            <MemoryRouter>
                <Catalogo />
            </MemoryRouter>
        );
        const selectores = await screen.findAllByRole("combobox");
        const selectorSocio = selectores[0];
        const selectorEjemplar = selectores[1];
        const user = userEvent.setup();
        await user.selectOptions(selectorSocio, socioMock.nombre);
        await user.selectOptions(selectorEjemplar, ejemplarMock[0].idEjemplar.toString());
        const boton = screen.getByRole("button", {name: /Realizar/i});
        await user.click(boton);
        expect(PrestamoService.crearPrestamo).toHaveBeenCalledWith(1, 1);
        expect(PrestamoService.crearPrestamo).toHaveBeenCalledTimes(1);
    })

    afterEach(() => {
        localStorage.removeItem("rol");
        vi.clearAllMocks();
    })

})