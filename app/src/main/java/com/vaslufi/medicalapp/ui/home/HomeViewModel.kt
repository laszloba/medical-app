package com.vaslufi.medicalapp.ui.home

import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {
    /**
     * Returns the state flow of the UI state.
     */
    val state: StateFlow<HomeState>

    /**
     * Returns the state flow of the debug information.
     */
    val debugInfo: StateFlow<DebugInformation>

    /**
     * Perform a check-in with the given temperature and feeling better status.
     *
     * This method is called when the user performs a check-in action on the Home screen.
     * It takes the user's temperature and whether they are feeling better as parameters and updates the UI state accordingly.
     *
     * @param temperature The user's recorded temperature.
     * @param feelingBetter A boolean indicating whether the user is feeling better or not.
     */
    fun checkIn(temperature: Double, feelingBetter: Boolean)

    /**
     * Record a dosage with the given amount.
     *
     * This method is called when the user records a dosage on the Home screen.
     * It takes the dosage amount as a parameter and updates the UI state accordingly.
     *
     * @param amount The amount of dosage to be recorded.
     */
    fun recordDosage(amount: Double)

    /**
     * Reset debug information.
     *
     * This method is used for development and testing purposes.
     * It resets any debug information stored in the ViewModel, allowing developers to start fresh with debug testing.
     */
    fun debugReset()

    /**
     * Advance time by the given number of minutes for debugging purposes.
     *
     * This method is used for development and testing purposes to simulate the passage of time.
     * It allows developers to fast-forward time to test various scenarios.
     *
     * @param minutes The number of minutes to advance the time by.
     */
    fun debugAdvanceTimeBy(minutes: Int)

    /**
     * Set the patient's age for debugging purposes.
     *
     * This method is used for development and testing purposes to set the patient's age.
     * It allows developers to simulate different patient ages to test age-dependent functionality.
     *
     * @param age The age of the patient to be set.
     */
    fun debugSetPatientAge(age: Int)
}
