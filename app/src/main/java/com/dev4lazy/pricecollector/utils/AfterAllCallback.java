package com.dev4lazy.pricecollector.utils;

/**
 * Interfejs używany do wykonania kodu po zakończeniu jakiegoś zaadnia.
 * Przykład:
 * - PopupWindowWrapper implementuje AfterAllCallback w celu zamknięcie okna popup, które opakowuje.
 * - ProgressPresentingManager przyjmuje parametr AfterAllCallback (instancja PopupWindowWrapper),
 *   którego metoda doIt() wołana jest po zakończeniu prezentacji.
 *
 */
public interface AfterAllCallback {

    void doIt();

}
