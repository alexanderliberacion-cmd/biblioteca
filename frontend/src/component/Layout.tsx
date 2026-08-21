import {Header} from "./Header.tsx";
import {Footer} from "./Footer.tsx";
import {Outlet} from "react-router";
import type {JSX} from "react";

export function Layout(): JSX.Element {
    return (
            <div className="flex min-h-screen w-full max-w-full overflow-x-hidden overflow-y-auto flex-col justify-between bg-linear-to-r from-amber-200 to-orange-300">
                <Header />
                <div className="mx-auto flex w-full max-w-6xl flex-1 items-start justify-center px-4">
                <Outlet />
                </div>
                <Footer />
            </div>
    );
}
