import {afterEach, describe, it, expect, vi} from "vitest";
import { render, screen} from "@testing-library/react";
import type {Libro, Socio} from "../Types.ts";
import {LibroService} from "../Services/LibroService.ts";
import {Reportes} from "../pages/Reportes.tsx";
import {PrestamoService} from "../Services/PrestamoService.ts";
import {MemoryRouter} from "react-router";
import {userEvent} from "@testing-library/user-event/dist/cjs/setup/index.js";

const librosMock: Libro[] = [
    { isbn: "123", titulo: "Libro de prueba", autor: "Autor de prueba" }
];

const meses: Record<string, number> = {"February": 23, "March": 12}
const librosString: string[] = librosMock.map(libro => String(libro));
const socioMock: Socio = {idSocio: 1, nombre: "alexander", email: "alexanderliberacion@gmail.com"};



vi.mock("../Services/LibroService.ts");
vi.mock("../Services/PrestamoService.ts");
vi.mocked(PrestamoService.librosMasPrestamos).mockResolvedValue(librosString);
vi.mocked(PrestamoService.mesesMasPrestamos).mockResolvedValue(meses);
vi.mocked(PrestamoService.sociosAtrasados).mockResolvedValue([socioMock]);
vi.mocked(LibroService.listarLibros).mockResolvedValue(librosMock);

describe("Reportes tests", () => {
    it("Should render", () => {
        render(
            <MemoryRouter>
                <Reportes/>
            </MemoryRouter>
        )
    })

    it("Should load the reports", async () => {
        const [socios, libroClaves, meses, libros] = await Promise.all([
            await PrestamoService.sociosAtrasados(),
            await PrestamoService.librosMasPrestamos(),
            await PrestamoService.mesesMasPrestamos(),
            await LibroService.listarLibros()
        ]);
        render(
            <MemoryRouter>
                <Reportes/>
            </MemoryRouter>
        )
        expect(socios.length).toBeGreaterThan(0);
        expect(libroClaves.length).toBeGreaterThan(0);
        expect(meses.length).toBeGreaterThan(0);
        expect(libros.length).toBeGreaterThan(0);
        const boton = screen.getByRole("button", {name: /Mostrar Reportes/i});
        const user = userEvent.setup();
        await user.click(boton);
        const nombre = await screen.findByText(socios[0].nombre);
        const tituloListado = await screen.findByText(libros[0].titulo);
        expect(nombre).toBeInTheDocument();
        expect(tituloListado).toBeInTheDocument()
    })

    afterEach(() => {
        vi.clearAllMocks();
    })
})