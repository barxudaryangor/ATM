package org.example.atm.services_interfaces;

import org.example.atm.dtos.SpecificationResponse;

import java.awt.print.Pageable;

public interface GenericFilterService<T> {
    SpecificationResponse<T> filter(SpecificationResponse<T> spec, Pageable pageable);
}
