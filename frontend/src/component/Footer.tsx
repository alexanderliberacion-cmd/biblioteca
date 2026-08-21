import type {JSX} from "react";

export function Footer(): JSX.Element {
    return (
            <footer
                className="flex h-24 w-full items-center justify-center bg-linear-to-r from-amber-200 to-orange-300 text-sm text-stone-600 mix-blend-multiply">
                <p>&copy 2026 Biblioteca Personal</p>
            </footer>

    );
}
