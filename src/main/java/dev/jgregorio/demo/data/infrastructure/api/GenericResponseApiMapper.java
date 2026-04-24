package dev.jgregorio.demo.data.infrastructure.api;

public interface GenericResponseApiMapper<D, R> {

    R toResponse(D domain);
}
