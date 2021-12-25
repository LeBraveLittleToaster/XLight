package de.pschiessle.xlight.xlightserver.controller.requests;

import javax.validation.constraints.NotNull;

public record SetMoodRequest(
    @NotNull String moodId
) { }
