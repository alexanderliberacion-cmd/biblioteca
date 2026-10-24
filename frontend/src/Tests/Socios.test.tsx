import {describe, it, expect, vi, afterEach} from "vitest";
import {cleanup, render, screen} from "@testing-library/react";
import {SocioService} from "../Services/SocioService.ts";
import {PrestamoService} from "../Services/PrestamoService.ts";
import {MemoryRouter} from "react-router";
import {userEvent} from "@testing-library/user-event/dist/cjs/setup/index.js";
import {Socios} from "../pages/Socios.tsx";
import type {Prestamo, Socio, Usuario} from "../Types.ts";

const prestamoMock: Prestamo = {fechaInicio: "21-02-2000", fechaLimite: "21-03-2001", idSocio: 1, idEjemplar: 1};
const socioMock: Socio = {idSocio: 1, nombre: "alexander", email: "alexanderliberacion@gmail.com"};
const usuarioMock: Usuario = {id: 1, email: "alexanderliberacion@gmail.com", rol: "ADMIN"};

vi.mock("../Services/SocioService.ts");
vi.mock("../Services/PrestamoService.ts");
vi.mocked(SocioService.listarSocios).mockResolvedValue([socioMock]);
vi.mocked(PrestamoService.listarPrestamos).mockResolvedValue([prestamoMock]);
vi.mocked(SocioService.usuariosPendientes).mockResolvedValue([usuarioMock]);
vi.mocked(SocioService.crearSocio).mockResolvedValue(socioMock);

describe("Socios tests", () => {

    it("should render", () => {
        render(
            <MemoryRouter>
                <Socios/>
            </MemoryRouter>
        )
    })

    it("should load the socios and prestamos", async () => {
        localStorage.setItem("rol", "SOCIO");
        render(
            <MemoryRouter>
                <Socios/>
            </MemoryRouter>
        )
        const user = userEvent.setup();
        await user.click(screen.getByRole("button", {name: /abrir formulario de edicion/i}));
        const selectores = await screen.findAllByRole("combobox");
        const selectorSocio = selectores[0];
        await user.selectOptions(selectorSocio, socioMock.idSocio.toString());
        expect(screen.getAllByText(socioMock.nombre).length).toBeGreaterThan(0);
        await screen.findByText(prestamoMock.fechaInicio);
        expect(screen.getByText(prestamoMock.fechaInicio)).toBeInTheDocument();
        expect(SocioService.listarSocios).toHaveBeenCalledTimes(1);
        expect(PrestamoService.listarPrestamos).toHaveBeenCalledTimes(1);
        expect(SocioService.usuariosPendientes).toHaveBeenCalledTimes(1);
    })

    it("should create a socio", async () => {
        render(
            <MemoryRouter>
                <Socios/>
            </MemoryRouter>
        )
        const boton = screen.getByRole("button", {name: /Crear/i});
        const user = userEvent.setup();
        await user.click(boton);
        await SocioService.crearSocio(socioMock);
        expect(SocioService.crearSocio).toHaveBeenCalledWith(socioMock);
        expect(SocioService.crearSocio).toHaveBeenCalledTimes(1);
    })

    afterEach(() => {
        localStorage.removeItem("rol");
        vi.clearAllMocks();
        cleanup();
    });
})