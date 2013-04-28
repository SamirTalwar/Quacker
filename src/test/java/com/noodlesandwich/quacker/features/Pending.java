package com.noodlesandwich.quacker.features;

import cucumber.api.PendingException;
import cucumber.api.java.Before;

public class Pending {
    @Before("@wip")
    public void mark_Work_In_Progress_scenarios_as_pending() {
        throw new PendingException();
    }
}
