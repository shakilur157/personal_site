package com.sazzadur.controllers;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AcceptServiceRequest {
    @NotNull
    private Long serviceCentreId;
    @NotNull
    private Long serviceRequestId;
}
