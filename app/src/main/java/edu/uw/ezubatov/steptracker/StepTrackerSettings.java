package edu.uw.ezubatov.steptracker;

/**
 * Created by Eugen on 4/9/2018.
 */

class StepTrackerSettings {
    private static final StepTrackerSettings ourInstance = new StepTrackerSettings();

    static StepTrackerSettings getInstance() {
        return ourInstance;
    }

    private StepTrackerSettings() {
    }
}
