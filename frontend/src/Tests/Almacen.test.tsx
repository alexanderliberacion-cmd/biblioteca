import {Almacen} from "../pages/Almacen.tsx";
import {afterEach, describe, it, expect, vi} from "vitest";
import { cleanup, render, screen} from "@testing-library/react";
import {LibroService} from "../Services/LibroService.ts";
import {EjemplarService} from "../Services/EjemplarService.ts";
import type {Ejemplar, Libro} from "../Types.ts";



vi.mock("../Services/LibroService.ts");
vi.mock("../Services/EjemplarService.ts");
const librosMock: Libro[] = [
    { isbn: "123", titulo: "Libro de prueba", autor: "Autor de prueba" }
];
const ejemplarMock: Ejemplar[] = [
    { idEjemplar: 1, isbn: "123", estado: "DISPONIBLE" }
];

vi.mocked(LibroService.listarLibros).mockResolvedValue(librosMock);
vi.mocked(EjemplarService.listarEjemplares).mockResolvedValue(ejemplarMock);
vi.mocked(LibroService.borrarLibro).mockResolvedValue(undefined);


describe('Test de Almacen', () => {
    afterEach(cleanup);

    it('Should render', () => {
        render(<Almacen />);
    })

    it('Should load the books and copies', async () => {

        const [libros, ejemplares] = await Promise.all([
            LibroService.listarLibros(),
            EjemplarService.listarEjemplares()
        ])
        render(<Almacen />)
        expect(libros.length).toBeGreaterThan(0)
        expect(ejemplares.length).toBeGreaterThan(0)

        const titulo = await screen.findByText(libros[0].titulo);

        expect(titulo).toBeInTheDocument();
    })

    it('Should create a book', async () => {
        const libro: Libro = {isbn: "", titulo: "", autor: ""};
        await LibroService.crearLibro(libro);
        expect(LibroService.crearLibro).toHaveBeenCalledWith(libro);
        expect(LibroService.crearLibro).toHaveBeenCalledTimes(1);
    })

    afterEach(() => {
        vi.clearAllMocks();
    })





})