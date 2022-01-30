package com.dev4lazy.pricecollector.utils;

/**
 * Interfejs używany do wykonania kodu np. po zakończeniu jakiegoś zaadnia.
 * Przykład:
 * - PopupWindowWrapper implementuje DoItCallback w celu zamknięcie okna popup, które opakowuje.
 * - ProgressPresentingManager przyjmuje parametr DoItCallback (instancja PopupWindowWrapper),
 *   którego metoda doIt() wołana jest po zakończeniu prezentacji.
 *
 */
public interface DoItCallback {

    /**
     * Metoda, w której wykonywany jest kod.
     * @param parameters: parametry potrzebne do wykonania zadania. W ciele metody należy je zrzutować
     *                  na właściwe typy
     */
    void doIt( Object... parameters );

}
